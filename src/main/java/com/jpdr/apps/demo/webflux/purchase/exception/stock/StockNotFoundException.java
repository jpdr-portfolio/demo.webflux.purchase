package com.jpdr.apps.demo.webflux.purchase.exception.stock;

public class StockNotFoundException extends RuntimeException{
  
  public StockNotFoundException(Integer productId, Throwable ex){
    super("There is no stock for product " + productId, ex);
  }
  
}
