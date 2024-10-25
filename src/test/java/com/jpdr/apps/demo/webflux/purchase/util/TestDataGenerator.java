package com.jpdr.apps.demo.webflux.purchase.util;

import com.jpdr.apps.demo.webflux.purchase.model.Purchase;
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
import com.jpdr.apps.demo.webflux.purchase.service.mapper.PurchaseMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

public class TestDataGenerator {
  
  public static final String CREATION_DATE = "2024-10-14T10:39:45.732446-03:00";
  public static final String PRODUCT_NAME = "Product Name";
  public static final String CATEGORY_NAME = "Category Name";
  public static final String RETAILER_NAME = "Retailer Name";
  public static final String DESCRIPTION = "Description";
  public static final String NAME = "John Smith";
  public static final String EMAIL = "johnsmith@mail.com";
  public static final String ADDRESS = "123 Street, City, State";
  public static final BigDecimal TOTAL_PRICE = new BigDecimal("100.00");
  public static final UUID NUMBER = UUID.fromString("f9bcbc7c-8bbe-4306-8feb-7fd598686d4e");
  
  
  public static PurchaseDto getNewPurchaseDto(){
    return PurchaseDto.builder()
      .id(null)
      .userId(1)
      .userAddress(null)
      .userEmail(null)
      .description(null)
      .retailerId(null)
      .retailerName(null)
      .accountId(1)
      .accountNumber(null)
      .productId(1)
      .productName(null)
      .quantity(1)
      .unitPrice(TOTAL_PRICE)
      .totalPrice(TOTAL_PRICE)
      .status(null)
      .creationDate(null)
      .cancellationDate(null)
      .build();
  }
  
  public static List<PurchaseDto> getPurchasesDto(){
    return getList(TestDataGenerator::getPurchaseDto);
  }
  
  public static PurchaseDto getPurchaseDto(){
    return getPurchaseDto(1);
  }
  
  public static PurchaseDto getPurchaseDto(int purchaseId){
    return PurchaseMapper.INSTANCE.entityToDto(getPurchase(purchaseId));
  }
  
  public static List<Purchase> getPurchases(){
    return getList(TestDataGenerator::getPurchase);
  }
  
  
  public static Purchase getPurchase(){
    return getPurchase(1);
  }
  
  
  public static Purchase getPurchase(long purchaseId){
    return Purchase.builder()
      .id(purchaseId)
      .description(DESCRIPTION)
      .accountId(1)
      .userId(1)
      .userAddress(ADDRESS)
      .userEmail(EMAIL)
      .retailerId(1)
      .retailerName(RETAILER_NAME)
      .productId(1)
      .productName(PRODUCT_NAME)
      .quantity(1)
      .status(PurchaseStatusEnum.FULFILLED.getValue())
      .totalPrice(TOTAL_PRICE)
      .build();
  }
  
  public static UserDto getUserDto(){
    return getUserDto(1);
  }
  
  public static UserDto getUserDto(int id){
    return UserDto.builder()
      .id(id)
      .name(NAME)
      .email(EMAIL)
      .address(ADDRESS)
      .isActive(true)
      .creationDate(CREATION_DATE)
      .deletionDate(null)
      .build();
  }
  
  
  public static ProductDto getProductDto(){
    return getProductDto(1);
  }
  public static ProductDto getProductDto(int productId){
    return ProductDto.builder()
      .id(productId)
      .name(PRODUCT_NAME)
      .categoryId(1)
      .categoryName(CATEGORY_NAME)
      .retailerId(1)
      .retailerName(RETAILER_NAME)
      .isActive(true)
      .creationDate(CREATION_DATE)
      .deletionDate(null)
      .build();
  }
  

  
  public static StockDto getStockDto(){
    return getStockDto(1);
  }
  
  public static StockDto getStockDto(int productId){
    return StockDto.builder()
      .productId(productId)
      .quantity(100)
      .unitPrice(BigDecimal.valueOf(100.00))
      .lastTransactionId(1)
      .lastTransactionDate(CREATION_DATE)
      .build();
  }
  
  
  public static AccountDto getAccountDto(){
    return getAccountDto(1);
  }
  
  public static AccountDto getAccountDto(int accountId){
    return AccountDto.builder()
      .id(accountId)
      .ownerId(1)
      .ownerName(NAME)
      .creationDate(CREATION_DATE)
      .balance(TOTAL_PRICE)
      .deletionDate(null)
      .lastTransactionDate(null)
      .lastTransactionId(null)
      .number(NUMBER)
      .isActive(true)
      .build();
  }
  
  public static AccountTransactionDto getAccountTransactionDto(
    AccountTransactionTypeEnum type, BigDecimal amount, BigDecimal previousBalance){
    return AccountTransactionDto.builder()
      .id(1)
      .transactionDate((CREATION_DATE))
      .transactionAmount(amount)
      .transactionDescription(DESCRIPTION)
      .transactionType(type)
      .previousBalance(previousBalance)
      .currentBalance(previousBalance.add(amount))
      .build();
  }
  
  
  public static StockTransactionDto getStockTransactionDto(StockTransactionTypeEnum type, int quantity){
    return StockTransactionDto.builder()
      .id(1)
      .productId(1)
      .quantity(quantity)
      .description(DESCRIPTION)
      .unitPrice(BigDecimal.ZERO)
      .transactionDate(CREATION_DATE)
      .transactionType(type)
      .build();
  }
  
  
  private static <R> List<R> getList(Function<Integer,R> function){
    return Stream.iterate(1, n -> n + 1)
      .limit(3)
      .map(function)
      .toList();
  }
  
  
  
}
