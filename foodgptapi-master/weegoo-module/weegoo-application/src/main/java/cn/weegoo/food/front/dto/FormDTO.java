package cn.weegoo.food.front.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FormDTO {

    /**
     * 标题
     */
    @ApiModelProperty("标题")
    private String name;

    /**
     * 内容
     */
    @ApiModelProperty("内容")
    private String value;

    @ApiModelProperty("提示")
    private String tags;
}
