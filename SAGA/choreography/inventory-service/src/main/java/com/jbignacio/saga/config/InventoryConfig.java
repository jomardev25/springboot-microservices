package com.jbignacio.saga.config;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jbignacio.saga.events.inventory.InventoryEvent;
import com.jbignacio.saga.events.order.OrderEvent;
import com.jbignacio.saga.events.order.OrderStatus;
import com.jbignacio.saga.service.ProductInventoryService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
public class InventoryConfig {

	@Autowired
    private ProductInventoryService service;

    @Bean
    public Function<Flux<OrderEvent>, Flux<InventoryEvent>> inventoryProcessor() {
    	return flux -> flux.flatMap(this::processInventory);
    }

    private Mono<InventoryEvent> processInventory(OrderEvent event){

    	if(event.getOrderStatus().equals(OrderStatus.ORDER_CREATED)){
    		log.info("ORDER CREATED Order Event: {}", event);
    		return service.updateProductInventory(event);
    	}else {
    		log.info("ORDER CANCELLED Order Event: {}", event);
    		return Mono.fromRunnable(() -> service.cancelProductInventory(event));
    	}


    }

}
