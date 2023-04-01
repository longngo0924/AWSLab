package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootApplication
public class RedisApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedisApplication.class, args);
	}

	@Bean
	public LettuceConnectionFactory redisConnectionFactory() {
		return new LettuceConnectionFactory();
	}

	@Bean
	RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {

		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		return template;
	}

}
