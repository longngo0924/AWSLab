package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class DemoApplication implements CommandLineRunner {

	@Value("${spring.port}")
	private String port;
	
	@Value("${spring.message}")
	private String message;


	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {

		log.info("Resolved port parameter: {}", port);
		log.info("Resolved message parameter: {}", message);


	}

}
