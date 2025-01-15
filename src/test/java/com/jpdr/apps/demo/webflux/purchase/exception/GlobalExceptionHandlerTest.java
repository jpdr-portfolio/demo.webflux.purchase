package com.jpdr.apps.demo.webflux.purchase.exception;

import com.jpdr.apps.demo.webflux.purchase.exception.account.AccountNotFoundException;
import com.jpdr.apps.demo.webflux.purchase.exception.account.AccountRepositoryException;
import com.jpdr.apps.demo.webflux.purchase.exception.account.InsufficientFundsException;
import com.jpdr.apps.demo.webflux.purchase.exception.dto.ErrorDto;
import com.jpdr.apps.demo.webflux.purchase.exception.product.ProductNotFoundException;
import com.jpdr.apps.demo.webflux.purchase.exception.product.ProductRepositoryException;
import com.jpdr.apps.demo.webflux.purchase.exception.stock.InsufficientQuantityException;
import com.jpdr.apps.demo.webflux.purchase.exception.stock.StockNotFoundException;
import com.jpdr.apps.demo.webflux.purchase.exception.stock.StockRepositoryException;
import com.jpdr.apps.demo.webflux.purchase.exception.user.UserNotFoundException;
import com.jpdr.apps.demo.webflux.purchase.exception.user.UserRepositoryException;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {
  
  @InjectMocks
  GlobalExceptionHandler globalExceptionHandler;
  
  @Test
  @DisplayName("Error - MethodNotAllowedException")
  void givenMethodNotAllowedExceptionWhenHandleExceptionThenReturnError(){
    MethodNotAllowedException exception = new MethodNotAllowedException(HttpMethod.GET, List.of(HttpMethod.POST));
    ResponseEntity<Mono<ErrorDto>> response = globalExceptionHandler.handleException(exception);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }
  
  @Test
  @DisplayName("Error - ServerWebInputException")
  void givenServerWebInputExceptionWhenHandleExceptionThenReturnError(){
    ServerWebInputException exception = new ServerWebInputException("");
    ResponseEntity<Mono<ErrorDto>> response = globalExceptionHandler.handleException(exception);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }
  
  @Test
  @DisplayName("Error - ValidationException")
  void givenValidationExceptionWhenHandleValidationExceptionThenReturnError(){
    ValidationException exception = new ValidationException("");
    ResponseEntity<Mono<ErrorDto>> response = globalExceptionHandler.handleException(exception);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }
  
  
  @Test
  @DisplayName("Error - UserNotFoundException")
  void givenUserNotFoundExceptionWhenHandleUserNotFoundExceptionThenReturnError(){
    UserNotFoundException exception = new UserNotFoundException(1L, new RuntimeException());
    ResponseEntity<Mono<ErrorDto>> response = globalExceptionHandler.handleException(exception);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }
  

  
  @Test
  @DisplayName("Error - AccountNotFoundException")
  void givenAccountNotFoundExceptionWhenHandleExceptionThenReturnError(){
    AccountNotFoundException exception = new AccountNotFoundException(1L, new RuntimeException());
    ResponseEntity<Mono<ErrorDto>> response = globalExceptionHandler.handleException(exception);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }
  
  @Test
  @DisplayName("Error - ProductNotFoundException")
  void givenProductNotFoundExceptionWhenHandleExceptionThenReturnError(){
    ProductNotFoundException exception = new ProductNotFoundException(1L, new RuntimeException());
    ResponseEntity<Mono<ErrorDto>> response = globalExceptionHandler.handleException(exception);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }
  
  @Test
  @DisplayName("Error - StockNotFoundException")
  void givenStockNotFoundExceptionWhenHandleExceptionThenReturnError(){
    StockNotFoundException exception = new StockNotFoundException(1L, new RuntimeException());
    ResponseEntity<Mono<ErrorDto>> response = globalExceptionHandler.handleException(exception);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }
  
  
  @Test
  @DisplayName("Error - InsufficientFundsException")
  void givenInsufficientFundsExceptionWhenHandleExceptionThenReturnError(){
    InsufficientFundsException exception = new InsufficientFundsException(BigDecimal.ONE);
    ResponseEntity<Mono<ErrorDto>> response = globalExceptionHandler.handleException(exception);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }
  
  @Test
  @DisplayName("Error - InsufficientFundsException")
  void givenInsufficientQuantityExceptionWhenHandleExceptionThenReturnError(){
    InsufficientQuantityException exception = new InsufficientQuantityException(1L, 0);
    ResponseEntity<Mono<ErrorDto>> response = globalExceptionHandler.handleException(exception);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }
  

  
  
  
  @Test
  @DisplayName("Error - UserRepositoryException")
  void givenUserRepositoryExceptionWhenHandleExceptionThenReturnError(){
    UserRepositoryException exception = new UserRepositoryException(1L, new RuntimeException());
    ResponseEntity<Mono<ErrorDto>> response = globalExceptionHandler.handleException(exception);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }
  
  
  @Test
  @DisplayName("Error - ProductRepositoryException")
  void givenProductRepositoryExceptionWhenHandleExceptionThenReturnError(){
    ProductRepositoryException exception = new ProductRepositoryException(1L, new RuntimeException());
    ResponseEntity<Mono<ErrorDto>> response = globalExceptionHandler.handleException(exception);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }
  
  
  @Test
  @DisplayName("Error - AccountRepositoryException")
  void givenAccountRepositoryExceptionWhenHandleExceptionThenReturnError(){
    AccountRepositoryException exception = new AccountRepositoryException(1L, new RuntimeException());
    ResponseEntity<Mono<ErrorDto>> response = globalExceptionHandler.handleException(exception);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }
  
  
  @Test
  @DisplayName("Error - StockRepositoryException")
  void givenStockRepositoryExceptionWhenHandleExceptionThenReturnError(){
    StockRepositoryException exception = new StockRepositoryException(1L, new RuntimeException());
    ResponseEntity<Mono<ErrorDto>> response = globalExceptionHandler.handleException(exception);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }
  
  

  
  @Test
  @DisplayName("Error - RuntimeException")
  void givenRuntimeExceptionWhenHandleRuntimeExceptionThenReturnError(){
    RuntimeException exception = new RuntimeException();
    ResponseEntity<Mono<ErrorDto>> response = globalExceptionHandler.handleException(exception);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }
  
  @Test
  @DisplayName("Error - Exception")
  void givenExceptionWhenHandleExceptionThenReturnError(){
    Exception exception = new Exception();
    ResponseEntity<Mono<ErrorDto>> response = globalExceptionHandler.handleException(exception);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }
  
}
