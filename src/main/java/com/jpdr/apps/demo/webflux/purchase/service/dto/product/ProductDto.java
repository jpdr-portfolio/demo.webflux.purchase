package com.jpdr.apps.demo.webflux.purchase.service.dto.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.NonNull;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDto {
  
  @JsonInclude(Include.NON_NULL)
  Integer id;
  @JsonInclude(Include.NON_NULL)
  String name;
  @JsonInclude(Include.NON_NULL)
  Integer categoryId;
  @JsonInclude(Include.NON_NULL)
  String categoryName;
  @JsonInclude(Include.NON_NULL)
  Integer retailerId;
  @JsonInclude(Include.NON_NULL)
  String retailerName;
  @JsonInclude(Include.NON_NULL)
  Boolean isActive;
  @JsonInclude(Include.NON_EMPTY)
  String creationDate;
  @JsonInclude(Include.NON_EMPTY)
  String deletionDate;
  
}
