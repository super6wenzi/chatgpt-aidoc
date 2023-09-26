/**
 * Copyright © 2021-2025 <a href="http://www.weegoo.cn/">weegoo</a> All rights reserved.
 */
package cn.weegoo.food.service.mapstruct;


import cn.weegoo.core.mapstruct.EntityWrapper;
import cn.weegoo.food.service.dto.FoodConfigDTO;
import cn.weegoo.food.domain.FoodConfig;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 *  FoodConfigWrapper
 * @author weegoo
 * @version 2023-06-01
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {} )
public interface FoodConfigWrapper extends EntityWrapper<FoodConfigDTO, FoodConfig> {

    FoodConfigWrapper INSTANCE = Mappers.getMapper(FoodConfigWrapper.class);
}

