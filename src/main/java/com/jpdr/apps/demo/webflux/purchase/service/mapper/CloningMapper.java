package com.jpdr.apps.demo.webflux.purchase.service.mapper;

import com.jpdr.apps.demo.webflux.purchase.model.Purchase;
import org.mapstruct.Mapper;
import org.mapstruct.control.DeepClone;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", mappingControl = DeepClone.class)

public interface CloningMapper {
  
  CloningMapper INSTANCE = Mappers.getMapper(CloningMapper.class);
  
  Purchase clone(Purchase entity);
  
}
