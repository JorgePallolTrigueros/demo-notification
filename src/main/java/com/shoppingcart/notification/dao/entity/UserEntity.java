package com.shoppingcart.notification.dao.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
@Entity(name = "user")
public class UserEntity {

    @Id
    @NotNull
    private String email;

    @NotNull
    private String name;

    @NotNull
    private String phoneNumber;

    @NotNull
    private String address;

    @NotNull
    private String password;

    private Date createdAt;

}
