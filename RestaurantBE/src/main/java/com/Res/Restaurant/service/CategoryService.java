package com.Res.Restaurant.service;

import com.Res.Restaurant.Entity.Category;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    ResponseEntity<String> addNewCategory(Map<String, String> request);
    ResponseEntity<List<Category>> getAllCategory();
    ResponseEntity<String> updateCategory(Map<String, String> request);
}
