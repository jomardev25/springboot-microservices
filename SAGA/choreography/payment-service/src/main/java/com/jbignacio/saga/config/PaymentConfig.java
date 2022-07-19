package com.jbignacio.saga.config;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jbignacio.saga.events.order.OrderEvent;
import com.jbignacio.saga.events.order.OrderStatus;
import com.jbignacio.saga.events.payment.PaymentEvent;
import com.jbignacio.saga.service.PaymentService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
public class PaymentConfig {

	@Autowired
	private PaymentService service;

	@Bean
    public Function<Flux<OrderEvent>, Flux<PaymentEvent>> paymentProcessor() {
        return flux -> flux.flatMap(this::processPayment);
    }

	private Mono<PaymentEvent> processPayment(OrderEvent event){
        if(event.getOrderStatus().equals(OrderStatus.ORDER_CREATED)){
        	log.info("ORDER CREATED Order Event: {}", event);
            return service.updateUserBalance(event);
        }else {
        	log.info("ORDER CANCELLED Order Event: {}", event);
        	return Mono.fromRunnable(() -> service.cancelOrderEvent(event).subscribe());
        }
    }

}
