package com.jpdr.apps.demo.webflux.purchase.exception.stock;

public class StockRepositoryException extends RuntimeException{
  
  public StockRepositoryException(Long productId, Throwable ex){
    super("An error occurred while retrieving stock data for product " + productId +".",ex);
  }
  
}
