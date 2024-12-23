package com.Res.Restaurant.DAO;

import com.Res.Restaurant.Entity.Product;
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

    @Modifying
    @Transactional
    @Query(value = "UPDATE product set status =:status where id =:id", nativeQuery = true)
    void updateStatus(@Param("id") Integer id, @Param("status") String status);
}
