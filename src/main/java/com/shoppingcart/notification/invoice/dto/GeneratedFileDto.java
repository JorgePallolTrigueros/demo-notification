package com.shoppingcart.notification.invoice.dto;

import lombok.Builder;
import lombok.Data;

import java.io.ByteArrayInputStream;
import java.util.Date;

@Data
@Builder
public class GeneratedFileDto {

    private String name;
    private String type;
    private Date createdAt;
    private ByteArrayInputStream file;

}
