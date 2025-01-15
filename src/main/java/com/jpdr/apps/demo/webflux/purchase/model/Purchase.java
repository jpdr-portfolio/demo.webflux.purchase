package com.jpdr.apps.demo.webflux.purchase.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Table("purchase")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Purchase {
  
  @Id
  @Column("id")
  Long id;
  @Column("purchase_date")
  OffsetDateTime purchaseDate;
  @Column("purchase_description")
  String purchaseDescription;
  @Column("purchase_status")
  String purchaseStatus;
  @Column("product_id")
  Long productId;
  @Column("product_name")
  String productName;
  @Column("product_quantity")
  Integer productQuantity;
  @Column("product_unit_price")
  BigDecimal productUnitPrice;
  @Column("product_total_price")
  BigDecimal productTotalPrice;
  @Column("user_id")
  Long userId;
  @Column("user_email")
  String userEmail;
  @Column("user_address")
  String userAddress;
  @Column("account_id")
  Long accountId;
  @Column("account_number")
  UUID accountNumber;
  @Column("purchase_cancellation_date")
  OffsetDateTime purchaseCancellationDate;
  
}
