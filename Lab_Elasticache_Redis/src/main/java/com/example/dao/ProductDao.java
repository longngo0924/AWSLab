package com.example.dao;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import com.example.entity.Product;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class ProductDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	private ObjectMapper objectMapper;

	private ObjectMapper getObjectMapper() {
		if (objectMapper != null)
			return objectMapper;
		return new ObjectMapper();
	}
	
	// function work with MongoDB

	public Product addProduct(Product p) {
		return mongoTemplate.save(p);
	}

	public Product getProductById(String id) {
		Class<?> productClass = Product.class;
		Product product = (Product) mongoTemplate.findById(id, productClass);
		log.info("Got product from Database {}", product);
		return product;
	}
	
	// function work with Redis

	public ValueOperations<String, Object> addProductToRedis(Product p) {
		try {
			ValueOperations<String, Object> cachedProduct = redisTemplate.opsForValue();
			objectMapper = getObjectMapper();
			Map<?, ?> map = objectMapper.convertValue(p, Map.class);
			cachedProduct.set(p.getId(), map, 10, TimeUnit.SECONDS);
			log.info("Add product to cache {}", map);
			return cachedProduct;
		} catch (RedisConnectionFailureException e) {
			log.info("Cannot add product, Redis service go down...");
		}

		return null;

	}

	public Product getProductFromRedis(String key) {

		ValueOperations<String, Object> cachedProduct = redisTemplate.opsForValue();
		try {
			Object result = cachedProduct.getAndExpire(key, 10, TimeUnit.SECONDS);
			objectMapper = getObjectMapper();
			Product product = objectMapper.convertValue(result, Product.class);
			log.info("Got product from cache {}", product);
			return product;
		} catch (RedisConnectionFailureException e) {
			log.info("Cannot get product, Redis service go down...");
		}

		return null;
	}

}
