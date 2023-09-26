/**
 * Copyright © 2021-2025 <a href="http://www.weegoo.cn/">weegoo</a> All rights reserved.
 */
package cn.weegoo.food.service.dto;

import cn.weegoo.core.excel.ExcelFieldDTOConverter;
import cn.weegoo.core.excel.annotation.ExcelFieldProperty;
import cn.weegoo.food.service.dto.FoodUserDTO;
import cn.weegoo.food.service.dto.FoodBrandDTO;
import cn.weegoo.food.service.dto.FoodBuildTypeDTO;
import java.util.List;
import com.google.common.collect.Lists;
import cn.weegoo.core.query.Query;
import cn.weegoo.core.query.QueryType;
import com.alibaba.excel.annotation.ExcelProperty;
import cn.weegoo.core.service.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 聊天记录DTO
 * @author xx
 * @version 2023-06-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FoodMessageDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	        
	/**
     * 用户id
     */
    @Query(tableColumn = "a.user_id", javaField = "user.id", type = QueryType.EQ)
	@ExcelProperty(value = "用户id", converter = ExcelFieldDTOConverter.class)
	@ExcelFieldProperty(value = "user.name", service =  "cn.weegoo.food.service.FoodUserService", wrapper= "cn.weegoo.food.service.mapstruct.FoodUserWrapper")
	private FoodUserDTO user;
	        
	/**
     * 品牌id
     */
    @Query(tableColumn = "a.brand_id", javaField = "brand.id", type = QueryType.EQ)
	@ExcelProperty(value = "品牌id", converter = ExcelFieldDTOConverter.class)
	@ExcelFieldProperty(value = "brand.name", service =  "cn.weegoo.food.service.FoodBrandService", wrapper= "cn.weegoo.food.service.mapstruct.FoodBrandWrapper")
	private FoodBrandDTO brand;
	        
	/**
     * 生成类型id
     */
    @Query(tableColumn = "a.food_build_id", javaField = "foodBuild.id", type = QueryType.EQ)
	@ExcelProperty(value = "生成类型id", converter = ExcelFieldDTOConverter.class)
	@ExcelFieldProperty(value = "foodBuild.title", service =  "cn.weegoo.food.service.FoodBuildTypeService", wrapper= "cn.weegoo.food.service.mapstruct.FoodBuildTypeWrapper")
	private FoodBuildTypeDTO foodBuild;
    /**
     *子表列表
     */
	private List<FoodMessageDetailDTO> foodMessageDetailDTOList = Lists.newArrayList();

	/**
	 * 0内置，1系统，2用户
	 */
	private String type;
}
