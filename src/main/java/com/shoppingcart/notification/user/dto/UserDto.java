package com.shoppingcart.notification.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor   // new UserDto()
@AllArgsConstructor //  new UserDto(email,......)
public class UserDto {
    private String email;
    private String name;
    private String phoneNumber;
    private String address;
    private Date createdAt;
}
