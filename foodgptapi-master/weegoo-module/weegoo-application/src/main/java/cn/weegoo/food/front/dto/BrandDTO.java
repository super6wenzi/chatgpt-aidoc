package cn.weegoo.food.front.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author damin
 */
@Data
public class BrandDTO {
    @ApiModelProperty("品牌Id为空代表新增")
    private String id;
    /**
     * 品牌名称
     */
    @ApiModelProperty("品牌名称")
    @NotNull(message="品牌名称不能为空")
    private String name;
    /**
     * 主营品类
     */
    @NotNull(message="主营品类不能为空")
    @ApiModelProperty("主营品类")
    private String category;
    /**
     * 餐厅地址
     */
    @NotNull(message="餐厅地址不能为空")
    @ApiModelProperty("餐厅地址")
    private String address;
    /**
     * 客流量
     */
    @NotNull(message="客流量不能为空")
    @ApiModelProperty("客流量")
    private Integer passengerFlow;
    /**
     * 客单价
     */
    @NotNull(message="客单价不能为空")
    @ApiModelProperty("客单价")
    private Double price;
    /**
     * 起始客户群体
     */
    @NotNull(message="起始客户群体不能为空")
    @ApiModelProperty("起始客户群体")
    private Integer startCustomerGroups;

    @ApiModelProperty("客户性别")
    private String customerSex;
    /**
     * 客流旺季
     */
    @NotNull(message="客流旺季不能为空")
    @ApiModelProperty("客流旺季")
    private String peakSeason;
    /**
     * 品牌特色
     */
    @NotNull(message="品牌特色不能为空")
    @ApiModelProperty("品牌特色")
    private String characteristic;
    /**
     * 结束客户群体
     */
    @NotNull(message="结束客户群体不能为空")
    @ApiModelProperty("结束客户群体")
    private Integer endCustomerGroups;

}
