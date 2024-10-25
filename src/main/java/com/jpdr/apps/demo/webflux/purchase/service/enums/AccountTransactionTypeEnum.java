package com.jpdr.apps.demo.webflux.purchase.service.enums;

import jakarta.validation.ValidationException;

public enum AccountTransactionTypeEnum {
  
  DEBIT("D"),
  CREDIT("C");
  
  private final String value;
  
  AccountTransactionTypeEnum(String value){
    this.value = value;
  }
  
  public String getValue(){
    return this.value;
  }
  
  public static AccountTransactionTypeEnum fromValue(String type){
    for(AccountTransactionTypeEnum enumType : AccountTransactionTypeEnum.values()){
      String valueType = enumType.getValue();
      if(valueType.equals(type)){
        return enumType;
      }
    }
    throw new ValidationException("Invalid account transaction type: " + type);
  }
  
}
