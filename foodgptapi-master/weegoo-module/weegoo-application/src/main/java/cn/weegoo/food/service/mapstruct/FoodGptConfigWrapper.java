/**
 * Copyright © 2021-2025 <a href="http://www.weegoo.cn/">weegoo</a> All rights reserved.
 */
package cn.weegoo.food.service.mapstruct;


import cn.weegoo.core.mapstruct.EntityWrapper;
import cn.weegoo.food.service.dto.FoodGptConfigDTO;
import cn.weegoo.food.domain.FoodGptConfig;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 *  FoodGptConfigWrapper
 * @author xx
 * @version 2023-05-29
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {} )
public interface FoodGptConfigWrapper extends EntityWrapper<FoodGptConfigDTO, FoodGptConfig> {

    FoodGptConfigWrapper INSTANCE = Mappers.getMapper(FoodGptConfigWrapper.class);
}

