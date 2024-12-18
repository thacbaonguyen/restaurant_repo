package com.Cafe.CafeManagement.service;

import com.Cafe.CafeManagement.wrapper.ProductWrapper;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface ProductService {
    ResponseEntity<String> addNewProduct(Map<String, String> request);
    ResponseEntity<List<ProductWrapper>> getAllProduct();
    ResponseEntity<String> updateProduct(Map<String, String> request);

    ResponseEntity<String> deleteProduct(Integer id);
    ResponseEntity<?> getByCategory(Integer id);

    ResponseEntity<?> getById(Integer id);

    ResponseEntity<String> updateStatus(Map<String, String> request);
}
