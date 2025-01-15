package com.jpdr.apps.demo.webflux.purchase.repository.account.impl;

import com.jpdr.apps.demo.webflux.purchase.exception.account.AccountNotFoundException;
import com.jpdr.apps.demo.webflux.purchase.exception.account.AccountRepositoryException;
import com.jpdr.apps.demo.webflux.purchase.repository.account.AccountRepository;
import com.jpdr.apps.demo.webflux.purchase.service.dto.account.AccountDto;
import com.jpdr.apps.demo.webflux.purchase.service.dto.account.AccountTransactionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
public class AccountRepositoryImpl implements AccountRepository {
  
  private final WebClient webClient;
  
  public AccountRepositoryImpl(@Qualifier(value = "accountWebClient") WebClient webClient ){
    this.webClient = webClient;
  }
  
  @Override
  @Cacheable(key = "#accountId", value = "accounts", sync = true)
  public Mono<AccountDto> getById (Long accountId) {
    return this.webClient.get()
      .uri("/accounts/{accountId}", accountId)
      .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .retrieve()
      .onStatus(HttpStatus.NOT_FOUND::equals,
        response -> response.createException()
          .map(error -> new AccountNotFoundException(accountId,error)))
      .onStatus(HttpStatusCode::isError,
        response -> response.createException()
          .map(error -> new AccountRepositoryException(accountId, error))
      )
      .bodyToMono(AccountDto.class);
  }
  
  @Override
  public Mono<AccountTransactionDto> createTransaction(Long accountId, AccountTransactionDto transaction) {
    return this.webClient.post()
      .uri("/accounts/{accountId}/transactions", accountId)
      .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .bodyValue(transaction)
      .retrieve()
      .onStatus(HttpStatus.NOT_FOUND::equals,
        response -> response.createException()
          .map(error -> new AccountNotFoundException(accountId,error)))
      .onStatus(HttpStatusCode::isError,
        response -> response.createException()
          .map(error -> new AccountRepositoryException(accountId, error))
      )
      .bodyToMono(AccountTransactionDto.class);
  }
}
