package cn.weegoo.food.front.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author damin
 */
@Data
public class BrandVO {
    private String id;
    /**
     * 品牌名称
     */
    @ApiModelProperty("品牌名称")
    private String name;
    /**
     * 主营品类
     */
    @ApiModelProperty("主营品类")
    private String category;
    /**
     * 餐厅地址
     */
    @ApiModelProperty("餐厅地址")
    private String address;
    /**
     * 客流量
     */
    @ApiModelProperty("客流量")
    private Integer passengerFlow;
    /**
     * 客单价
     */
    @ApiModelProperty("客单价")
    private Double price;
    /**
     * 起始客户群体
     */
    @ApiModelProperty("起始客户群体")
    private Integer startCustomerGroups;

    @ApiModelProperty("客户性别")
    private String customerSex;

    /**
     * 客流旺季
     */
    @ApiModelProperty("客流旺季")
    private String peakSeason;
    /**
     * 品牌特色
     */
    @ApiModelProperty("品牌特色")
    private String characteristic;
    /**
     * 结束客户群体
     */
    @ApiModelProperty("结束客户群体")
    private Integer endCustomerGroups;
}
