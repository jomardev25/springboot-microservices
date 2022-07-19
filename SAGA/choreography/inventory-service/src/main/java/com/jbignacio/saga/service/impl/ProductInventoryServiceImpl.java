package com.jbignacio.saga.service.impl;

import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jbignacio.saga.dto.InventoryDto;
import com.jbignacio.saga.entity.InventoryMovement;
import com.jbignacio.saga.entity.ProductInventory;
import com.jbignacio.saga.events.inventory.InventoryEvent;
import com.jbignacio.saga.events.inventory.InventoryStatus;
import com.jbignacio.saga.events.order.OrderEvent;
import com.jbignacio.saga.repository.ProductInventoryRepository;
import com.jbignacio.saga.service.ProductInventoryService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@SuppressWarnings("deprecation")
@Slf4j
@AllArgsConstructor
@Service
@Transactional
public class ProductInventoryServiceImpl implements ProductInventoryService {

	private final ProductInventoryRepository productRepository;

	private final DatabaseClient databaseClient;

	private final R2dbcEntityTemplate template;

	public Mono<InventoryEvent> updateProductInventory(OrderEvent event) {

		InventoryDto inventoryDto = InventoryDto.builder()
				.orderId(event.getOrder().getOrderId())
				.productId(event.getOrder().getProductId())
				.qty(event.getOrder().getQty())
				.build();

			return update(event)
						.flatMap(p -> Mono.just(new InventoryEvent(inventoryDto, InventoryStatus.RESERVED)))
						.switchIfEmpty(Mono.just(new InventoryEvent(inventoryDto, InventoryStatus.REJECTED)));
	}

	private Mono<ProductInventory> update(OrderEvent event) {
		int orderQty = event.getOrder().getQty();

		return productRepository.findById(event.getOrder().getProductId())
								.log()
								.filter(p -> p.getQty() >= orderQty)
								.doOnNext(p -> p.setQty(p.getQty() - orderQty))
								.flatMap(p -> {
									return productRepository.save(p)
													.doOnSuccess(pi -> {

														InventoryMovement inventoryMovement = InventoryMovement.builder()
																.orderId(event.getOrder().getOrderId())
																.productId(event.getOrder().getProductId())
																.qty(orderQty)
																.build();

														log.info("Inventory Movement: {}", inventoryMovement);

														template.insert(InventoryMovement.class)
																.using(inventoryMovement)
																.then()
																.subscribe();

														/*
														 * databaseClient .insert() .into(InventoryMovement.class)
														 * .using(inventoryMovement) .then() .subscribe();
														 */
													});
								});

	}

	@Override
	public Mono<Void> cancelProductInventory(OrderEvent event) {

		int orderId = event.getOrder().getOrderId();
		int productId = event.getOrder().getProductId();
		int qty = event.getOrder().getQty();

		return productRepository
			.findById(productId)
			.doOnNext(p -> p.setQty(p.getQty() + qty))
			.flatMap(p -> productRepository.save(p))
			.then(
					databaseClient
					.delete()
					.from(InventoryMovement.class)
					.matching( Criteria.where("order_id").is(orderId) )
					.then()
			);

	}

}
