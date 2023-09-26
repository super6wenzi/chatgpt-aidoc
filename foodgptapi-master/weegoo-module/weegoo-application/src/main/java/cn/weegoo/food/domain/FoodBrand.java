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
 * 品牌Entity
 * @author weegoo
 * @version 2023-05-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("food_brand")
public class FoodBrand extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
     * 品牌名称
     */
	private String name;
	/**
     * 主营品类
     */
	private String category;
	/**
     * 餐厅地址
     */
	private String address;
	/**
     * 客流量
     */
	private Integer passengerFlow;
	/**
     * 客单价
     */
	private Double price;
	/**
     * 起始客户群体
     */
	private Integer startCustomerGroups;
	/**
	 * 客户性别
	 */
	private String customerSex;
	/**
     * 客流旺季
     */
	private String peakSeason;
	/**
     * 品牌特色
     */
	private String characteristic;
	/**
     * 结束客户群体
     */
	private Integer endCustomerGroups;
	/**
     * 所属用户
     */
    @TableField("food_user")
	private String foodUserId;

}
