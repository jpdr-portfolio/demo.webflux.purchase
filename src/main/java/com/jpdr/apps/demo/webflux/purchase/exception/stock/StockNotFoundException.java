package com.jpdr.apps.demo.webflux.purchase.exception.stock;

public class StockNotFoundException extends RuntimeException{
  
  public StockNotFoundException(Long productId, Throwable ex){
    super("There is no stock for product " + productId, ex);
  }
  
}
