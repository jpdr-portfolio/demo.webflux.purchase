package com.jpdr.apps.demo.webflux.purchase.service.enums;

import jakarta.validation.ValidationException;

public enum StockTransactionTypeEnum {

  INCREASE("I"),
  DECREASE("D"),
  SET_PRICE("S");
  
  private final String value;
  
  StockTransactionTypeEnum(String value){
    this.value = value;
  }
  
  public String getValue(){
    return this.value;
  }
  
  public static StockTransactionTypeEnum fromValue(String type){
    for(StockTransactionTypeEnum enumType : StockTransactionTypeEnum.values()){
      String valueType = enumType.getValue();
      if(valueType.equals(type)){
        return enumType;
      }
    }
    throw new ValidationException("Invalid stock transaction type: " + type);
  }

}
