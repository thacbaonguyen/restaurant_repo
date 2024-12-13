package com.Cafe.CafeManagement.DAO;

import com.Cafe.CafeManagement.POJO.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {
    List<Bill> findByCreatedBy(String email);
}
