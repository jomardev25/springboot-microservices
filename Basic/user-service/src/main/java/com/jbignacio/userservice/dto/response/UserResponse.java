package com.jbignacio.userservice.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.jbignacio.userservice.entity.User;

import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("deprecation")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
@NoArgsConstructor
public class UserResponse {

	private User user;
	private Department department;
	private String port;

	public UserResponse(User user, Department department, String port) {
		this.user = user;
		this.department = department;
		this.port = port;
	}

}
