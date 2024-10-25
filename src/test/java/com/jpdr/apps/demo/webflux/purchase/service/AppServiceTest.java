package com.jpdr.apps.demo.webflux.purchase.service;

import com.jpdr.apps.demo.webflux.purchase.exception.account.InsufficientFundsException;
import com.jpdr.apps.demo.webflux.purchase.exception.stock.InsufficientQuantityException;
import com.jpdr.apps.demo.webflux.purchase.model.Purchase;
import com.jpdr.apps.demo.webflux.purchase.repository.account.AccountRepository;
import com.jpdr.apps.demo.webflux.purchase.repository.product.ProductRepository;
import com.jpdr.apps.demo.webflux.purchase.repository.purchase.PurchaseRepository;
import com.jpdr.apps.demo.webflux.purchase.repository.stock.StockRepository;
import com.jpdr.apps.demo.webflux.purchase.repository.user.UserRepository;
import com.jpdr.apps.demo.webflux.purchase.service.dto.account.AccountDto;
import com.jpdr.apps.demo.webflux.purchase.service.dto.account.AccountTransactionDto;
import com.jpdr.apps.demo.webflux.purchase.service.dto.product.ProductDto;
import com.jpdr.apps.demo.webflux.purchase.service.dto.purchase.PurchaseDto;
import com.jpdr.apps.demo.webflux.purchase.service.dto.stock.StockDto;
import com.jpdr.apps.demo.webflux.purchase.service.dto.stock.StockTransactionDto;
import com.jpdr.apps.demo.webflux.purchase.service.dto.user.UserDto;
import com.jpdr.apps.demo.webflux.purchase.service.enums.AccountTransactionTypeEnum;
import com.jpdr.apps.demo.webflux.purchase.service.enums.PurchaseStatusEnum;
import com.jpdr.apps.demo.webflux.purchase.service.enums.StockTransactionTypeEnum;
import com.jpdr.apps.demo.webflux.purchase.service.impl.AppServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.jpdr.apps.demo.webflux.purchase.util.TestDataGenerator.TOTAL_PRICE;
import static com.jpdr.apps.demo.webflux.purchase.util.TestDataGenerator.getAccountDto;
import static com.jpdr.apps.demo.webflux.purchase.util.TestDataGenerator.getAccountTransactionDto;
import static com.jpdr.apps.demo.webflux.purchase.util.TestDataGenerator.getNewPurchaseDto;
import static com.jpdr.apps.demo.webflux.purchase.util.TestDataGenerator.getProductDto;
import static com.jpdr.apps.demo.webflux.purchase.util.TestDataGenerator.getPurchase;
import static com.jpdr.apps.demo.webflux.purchase.util.TestDataGenerator.getPurchases;
import static com.jpdr.apps.demo.webflux.purchase.util.TestDataGenerator.getStockDto;
import static com.jpdr.apps.demo.webflux.purchase.util.TestDataGenerator.getStockTransactionDto;
import static com.jpdr.apps.demo.webflux.purchase.util.TestDataGenerator.getUserDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.DisplayName.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AppServiceTest {
  
  @InjectMocks
  private AppServiceImpl appService;
  @Mock
  private PurchaseRepository purchaseRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private ProductRepository productRepository;
  @Mock
  private AccountRepository accountRepository;
  @Mock
  private StockRepository stockRepository;
  
  
  @Test
  @DisplayName("OK - Find Purchases - Null User Id")
  void givenNullUserIdWhenFindPurchasesThenReturnAllPurchases(){
    
    List<Purchase> expectedPurchases = getPurchases();
    Map<Long, Purchase> expectedPurchasesMap = expectedPurchases.stream()
      .collect(Collectors.toMap(Purchase::getId, Function.identity()));
    
    when(purchaseRepository.findAll())
      .thenReturn(Flux.fromIterable(expectedPurchases));
    
    StepVerifier.create(appService.findPurchases(null))
      .assertNext(receivedPurchase -> assertPurchase(expectedPurchasesMap.get(receivedPurchase.getId()),
        receivedPurchase))
      .assertNext(receivedPurchase -> assertPurchase(expectedPurchasesMap.get(receivedPurchase.getId()),
      receivedPurchase))
      .assertNext(receivedPurchase -> assertPurchase(expectedPurchasesMap.get(receivedPurchase.getId()),
      receivedPurchase))
      .expectComplete()
      .verify();
    
  }
  
  @Test
  @DisplayName("OK - Find Purchases - With User Id")
  void givenUserIdWhenFindPurchasesThenReturnAllPurchases(){
    
    List<Purchase> expectedPurchases = getPurchases();
    Map<Long, Purchase> expectedPurchasesMap = expectedPurchases.stream()
      .collect(Collectors.toMap(Purchase::getId, Function.identity()));
    UserDto userDto = getUserDto();
    
    when(userRepository.getById(anyInt()))
      .thenReturn(Mono.just(userDto));
    
    when(purchaseRepository.findAllByUserId(anyInt()))
      .thenReturn(Flux.fromIterable(expectedPurchases));
    
    StepVerifier.create(appService.findPurchases(1))
      .assertNext(receivedPurchase -> assertPurchase(expectedPurchasesMap.get(receivedPurchase.getId()),
        receivedPurchase))
      .assertNext(receivedPurchase -> assertPurchase(expectedPurchasesMap.get(receivedPurchase.getId()),
        receivedPurchase))
      .assertNext(receivedPurchase -> assertPurchase(expectedPurchasesMap.get(receivedPurchase.getId()),
        receivedPurchase))
      .expectComplete()
      .verify();
  }
  
  @Test
  @DisplayName("OK - Find Purchase By Id")
  void givenPurchaseIdWhenFindPurchaseThenReturnPurchase(){
    
    Purchase expectedPurchase = getPurchase();
    
    when(purchaseRepository.findById(anyInt()))
      .thenReturn(Mono.just(expectedPurchase));
    
    StepVerifier.create(appService.findPurchaseById(1))
      .assertNext(receivedPurchase -> assertPurchase(expectedPurchase,
        receivedPurchase))
      .expectComplete()
      .verify();
  }
  
  @Test
  @DisplayName("OK - Create Purchase")
  void givenPurchaseWhenCreatePurchaseThenReturnPurchase(){
    
    PurchaseDto requestPurchase = getNewPurchaseDto();
    UserDto userDto = getUserDto();
    ProductDto productDto = getProductDto();
    StockDto stockDto = getStockDto();
    AccountDto accountDto = getAccountDto();
    AccountTransactionDto accountTransactionDto =
      getAccountTransactionDto(AccountTransactionTypeEnum.DEBIT, TOTAL_PRICE,TOTAL_PRICE);
    StockTransactionDto stockTransactionDto =
      getStockTransactionDto(StockTransactionTypeEnum.DECREASE, 1);
    Purchase expectedPurchase = getPurchase();
    
    when(userRepository.getById(anyInt()))
      .thenReturn(Mono.just(userDto));
    when(productRepository.getById(anyInt()))
      .thenReturn(Mono.just(productDto));
    when(accountRepository.getById(anyInt()))
      .thenReturn(Mono.just(accountDto));
    when(stockRepository.getById(anyInt()))
      .thenReturn(Mono.just(stockDto));
    when(accountRepository.createTransaction(anyInt(), any(AccountTransactionDto.class)))
      .thenReturn(Mono.just(accountTransactionDto));
    when(stockRepository.createTransaction(anyInt(), any(StockTransactionDto.class)))
      .thenReturn(Mono.just(stockTransactionDto));
    when(purchaseRepository.save(any(Purchase.class)))
      .thenReturn(Mono.just(expectedPurchase));
    
    StepVerifier.create(appService.createPurchase(requestPurchase))
      .assertNext(receivedPurchase -> {
        assertEquals(1L, receivedPurchase.getId());
        assertEquals(userDto.getId(), receivedPurchase.getUserId());
        assertEquals(accountDto.getId(), receivedPurchase.getAccountId());
        assertEquals(productDto.getId(), receivedPurchase.getProductId());
        assertEquals(1, receivedPurchase.getQuantity());
        assertEquals(TOTAL_PRICE, receivedPurchase.getTotalPrice());
      })
      .expectComplete()
      .verify();
    
  }
  
  @Test
  @DisplayName("Error - Create Purchase - Insufficient Funds")
  void givenInsufficientFundsWhenCreatePurchaseThenReturnError(){
    
    PurchaseDto requestPurchase = getNewPurchaseDto();
    UserDto userDto = getUserDto();
    ProductDto productDto = getProductDto();
    StockDto stockDto = getStockDto();
    AccountDto accountDto = getAccountDto();
    accountDto.setBalance(BigDecimal.ZERO);
    
    when(userRepository.getById(anyInt()))
      .thenReturn(Mono.just(userDto));
    when(productRepository.getById(anyInt()))
      .thenReturn(Mono.just(productDto));
    when(accountRepository.getById(anyInt()))
      .thenReturn(Mono.just(accountDto));
    when(stockRepository.getById(anyInt()))
      .thenReturn(Mono.just(stockDto));
    
    StepVerifier.create(appService.createPurchase(requestPurchase))
      .expectError(InsufficientFundsException.class)
      .verify();
    
  }
  
  @Test
  @DisplayName("Error - Create Purchase - Insufficient Quantity")
  void givenInsufficientQuantityWhenCreatePurchaseThenReturnError(){
    
    PurchaseDto requestPurchase = getNewPurchaseDto();
    UserDto userDto = getUserDto();
    ProductDto productDto = getProductDto();
    StockDto stockDto = getStockDto();
    stockDto.setQuantity(0);
    AccountDto accountDto = getAccountDto();
    
    when(userRepository.getById(anyInt()))
      .thenReturn(Mono.just(userDto));
    when(productRepository.getById(anyInt()))
      .thenReturn(Mono.just(productDto));
    when(accountRepository.getById(anyInt()))
      .thenReturn(Mono.just(accountDto));
    when(stockRepository.getById(anyInt()))
      .thenReturn(Mono.just(stockDto));
    
    StepVerifier.create(appService.createPurchase(requestPurchase))
      .expectError(InsufficientQuantityException.class)
      .verify();
    
  }
  
  
  
  
  @Test
  @DisplayName("OK - Cancel Purchase")
  void givenPurchaseWhenCancelPurchaseThenReturnPurchase(){
    
    Purchase expectedPurchase = getPurchase();
    AccountTransactionDto accountTransactionDto =
      getAccountTransactionDto(AccountTransactionTypeEnum.DEBIT, TOTAL_PRICE,TOTAL_PRICE);
    StockTransactionDto stockTransactionDto =
      getStockTransactionDto(StockTransactionTypeEnum.DECREASE, 1);
    
    when(purchaseRepository.findById(anyInt()))
      .thenReturn(Mono.just(expectedPurchase));
    when(accountRepository.createTransaction(anyInt(), any(AccountTransactionDto.class)))
      .thenReturn(Mono.just(accountTransactionDto));
    when(stockRepository.createTransaction(anyInt(), any(StockTransactionDto.class)))
      .thenReturn(Mono.just(stockTransactionDto));
    when(purchaseRepository.save(any(Purchase.class)))
      .thenReturn(Mono.just(expectedPurchase));
    
    StepVerifier.create(appService.cancelPurchaseById(1))
      .assertNext(receivedPurchase -> {
        assertEquals(1L, receivedPurchase.getId());
        assertEquals(PurchaseStatusEnum.CANCELED.getValue(), receivedPurchase.getStatus());
        assertNotNull(receivedPurchase.getCancellationDate());
      })
      .expectComplete()
      .verify();
    
  }
  
  
  
  
  
  
  
  
  
  private static void assertPurchase(Purchase entity, PurchaseDto dto){
    assertEquals(entity.getId(), dto.getId());
    assertEquals(entity.getAccountId(), dto.getAccountId());
    assertEquals(entity.getAccountNumber(), dto.getAccountNumber());
    assertEquals(entity.getQuantity(), dto.getQuantity());
    assertEquals(entity.getDescription(), dto.getDescription());
    assertEquals(entity.getProductId(), dto.getProductId());
    assertEquals(entity.getProductName(), dto.getProductName());
    assertEquals(entity.getRetailerId(), dto.getRetailerId());
    assertEquals(entity.getRetailerName(), dto.getRetailerName());
    assertEquals(entity.getUnitPrice(), dto.getUnitPrice());
    assertEquals(entity.getTotalPrice(), dto.getTotalPrice());
  }
  

}
