/**
 * Copyright © 2021-2025 <a href="http://www.weegoo.cn/">weegoo</a> All rights reserved.
 */
package cn.weegoo.food.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import cn.weegoo.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 用户Entity
 * @author weegoo
 * @version 2023-05-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("food_user")
public class FoodUser extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
     * 头像
     */
	private String avatar;
	/**
     * 名称
     */
	private String name;
	/**
     * 微信标识
     */
	private String openid;
	/**
     * 手机号
     */
	private String phone;

	/**
	 * 会员等级
	 */
	private String vip;
	/**
	 * 用户身份
	 */
	private String userType;

	private Integer times;
}
