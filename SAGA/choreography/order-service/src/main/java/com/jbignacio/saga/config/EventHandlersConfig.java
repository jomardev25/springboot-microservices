package com.jbignacio.saga.config;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jbignacio.saga.events.inventory.InventoryEvent;
import com.jbignacio.saga.events.payment.PaymentEvent;
import com.jbignacio.saga.service.OrderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class EventHandlersConfig {

	@Autowired
	private OrderService orderService;

	@Bean
    public Consumer<PaymentEvent> paymentEventConsumer(){
        return event -> {
        	orderService.updatePaymentStatus(event);
        };
    }

    @Bean
    public Consumer<InventoryEvent> inventoryEventConsumer(){
        return event -> {
        	log.info("Inventory Event: {}", event);
        	orderService.updateInventoryStatus(event);
        };
    }
}
