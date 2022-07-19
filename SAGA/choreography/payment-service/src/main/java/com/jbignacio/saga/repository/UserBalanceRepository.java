package com.jbignacio.saga.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.jbignacio.saga.entity.UserBalance;

public interface UserBalanceRepository extends ReactiveCrudRepository<UserBalance, Integer> {

}
