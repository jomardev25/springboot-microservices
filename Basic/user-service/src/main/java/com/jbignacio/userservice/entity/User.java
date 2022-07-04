package com.jbignacio.userservice.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String firstName;

	private String lastName;

	private int departmentId;

	public User() {

	}

	public User(String firstName, String lastName, int departmentId) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.departmentId = departmentId;
	}
}
