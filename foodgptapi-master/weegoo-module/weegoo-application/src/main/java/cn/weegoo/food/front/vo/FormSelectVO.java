package cn.weegoo.food.front.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FormSelectVO {

    /**
     * 标题
     */
    @ApiModelProperty("下拉选择名")
    private String name;

    /**
     * 图标
     */
    @ApiModelProperty("下拉选择值")
    private String value;

}
