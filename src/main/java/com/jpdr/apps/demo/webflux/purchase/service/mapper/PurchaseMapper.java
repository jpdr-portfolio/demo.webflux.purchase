package com.jpdr.apps.demo.webflux.purchase.service.mapper;

import com.jpdr.apps.demo.webflux.purchase.model.Purchase;
import com.jpdr.apps.demo.webflux.purchase.service.dto.purchase.PurchaseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", imports = java.util.Objects.class)
public interface PurchaseMapper {
  
  PurchaseMapper INSTANCE = Mappers.getMapper(PurchaseMapper.class);
  
  @Mapping(target = "id", expression = "java(null)")
  @Mapping(target = "purchaseDate", expression = "java(null)")
  @Mapping(target = "purchaseCancellationDate", expression = "java(null)")
  Purchase dtoToEntity(PurchaseDto dto);
  
  @Mapping(target = "purchaseDate", expression = "java(Objects.toString(entity.getPurchaseDate(),null))" )
  @Mapping(target = "purchaseCancellationDate", expression = "java(Objects.toString(entity.getPurchaseCancellationDate(),null))" )
  PurchaseDto entityToDto(Purchase entity);
  
}
