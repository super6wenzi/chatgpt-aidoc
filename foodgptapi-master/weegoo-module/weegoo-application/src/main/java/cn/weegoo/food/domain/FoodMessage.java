/**
 * Copyright © 2021-2025 <a href="http://www.weegoo.cn/">weegoo</a> All rights reserved.
 */
package cn.weegoo.food.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import cn.weegoo.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 聊天记录Entity
 * @author xx
 * @version 2023-06-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("food_message")
public class FoodMessage extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
     * 用户id
     */
	private String userId;
	/**
     * 品牌id
     */
	private String brandId;
	/**
     * 生成类型id
     */
	private String foodBuildId;

}
