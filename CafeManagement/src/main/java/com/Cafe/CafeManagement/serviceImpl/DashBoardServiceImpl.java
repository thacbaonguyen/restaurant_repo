package com.Cafe.CafeManagement.serviceImpl;

import com.Cafe.CafeManagement.DAO.BillRepository;
import com.Cafe.CafeManagement.DAO.CategoryRepository;
import com.Cafe.CafeManagement.DAO.ProductRepository;
import com.Cafe.CafeManagement.service.DashBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashBoardServiceImpl implements DashBoardService {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    BillRepository billRepository;
    @Override
    public ResponseEntity<?> getCount() {
        Map<String, Object> map = new HashMap<>();
        map.put("category", categoryRepository.count());
        map.put("product", productRepository.count());
        map.put("bill", billRepository.count());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
