package com.jbignacio.departmetservice.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.jbignacio.departmetservice.dto.request.DepartmentRequest;
import com.jbignacio.departmetservice.dto.response.DepartmentResponse;
import com.jbignacio.departmetservice.entity.Department;
import com.jbignacio.departmetservice.exception.ResourceNotFound;
import com.jbignacio.departmetservice.repository.DepartmentRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DepartmentService {

	private final DepartmentRepository departmentRepository;
	private final ModelMapper modelMapper;

	public void saveAllDepartments(List<Department> departments) {
		departmentRepository.saveAll(departments);
	}

	public DepartmentResponse saveDepartment(DepartmentRequest departmentRequest) {
		Department department = modelMapper.map(departmentRequest, Department.class);
		department = departmentRepository.save(department);
		DepartmentResponse response = modelMapper.map(department, DepartmentResponse.class);
		return response;
	}

	public DepartmentResponse findById(int id) {
		Department department = departmentRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Department", "id", id));
		DepartmentResponse response = modelMapper.map(department, DepartmentResponse.class);
		return response;
	}



}
