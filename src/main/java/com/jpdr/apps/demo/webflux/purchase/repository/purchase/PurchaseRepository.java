package com.jpdr.apps.demo.webflux.purchase.repository.purchase;

import com.jpdr.apps.demo.webflux.purchase.model.Purchase;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface PurchaseRepository extends ReactiveCrudRepository<Purchase, Integer> {
  
  Flux<Purchase> findAllByUserId(Integer userId);
  
}
