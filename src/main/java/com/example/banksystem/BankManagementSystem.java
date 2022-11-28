package com.example.banksystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class BankManagementSystem {
	public static void main(String[] args) {
		SpringApplication.run(BankManagementSystem.class, args);
	}
}
