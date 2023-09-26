/**
 * Copyright © 2021-2025 <a href="http://www.weegoo.cn/">weegoo</a> All rights reserved.
 */
package cn.weegoo.food.service.mapstruct;


import cn.weegoo.core.mapstruct.EntityWrapper;
import cn.weegoo.food.service.dto.FoodUserDTO;
import cn.weegoo.food.domain.FoodUser;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 *  FoodUserWrapper
 * @author weegoo
 * @version 2023-05-26
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {} )
public interface FoodUserWrapper extends EntityWrapper<FoodUserDTO, FoodUser> {

    FoodUserWrapper INSTANCE = Mappers.getMapper(FoodUserWrapper.class);
}

