package com.jpdr.apps.demo.webflux.purchase.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpdr.apps.demo.webflux.purchase.exception.account.AccountNotFoundException;
import com.jpdr.apps.demo.webflux.purchase.exception.account.AccountRepositoryException;
import com.jpdr.apps.demo.webflux.purchase.exception.dto.ErrorDto;
import com.jpdr.apps.demo.webflux.purchase.repository.account.impl.AccountRepositoryImpl;
import com.jpdr.apps.demo.webflux.purchase.service.dto.account.AccountDto;
import com.jpdr.apps.demo.webflux.purchase.service.dto.account.AccountTransactionDto;
import com.jpdr.apps.demo.webflux.purchase.service.enums.AccountTransactionTypeEnum;
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

import static com.jpdr.apps.demo.webflux.purchase.util.TestDataGenerator.TOTAL_PRICE;
import static com.jpdr.apps.demo.webflux.purchase.util.TestDataGenerator.getAccountDto;
import static com.jpdr.apps.demo.webflux.purchase.util.TestDataGenerator.getAccountTransactionDto;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.DisplayName.class)
class AccountRepositoryTest {
  
  private AccountRepositoryImpl accountRepository;
  
  private static MockWebServer mockWebServer;
  
  private ObjectMapper objectMapper;
  
  @BeforeAll
  static void setupOnce() throws IOException {
    mockWebServer = new MockWebServer();
    mockWebServer.start(9999);
  }
  
  
  @BeforeEach
  void setupEach() {
    String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
    WebClient webClient = WebClient.builder()
      .baseUrl(baseUrl).build();
    accountRepository = new AccountRepositoryImpl(webClient);
    objectMapper = new ObjectMapper();
  }
  
  @Test
  @DisplayName("OK - Find By Account By Id")
  void givenAccountIdWhenFindAccountByIdThenReturnAccount() throws JsonProcessingException {
    
    AccountDto expectedAccount = getAccountDto();
    String responseBody = objectMapper.writeValueAsString(expectedAccount);
    
    MockResponse response = new MockResponse();
    response.setResponseCode(HttpStatus.OK.value());
    response.setBody(responseBody);
    response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    mockWebServer.enqueue(response);
    
    StepVerifier.create(accountRepository.getById(1))
      .assertNext(receivedAccount -> {
        assertEquals(expectedAccount.getId(), receivedAccount.getId());
        assertEquals(expectedAccount.getNumber(), receivedAccount.getNumber());
        assertEquals(expectedAccount.getBalance(), receivedAccount.getBalance());
        assertEquals(expectedAccount.getOwnerId(), receivedAccount.getOwnerId());
        assertEquals(expectedAccount.getOwnerName(), receivedAccount.getOwnerName());
        assertEquals(expectedAccount.getIsActive(), receivedAccount.getIsActive());
        assertEquals(expectedAccount.getCreationDate(), receivedAccount.getCreationDate());
        assertEquals(expectedAccount.getDeletionDate(), receivedAccount.getDeletionDate());
      })
      .expectComplete()
      .verify();
  }
  
  
  @Test
  @DisplayName("Error - Find By Account By Id - Account Not Found")
  void givenAccountNotFoundWhenFindAccountByIdThenReturnError() throws JsonProcessingException {
    
    ErrorDto expectedError = new ErrorDto("The Account 1 wasn't found");
    String responseBody = objectMapper.writeValueAsString(expectedError);
    
    MockResponse response = new MockResponse();
    response.setResponseCode(HttpStatus.NOT_FOUND.value());
    response.setBody(responseBody);
    response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    mockWebServer.enqueue(response);
    
    StepVerifier.create(accountRepository.getById(1))
      .expectError(AccountNotFoundException.class)
      .verify();
  }
  
  
  @Test
  @DisplayName("Error - Find By Account By Id - Internal Server Error")
  void givenInternalServerErrorWhenFindAccountByIdThenReturnError() {
    
    MockResponse response = new MockResponse();
    response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    mockWebServer.enqueue(response);
    
    StepVerifier.create(accountRepository.getById(1))
      .expectError(AccountRepositoryException.class)
      .verify();
  }
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  @Test
  @DisplayName("OK - Create Account Transaction")
  void givenTransactionWhenCreateTransactionThenReturnTransaction() throws JsonProcessingException {
    
    AccountTransactionDto requestTransaction = getAccountTransactionDto(AccountTransactionTypeEnum.DEBIT,TOTAL_PRICE, TOTAL_PRICE);
    AccountTransactionDto expectedTransaction = getAccountTransactionDto(AccountTransactionTypeEnum.DEBIT,TOTAL_PRICE, TOTAL_PRICE);
    String responseBody = objectMapper.writeValueAsString(expectedTransaction);
    
    MockResponse response = new MockResponse();
    response.setResponseCode(HttpStatus.CREATED.value());
    response.setBody(responseBody);
    response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    mockWebServer.enqueue(response);
    
    StepVerifier.create(accountRepository.createTransaction(1,requestTransaction))
      .assertNext(receivedTransaction -> {
        assertEquals(expectedTransaction.getId(), receivedTransaction.getId());
        assertEquals(expectedTransaction.getTransactionAmount(), receivedTransaction.getTransactionAmount());
        assertEquals(expectedTransaction.getTransactionDescription(), receivedTransaction.getTransactionDescription());
        assertEquals(expectedTransaction.getTransactionDate(), receivedTransaction.getTransactionDate());
        assertEquals(expectedTransaction.getTransactionType(), receivedTransaction.getTransactionType());
        assertEquals(expectedTransaction.getCurrentBalance(), receivedTransaction.getCurrentBalance());
        assertEquals(expectedTransaction.getPreviousBalance(), receivedTransaction.getPreviousBalance());
      })
      .expectComplete()
      .verify();
  }
  
  
  @Test
  @DisplayName("Error - Create Account Transaction - Account Not Found")
  void givenAccountNotFoundWhenCreateTransactionThenReturnError() {
    
    AccountTransactionDto requestTransaction = getAccountTransactionDto(AccountTransactionTypeEnum.DEBIT,TOTAL_PRICE, TOTAL_PRICE);
    
    MockResponse response = new MockResponse();
    response.setResponseCode(HttpStatus.NOT_FOUND.value());
    response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    mockWebServer.enqueue(response);
    
    StepVerifier.create(accountRepository.createTransaction(1, requestTransaction))
      .expectError(AccountNotFoundException.class)
      .verify();
  }
  
  
  @Test
  @DisplayName("Error - Create Account Transaction - Internal Server Error")
  void givenInternalServerErrorWhenCreateTransactionThenReturnError() {
    
    AccountTransactionDto requestTransaction = getAccountTransactionDto(AccountTransactionTypeEnum.DEBIT,TOTAL_PRICE, TOTAL_PRICE);
    
    MockResponse response = new MockResponse();
    response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    mockWebServer.enqueue(response);
    
    StepVerifier.create(accountRepository.createTransaction(1, requestTransaction))
      .expectError(AccountRepositoryException.class)
      .verify();
  }
  
  
  
  
  
  @AfterAll
  static void closeOnce() throws IOException {
    mockWebServer.shutdown();
  }
  
}
