/**
 * Copyright © 2021-2025 <a href="http://www.weegoo.cn/">weegoo</a> All rights reserved.
 */
package cn.weegoo.food.service.mapstruct;


import cn.weegoo.core.mapstruct.EntityWrapper;
import cn.weegoo.food.service.dto.FoodBuildTypeDTO;
import cn.weegoo.food.domain.FoodBuildType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 *  FoodBuildTypeWrapper
 * @author weegoo
 * @version 2023-05-26
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {} )
public interface FoodBuildTypeWrapper extends EntityWrapper<FoodBuildTypeDTO, FoodBuildType> {

    FoodBuildTypeWrapper INSTANCE = Mappers.getMapper(FoodBuildTypeWrapper.class);
     @Mappings({
            @Mapping(source = "foodContentType.id", target = "foodContentTypeId"),
            @Mapping(source = "createBy.id", target = "createBy"),
            @Mapping (source = "updateBy.id", target = "updateBy")})
    FoodBuildType toEntity(FoodBuildTypeDTO dto);


    @Mappings({
            @Mapping(source = "foodContentTypeId", target = "foodContentType.id"),
            @Mapping (source = "createBy", target = "createBy.id"),
            @Mapping (source = "updateBy", target = "updateBy.id")})
    FoodBuildTypeDTO toDTO(FoodBuildType entity);
}

