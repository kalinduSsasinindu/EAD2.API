package com.example.ead2project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Ead2projectApplication {

	public static void main(String[] args) {
		SpringApplication.run(Ead2projectApplication.class, args);
	}

	@Bean
	public String mongoDBUri() {
		return "your_mongodb_connection_string_here";  // Replace with your actual MongoDB connection string
	}

}
