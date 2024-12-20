package com.jpdr.apps.demo.webflux.purchase.service.dto.stock;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.jpdr.apps.demo.webflux.purchase.service.enums.StockTransactionTypeEnum;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StockTransactionDto implements Serializable {
  
  @JsonInclude(Include.NON_NULL)
  Integer id;
  @JsonInclude(Include.NON_NULL)
  Integer productId;
  @JsonInclude(Include.NON_NULL)
  String description;
  @JsonInclude(Include.NON_NULL)
  Integer quantity;
  @JsonInclude(Include.NON_NULL)
  BigDecimal unitPrice;
  @JsonInclude(Include.NON_NULL)
  StockTransactionTypeEnum transactionType;
  @JsonInclude(Include.NON_EMPTY)
  String transactionDate;
  
}
