package com.jpdr.apps.demo.webflux.purchase.service.enums;

import jakarta.validation.ValidationException;

public enum PurchaseStatusEnum {
  
  FULFILLED("F"),
  CANCELED("C");
  
  private final String value;
  
  PurchaseStatusEnum(String value){
    this.value = value;
  }
  
  public String getValue(){
    return this.value;
  }
  
  public static PurchaseStatusEnum fromValue(String status){
    for(PurchaseStatusEnum enumType : PurchaseStatusEnum.values()){
      String statusValue = enumType.getValue();
      if(statusValue.equals(status)){
        return enumType;
      }
    }
    throw new ValidationException("Invalid purchase status: " + status);
  }
  
}
