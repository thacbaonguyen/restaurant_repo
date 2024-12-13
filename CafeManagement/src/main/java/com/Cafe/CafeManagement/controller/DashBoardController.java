package com.Cafe.CafeManagement.controller;

import com.Cafe.CafeManagement.service.DashBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashBoard")
public class DashBoardController {
    @Autowired
    DashBoardService dashBoardService;
    @GetMapping("/details")
    public ResponseEntity<?> getCount(){
        return dashBoardService.getCount();
    }
}
