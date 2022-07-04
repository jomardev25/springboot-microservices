package com.jbignacio.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jbignacio.userservice.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}
