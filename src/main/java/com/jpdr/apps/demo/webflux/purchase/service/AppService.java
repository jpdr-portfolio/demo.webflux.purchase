package com.jpdr.apps.demo.webflux.purchase.service;

import com.jpdr.apps.demo.webflux.purchase.service.dto.purchase.PurchaseDto;
import reactor.core.publisher.Mono;

import java.util.List;

public interface AppService {
  
  Mono<List<PurchaseDto>> findPurchases(Long userId);
  Mono<List<PurchaseDto>> findAllPurchases();
  Mono<List<PurchaseDto>> findPurchasesByUserId(Long userId);
  Mono<PurchaseDto> findPurchaseById(Long purchaseId);
  Mono<PurchaseDto> createPurchase(PurchaseDto purchaseDto);
  Mono<PurchaseDto> cancelPurchaseById(Long purchaseId);

}
