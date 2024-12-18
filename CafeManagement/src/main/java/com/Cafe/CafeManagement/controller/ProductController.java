package com.Cafe.CafeManagement.controller;

import com.Cafe.CafeManagement.constants.CafeConstants;
import com.Cafe.CafeManagement.service.ProductService;
import com.Cafe.CafeManagement.utils.CafeResponse;
import com.Cafe.CafeManagement.wrapper.ProductWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<String> addNewProduct(@RequestBody(required = true)Map<String, String> request){
        try {
            return productService.addNewProduct(request);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return CafeResponse.getResponseEntity(CafeConstants.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<ProductWrapper>> getAllProduct(){
        try {
            return productService.getAllProduct();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateProduct(@RequestBody(required = true) Map<String, String> request){
        try {
            return productService.updateProduct(request);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return CafeResponse.getResponseEntity(CafeConstants.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/updateStatus")
    public ResponseEntity<String> updateStatus(@RequestBody Map<String, String> request){
        try {
            return productService.updateStatus(request);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return CafeResponse.getResponseEntity(CafeConstants.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Integer id){
        try {
            return productService.deleteProduct(id);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return CafeResponse.getResponseEntity(CafeConstants.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/get-by-category/{id}")
    public ResponseEntity<?> getByCategory(@PathVariable("id") Integer id){
        try {
            return productService.getByCategory(id);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Integer id){
        try {
            return productService.getById(id);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return CafeResponse.getResponseEntity(CafeConstants.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
