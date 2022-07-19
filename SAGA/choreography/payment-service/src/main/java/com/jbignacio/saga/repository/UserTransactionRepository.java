package com.jbignacio.saga.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.jbignacio.saga.entity.UserTransaction;

public interface UserTransactionRepository extends ReactiveCrudRepository<UserTransaction, Integer>{

}
