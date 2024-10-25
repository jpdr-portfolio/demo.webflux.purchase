package com.jpdr.apps.demo.webflux.purchase.controller;

import com.jpdr.apps.demo.webflux.purchase.service.AppService;
import com.jpdr.apps.demo.webflux.purchase.service.dto.purchase.PurchaseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AppController {
  
  private final AppService appService;
  
  @GetMapping("/purchases")
  public ResponseEntity<Flux<PurchaseDto>> findPurchases(@RequestParam(name = "userId", required = false) Integer userId ){
    return new ResponseEntity<>(appService.findPurchases(userId), HttpStatus.OK);
  }
  
  @PostMapping("/purchases")
  public ResponseEntity<Mono<PurchaseDto>> createPurchase(@RequestBody PurchaseDto purchaseDto){
    return new ResponseEntity<>(appService.createPurchase(purchaseDto), HttpStatus.CREATED);
  }
  
  @GetMapping("/purchases/{purchaseId}")
  public ResponseEntity<Mono<PurchaseDto>> findPurchaseById(@PathVariable(name = "purchaseId") Integer purchaseId ){
    return new ResponseEntity<>(appService.findPurchaseById(purchaseId), HttpStatus.OK);
  }
  
  @PostMapping("/purchases/{purchaseId}/cancel")
  public ResponseEntity<Mono<PurchaseDto>> cancelPurchase(@PathVariable(name = "purchaseId") Integer purchaseId){
    return new ResponseEntity<>(appService.cancelPurchaseById(purchaseId), HttpStatus.OK);
  }
  
}
