package com.Res.Restaurant.DAO;

import com.Res.Restaurant.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Category findByName(String name);

    @Modifying
    @Transactional
    @Query(value = "UPDATE category SET name = :name WHERE id = :id", nativeQuery = true)
    void updateCategoryNameById(@Param("id") int id, @Param("name") String name);
}
