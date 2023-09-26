/**
 * Copyright © 2021-2025 <a href="http://www.weegoo.cn/">weegoo</a> All rights reserved.
 */
package cn.weegoo.food.service.dto;

import javax.validation.constraints.NotNull;

import cn.weegoo.core.excel.ExcelFieldDTOConverter;
import cn.weegoo.core.excel.annotation.ExcelFieldProperty;
import cn.weegoo.food.service.dto.FoodUserDTO;
import cn.weegoo.core.query.Query;
import cn.weegoo.core.query.QueryType;
import com.alibaba.excel.annotation.ExcelProperty;
import cn.weegoo.core.service.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 品牌DTO
 * @author weegoo
 * @version 2023-05-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FoodBrandDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	        
	/**
     * 品牌名称
     */
	@NotNull(message="品牌名称不能为空")
	@ExcelProperty("品牌名称") 
	private String name;
	        
	/**
     * 主营品类
     */
	@NotNull(message="主营品类不能为空")
	@ExcelProperty("主营品类") 
	private String category;
	        
	/**
     * 餐厅地址
     */
	@NotNull(message="餐厅地址不能为空")
	@ExcelProperty("餐厅地址") 
	private String address;
	        
	/**
     * 客流量
     */
	@NotNull(message="客流量不能为空")
	@ExcelProperty("客流量") 
	private Integer passengerFlow;
	        
	/**
     * 客单价
     */
	@NotNull(message="客单价不能为空")
	@ExcelProperty("客单价") 
	private Double price;
	        
	/**
     * 起始客户群体
     */
	@NotNull(message="起始客户群体不能为空")
	@ExcelProperty("起始客户群体") 
	private Integer startCustomerGroups;
	        
	/**
     * 客流旺季
     */
	@NotNull(message="客流旺季不能为空")
	@ExcelProperty("客流旺季") 
	private String peakSeason;
	        
	/**
     * 品牌特色
     */
	@NotNull(message="品牌特色不能为空")
	@ExcelProperty("品牌特色") 
	private String characteristic;
	        
	/**
     * 结束客户群体
     */
	@NotNull(message="结束客户群体不能为空")
	@ExcelProperty("结束客户群体") 
	private Integer endCustomerGroups;
	        
	/**
     * 所属用户
     */
    @Query(tableColumn = "a.food_user", javaField = "foodUser.id", type = QueryType.EQ)
	@ExcelProperty(value = "所属用户", converter = ExcelFieldDTOConverter.class)
	@ExcelFieldProperty(value = "foodUser.avatar", service =  "cn.weegoo.food.service.FoodUserService", wrapper= "cn.weegoo.food.service.mapstruct.FoodUserWrapper")
	private FoodUserDTO foodUser;

}
