package com.Cafe.CafeManagement.service;

import com.Cafe.CafeManagement.POJO.Category;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    ResponseEntity<String> addNewCategory(Map<String, String> request);
    ResponseEntity<List<Category>> getAllCategory();
    ResponseEntity<String> updateCategory(Map<String, String> request);
}
