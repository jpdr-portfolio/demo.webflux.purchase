package com.jpdr.apps.demo.webflux.purchase.repository.stock;

import com.jpdr.apps.demo.webflux.purchase.repository.BaseRepository;
import com.jpdr.apps.demo.webflux.purchase.service.dto.stock.StockDto;
import com.jpdr.apps.demo.webflux.purchase.service.dto.stock.StockTransactionDto;
import reactor.core.publisher.Mono;

public interface StockRepository extends BaseRepository<StockDto, Long> {

  Mono<StockTransactionDto> createTransaction(Long productId, StockTransactionDto transactionDto);

}
