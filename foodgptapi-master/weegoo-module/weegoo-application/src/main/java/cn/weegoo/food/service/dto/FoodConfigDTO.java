/**
 * Copyright © 2021-2025 <a href="http://www.weegoo.cn/">weegoo</a> All rights reserved.
 */
package cn.weegoo.food.service.dto;

import cn.weegoo.core.excel.annotation.ExcelDictProperty;
import cn.weegoo.core.excel.converter.ExcelDictDTOConverter;
import cn.weegoo.core.query.Query;
import cn.weegoo.core.query.QueryType;
import com.alibaba.excel.annotation.ExcelProperty;
import cn.weegoo.core.service.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * model配置DTO
 * @author weegoo
 * @version 2023-06-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FoodConfigDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	        
	/**
     * 配置标识
     */
    @Query(type = QueryType.EQ)
	@ExcelProperty("配置标识") 
	private String configKey;
	        
	/**
     * 配置值
     */
	@ExcelProperty("配置值") 
	private String configValue;
	        
	/**
     * 配置说明
     */
	@ExcelProperty("配置说明") 
	private String remarks;
	        
	/**
     * 渲染类型
     */
	@ExcelProperty(value = "渲染类型", converter = ExcelDictDTOConverter.class)
	@ExcelDictProperty("renderType")
	private String renderType;

}
