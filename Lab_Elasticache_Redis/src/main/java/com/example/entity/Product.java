package com.example.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document("product")
@Data
public class Product {

	private String id;
	private String name;
	private String category;
	private int quantity;
}
