package com.Cafe.CafeManagement.service;

import com.Cafe.CafeManagement.wrapper.UserWrapper;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface UserService {
    ResponseEntity<String> signUp(Map<String, String> request);
    ResponseEntity<String> login(Map<String, String> request);
    ResponseEntity<List<UserWrapper>> getAllUser();

    ResponseEntity<String> changePassword(Map<String, String> request);
}
