package com.Cafe.CafeManagement.controller;

import com.Cafe.CafeManagement.constants.CafeConstants;
import com.Cafe.CafeManagement.service.UserService;
import com.Cafe.CafeManagement.utils.CafeResponse;
import com.Cafe.CafeManagement.wrapper.UserWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody(required = true) Map<String, String> request){
        try {
            return userService.signUp(request);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeResponse.getResponseEntity(CafeConstants.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody(required = true) Map<String, String> request){
        try {
            return userService.login(request);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeResponse.getResponseEntity(CafeConstants.SOME_THING_WENT_WRONG, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<UserWrapper>> getAllUser(){
        try {
            return userService.getAllUser();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<String> changPassword(@RequestBody(required = true) Map<String, String> request){
        try {
            return userService.changePassword(request);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return CafeResponse.getResponseEntity(CafeConstants.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
