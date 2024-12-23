package com.Res.Restaurant.wrapper;

import com.Res.Restaurant.Entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductWrapper {
    private Integer id;

    private String name;

    private String description;

    private Long price;

    private String status;

    private Category category;
}
