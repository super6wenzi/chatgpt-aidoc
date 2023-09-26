/**
 * Copyright © 2021-2025 <a href="http://www.weegoo.cn/">weegoo</a> All rights reserved.
 */
package cn.weegoo.food.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import cn.weegoo.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * model配置Entity
 * @author weegoo
 * @version 2023-06-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("food_config")
public class FoodConfig extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
     * 配置标识
     */
	private String configKey;
	/**
     * 配置值
     */
	private String configValue;
	/**
     * 配置说明
     */
	private String remarks;
	/**
     * 渲染类型
     */
	private String renderType;

}
