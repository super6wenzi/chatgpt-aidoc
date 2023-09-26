/**
 * Copyright © 2021-2025 <a href="http://www.weegoo.cn/">weegoo</a> All rights reserved.
 */
package cn.weegoo.food.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import cn.weegoo.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 内容类型Entity
 * @author weegoo
 * @version 2023-05-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("food_content_type")
public class FoodContentType extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
     * 名称
     */
	private String name;
	/**
     * 是否显示
     */
	private String isShow;
	/**
     * 显示顺序
     */
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
