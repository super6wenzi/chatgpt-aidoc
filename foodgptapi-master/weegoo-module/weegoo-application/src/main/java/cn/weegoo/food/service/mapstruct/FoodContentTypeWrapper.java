/**
 * Copyright © 2021-2025 <a href="http://www.weegoo.cn/">weegoo</a> All rights reserved.
 */
package cn.weegoo.food.service.mapstruct;


import cn.weegoo.core.mapstruct.EntityWrapper;
import cn.weegoo.food.service.dto.FoodContentTypeDTO;
import cn.weegoo.food.domain.FoodContentType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 *  FoodContentTypeWrapper
 * @author weegoo
 * @version 2023-05-25
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {} )
public interface FoodContentTypeWrapper extends EntityWrapper<FoodContentTypeDTO, FoodContentType> {

    FoodContentTypeWrapper INSTANCE = Mappers.getMapper(FoodContentTypeWrapper.class);
}

