package cn.weegoo.core.service.dto;


import com.alibaba.excel.annotation.ExcelIgnore;
import cn.weegoo.config.swagger.IgnoreSwaggerParameter;
import cn.weegoo.sys.service.dto.UserDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public abstract class BaseDTO<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 实体主键
     */
    @ExcelIgnore
    protected String id;

    /**
     * 创建日期
     */
    @ExcelIgnore
    @IgnoreSwaggerParameter
    @ApiModelProperty(hidden = true)
    protected Date createDate;

    /**
     * 创建人
     */
    @ExcelIgnore
    @IgnoreSwaggerParameter
    @ApiModelProperty(hidden = true)
    protected UserDTO createBy;

    /**
     * 更新日期
     */
    @ExcelIgnore
    @IgnoreSwaggerParameter
    @ApiModelProperty(hidden = true)
    protected Date updateDate;

    /**
     * 更新人
     */
    @ExcelIgnore
    @IgnoreSwaggerParameter
    @ApiModelProperty(hidden = true)
    protected UserDTO updateBy;

    /**
     * 逻辑删除标记
     */
    @ExcelIgnore
    @IgnoreSwaggerParameter
    @ApiModelProperty(hidden = true)
    protected Integer delFlag;

    /**
     * 构造函数
     */
    public BaseDTO() {

    }

    /**
     * 构造函数
     *
     * @param id
     */
    public BaseDTO(String id) {
        this.id = id;
    }


}

