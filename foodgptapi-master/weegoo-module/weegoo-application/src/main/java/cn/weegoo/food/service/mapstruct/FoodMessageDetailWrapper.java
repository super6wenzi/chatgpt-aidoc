/**
 * Copyright © 2021-2025 <a href="http://www.weegoo.cn/">weegoo</a> All rights reserved.
 */
package cn.weegoo.food.service.mapstruct;


import cn.weegoo.core.mapstruct.EntityWrapper;
import cn.weegoo.food.service.dto.FoodMessageDetailDTO;
import cn.weegoo.food.domain.FoodMessageDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 *  FoodMessageDetailWrapper
 * @author xx
 * @version 2023-06-07
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {} )
public interface FoodMessageDetailWrapper extends EntityWrapper<FoodMessageDetailDTO, FoodMessageDetail> {

    FoodMessageDetailWrapper INSTANCE = Mappers.getMapper(FoodMessageDetailWrapper.class);
     @Mappings({
            @Mapping(source = "message.id", target = "messageId"),
            @Mapping(source = "createBy.id", target = "createBy"),
            @Mapping (source = "updateBy.id", target = "updateBy")})
    FoodMessageDetail toEntity(FoodMessageDetailDTO dto);


    @Mappings({
            @Mapping(source = "messageId", target = "message.id"),
            @Mapping (source = "createBy", target = "createBy.id"),
            @Mapping (source = "updateBy", target = "updateBy.id")})
    FoodMessageDetailDTO toDTO(FoodMessageDetail entity);
}

