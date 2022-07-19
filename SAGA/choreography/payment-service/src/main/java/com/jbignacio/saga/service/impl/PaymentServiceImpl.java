package com.jbignacio.saga.service.impl;

import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.query.Criteria;
import org.springframework.stereotype.Service;

import com.jbignacio.saga.dto.PaymentDto;
import com.jbignacio.saga.entity.UserBalance;
import com.jbignacio.saga.entity.UserTransaction;
import com.jbignacio.saga.events.order.OrderEvent;
import com.jbignacio.saga.events.payment.PaymentEvent;
import com.jbignacio.saga.events.payment.PaymentStatus;
import com.jbignacio.saga.repository.UserBalanceRepository;
import com.jbignacio.saga.service.PaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@SuppressWarnings("deprecation")
@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

	private final UserBalanceRepository balanceRepository;
	private final R2dbcEntityTemplate template;
	private final DatabaseClient databaseClient;

	@Override
	public Mono<PaymentEvent> updateUserBalance(OrderEvent event) {

		int orderId = event.getOrder().getOrderId();
		int userId = event.getOrder().getUserId();
		double amount =  event.getOrder().getPrice();

		PaymentDto paymentDto = PaymentDto.of(orderId, userId, amount);
		return newOrderEvent(event)
				.flatMap(p -> Mono.just(new PaymentEvent(paymentDto, PaymentStatus.PAID)))
				.switchIfEmpty(Mono.just(new PaymentEvent(paymentDto, PaymentStatus.UNPAID)));
	}

	@Override
	public Mono<Void> cancelOrderEvent(OrderEvent event) {
		return databaseClient
			.select()
			.from(UserBalance.class)
			.matching(Criteria.where("user_id").is(event.getOrder().getUserId()))
			.fetch()
			.first()
			.flatMap(ub -> {
				double balance = ub.getBalance() + event.getOrder().getPrice();
				return databaseClient
						.execute("UPDATE user_balance SET balance=$1 WHERE user_id= $2")
						.bind(0, balance)
						.bind(1, ub.getUserId())
						.as(UserBalance.class)
						.fetch()
						.rowsUpdated();
				}
			)
			.then(
				databaseClient
					.delete()
					.from(UserTransaction.class)
					.matching(Criteria.where("order_id").is(event.getOrder().getOrderId()))
					.then()
			);
	}

	private Mono<UserBalance> newOrderEvent(OrderEvent event) {
		int userId = event.getOrder().getUserId();
		double amountToDeduct =  event.getOrder().getPrice();

		return balanceRepository
			.findById(userId)
			.filter(ub -> ub.getBalance() >= amountToDeduct)
			.doOnNext(ub -> {
				log.info("New Order -> Current User Balance: {}, Due: {}", ub.getBalance(), amountToDeduct);
				ub.setBalance(ub.getBalance() - amountToDeduct);
				log.info("New Order -> New User Balance: {}", ub.getBalance());
			})
			.flatMap(balanceRepository::save)
			.doOnNext(ub -> {
				UserTransaction transaction = UserTransaction.builder()
						.orderId(event.getOrder().getOrderId())
						.userId(event.getOrder().getUserId())
						.amount(amountToDeduct)
						.build();
				template
					.insert(UserTransaction.class)
					.using(transaction)
					.then()
					.subscribe();
			});
	}

}
