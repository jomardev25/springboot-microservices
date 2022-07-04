package com.jbignacio.userservice.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.jbignacio.userservice.dto.request.UserRequest;
import com.jbignacio.userservice.dto.response.Department;
import com.jbignacio.userservice.dto.response.UserResponse;
import com.jbignacio.userservice.entity.User;
import com.jbignacio.userservice.exception.ResourceNotFound;
import com.jbignacio.userservice.repository.UserRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final RestTemplate restTemplate;
	private final ModelMapper modelMapper;
	private final Environment environment;
	private final String ROOT_SERVICE_URL = "http://department-service/v1/departments/";

	public void saveAllUsers(List<User> users) {
		userRepository.saveAll(users);
	}

	public UserResponse saveUser(UserRequest userRequest) {
		User user = modelMapper.map(userRequest, User.class);
		user = userRepository.save(user);
		Department department = restTemplate.getForObject(ROOT_SERVICE_URL + user.getDepartmentId(), Department.class);
		return new UserResponse(user, department, environment.getProperty("server.port"));
	}

	@CircuitBreaker(name="departmentServiceCircuitBreaker", fallbackMethod = "findByIdFallback") // Circuit will handle the failed called
	@Retry(name="departmentServiceRetry")
	public UserResponse findById(int id) {
		User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFound("User", "id", id));
		String serviceUrl = ROOT_SERVICE_URL + user.getDepartmentId();
		log.info("Making a request to {}", serviceUrl);
		Department department = restTemplate.getForObject(serviceUrl, Department.class);
		return new UserResponse(user, department, environment.getProperty("server.port"));
	}

	public UserResponse findByIdFallback(Exception e) {
		return new UserResponse(null, null,  environment.getProperty("server.port"));
	}

}
