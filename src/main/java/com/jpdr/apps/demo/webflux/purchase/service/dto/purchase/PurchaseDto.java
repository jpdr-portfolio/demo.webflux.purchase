package com.jpdr.apps.demo.webflux.purchase.service.dto.purchase;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseDto implements Serializable {
  
  @JsonInclude(Include.NON_NULL)
  Long id;
  @JsonInclude(Include.NON_EMPTY)
  String creationDate;
  @JsonInclude(Include.NON_NULL)
  String description;
  @JsonInclude(Include.NON_NULL)
  Integer quantity;
  @JsonInclude(Include.NON_NULL)
  BigDecimal unitPrice;
  @JsonInclude(Include.NON_NULL)
  BigDecimal totalPrice;
  @JsonInclude(Include.NON_NULL)
  Integer userId;
  @JsonInclude(Include.NON_NULL)
  String userEmail;
  @JsonInclude(Include.NON_NULL)
  String userAddress;
  @JsonInclude(Include.NON_NULL)
  String status;
  @JsonInclude(Include.NON_NULL)
  Integer accountId;
  @JsonInclude(Include.NON_NULL)
  UUID accountNumber;
  @JsonInclude(Include.NON_NULL)
  Integer productId;
  @JsonInclude(Include.NON_NULL)
  String productName;
  @JsonInclude(Include.NON_NULL)
  Integer retailerId;
  @JsonInclude(Include.NON_NULL)
  String retailerName;
  @JsonInclude(Include.NON_NULL)
  String cancellationDate;
  
}
