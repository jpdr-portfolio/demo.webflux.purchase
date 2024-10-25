package com.jpdr.apps.demo.webflux.purchase.exception.stock;

public class InsufficientQuantityException extends RuntimeException{
  
  public InsufficientQuantityException(Integer productId, Integer quantity){
    super("The quantity of product "+ productId + " in stock is less than required. Current quantity: " + quantity);
  }
  
}
