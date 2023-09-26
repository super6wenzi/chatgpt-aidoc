/**
 * Copyright © 2021-2025 <a href="http://www.weegoo.cn/">weegoo</a> All rights reserved.
 */
package cn.weegoo.food.service.dto;

import cn.weegoo.core.excel.ExcelFieldDTOConverter;
import cn.weegoo.core.excel.annotation.ExcelDictProperty;
import cn.weegoo.core.excel.annotation.ExcelFieldProperty;
import cn.weegoo.core.excel.converter.ExcelDictDTOConverter;
import cn.weegoo.food.service.dto.FoodBuildTypeDTO;
import java.util.List;
import com.google.common.collect.Lists;
import cn.weegoo.core.query.Query;
import cn.weegoo.core.query.QueryType;
import com.alibaba.excel.annotation.ExcelProperty;
import cn.weegoo.core.service.dto.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 表单配置DTO
 * @author xx
 * @version 2023-06-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FoodFormDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	        
	/**
     * 生成类型
     */
    @Query(tableColumn = "a.build_type_id", javaField = "buildType.id", type = QueryType.EQ)
	@ExcelProperty(value = "生成类型", converter = ExcelFieldDTOConverter.class)
	@ExcelFieldProperty(value = "buildType.title", service =  "cn.weegoo.food.service.FoodBuildTypeService", wrapper= "cn.weegoo.food.service.mapstruct.FoodBuildTypeWrapper")
	private FoodBuildTypeDTO buildType;
	        
	/**
     * 类型，1文本，2下拉选项
     */
    @Query(tableColumn = "a.type", javaField = "type", type = QueryType.EQ)
	@ExcelProperty(value = "类型，1文本，2下拉选项", converter = ExcelDictDTOConverter.class)
	@ExcelDictProperty("form_type")
	private String type;
	        
	/**
     * 标题
     */
	@ExcelProperty("标题") 
	private String name;
	        
	/**
     * 排序
     */
	@ExcelProperty("排序") 
	private String sort;

	@ApiModelProperty("提示")
	private String tags;

    /**
     *子表列表
     */
	private List<FoodFormSelectDTO> foodFormSelectDTOList = Lists.newArrayList();

}
