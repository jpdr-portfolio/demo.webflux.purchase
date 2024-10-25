package com.jpdr.apps.demo.webflux.purchase.exception.account;

public class AccountRepositoryException extends RuntimeException{
  public AccountRepositoryException(int accountId, Throwable ex) {
    super("There was an error while retrieving the account " + accountId + ".", ex);
  }
}
