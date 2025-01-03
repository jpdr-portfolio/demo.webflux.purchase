package com.jpdr.apps.demo.webflux.purchase.configuration;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpdr.apps.demo.webflux.commons.caching.DtoSerializer;
import com.jpdr.apps.demo.webflux.purchase.service.dto.account.AccountDto;
import com.jpdr.apps.demo.webflux.purchase.service.dto.product.ProductDto;
import com.jpdr.apps.demo.webflux.purchase.service.dto.purchase.PurchaseDto;
import com.jpdr.apps.demo.webflux.purchase.service.dto.stock.StockDto;
import com.jpdr.apps.demo.webflux.purchase.service.dto.user.UserDto;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@EnableCaching
@Configuration
public class CacheConfig {
  
  @Bean
  public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory, ObjectMapper objectMapper){
    
    ObjectMapper mapper = objectMapper.copy()
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.setVisibility(PropertyAccessor.ALL, Visibility.ANY);
    mapper.findAndRegisterModules();
    
    DtoSerializer<PurchaseDto> purchaseDtoDtoSerializer = new DtoSerializer<>(mapper, PurchaseDto.class);
    DtoSerializer<AccountDto> accountDtoDtoSerializer = new DtoSerializer<>(mapper, AccountDto.class);
    DtoSerializer<UserDto> userDtoDtoSerializer = new DtoSerializer<>(mapper, UserDto.class);
    DtoSerializer<StockDto> stockDtoDtoSerializer = new DtoSerializer<>(mapper, StockDto.class);
    DtoSerializer<ProductDto> productDtoDtoSerializer = new DtoSerializer<>(mapper, ProductDto.class);
    
    RedisSerializationContext.SerializationPair<PurchaseDto> purchaseDtoSerializationPair =
      RedisSerializationContext.SerializationPair.fromSerializer(purchaseDtoDtoSerializer);
    RedisSerializationContext.SerializationPair<AccountDto> accountDtoSerializationPair =
      RedisSerializationContext.SerializationPair.fromSerializer(accountDtoDtoSerializer);
    RedisSerializationContext.SerializationPair<UserDto> userDtoSerializationPair =
      RedisSerializationContext.SerializationPair.fromSerializer(userDtoDtoSerializer);
    RedisSerializationContext.SerializationPair<StockDto> stockDtoSerializationPair =
      RedisSerializationContext.SerializationPair.fromSerializer(stockDtoDtoSerializer);
    RedisSerializationContext.SerializationPair<ProductDto> productDtoSerializationPair =
      RedisSerializationContext.SerializationPair.fromSerializer(productDtoDtoSerializer);
    
    RedisCacheConfiguration purchaseCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
      .serializeValuesWith(purchaseDtoSerializationPair);
    RedisCacheConfiguration userCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
      .serializeValuesWith(userDtoSerializationPair);
    RedisCacheConfiguration accountCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
      .serializeValuesWith(accountDtoSerializationPair);
    RedisCacheConfiguration stockCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
      .serializeValuesWith(stockDtoSerializationPair);
    RedisCacheConfiguration productCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
      .serializeValuesWith(productDtoSerializationPair);
    
    return RedisCacheManager.builder(redisConnectionFactory)
      .withCacheConfiguration("purchases",purchaseCacheConfiguration)
      .withCacheConfiguration("users", userCacheConfiguration)
      .withCacheConfiguration("accounts",accountCacheConfiguration)
      .withCacheConfiguration("stock", stockCacheConfiguration)
      .withCacheConfiguration("products", productCacheConfiguration)
      .build();
  }

}
