package com.jbignacio.saga.service;

import com.jbignacio.saga.events.inventory.InventoryEvent;
import com.jbignacio.saga.events.order.OrderEvent;

import reactor.core.publisher.Mono;

public interface ProductInventoryService {

	Mono<InventoryEvent> updateProductInventory(OrderEvent event);

	Mono<Void> cancelProductInventory(OrderEvent orderEvent);

}
