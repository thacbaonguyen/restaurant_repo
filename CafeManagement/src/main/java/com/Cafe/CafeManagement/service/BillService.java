package com.Cafe.CafeManagement.service;

import com.Cafe.CafeManagement.POJO.Bill;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface BillService {
    ResponseEntity<String> generateReport(Map<String, Object> request);
    ResponseEntity<List<Bill>> getBills();
    ResponseEntity<byte[]> getPdf(Map<String, Object> request);
    ResponseEntity<String> deleteBill(Integer id);
}
