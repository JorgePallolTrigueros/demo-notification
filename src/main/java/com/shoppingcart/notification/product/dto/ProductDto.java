package com.shoppingcart.notification.product.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private Long id;

    private String name;

    private String description;

    private double price;

    private String category;

    private Date createdAt;

    private BigDecimal quantity;

    @Builder.Default
    private List<GalleryDto> galleries = new ArrayList<>();
}

