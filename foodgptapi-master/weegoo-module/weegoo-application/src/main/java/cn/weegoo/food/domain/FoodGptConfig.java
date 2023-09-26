/**
 * Copyright © 2021-2025 <a href="http://www.weegoo.cn/">weegoo</a> All rights reserved.
 */
package cn.weegoo.food.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import cn.weegoo.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * gpt账号配置Entity
 * @author xx
 * @version 2023-05-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("food_gpt_config")
public class FoodGptConfig extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
     * 账号
     */
	private String apiKey;
	/**
     * 是否可用
     */
	private String isAvailable;
	/**
     * 使用次数
     */
	private Integer frequency;

}
