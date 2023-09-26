/**
 * Copyright © 2021-2025 <a href="http://www.weegoo.cn/">weegoo</a> All rights reserved.
 */
package cn.weegoo.food.service.mapstruct;


import cn.weegoo.core.mapstruct.EntityWrapper;
import cn.weegoo.food.service.dto.FoodFormDTO;
import cn.weegoo.food.domain.FoodForm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 *  FoodFormWrapper
 * @author xx
 * @version 2023-06-15
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {} )
public interface FoodFormWrapper extends EntityWrapper<FoodFormDTO, FoodForm> {

    FoodFormWrapper INSTANCE = Mappers.getMapper(FoodFormWrapper.class);
     @Mappings({
            @Mapping(source = "buildType.id", target = "buildTypeId"),
            @Mapping(source = "createBy.id", target = "createBy"),
            @Mapping (source = "updateBy.id", target = "updateBy")})
    FoodForm toEntity(FoodFormDTO dto);


    @Mappings({
            @Mapping(source = "buildTypeId", target = "buildType.id"),
            @Mapping (source = "createBy", target = "createBy.id"),
            @Mapping (source = "updateBy", target = "updateBy.id")})
    FoodFormDTO toDTO(FoodForm entity);
}

