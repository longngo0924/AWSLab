package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Product;
import com.example.service.ProductService;

@RestController
@CrossOrigin
public class ProductController {

	@Autowired
	private ProductService productService;

	@PostMapping("/products")
	public Product addProduct(@RequestBody Product p) {
		return productService.addProduct(p);
	}

	@GetMapping("/products/{id}")
	public Product getProduct(@PathVariable String id) {
		return productService.getProductById(id);
	}
}
