package com.jbignacio.saga.service.impl;

import java.util.Objects;

import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;

import com.jbignacio.saga.dto.OrderDto;
import com.jbignacio.saga.dto.OrderRequestDto;
import com.jbignacio.saga.dto.OrderResponseDto;
import com.jbignacio.saga.entity.Order;
import com.jbignacio.saga.events.inventory.InventoryEvent;
import com.jbignacio.saga.events.inventory.InventoryStatus;
import com.jbignacio.saga.events.order.OrderEvent;
import com.jbignacio.saga.events.order.OrderStatus;
import com.jbignacio.saga.events.payment.PaymentEvent;
import com.jbignacio.saga.events.payment.PaymentStatus;
import com.jbignacio.saga.repository.OrderRepository;
import com.jbignacio.saga.service.OrderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@SuppressWarnings("deprecation")
@Slf4j
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;
	private final Sinks.Many<OrderEvent> orderSink;
	private final DatabaseClient databaseClient;

	public Flux<OrderResponseDto> findAll() {
		return orderRepository.findAll().map(order -> mapToDto(order));
	}

	@Override
	public Mono<OrderResponseDto> findById(int id) {
		return orderRepository.findById(id).map(order -> mapToDto(order));
	}

	public Mono<OrderResponseDto> createOrder(OrderRequestDto request) {
		return orderRepository.save(mapToEntity(request))
				.doOnSuccess( order -> {
					raiseOrderEvent(order, OrderStatus.ORDER_CREATED);
				})
				.map(this::mapToDto);
	}

	public void updatePaymentStatus(PaymentEvent event) {
		databaseClient.execute("UPDATE orders SET payment_status=$1 WHERE id=$2")
		.bind(0, event.getPaymentStatus())
		.bind(1, event.getPayment().getOrderId())
		.as(Order.class).fetch().rowsUpdated()
		.then(
			databaseClient.execute("SELECT * FROM orders WHERE id=$1")
				.bind(0, event.getPayment().getOrderId())
				.as(Order.class)
				.fetch()
				.first()
		).delayUntil(order -> Mono.fromRunnable(() -> this.updateOrder(order))).subscribe();
	}

	public void updateInventoryStatus(InventoryEvent event) {
		databaseClient.execute("UPDATE orders SET inventory_status=$1 WHERE id=$2")
		.bind(0, event.getInventoryStatus())
		.bind(1, event.getInventory().getOrderId())
		.as(Order.class).fetch().rowsUpdated()
		.then(
			databaseClient.execute("SELECT * FROM orders WHERE id=$1")
				.bind(0, event.getInventory().getOrderId())
				.as(Order.class)
				.fetch()
				.first()
		).delayUntil(order -> Mono.fromRunnable(() -> this.updateOrder(order))).subscribe();
	}

	private void updateOrder(Order order) {
		log.info("Update -> Order: {}", order);

		if(Objects.isNull(order.getPaymentStatus()) || Objects.isNull(order.getInventoryStatus()))
			return;

		boolean isComplete = PaymentStatus.PAID.equals(order.getPaymentStatus()) && InventoryStatus.RESERVED.equals(order.getInventoryStatus());
		OrderStatus orderStatus = isComplete ? OrderStatus.ORDER_COMPLETED : OrderStatus.ORDER_CANCELLED;
		order.setOrderStatus(orderStatus);
		orderRepository.save(order).subscribe();
		if (!isComplete){
			raiseOrderEvent(order, OrderStatus.ORDER_CANCELLED);
        }
	}

	private void raiseOrderEvent(Order order, OrderStatus orderStatus) {
		log.info("Raised Event for Order: {}, Status: {}", order, orderStatus);
		OrderDto orderDto = OrderDto.builder()
									.orderId(order.getOrderId())
									.userId(order.getUserId())
									.productId(order.getProductId())
									.qty(order.getQty())
									.price(order.getPrice())
									.build();
		OrderEvent orderEvent = new OrderEvent(orderDto, orderStatus);
		this.orderSink.tryEmitNext(orderEvent);
	}

	private OrderResponseDto mapToDto(Order order){
		return OrderResponseDto.builder()
				.orderId(order.getOrderId())
				.userId(order.getUserId())
				.productId(order.getProductId())
				.qty(order.getQty())
				.price(order.getPrice())
				.build();
	}

	private Order mapToEntity(OrderRequestDto request){
		return Order.builder()
					.userId(request.getUserId())
					.productId(request.getProductId())
					.qty(request.getOrderQty())
					.price(request.getPrice())
					.orderStatus(OrderStatus.ORDER_CREATED)
					.build();
	}
}
