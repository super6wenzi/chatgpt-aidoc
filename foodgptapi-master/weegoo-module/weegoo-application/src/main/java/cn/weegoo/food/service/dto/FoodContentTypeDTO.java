/**
 * Copyright © 2021-2025 <a href="http://www.weegoo.cn/">weegoo</a> All rights reserved.
 */
package cn.weegoo.food.service.dto;

import cn.weegoo.core.excel.annotation.ExcelDictProperty;
import cn.weegoo.core.query.Query;
import cn.weegoo.core.query.QueryType;
import com.alibaba.excel.annotation.ExcelProperty;

import cn.weegoo.core.service.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 内容类型DTO
 * @author weegoo
 * @version 2023-05-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FoodContentTypeDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	        
	/**
     * 名称
     */
	@ExcelProperty("名称") 
	private String name;
	        
	/**
     * 是否显示
     */
    @Query(type = QueryType.EQ)
	@ExcelProperty(value = "是否显示")
	@ExcelDictProperty("yes_no")
	private String isShow;
	        
	/**
     * 显示顺序
     */
	@ExcelProperty("显示顺序") 
	private String sort;

	/**
	 * 生成标题
	 */
	private String buildTitle;
	/**
	 * 生成提示
	 */
	private String buildTips;
}
