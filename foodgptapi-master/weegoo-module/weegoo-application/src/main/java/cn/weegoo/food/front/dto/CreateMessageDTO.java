package cn.weegoo.food.front.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author damin
 */
@Data
public class CreateMessageDTO {

    @ApiModelProperty("生成类型id")
    private String foodBuildId;

    @ApiModelProperty("表单内容")
    private List<FormDTO> formDTOList;

}
