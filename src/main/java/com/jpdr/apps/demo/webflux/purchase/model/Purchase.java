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
  @Column("creation_date")
  OffsetDateTime creationDate;
  @Column("description")
  String description;
  @Column("quantity")
  Integer quantity;
  @Column("unit_price")
  BigDecimal unitPrice;
  @Column("total_price")
  BigDecimal totalPrice;
  @Column("user_id")
  Integer userId;
  @Column("user_email")
  String userEmail;
  @Column("user_address")
  String userAddress;
  @Column("status")
  String status;
  @Column("account_id")
  Integer accountId;
  @Column("account_number")
  UUID accountNumber;
  @Column("product_id")
  Integer productId;
  @Column("product_name")
  String productName;
  @Column("retailer_id")
  Integer retailerId;
  @Column("retailer_name")
  String retailerName;
  @Column("cancellation_date")
  OffsetDateTime cancellationDate;
  
}
