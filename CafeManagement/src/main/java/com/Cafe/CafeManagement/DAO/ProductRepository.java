package com.Cafe.CafeManagement.DAO;

import com.Cafe.CafeManagement.POJO.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByCategoryId(Integer id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE product SET name = :name, description =:description, price=:price," +
            " category_fk =:categoryId WHERE id = :id", nativeQuery = true)
    void updateProduct(@Param("id") int id, @Param("name") String name, @Param("description") String description,
                       @Param("price") Long price, @Param("categoryId") int categoryId);
}
