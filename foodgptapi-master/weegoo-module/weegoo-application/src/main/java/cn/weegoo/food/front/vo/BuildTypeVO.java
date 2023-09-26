package cn.weegoo.food.front.vo;

import cn.weegoo.core.excel.ExcelFieldDTOConverter;
import cn.weegoo.core.excel.annotation.ExcelFieldProperty;
import cn.weegoo.food.service.dto.FoodContentTypeDTO;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BuildTypeVO {


    private String id;
    /**
     * 所属内容分类
     */
    @ApiModelProperty("所属内容分类")
    @ExcelProperty(value = "所属内容分类", converter = ExcelFieldDTOConverter.class)
    @ExcelFieldProperty(value = "foodContentType.name", service =  "cn.weegoo.food.service.FoodContentTypeService", wrapper= "cn.weegoo.food.service.mapstruct.FoodContentTypeWrapper")
    private FoodContentTypeDTO foodContentType;

    /**
     * 标题
     */
    @ApiModelProperty("标题")
    private String title;

    /**
     * 图标
     */
    @ApiModelProperty("图标")
    private String icon;

    /**
     * 显示顺序
     */
    @ApiModelProperty("显示顺序")
    private String sort;

    /**
     * 是否显示下一步
     */
    private String isShowNext;
}
