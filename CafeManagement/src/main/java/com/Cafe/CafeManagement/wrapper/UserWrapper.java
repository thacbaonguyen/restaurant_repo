package com.Cafe.CafeManagement.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserWrapper {
    private Integer id;

    private String name;

    private String phoneNumber;

    private String email;

    private String status;
    private String role;

}
