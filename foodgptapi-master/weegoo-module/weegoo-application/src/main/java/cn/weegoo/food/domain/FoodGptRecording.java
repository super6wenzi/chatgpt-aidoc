/**
 * Copyright © 2021-2025 <a href="http://www.weegoo.cn/">weegoo</a> All rights reserved.
 */
package cn.weegoo.food.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import cn.weegoo.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 生成记录表Entity
 * @author weegoo
 * @version 2023-05-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("food_gpt_recording")
public class FoodGptRecording extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
     * 所属品牌
     */
	private String foodBrand;
	/**
     * 生成文本
     */
	private String generateText;

}
