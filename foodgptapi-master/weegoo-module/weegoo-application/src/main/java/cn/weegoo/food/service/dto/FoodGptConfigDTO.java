/**
 * Copyright © 2021-2025 <a href="http://www.weegoo.cn/">weegoo</a> All rights reserved.
 */
package cn.weegoo.food.service.dto;

import cn.weegoo.core.excel.annotation.ExcelDictProperty;
import cn.weegoo.core.excel.converter.ExcelDictDTOConverter;
import com.alibaba.excel.annotation.ExcelProperty;
import cn.weegoo.core.service.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * gpt账号配置DTO
 * @author xx
 * @version 2023-05-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FoodGptConfigDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	        
	/**
     * 账号
     */
	@ExcelProperty("账号") 
	private String apiKey;
	        
	/**
     * 是否可用
     */
	@ExcelProperty(value = "是否可用", converter = ExcelDictDTOConverter.class)
	@ExcelDictProperty("yes_no")
	private String isAvailable;
	        
	/**
     * 使用次数
     */
	@ExcelProperty("使用次数") 
	private Integer frequency;

}
