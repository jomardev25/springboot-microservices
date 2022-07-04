package com.jbignacio.userservice.dto.request;

import lombok.Data;

@Data
public class UserRequest {

	private int id;
	private String firstName;
	private String lastName;
	private int departmentId;

}
