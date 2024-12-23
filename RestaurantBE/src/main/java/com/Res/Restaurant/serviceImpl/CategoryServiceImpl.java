package com.Res.Restaurant.serviceImpl;

import com.Res.Restaurant.DAO.CategoryRepository;
import com.Res.Restaurant.JWT.JwtFilter;
import com.Res.Restaurant.Entity.Category;
import com.Res.Restaurant.constants.MessageConstants;
import com.Res.Restaurant.service.CategoryService;
import com.Res.Restaurant.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    JwtFilter jwtFilter;
    @Autowired
    CategoryRepository categoryRepository;
    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> request) {
        try {
            if(jwtFilter.isAdmin()){
                if(validateCategory(request)){
                    Category category = categoryRepository.findByName(request.get("name"));
                    if(!Objects.isNull(category)){
                        return ApiResponse.getResponseEntity("This category has existed!", HttpStatus.BAD_REQUEST);
                    }
                    categoryRepository.save(getCategoryFromMap(request));
                    return ApiResponse.getResponseEntity(MessageConstants.CREATE_SUCCESSFULLY, HttpStatus.OK);
                }
                return ApiResponse.getResponseEntity("Invalid data type", HttpStatus.BAD_REQUEST);
            }
            return ApiResponse.getResponseEntity("You are not admin, please leave now!", HttpStatus.UNAUTHORIZED);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return ApiResponse.getResponseEntity(MessageConstants.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Category>> getAllCategory() {
        try {
            return new ResponseEntity<>(categoryRepository.findAll(), HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> request) {
        try {
            if (jwtFilter.isAdmin()){
                if (validateCategory(request) && request.containsKey("id")){
                    Optional<Category> category = categoryRepository.findById(Integer.parseInt(request.get("id")));
                    if (!category.isEmpty()){
                        categoryRepository.updateCategoryNameById(Integer.parseInt(request.get("id")), request.get("name"));
                        return ApiResponse.getResponseEntity("Update category successfully!", HttpStatus.OK);
                    }
                    return ApiResponse.getResponseEntity("This category does existed", HttpStatus.BAD_REQUEST);
                }
                return ApiResponse.getResponseEntity(MessageConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
            return ApiResponse.getResponseEntity(MessageConstants.UN_AUTHORIZED, HttpStatus.UNAUTHORIZED);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return ApiResponse.getResponseEntity(MessageConstants.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateCategory(Map<String, String> request){
        return request.containsKey("name");
    }
    private Category getCategoryFromMap(Map<String, String> request){
        Category category = new Category();
        category.setName(request.get("name"));
        return category;
    }
}
