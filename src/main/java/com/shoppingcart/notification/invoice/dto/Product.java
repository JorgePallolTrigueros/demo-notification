package com.shoppingcart.notification.invoice.dto;


import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

  private Long id;
  private BigDecimal quantity;
  private String name;
  private String category;
  private String description;
  private List<String> galleries;
  private BigDecimal price;
  private BigDecimal subtotal;
}

