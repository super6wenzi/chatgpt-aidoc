/**
 * Copyright © 2021-2025 <a href="http://www.weegoo.cn/">weegoo</a> All rights reserved.
 */
package cn.weegoo.food.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import cn.weegoo.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 生成类型Entity
 * @author weegoo
 * @version 2023-05-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("food_build_type")
public class FoodBuildType extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
     * 所属内容分类
     */
    @TableField("food_content_type")
	private String foodContentTypeId;
	/**
     * 标题
     */
	private String title;
	/**
     * 图标
     */
	private String icon;
	/**
     * 显示顺序
     */
	private String sort;
	/**
     * 是否显示
     */
	private String isShow;
	/**
     * 关键字
     */
	private String keywords;

	/**
	 * 前置prompt
	 */
	private String preposition;

	/**
	 * 是否显示下一步
	 */
	private String isShowNext;
}
