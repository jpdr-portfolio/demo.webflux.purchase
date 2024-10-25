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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
  
  public static final String AN_ERROR_HAS_OCCURRED = "An error has occurred";
  
  @ExceptionHandler(MethodNotAllowedException.class)
  ResponseEntity<Mono<ErrorDto>> handleException(MethodNotAllowedException ex){
    log.warn(ExceptionUtils.getStackTrace(ex));
    ErrorDto errorDto = new ErrorDto(ex.getMessage());
    return new ResponseEntity<>(Mono.just(errorDto), HttpStatus.BAD_REQUEST);
  }
  
  @ExceptionHandler(ServerWebInputException.class)
  ResponseEntity<Mono<ErrorDto>> handleException(ServerWebInputException ex){
    log.warn(ExceptionUtils.getStackTrace(ex));
    ErrorDto errorDto = new ErrorDto(ex.getMessage());
    return new ResponseEntity<>(Mono.just(errorDto), HttpStatus.BAD_REQUEST);
  }
  
  @ExceptionHandler(ValidationException.class)
  ResponseEntity<Mono<ErrorDto>> handleException(ValidationException ex){
    log.warn(ExceptionUtils.getStackTrace(ex));
    ErrorDto errorDto = new ErrorDto(ex.getMessage());
    return new ResponseEntity<>(Mono.just(errorDto), HttpStatus.BAD_REQUEST);
  }
  
  @ExceptionHandler(UserNotFoundException.class)
  ResponseEntity<Mono<ErrorDto>> handleException(UserNotFoundException ex){
    log.warn(ExceptionUtils.getStackTrace(ex));
    ErrorDto errorDto = new ErrorDto(ex.getMessage());
    return new ResponseEntity<>(Mono.just(errorDto), HttpStatus.BAD_REQUEST);
  }
  
  @ExceptionHandler(AccountNotFoundException.class)
  ResponseEntity<Mono<ErrorDto>> handleException(AccountNotFoundException ex){
    log.warn(ExceptionUtils.getStackTrace(ex));
    ErrorDto errorDto = new ErrorDto(ex.getMessage());
    return new ResponseEntity<>(Mono.just(errorDto), HttpStatus.BAD_REQUEST);
  }
  
  @ExceptionHandler(ProductNotFoundException.class)
  ResponseEntity<Mono<ErrorDto>> handleException(ProductNotFoundException ex){
    log.warn(ExceptionUtils.getStackTrace(ex));
    ErrorDto errorDto = new ErrorDto(ex.getMessage());
    return new ResponseEntity<>(Mono.just(errorDto), HttpStatus.BAD_REQUEST);
  }
  
  
  @ExceptionHandler(StockNotFoundException.class)
  ResponseEntity<Mono<ErrorDto>> handleException(StockNotFoundException ex){
    log.warn(ExceptionUtils.getStackTrace(ex));
    ErrorDto errorDto = new ErrorDto(ex.getMessage());
    return new ResponseEntity<>(Mono.just(errorDto), HttpStatus.BAD_REQUEST);
  }
  
  
  @ExceptionHandler(InsufficientFundsException.class)
  ResponseEntity<Mono<ErrorDto>> handleException(InsufficientFundsException ex){
    log.warn(ExceptionUtils.getStackTrace(ex));
    ErrorDto errorDto = new ErrorDto(ex.getMessage());
    return new ResponseEntity<>(Mono.just(errorDto), HttpStatus.BAD_REQUEST);
  }
  
  
  @ExceptionHandler(InsufficientQuantityException.class)
  ResponseEntity<Mono<ErrorDto>> handleException(InsufficientQuantityException ex){
    log.warn(ExceptionUtils.getStackTrace(ex));
    ErrorDto errorDto = new ErrorDto(ex.getMessage());
    return new ResponseEntity<>(Mono.just(errorDto), HttpStatus.BAD_REQUEST);
  }
  
  
  
  
  
  @ExceptionHandler(UserRepositoryException.class)
  ResponseEntity<Mono<ErrorDto>> handleException(UserRepositoryException ex){
    log.error(ExceptionUtils.getStackTrace(ex));
    ErrorDto errorDto = new ErrorDto(AN_ERROR_HAS_OCCURRED);
    return new ResponseEntity<>(Mono.just(errorDto), HttpStatus.INTERNAL_SERVER_ERROR);
  }
  
  @ExceptionHandler(AccountRepositoryException.class)
  ResponseEntity<Mono<ErrorDto>> handleException(AccountRepositoryException ex){
    log.error(ExceptionUtils.getStackTrace(ex));
    ErrorDto errorDto = new ErrorDto(AN_ERROR_HAS_OCCURRED);
    return new ResponseEntity<>(Mono.just(errorDto), HttpStatus.INTERNAL_SERVER_ERROR);
  }
  
  @ExceptionHandler(ProductRepositoryException.class)
  ResponseEntity<Mono<ErrorDto>> handleException(ProductRepositoryException ex){
    log.error(ExceptionUtils.getStackTrace(ex));
    ErrorDto errorDto = new ErrorDto(AN_ERROR_HAS_OCCURRED);
    return new ResponseEntity<>(Mono.just(errorDto), HttpStatus.INTERNAL_SERVER_ERROR);
  }
  
  @ExceptionHandler(StockRepositoryException.class)
  ResponseEntity<Mono<ErrorDto>> handleException(StockRepositoryException ex){
    log.error(ExceptionUtils.getStackTrace(ex));
    ErrorDto errorDto = new ErrorDto(AN_ERROR_HAS_OCCURRED);
    return new ResponseEntity<>(Mono.just(errorDto), HttpStatus.INTERNAL_SERVER_ERROR);
  }
  
  
  @ExceptionHandler(RuntimeException.class)
  ResponseEntity<Mono<ErrorDto>> handleException(RuntimeException ex){
    log.error(ExceptionUtils.getStackTrace(ex));
    ErrorDto errorDto = new ErrorDto(AN_ERROR_HAS_OCCURRED);
    return new ResponseEntity<>(Mono.just(errorDto), HttpStatus.INTERNAL_SERVER_ERROR);
  }
  
  @ExceptionHandler(Exception.class)
  ResponseEntity<Mono<ErrorDto>> handleException(Exception ex){
    log.error(ExceptionUtils.getStackTrace(ex));
    ErrorDto errorDto = new ErrorDto(AN_ERROR_HAS_OCCURRED);
    return new ResponseEntity<>(Mono.just(errorDto), HttpStatus.INTERNAL_SERVER_ERROR);
  }
  
  
  
}
