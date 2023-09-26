/**
 * Copyright © 2021-2025 <a href="http://www.weegoo.cn/">weegoo</a> All rights reserved.
 */
package cn.weegoo.food.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import cn.weegoo.core.domain.BaseEntity;
import cn.weegoo.core.query.Query;
import cn.weegoo.core.query.QueryType;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 详细聊天内容Entity
 * @author xx
 * @version 2023-06-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("food_message_detail")
public class FoodMessageDetail extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
     * 聊天记录id
     */
	private String messageId;
	/**
     * 内容
     */
	private String content;
	/**
     * 0内置，1系统，2用户
     */
	private String type;

}
