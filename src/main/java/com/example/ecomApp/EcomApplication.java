package com.example.ecomApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class EcomApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(EcomApplication.class, args);
		System.out.println("Inside Main file");
	}

}