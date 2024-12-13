package com.Cafe.CafeManagement.controller;

import com.Cafe.CafeManagement.constants.CafeConstants;
import com.Cafe.CafeManagement.service.BillService;
import com.Cafe.CafeManagement.utils.CafeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/bill")
public class BillController {
    @Autowired
    BillService billService;

    @PostMapping("/generateReport")
    public ResponseEntity<String> generateReport(@RequestBody(required = true) Map<String, Object> request){
        try {
            return billService.generateReport(request);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return CafeResponse.getResponseEntity(CafeConstants.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
