package com.Cafe.CafeManagement.service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface BillService {
    ResponseEntity<String> generateReport(Map<String, Object> request);
}
