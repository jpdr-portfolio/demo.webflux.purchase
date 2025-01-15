package com.jpdr.apps.demo.webflux.purchase.service.dto.account;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.jpdr.apps.demo.webflux.purchase.service.enums.AccountTransactionTypeEnum;
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
public class AccountTransactionDto implements Serializable {
  
  @JsonInclude(Include.NON_NULL)
  Long id;
  @JsonInclude(Include.NON_EMPTY)
  String transactionDate;
  @NonNull
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
