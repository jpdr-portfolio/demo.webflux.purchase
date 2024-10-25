package com.jpdr.apps.demo.webflux.purchase.service.dto.account;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.jpdr.apps.demo.webflux.purchase.service.enums.AccountTransactionTypeEnum;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountTransactionDto {
  
  @JsonInclude(Include.NON_NULL)
  Integer id;
  @JsonInclude(Include.NON_EMPTY)
  String transactionDate;
  @JsonInclude(Include.NON_NULL)
  BigDecimal transactionAmount;
  @JsonInclude(Include.NON_NULL)
  AccountTransactionTypeEnum transactionType;
  @JsonInclude(Include.NON_NULL)
  String transactionDescription;
  @JsonInclude(Include.NON_NULL)
  BigDecimal previousBalance;
  @JsonInclude(Include.NON_NULL)
  BigDecimal currentBalance;
  
}
