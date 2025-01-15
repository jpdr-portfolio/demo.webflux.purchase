package com.jpdr.apps.demo.webflux.purchase.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpdr.apps.demo.webflux.purchase.exception.dto.ErrorDto;
import com.jpdr.apps.demo.webflux.purchase.exception.product.ProductNotFoundException;
import com.jpdr.apps.demo.webflux.purchase.exception.product.ProductRepositoryException;
import com.jpdr.apps.demo.webflux.purchase.repository.product.impl.ProductRepositoryImpl;
import com.jpdr.apps.demo.webflux.purchase.service.dto.product.ProductDto;
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

import static com.jpdr.apps.demo.webflux.purchase.util.TestDataGenerator.getProductDto;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.DisplayName.class)
class ProductRepositoryTest {

  private ProductRepositoryImpl productRepository;
  
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
    productRepository = new ProductRepositoryImpl(webClient);
    objectMapper = new ObjectMapper();
  }
  
  @Test
  @DisplayName("OK - Find By Product By Id")
  void givenUserIdWhenFindUserByIdThenReturnUser() throws JsonProcessingException {
    
    ProductDto expectedProduct = getProductDto();
    String responseBody = objectMapper.writeValueAsString(expectedProduct);
    
    MockResponse response = new MockResponse();
    response.setResponseCode(HttpStatus.OK.value());
    response.setBody(responseBody);
    response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    mockWebServer.enqueue(response);
    
    StepVerifier.create(productRepository.getById(1L))
      .assertNext(receivedProduct -> {
        assertEquals(expectedProduct.getId(), receivedProduct.getId());
        assertEquals(expectedProduct.getProductName(), receivedProduct.getProductName());
        assertEquals(expectedProduct.getCategoryId(), receivedProduct.getCategoryId());
        assertEquals(expectedProduct.getCategoryName(), receivedProduct.getCategoryName());
        assertEquals(expectedProduct.getIsActive(), receivedProduct.getIsActive());
        assertEquals(expectedProduct.getCreationDate(), receivedProduct.getCreationDate());
        assertEquals(expectedProduct.getDeletionDate(), receivedProduct.getDeletionDate());
      })
      .expectComplete()
      .verify();
  }
  
  
  @Test
  @DisplayName("Error - Find By Product By Id - Product Not Found")
  void givenProductNotFoundWhenFindProductByIdThenReturnError() throws JsonProcessingException {
    
    ErrorDto expectedError = new ErrorDto("The product 1 wasn't found");
    String responseBody = objectMapper.writeValueAsString(expectedError);
    
    MockResponse response = new MockResponse();
    response.setResponseCode(HttpStatus.NOT_FOUND.value());
    response.setBody(responseBody);
    response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    mockWebServer.enqueue(response);
    
    StepVerifier.create(productRepository.getById(1L))
      .expectError(ProductNotFoundException.class)
      .verify();
  }
  
  
  @Test
  @DisplayName("Error - Find By Product By Id - Internal Server Error")
  void givenInternalServerErrorWhenFindProductByIdThenReturnError() {
    
    MockResponse response = new MockResponse();
    response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    mockWebServer.enqueue(response);
    
    StepVerifier.create(productRepository.getById(1L))
      .expectError(ProductRepositoryException.class)
      .verify();
  }
  
  
  
  
  
  @AfterAll
  static void closeOnce() throws IOException {
    mockWebServer.shutdown();
  }

}
