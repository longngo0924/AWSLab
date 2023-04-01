package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.ProductDao;
import com.example.entity.Product;

@Service
public class ProductService {

	@Autowired
	private ProductDao productDao;

	public Product addProduct(Product p) {
		return productDao.addProduct(p);
	}

	public Product getProductById(String id) {
		Product product = productDao.getProductFromRedis(id);
		if (product == null) {
			product = productDao.getProductById(id);
			productDao.addProductToRedis(product);

		}

		return product;
	}
}
