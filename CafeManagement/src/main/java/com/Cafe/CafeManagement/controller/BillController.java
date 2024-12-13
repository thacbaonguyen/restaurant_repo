package com.Cafe.CafeManagement.controller;

import com.Cafe.CafeManagement.POJO.Bill;
import com.Cafe.CafeManagement.constants.CafeConstants;
import com.Cafe.CafeManagement.service.BillService;
import com.Cafe.CafeManagement.utils.CafeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
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

    @GetMapping("/getBills")
    public ResponseEntity<List<Bill>> getBills(){
        try {
            return billService.getBills();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/getPdf")
    public ResponseEntity<byte[]> getPdf(@RequestBody Map<String, Object> request){
        try {
            return billService.getPdf(request);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBill(@PathVariable("id") Integer id){
        try {
            return billService.deleteBill(id);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return CafeResponse.getResponseEntity(CafeConstants.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
