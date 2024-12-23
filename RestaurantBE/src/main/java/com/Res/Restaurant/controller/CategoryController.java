package com.Res.Restaurant.controller;

import com.Res.Restaurant.Entity.Category;
import com.Res.Restaurant.constants.MessageConstants;
import com.Res.Restaurant.service.CategoryService;
import com.Res.Restaurant.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @PostMapping("/add")
    public ResponseEntity<String> addNewCategory(@RequestBody(required = true) Map<String, String> request){
        try {
            return categoryService.addNewCategory(request);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return ApiResponse.getResponseEntity(MessageConstants.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Category>> getAllCategory(){
        try {
            return categoryService.getAllCategory();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateCategory(@RequestBody(required = true) Map<String, String> request){
        try {
            return categoryService.updateCategory(request);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return ApiResponse.getResponseEntity(MessageConstants.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
