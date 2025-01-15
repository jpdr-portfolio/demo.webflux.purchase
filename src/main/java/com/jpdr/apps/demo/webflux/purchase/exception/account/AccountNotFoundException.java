package com.jpdr.apps.demo.webflux.purchase.exception.account;

public class AccountNotFoundException extends RuntimeException{
  
  public AccountNotFoundException(long accountId, Throwable ex) {
    super("The account " + accountId + " wasn't found.", ex);
  }
  
}
