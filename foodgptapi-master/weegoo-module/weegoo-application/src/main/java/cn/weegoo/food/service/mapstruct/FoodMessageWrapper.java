/**
 * Copyright © 2021-2025 <a href="http://www.weegoo.cn/">weegoo</a> All rights reserved.
 */
package cn.weegoo.food.service.mapstruct;


import cn.weegoo.core.mapstruct.EntityWrapper;
import cn.weegoo.food.service.dto.FoodMessageDTO;
import cn.weegoo.food.domain.FoodMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 *  FoodMessageWrapper
 * @author xx
 * @version 2023-06-07
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {} )
public interface FoodMessageWrapper extends EntityWrapper<FoodMessageDTO, FoodMessage> {

    FoodMessageWrapper INSTANCE = Mappers.getMapper(FoodMessageWrapper.class);
     @Mappings({
            @Mapping(source = "user.id", target = "userId"),
            @Mapping(source = "brand.id", target = "brandId"),
            @Mapping(source = "foodBuild.id", target = "foodBuildId"),
            @Mapping(source = "createBy.id", target = "createBy"),
            @Mapping (source = "updateBy.id", target = "updateBy")})
    FoodMessage toEntity(FoodMessageDTO dto);


    @Mappings({
            @Mapping(source = "userId", target = "user.id"),
            @Mapping(source = "brandId", target = "brand.id"),
            @Mapping(source = "foodBuildId", target = "foodBuild.id"),
            @Mapping (source = "createBy", target = "createBy.id"),
            @Mapping (source = "updateBy", target = "updateBy.id")})
    FoodMessageDTO toDTO(FoodMessage entity);
}

