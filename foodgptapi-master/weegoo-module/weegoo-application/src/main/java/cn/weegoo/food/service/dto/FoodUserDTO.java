/**
 * Copyright © 2021-2025 <a href="http://www.weegoo.cn/">weegoo</a> All rights reserved.
 */
package cn.weegoo.food.service.dto;

import cn.weegoo.core.query.Query;
import cn.weegoo.core.query.QueryType;
import com.alibaba.excel.annotation.ExcelProperty;
import cn.weegoo.core.service.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 用户DTO
 * @author weegoo
 * @version 2023-05-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FoodUserDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	        
	/**
     * 头像
     */
	@ExcelProperty("头像") 
	private String avatar;
	        
	/**
     * 名称
     */
	@Query(type = QueryType.LIKE)
	@ExcelProperty("名称") 
	private String name;
	        
	/**
     * 微信标识
     */
    @Query(type = QueryType.EQ)
	@ExcelProperty("微信标识") 
	private String openid;
	        
	/**
     * 手机号
     */
	@ExcelProperty("手机号") 
	private String phone;
	/**
	 * 会员等级
	 */
	@ExcelProperty("会员等级")
	private String vip;
	/**
	 * 用户身份
	 */

	@ExcelProperty("用户身份")
	private String userType;

	private Integer times;
}
