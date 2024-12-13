package com.Cafe.CafeManagement.DAO;

import com.Cafe.CafeManagement.POJO.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {
}
