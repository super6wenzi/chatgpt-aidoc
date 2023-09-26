/**
 * Copyright © 2021-2025 <a href="http://www.weegoo.cn/">weegoo</a> All rights reserved.
 */
package cn.weegoo.food.service.dto;

import cn.weegoo.core.service.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 表单下拉选项Entity
 * @author xx
 * @version 2023-06-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FoodFormSelectDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	/**
     * 归属表单
     */
	private FoodFormDTO form;
	/**
     * 选项名
     */
	private String name;
	/**
     * 选择值
     */
	private String value;
	/**
     * 排序
     */
	private String sort;

}
