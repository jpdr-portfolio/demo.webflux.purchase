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
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AppController {
  
  private final AppService appService;
  
  @GetMapping("/purchases")
  public Mono<ResponseEntity<List<PurchaseDto>>> findPurchases(
    @RequestParam(name = "userId", required = false) Integer userId ){
    return this.appService.findPurchases(userId)
      .map(purchase -> new ResponseEntity<>(purchase, HttpStatus.OK));
  }
  
  @PostMapping("/purchases")
  public Mono<ResponseEntity<PurchaseDto>> createPurchase(@RequestBody PurchaseDto purchaseDto){
    return this.appService.createPurchase(purchaseDto)
      .map(purchase -> new ResponseEntity<>(purchase, HttpStatus.CREATED));
  }
  
  @GetMapping("/purchases/{purchaseId}")
  public Mono<ResponseEntity<PurchaseDto>> findPurchaseById(
    @PathVariable(name = "purchaseId") Integer purchaseId ){
    return this.appService.findPurchaseById(purchaseId)
      .map(purchase -> new ResponseEntity<>(purchase, HttpStatus.OK));
  }
  
  @PostMapping("/purchases/{purchaseId}/cancel")
  public Mono<ResponseEntity<PurchaseDto>> cancelPurchase(
    @PathVariable(name = "purchaseId") Integer purchaseId){
    return this.appService.cancelPurchaseById(purchaseId)
      .map(purchase -> new ResponseEntity<>(purchase, HttpStatus.OK));
  }
  
}
