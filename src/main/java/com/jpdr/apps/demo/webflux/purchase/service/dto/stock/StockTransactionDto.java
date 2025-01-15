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
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StockTransactionDto implements Serializable {
  
  @JsonInclude(Include.NON_NULL)
  Long id;
  @JsonInclude(Include.NON_NULL)
  Long productId;
  @JsonInclude(Include.NON_NULL)
  String description;
  @NonNull
  Integer quantity;
  @NonNull
  BigDecimal unitPrice;
  @NonNull
  StockTransactionTypeEnum transactionType;
  @JsonInclude(Include.NON_EMPTY)
  String transactionDate;
  
}
