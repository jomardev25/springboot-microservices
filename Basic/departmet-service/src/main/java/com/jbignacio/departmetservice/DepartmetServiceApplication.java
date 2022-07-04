package com.jbignacio.departmetservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class DepartmetServiceApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DepartmetServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	}

}
