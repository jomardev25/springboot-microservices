package com.jbignacio.departmetservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jbignacio.departmetservice.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {

}
