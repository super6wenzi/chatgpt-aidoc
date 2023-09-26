/**
 * Copyright © 2021-2025 <a href="http://www.weegoo.cn/">weegoo</a> All rights reserved.
 */
package cn.weegoo.food.service.mapstruct;


import cn.weegoo.core.mapstruct.EntityWrapper;
import cn.weegoo.food.service.dto.FoodBrandDTO;
import cn.weegoo.food.domain.FoodBrand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 *  FoodBrandWrapper
 * @author weegoo
 * @version 2023-05-26
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {} )
public interface FoodBrandWrapper extends EntityWrapper<FoodBrandDTO, FoodBrand> {

    FoodBrandWrapper INSTANCE = Mappers.getMapper(FoodBrandWrapper.class);
     @Mappings({
            @Mapping(source = "foodUser.id", target = "foodUserId"),
            @Mapping(source = "createBy.id", target = "createBy"),
            @Mapping (source = "updateBy.id", target = "updateBy")})
    FoodBrand toEntity(FoodBrandDTO dto);


    @Mappings({
            @Mapping(source = "foodUserId", target = "foodUser.id"),
            @Mapping (source = "createBy", target = "createBy.id"),
            @Mapping (source = "updateBy", target = "updateBy.id")})
    FoodBrandDTO toDTO(FoodBrand entity);
}

