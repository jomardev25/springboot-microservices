package com.jbignacio.userservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jbignacio.userservice.dto.response.UserResponse;
import com.jbignacio.userservice.service.UserService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/v1/users")
@AllArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping
	public String index() {
		return "You are in index page";
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserResponse> show(@PathVariable("id") int id) {
		return ResponseEntity.ok().body(userService.findById(id));
	}

}
