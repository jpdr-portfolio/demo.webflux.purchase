package com.jpdr.apps.demo.webflux.purchase.repository.product.impl;

import com.jpdr.apps.demo.webflux.purchase.exception.product.ProductNotFoundException;
import com.jpdr.apps.demo.webflux.purchase.exception.product.ProductRepositoryException;
import com.jpdr.apps.demo.webflux.purchase.repository.product.ProductRepository;
import com.jpdr.apps.demo.webflux.purchase.service.dto.product.ProductDto;
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
public class ProductRepositoryImpl implements ProductRepository {
  
  private final WebClient webClient;
  
  public ProductRepositoryImpl(@Qualifier(value = "productWebClient") WebClient webClient ){
    this.webClient = webClient;
  }
  
  @Override
  @Cacheable(key = "#productId", value = "products", sync = true)
  public Mono<ProductDto> getById (Long productId) {
    return this.webClient.get()
      .uri("/products/{productId}", productId)
      .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .retrieve()
      .onStatus(HttpStatus.NOT_FOUND::equals,
        response -> response.createException()
          .map(error -> new ProductNotFoundException(productId,error)))
      .onStatus(HttpStatusCode::isError,
        response -> response.createException()
          .map(error -> new ProductRepositoryException(productId, error))
      )
      .bodyToMono(ProductDto.class);
  }
}
