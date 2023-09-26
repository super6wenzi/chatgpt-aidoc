/**
 * Copyright © 2021-2025 <a href="http://www.weegoo.cn/">weegoo</a> All rights reserved.
 */
package cn.weegoo.food.service.dto;

import cn.weegoo.core.excel.ExcelFieldDTOConverter;
import cn.weegoo.core.excel.annotation.ExcelDictProperty;
import cn.weegoo.core.excel.annotation.ExcelFieldProperty;
import cn.weegoo.core.excel.converter.ExcelDictDTOConverter;
import cn.weegoo.food.service.dto.FoodContentTypeDTO;
import javax.validation.constraints.NotNull;
import cn.weegoo.core.query.Query;
import cn.weegoo.core.query.QueryType;
import com.alibaba.excel.annotation.ExcelProperty;
import cn.weegoo.core.service.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 生成类型DTO
 * @author weegoo
 * @version 2023-05-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FoodBuildTypeDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	        
	/**
     * 所属内容分类
     */
	@NotNull(message="所属内容分类不能为空")
    @Query(tableColumn = "a.food_content_type", javaField = "foodContentType.id", type = QueryType.EQ)
	@ExcelProperty(value = "所属内容分类", converter = ExcelFieldDTOConverter.class)
	@ExcelFieldProperty(value = "foodContentType.name", service =  "cn.weegoo.food.service.FoodContentTypeService", wrapper= "cn.weegoo.food.service.mapstruct.FoodContentTypeWrapper")
	private FoodContentTypeDTO foodContentType;
	        
	/**
     * 标题
     */
	@NotNull(message="标题不能为空")
	@ExcelProperty("标题") 
	private String title;
	        
	/**
     * 图标
     */
	@NotNull(message="图标不能为空")
	@ExcelProperty("图标") 
	private String icon;
	        
	/**
     * 显示顺序
     */
	@NotNull(message="显示顺序不能为空")
	@ExcelProperty("显示顺序") 
	private String sort;
	        
	/**
     * 是否显示
     */
	@NotNull(message="是否显示不能为空")
    @Query(tableColumn = "a.is_show", javaField = "isShow", type = QueryType.EQ)
	@ExcelProperty(value = "是否显示", converter = ExcelDictDTOConverter.class)
	@ExcelDictProperty("yes_no")
	private String isShow;
	        
	/**
     * 关键字
     */
	@ExcelProperty("关键字") 
	private String keywords;

	/**
	 * 关键字
	 */
	@ExcelProperty("前置prompt")
	private String preposition;

	/**
	 * 是否显示下一步
	 */
	private String isShowNext;
}
