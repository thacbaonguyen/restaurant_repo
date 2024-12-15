package com.Cafe.CafeManagement.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;

public class CafeResponse {
    public CafeResponse(){

    }
    public static ResponseEntity<String> getResponseEntity(String response, HttpStatus httpStatus){
        return new ResponseEntity<String>("{\"message\":\""+response+"\"}", httpStatus);
    }

}
