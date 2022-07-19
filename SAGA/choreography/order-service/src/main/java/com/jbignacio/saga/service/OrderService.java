package com.jbignacio.saga.service;

import com.jbignacio.saga.dto.OrderRequestDto;
import com.jbignacio.saga.dto.OrderResponseDto;
import com.jbignacio.saga.events.inventory.InventoryEvent;
import com.jbignacio.saga.events.payment.PaymentEvent;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderService {

	Flux<OrderResponseDto> findAll();

	Mono<OrderResponseDto> findById(int id);

	Mono<OrderResponseDto> createOrder(OrderRequestDto request);

	void updatePaymentStatus(PaymentEvent event);

	void updateInventoryStatus(InventoryEvent event);

}
