package com.jpdr.apps.demo.webflux.purchase.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpdr.apps.demo.webflux.purchase.exception.dto.ErrorDto;
import com.jpdr.apps.demo.webflux.purchase.exception.stock.StockNotFoundException;
import com.jpdr.apps.demo.webflux.purchase.exception.stock.StockRepositoryException;
import com.jpdr.apps.demo.webflux.purchase.repository.stock.impl.StockRepositoryImpl;
import com.jpdr.apps.demo.webflux.purchase.service.dto.stock.StockDto;
import com.jpdr.apps.demo.webflux.purchase.service.dto.stock.StockTransactionDto;
import com.jpdr.apps.demo.webflux.purchase.service.enums.StockTransactionTypeEnum;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.io.IOException;

import static com.jpdr.apps.demo.webflux.purchase.util.TestDataGenerator.getStockDto;
import static com.jpdr.apps.demo.webflux.purchase.util.TestDataGenerator.getStockTransactionDto;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.DisplayName.class)
class StockRepositoryTest {
  
  
  private StockRepositoryImpl stockRepository;
  
  private static MockWebServer mockWebServer;
  
  private ObjectMapper objectMapper;
  
  @BeforeAll
  static void setupOnce() throws IOException {
    mockWebServer = new MockWebServer();
    mockWebServer.start(0);
  }
  
  
  @BeforeEach
  void setupEach() {
    String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
    WebClient webClient = WebClient.builder()
      .baseUrl(baseUrl).build();
    stockRepository = new StockRepositoryImpl(webClient);
    objectMapper = new ObjectMapper();
  }
  
  @Test
  @DisplayName("OK - Find By Stock By Id")
  void givenUserIdWhenFindUserByIdThenReturnUser() throws JsonProcessingException {
    
    StockDto expectedStock = getStockDto();
    String responseBody = objectMapper.writeValueAsString(expectedStock);
    
    MockResponse response = new MockResponse();
    response.setResponseCode(HttpStatus.OK.value());
    response.setBody(responseBody);
    response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    mockWebServer.enqueue(response);
    
    StepVerifier.create(stockRepository.getById(1L))
      .assertNext(receivedStock -> {
        assertEquals(expectedStock.getProductId(), receivedStock.getProductId());
        assertEquals(expectedStock.getProductName(), receivedStock.getProductName());
        assertEquals(expectedStock.getQuantity(), receivedStock.getQuantity());
        assertEquals(expectedStock.getUnitPrice(), receivedStock.getUnitPrice());
        assertEquals(expectedStock.getLastTransactionId(), receivedStock.getLastTransactionId());
        assertEquals(expectedStock.getLastTransactionDate(), receivedStock.getLastTransactionDate());
      })
      .expectComplete()
      .verify();
  }
  
  
  @Test
  @DisplayName("Error - Find By Stock By Id - Stock Not Found")
  void givenStockNotFoundWhenFindStockByIdThenReturnError() throws JsonProcessingException {
    
    ErrorDto expectedError = new ErrorDto("The stock 1 wasn't found");
    String responseBody = objectMapper.writeValueAsString(expectedError);
    
    MockResponse response = new MockResponse();
    response.setResponseCode(HttpStatus.NOT_FOUND.value());
    response.setBody(responseBody);
    response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    mockWebServer.enqueue(response);
    
    StepVerifier.create(stockRepository.getById(1L))
      .expectError(StockNotFoundException.class)
      .verify();
  }
  
  
  @Test
  @DisplayName("Error - Find By Stock By Id - Internal Server Error")
  void givenInternalServerErrorWhenFindStockByIdThenReturnError() {
    
    MockResponse response = new MockResponse();
    response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    mockWebServer.enqueue(response);
    
    StepVerifier.create(stockRepository.getById(1L))
      .expectError(StockRepositoryException.class)
      .verify();
  }
  
  
  
  
  
  
  
  @Test
  @DisplayName("OK - Create Stock Transaction")
  void givenTransactionWhenCreateTransactionThenReturnTransaction() throws JsonProcessingException {
    
    StockTransactionDto requestTransaction = getStockTransactionDto(StockTransactionTypeEnum.DECREASE,1);
    StockTransactionDto expectedTransaction = getStockTransactionDto(StockTransactionTypeEnum.DECREASE,1);
    String responseBody = objectMapper.writeValueAsString(expectedTransaction);
    
    MockResponse response = new MockResponse();
    response.setResponseCode(HttpStatus.CREATED.value());
    response.setBody(responseBody);
    response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    mockWebServer.enqueue(response);
    
    StepVerifier.create(stockRepository.createTransaction(1L,requestTransaction))
      .assertNext(receivedTransaction -> {
        assertEquals(expectedTransaction.getId(), receivedTransaction.getId());
        assertEquals(expectedTransaction.getQuantity(), receivedTransaction.getQuantity());
        assertEquals(expectedTransaction.getDescription(), receivedTransaction.getDescription());
        assertEquals(expectedTransaction.getTransactionDate(), receivedTransaction.getTransactionDate());
        assertEquals(expectedTransaction.getTransactionType(), receivedTransaction.getTransactionType());
        assertEquals(expectedTransaction.getUnitPrice(), receivedTransaction.getUnitPrice());
      })
      .expectComplete()
      .verify();
  }
  
  
  @Test
  @DisplayName("Error - Create Stock Transaction - Account Not Found")
  void givenAccountNotFoundWhenCreateTransactionThenReturnError() {
    
    StockTransactionDto requestTransaction = getStockTransactionDto(StockTransactionTypeEnum.DECREASE,1);
    
    MockResponse response = new MockResponse();
    response.setResponseCode(HttpStatus.NOT_FOUND.value());
    response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    mockWebServer.enqueue(response);
    
    StepVerifier.create(stockRepository.createTransaction(1L, requestTransaction))
      .expectError(StockNotFoundException.class)
      .verify();
  }
  
  
  @Test
  @DisplayName("Error - Create Stock Transaction - Internal Server Error")
  void givenInternalServerErrorWhenCreateTransactionThenReturnError() {
    
    StockTransactionDto requestTransaction = getStockTransactionDto(StockTransactionTypeEnum.DECREASE,1);
    
    MockResponse response = new MockResponse();
    response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    mockWebServer.enqueue(response);
    
    StepVerifier.create(stockRepository.createTransaction(1L, requestTransaction))
      .expectError(StockRepositoryException.class)
      .verify();
  }
  
  
  
  
  
  
  @AfterAll
  static void closeOnce() throws IOException {
    mockWebServer.shutdown();
  }
  
  
}
