package com.jpdr.apps.demo.webflux.purchase.repository;

import reactor.core.publisher.Mono;

public interface BaseRepository <T,R>{
  
  Mono<T> getById(R id);
  
}
