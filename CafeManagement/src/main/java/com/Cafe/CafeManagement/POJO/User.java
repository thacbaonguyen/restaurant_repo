package com.Cafe.CafeManagement.POJO;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
//@NamedQuery(name = "User.findByEmail", query = "select u from user u where u.email =: email")
@Entity
@Data
@Table(name = "user")
@DynamicInsert
@DynamicUpdate
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "status")
    private String status;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;


}
