package com.jpdr.apps.demo.webflux.purchase.repository.account;

import com.jpdr.apps.demo.webflux.purchase.repository.BaseRepository;
import com.jpdr.apps.demo.webflux.purchase.service.dto.account.AccountDto;
import com.jpdr.apps.demo.webflux.purchase.service.dto.account.AccountTransactionDto;
import reactor.core.publisher.Mono;

public interface AccountRepository extends BaseRepository<AccountDto, Integer> {

  Mono<AccountTransactionDto> createTransaction(Integer accountId, AccountTransactionDto transaction);

}
