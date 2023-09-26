/**
 * Copyright © 2021-2025 <a href="http://www.weegoo.cn/">weegoo</a> All rights reserved.
 */
package cn.weegoo.food.service.dto;

import cn.weegoo.core.query.Query;
import cn.weegoo.core.query.QueryType;
import com.alibaba.excel.annotation.ExcelProperty;
import cn.weegoo.core.service.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 生成记录表DTO
 * @author weegoo
 * @version 2023-05-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FoodGptRecordingDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	        
	/**
     * 所属品牌
     */
    @Query(type = QueryType.EQ)
	@ExcelProperty("所属品牌") 
	private String foodBrand;
	        
	/**
     * 生成文本
     */
	@ExcelProperty("生成文本") 
	private String generateText;

}
