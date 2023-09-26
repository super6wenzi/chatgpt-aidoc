package cn.weegoo.food.front.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class FormVO {

    /**
     * 标题
     */
    @ApiModelProperty("标题")
    private String name;

    /**
     * 图标
     */
    @ApiModelProperty("1输入框，2下拉选项")
    private String type;

    @ApiModelProperty("提示")
    private String tags;

    @ApiModelProperty("标题")
    private List<FormSelectVO> selectVOList;
}
