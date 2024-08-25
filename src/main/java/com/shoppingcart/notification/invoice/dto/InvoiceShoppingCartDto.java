package com.shoppingcart.notification.invoice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceShoppingCartDto {

  private String id;
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime datetime;
  private String businessId;
  private String businessName;
  private BigDecimal subtotal;
  private BigDecimal total;
  private BigDecimal totalTax;
  private String taxDescription;
  private BigDecimal tax;
  private List<ProductDto> productDtos;

}

