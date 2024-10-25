package com.jpdr.apps.demo.webflux.purchase.service;

import com.jpdr.apps.demo.webflux.purchase.service.dto.purchase.PurchaseDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AppService {
  
  Flux<PurchaseDto> findPurchases(Integer userId);
  Flux<PurchaseDto> findAllPurchases();
  Flux<PurchaseDto> findPurchasesByUserId(Integer userId);
  Mono<PurchaseDto> findPurchaseById(Integer purchaseId);
  Mono<PurchaseDto> createPurchase(PurchaseDto purchaseDto);
  Mono<PurchaseDto> cancelPurchaseById(Integer purchaseId);

}
