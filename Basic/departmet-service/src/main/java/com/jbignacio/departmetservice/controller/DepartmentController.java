package com.jbignacio.departmetservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jbignacio.departmetservice.dto.response.DepartmentResponse;
import com.jbignacio.departmetservice.service.DepartmentService;

import java.util.List;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/v1/departments")
@AllArgsConstructor
public class DepartmentController {

	private final DepartmentService departmentService;
	private final DiscoveryClient discoveryClient;

	@GetMapping("/{id}")
	public ResponseEntity<DepartmentResponse> show(@PathVariable("id") int id) {

		return ResponseEntity.ok().body(departmentService.findById(id));

	}

	@RequestMapping("/clients/{applicationName}")
    public  ResponseEntity<List<ServiceInstance>> getClientsByApplicationName(@PathVariable String applicationName) {
        return ResponseEntity.ok().body(discoveryClient.getInstances(applicationName));
    }

}
