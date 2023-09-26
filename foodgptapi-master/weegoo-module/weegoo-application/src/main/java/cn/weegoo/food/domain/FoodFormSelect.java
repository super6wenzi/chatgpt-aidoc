/**
 * Copyright © 2021-2025 <a href="http://www.weegoo.cn/">weegoo</a> All rights reserved.
 */
package cn.weegoo.food.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import cn.weegoo.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 表单下拉选项Entity
 * @author xx
 * @version 2023-06-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("food_form_select")
public class FoodFormSelect extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
     * 归属表单
     */
	private String formId;
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
