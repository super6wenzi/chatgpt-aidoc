/**
 * Copyright © 2021-2025 <a href="http://www.weegoo.cn/">weegoo</a> All rights reserved.
 */
package cn.weegoo.food.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import cn.weegoo.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 表单配置Entity
 * @author xx
 * @version 2023-06-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("food_form")
public class FoodForm extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
     * 生成类型
     */
	private String buildTypeId;
	/**
     * 类型，1文本，2下拉选项
     */
	private String type;
	/**
     * 标题
     */
	private String name;
	/**
     * 排序
     */
	private String sort;

	private String tags;
}
