package com.jpdr.apps.demo.webflux.purchase.repository.stock.impl;

import com.jpdr.apps.demo.webflux.purchase.exception.stock.StockNotFoundException;
import com.jpdr.apps.demo.webflux.purchase.exception.stock.StockRepositoryException;
import com.jpdr.apps.demo.webflux.purchase.repository.stock.StockRepository;
import com.jpdr.apps.demo.webflux.purchase.service.dto.stock.StockDto;
import com.jpdr.apps.demo.webflux.purchase.service.dto.stock.StockTransactionDto;
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
public class StockRepositoryImpl implements StockRepository {
  
  private final WebClient webClient;
  
  public StockRepositoryImpl(@Qualifier(value = "stockWebClient") WebClient webClient ){
    this.webClient = webClient;
  }
  
  @Override
  @Cacheable(key = "#productId", value = "stock", sync = true)
  public Mono<StockDto> getById (Long productId) {
    return this.webClient.get()
      .uri("/stock/{productId}", productId)
      .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .retrieve()
      .onStatus(HttpStatus.NOT_FOUND::equals,
        response -> response.createException()
          .map(error -> new StockNotFoundException(productId,error)))
      .onStatus(HttpStatusCode::isError,
        response -> response.createException()
          .map(error -> new StockRepositoryException(productId, error))
      )
      .bodyToMono(StockDto.class);
  }
  
  @Override
  public Mono<StockTransactionDto> createTransaction(Long productId, StockTransactionDto transactionDto) {
    return this.webClient.post()
      .uri("/stock/{productId}/transactions", productId)
      .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .bodyValue(transactionDto)
      .retrieve()
      .onStatus(HttpStatus.NOT_FOUND::equals,
        response -> response.createException()
          .map(error -> new StockNotFoundException(productId,error)))
      .onStatus(HttpStatusCode::isError,
        response -> response.createException()
          .map(error -> new StockRepositoryException(productId, error))
      )
      .bodyToMono(StockTransactionDto.class);
  }
}
