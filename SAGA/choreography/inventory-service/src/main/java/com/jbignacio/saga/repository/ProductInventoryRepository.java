package com.jbignacio.saga.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.jbignacio.saga.entity.ProductInventory;

public interface ProductInventoryRepository extends ReactiveCrudRepository<ProductInventory, Integer> {

}
