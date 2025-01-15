package com.jpdr.apps.demo.webflux.purchase.exception.product;

public class ProductRepositoryException extends RuntimeException{
  
  public ProductRepositoryException(Long productId, Throwable ex){
    super("There was an error while retrieving the product " + productId, ex);
  }
  
}
