package com.jpdr.apps.demo.webflux.purchase.exception.product;

public class ProductNotFoundException extends RuntimeException{
  
  public ProductNotFoundException(Integer productId, Throwable ex){
    super("The product " + productId + " wasn't found.", ex);
  }
  
}
