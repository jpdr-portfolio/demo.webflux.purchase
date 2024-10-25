package com.jpdr.apps.demo.webflux.purchase.service.impl;

import com.jpdr.apps.demo.webflux.purchase.exception.account.InsufficientFundsException;
import com.jpdr.apps.demo.webflux.purchase.exception.stock.InsufficientQuantityException;
import com.jpdr.apps.demo.webflux.purchase.model.Purchase;
import com.jpdr.apps.demo.webflux.purchase.repository.account.AccountRepository;
import com.jpdr.apps.demo.webflux.purchase.repository.product.ProductRepository;
import com.jpdr.apps.demo.webflux.purchase.repository.purchase.PurchaseRepository;
import com.jpdr.apps.demo.webflux.purchase.repository.stock.StockRepository;
import com.jpdr.apps.demo.webflux.purchase.repository.user.UserRepository;
import com.jpdr.apps.demo.webflux.purchase.service.AppService;
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
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple5;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppServiceImpl implements AppService {
  
  private static final String CANCELED = "Canceled";
  
  private final PurchaseRepository purchaseRepository;
  private final UserRepository userRepository;
  private final ProductRepository productRepository;
  private final AccountRepository accountRepository;
  private final StockRepository stockRepository;
  
  @Override
  public Flux<PurchaseDto> findPurchases(Integer userId) {
    return Mono.just(Optional.ofNullable(userId))
      .flatMapMany(optional -> {
          if (optional.isPresent()) {
            return findPurchasesByUserId(optional.get());
          }
          return findAllPurchases();
        }
      );
  }
  
  @Override
  public Flux<PurchaseDto> findAllPurchases() {
    log.debug("findAllPurchases");
    return this.purchaseRepository.findAll()
      .map(PurchaseMapper.INSTANCE::entityToDto)
      .doOnNext(purchase -> log.debug(purchase.toString()));
  }
  
  
  @Override
  public Flux<PurchaseDto> findPurchasesByUserId(Integer userId) {
    log.debug("findPurchasesByUserId");
    return this.userRepository.getById(userId)
      .map(UserDto::getId)
      .flatMapMany(this.purchaseRepository::findAllByUserId)
      .map(PurchaseMapper.INSTANCE::entityToDto)
      .doOnNext(purchase -> log.debug(purchase.toString()));
  }
  
  @Override
  public Mono<PurchaseDto> findPurchaseById(Integer purchaseId) {
    log.debug("findPurchaseById");
    return this.purchaseRepository.findById(purchaseId)
      .map(PurchaseMapper.INSTANCE::entityToDto)
      .doOnNext(purchase -> log.debug(purchase.toString()));
  }
  
  @Override
  @Transactional
  public Mono<PurchaseDto> createPurchase(PurchaseDto purchaseDto) {
    log.debug("createPurchase");
    return Mono.from(isValidPurchase(purchaseDto))
      .flatMap(purchase -> Mono.zip(
        Mono.just(purchase),
        Mono.from(this.userRepository.getById(purchase.getUserId())),
        Mono.from(this.productRepository.getById(purchase.getProductId())),
        Mono.from(this.accountRepository.getById(purchase.getAccountId())),
        Mono.from(this.stockRepository.getById(purchase.getProductId()))
      ))
      .flatMap(this::getPurchaseFromData)
      .flatMap(this.purchaseRepository::save)
      .flatMap(savedPurchase ->
          Mono.zip(
            Mono.just(savedPurchase),
            Mono.from(createAccountTransaction(savedPurchase))))
      .flatMap(tuple ->
        Mono.zip(
          Mono.just(tuple.getT1()),
          Mono.from(createStockTransaction(tuple.getT1())
            .onErrorResume(ex -> cancelAccountTransaction(tuple.getT1().getAccountId(),tuple.getT2())
              .flatMap(canceledAccountTransaction -> Mono.error(ex))))))
      .map(tuple -> PurchaseMapper.INSTANCE.entityToDto(tuple.getT1()));
  }
  
  @Override
  @Transactional
  public Mono<PurchaseDto> cancelPurchaseById(Integer purchaseId) {
    log.debug("cancelPurchaseById");
    return this.purchaseRepository.findById(purchaseId)
      .flatMap(this::getNewCancelledPurchase)
      .flatMap(this.purchaseRepository::save)
      .flatMap(purchase ->
        Mono.zip(
          Mono.just(purchase),
          Mono.from(cancelAccountTransaction(purchase))))
      .flatMap(tuple ->
        Mono.zip(
          Mono.just(tuple.getT1()),
          Mono.just(tuple.getT2()),
          Mono.from(cancelStockTransaction(tuple.getT1()))))
      .map(tuple -> PurchaseMapper.INSTANCE.entityToDto(tuple.getT1()));
  }
  
  
  private static Mono<PurchaseDto> isValidPurchase(PurchaseDto purchaseDto) {
    return Mono.just(purchaseDto)
      .filter(purchase ->
        (purchase.getUserId() != null ) &&
          (purchase.getProductId() != null) &&
            (purchase.getProductId() != null) &&
              (purchase.getQuantity() > 0)  &&
                (purchase.getUnitPrice().compareTo(BigDecimal.ZERO) > 0) &&
                  (purchase.getUnitPrice()
                    .multiply(BigDecimal.valueOf(purchase.getQuantity()))
                    .compareTo(purchase.getTotalPrice()) == 0))
      .switchIfEmpty(Mono.error(new ValidationException("Invalid purchase values")));
  }
  
  private Mono<Purchase> getPurchaseFromData(Tuple5<PurchaseDto, UserDto, ProductDto, AccountDto, StockDto> purchaseData){
    return Mono.just(purchaseData)
      .filter(tuple -> tuple.getT4().getBalance().compareTo(tuple.getT1().getTotalPrice()) >= 0)
      .switchIfEmpty(Mono.error(new InsufficientFundsException(purchaseData.getT4().getBalance())))
      .filter(tuple -> tuple.getT5().getQuantity() >= purchaseData.getT1().getQuantity())
      .switchIfEmpty(Mono.error(new InsufficientQuantityException(purchaseData.getT1().getProductId(),
          purchaseData.getT5().getQuantity())))
      .map(tuple -> {
        Purchase purchase = PurchaseMapper.INSTANCE.dtoToEntity(tuple.getT1());
        purchase.setCreationDate(OffsetDateTime.now());
        purchase.setDescription(tuple.getT3().getName());
        purchase.setUserEmail(tuple.getT2().getEmail());
        purchase.setUserAddress(tuple.getT2().getAddress());
        purchase.setAccountNumber(tuple.getT4().getNumber());
        purchase.setProductName(tuple.getT3().getName());
        purchase.setRetailerId(tuple.getT3().getRetailerId());
        purchase.setRetailerName(tuple.getT3().getRetailerName());
        purchase.setStatus(PurchaseStatusEnum.FULFILLED.getValue());
        purchase.setCancellationDate(null);
        return purchase;
      });
  }
  
  
  
  
  private Mono<AccountTransactionDto> createAccountTransaction(Purchase savedPurchase){
    return Mono.zip(
        Mono.just(savedPurchase),
        Mono.just(AccountTransactionDto.builder()
          .transactionType(AccountTransactionTypeEnum.DEBIT)
          .transactionAmount(savedPurchase.getTotalPrice())
          .transactionDescription("Purchase Id: " + savedPurchase.getId())
          .build()))
      .flatMap(tuple -> this.accountRepository.createTransaction(tuple.getT1().getAccountId(), tuple.getT2()));
  }
  
  private Mono<StockTransactionDto> createStockTransaction(Purchase savedPurchase){
    return Mono.zip(
        Mono.just(savedPurchase),
        Mono.just(StockTransactionDto.builder()
          .transactionType(StockTransactionTypeEnum.DECREASE)
          .quantity(savedPurchase.getQuantity())
          .unitPrice(BigDecimal.ZERO)
          .description("Purchase Id: " + savedPurchase.getId())
          .build()))
      .flatMap(tuple -> this.stockRepository.createTransaction(tuple.getT1().getProductId(), tuple.getT2()));
  }
  
  private Mono<AccountTransactionDto> cancelAccountTransaction(Integer accountId, AccountTransactionDto transactionDto){
    return Mono.zip(
        Mono.just(accountId),
        Mono.just(AccountTransactionDto.builder()
          .transactionType(AccountTransactionTypeEnum.CREDIT)
          .transactionAmount(transactionDto.getTransactionAmount())
          .transactionDescription(CANCELED + " " + transactionDto.getTransactionDescription())
          .build()))
      .flatMap(tuple -> this.accountRepository.createTransaction(tuple.getT1(), tuple.getT2()));
  }
  
  private Mono<AccountTransactionDto> cancelAccountTransaction(Purchase purchase){
    return Mono.zip(
        Mono.just(purchase),
        Mono.just(AccountTransactionDto.builder()
          .transactionType(AccountTransactionTypeEnum.CREDIT)
          .transactionAmount(purchase.getTotalPrice())
          .transactionDescription(CANCELED + " Purchase Id: " + purchase.getId())
          .build()))
      .flatMap(tuple -> this.accountRepository.createTransaction(tuple.getT1().getAccountId(), tuple.getT2()));
  }
  
  private Mono<StockTransactionDto> cancelStockTransaction(Purchase purchase){
    return Mono.zip(
      Mono.just(purchase),
      Mono.just(StockTransactionDto.builder()
        .transactionType(StockTransactionTypeEnum.INCREASE)
        .quantity(purchase.getQuantity())
        .unitPrice(BigDecimal.ZERO)
        .description(CANCELED + " Purchase Id: " + purchase.getId())
        .build()))
      .flatMap(tuple -> this.stockRepository.createTransaction(tuple.getT1().getProductId(), tuple.getT2()));
  }
  
  private Mono<Purchase> getNewCancelledPurchase(Purchase purchase){
    return Mono.just(purchase)
      .map(updatedPurchase -> {
        updatedPurchase.setStatus(PurchaseStatusEnum.CANCELED.getValue());
        updatedPurchase.setCancellationDate(OffsetDateTime.now());
        return updatedPurchase;
        });
  }
  
}