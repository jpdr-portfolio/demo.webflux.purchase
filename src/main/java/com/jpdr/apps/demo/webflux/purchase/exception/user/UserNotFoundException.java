package com.jpdr.apps.demo.webflux.purchase.exception.user;

public class UserNotFoundException extends RuntimeException{
  
  public UserNotFoundException(long userId, Throwable ex){
    super("User "+ userId +" not found", ex);
  }
  
}
