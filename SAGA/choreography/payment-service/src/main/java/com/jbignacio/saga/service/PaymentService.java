package com.jbignacio.saga.service;

import com.jbignacio.saga.events.order.OrderEvent;
import com.jbignacio.saga.events.payment.PaymentEvent;

import reactor.core.publisher.Mono;

public interface PaymentService {

	Mono<PaymentEvent> updateUserBalance(OrderEvent event);

	Mono<Void> cancelOrderEvent(OrderEvent event);

}
