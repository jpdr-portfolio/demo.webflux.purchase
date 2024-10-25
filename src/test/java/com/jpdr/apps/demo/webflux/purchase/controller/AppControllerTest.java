package com.jpdr.apps.demo.webflux.purchase.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpdr.apps.demo.webflux.purchase.service.AppService;
import com.jpdr.apps.demo.webflux.purchase.service.dto.purchase.PurchaseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static com.jpdr.apps.demo.webflux.purchase.util.TestDataGenerator.getNewPurchaseDto;
import static com.jpdr.apps.demo.webflux.purchase.util.TestDataGenerator.getPurchaseDto;
import static com.jpdr.apps.demo.webflux.purchase.util.TestDataGenerator.getPurchasesDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@ExtendWith(MockitoExtension.class)
class AppControllerTest {
  
  @Autowired
  private WebTestClient webTestClient;
  @MockBean
  private AppService appService;
  @Autowired
  private ObjectMapper objectMapper;
  
  
  @Test
  @DisplayName("OK - Find Purchases - No userId")
  void givenPurchasesNoUserIdWhenFindPurchasesThenReturnPurchases() throws JsonProcessingException {
    
    List<PurchaseDto> expectedPurchases = getPurchasesDto();
    
    String expectedBody = objectMapper.writeValueAsString(expectedPurchases);
    
    when(appService.findPurchases(isNull()))
      .thenReturn(Flux.fromIterable(expectedPurchases));
    
    FluxExchangeResult<String> exchangeResult = this.webTestClient.get()
      .uri("/purchases")
      .exchange()
      .expectHeader()
      .contentType(MediaType.APPLICATION_JSON)
      .expectStatus()
      .isOk()
      .returnResult(String.class);
    
    StepVerifier.create(exchangeResult.getResponseBody())
      .assertNext(receivedBody -> assertEquals(expectedBody, receivedBody))
      .expectComplete()
      .verify();
  
  }
  
  @Test
  @DisplayName("OK - Find Purchases - With UserId")
  void givenPurchasesWithUserIdWhenFindPurchasesThenReturnPurchases() throws JsonProcessingException{
    
    List<PurchaseDto> expectedPurchases = getPurchasesDto();
    
    String expectedBody = objectMapper.writeValueAsString(expectedPurchases);
    
    when(appService.findPurchases(anyInt()))
      .thenReturn(Flux.fromIterable(expectedPurchases));
    
    FluxExchangeResult<String> exchangeResult = this.webTestClient.get()
      .uri("/purchases" + "?userId=" + 1)
      .exchange()
      .expectHeader()
      .contentType(MediaType.APPLICATION_JSON)
      .expectStatus()
      .isOk()
      .returnResult(String.class);
    
    StepVerifier.create(exchangeResult.getResponseBody())
      .assertNext(receivedBody -> assertEquals(expectedBody, receivedBody))
      .expectComplete()
      .verify();
  }
  
  
  @Test
  @DisplayName("OK - Find Purchase By Id")
  void givenPurchaseWhenFindByIdThenReturnPurchase() {
    
    PurchaseDto expectedBody = getPurchaseDto();
    
    when(appService.findPurchaseById(anyInt()))
      .thenReturn(Mono.just(expectedBody));
    
    FluxExchangeResult<PurchaseDto> exchangeResult = this.webTestClient.get()
      .uri("/purchases" + "/" + 1)
      .exchange()
      .expectHeader()
      .contentType(MediaType.APPLICATION_JSON)
      .expectStatus()
      .isOk()
      .returnResult(PurchaseDto.class);
    
    StepVerifier.create(exchangeResult.getResponseBody())
      .assertNext(receivedBody -> assertEquals(expectedBody, receivedBody))
      .expectComplete()
      .verify();
  }
  
  @Test
  @DisplayName("OK - Create Purchase")
  void givenPurchaseWhenCreatePurchaseReturnPurchase() {
    
    PurchaseDto requestBody = getNewPurchaseDto();
    PurchaseDto expectedBody = getPurchaseDto();
    
    when(appService.createPurchase(any(PurchaseDto.class)))
      .thenReturn(Mono.just(expectedBody));
    
    FluxExchangeResult<PurchaseDto> exchangeResult = this.webTestClient.post()
      .uri("/purchases")
      .bodyValue(requestBody)
      .exchange()
      .expectHeader()
      .contentType(MediaType.APPLICATION_JSON)
      .expectStatus()
      .isCreated()
      .returnResult(PurchaseDto.class);
    
    StepVerifier.create(exchangeResult.getResponseBody())
      .assertNext(receivedBody -> assertEquals(expectedBody, receivedBody))
      .expectComplete()
      .verify();
    
  }
  
  @Test
  @DisplayName("OK - Cancel Purchase By Id")
  void givenPurchaseWhenCancelPurchaseByIdThenReturnEmpty() {
    
    PurchaseDto expectedBody = getPurchaseDto();
    
    when(appService.cancelPurchaseById(anyInt()))
      .thenReturn(Mono.just(expectedBody));
    
    FluxExchangeResult<PurchaseDto> exchangeResult = this.webTestClient.post()
      .uri("/purchases" + "/" + 1 + "/" + "cancel")
      .exchange()
      .expectHeader()
      .contentType(MediaType.APPLICATION_JSON)
      .expectStatus()
      .isOk()
      .returnResult(PurchaseDto.class);
    
    StepVerifier.create(exchangeResult.getResponseBody())
      .assertNext(receivedBody -> assertEquals(expectedBody, receivedBody))
      .expectComplete()
      .verify();
  }
  
  
}


