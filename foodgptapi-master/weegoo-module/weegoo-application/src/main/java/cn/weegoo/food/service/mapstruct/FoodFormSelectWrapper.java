/**
 * Copyright © 2021-2025 <a href="http://www.weegoo.cn/">weegoo</a> All rights reserved.
 */
package cn.weegoo.food.service.mapstruct;


import cn.weegoo.core.mapstruct.EntityWrapper;
import cn.weegoo.food.service.dto.FoodFormSelectDTO;
import cn.weegoo.food.domain.FoodFormSelect;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 *  FoodFormSelectWrapper
 * @author xx
 * @version 2023-06-15
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {} )
public interface FoodFormSelectWrapper extends EntityWrapper<FoodFormSelectDTO, FoodFormSelect> {

    FoodFormSelectWrapper INSTANCE = Mappers.getMapper(FoodFormSelectWrapper.class);
     @Mappings({
            @Mapping(source = "form.id", target = "formId"),
            @Mapping(source = "createBy.id", target = "createBy"),
            @Mapping (source = "updateBy.id", target = "updateBy")})
    FoodFormSelect toEntity(FoodFormSelectDTO dto);


    @Mappings({
            @Mapping(source = "formId", target = "form.id"),
            @Mapping (source = "createBy", target = "createBy.id"),
            @Mapping (source = "updateBy", target = "updateBy.id")})
    FoodFormSelectDTO toDTO(FoodFormSelect entity);
}

