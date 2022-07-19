package com.jbignacio.saga.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.jbignacio.saga.entity.Order;
import com.jbignacio.saga.events.inventory.InventoryStatus;
import com.jbignacio.saga.events.payment.PaymentStatus;

import reactor.core.publisher.Mono;

public interface OrderRepository extends R2dbcRepository<Order, Integer> {

	@Query("UPDATE orders SET payment_status=$2 WHERE id=$1")
	Mono<Order> updatePaymentStatus(int orderId, PaymentStatus paymentStatus);

	@Query("UPDATE orders SET inventory_status=$2 WHERE id=$1")
	Mono<Order> updateInventoryStatus(int orderId, InventoryStatus inventoryStatus);

}
