package com.booking.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class BookingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookingServiceApplication.class, args);
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@PostConstruct
	public void executeSqlScripts() {
		jdbcTemplate.execute("INSERT INTO transactions (transaction_id,amount,currency,type) VALUES (1,100,'INR','expenses'),(2,200,'USD','restaurant');");
		jdbcTemplate.execute("INSERT INTO transactions (transaction_id,amount,currency,type,parent_transaction_transaction_id) VALUES (3,300,'INR','expenses',1),(4,600,'INR','shopping',1),(5,50,'USD','restaurant',2),(6,20,'USD','travelling',2);");
	}
}
