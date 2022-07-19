package com.jbignacio.saga.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jbignacio.saga.dto.OrderRequestDto;
import com.jbignacio.saga.dto.OrderResponseDto;
import com.jbignacio.saga.service.OrderService;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController
@RequestMapping("/v1/orders")
public class OrderController {

	private final OrderService orderService;

	@GetMapping
	public Flux<OrderResponseDto> index(){
		return orderService.findAll();
	}

	@GetMapping("/{id}")
	public Mono<OrderResponseDto> show(@PathVariable("id") Integer id){

		return orderService.findById(id);
	}

	@PostMapping
	public Mono<OrderResponseDto> create(@RequestBody OrderRequestDto request){
		return orderService.createOrder(request);
	}

}
