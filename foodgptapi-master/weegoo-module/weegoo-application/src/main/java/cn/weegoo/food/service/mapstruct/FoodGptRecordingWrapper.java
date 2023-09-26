/**
 * Copyright © 2021-2025 <a href="http://www.weegoo.cn/">weegoo</a> All rights reserved.
 */
package cn.weegoo.food.service.mapstruct;


import cn.weegoo.core.mapstruct.EntityWrapper;
import cn.weegoo.food.service.dto.FoodGptRecordingDTO;
import cn.weegoo.food.domain.FoodGptRecording;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 *  FoodGptRecordingWrapper
 * @author weegoo
 * @version 2023-05-26
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {} )
public interface FoodGptRecordingWrapper extends EntityWrapper<FoodGptRecordingDTO, FoodGptRecording> {

    FoodGptRecordingWrapper INSTANCE = Mappers.getMapper(FoodGptRecordingWrapper.class);
}

