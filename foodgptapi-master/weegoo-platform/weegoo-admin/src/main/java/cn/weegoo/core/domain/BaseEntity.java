package cn.weegoo.core.domain;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import cn.weegoo.config.swagger.IgnoreSwaggerParameter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/*
 * 数据Entity类
 * @author weegoo
 * @version 2021-05-16
 */
@Data
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 实体主键
     */
    @ExcelIgnore
    @TableId
    private String id;

    /**
     * 创建日期
     */
    @ExcelIgnore
    @IgnoreSwaggerParameter
    @ApiModelProperty(hidden = true)
    @TableField(fill = FieldFill.INSERT)
    private Date createDate;

    /**
     * 创建人
     */
    @ExcelIgnore
    @IgnoreSwaggerParameter
    @ApiModelProperty(hidden = true)
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 更新日期
     */
    @ExcelIgnore
    @IgnoreSwaggerParameter
    @ApiModelProperty(hidden = true)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateDate;

    /**
     * 更新人
     */
    @ExcelIgnore
    @IgnoreSwaggerParameter
    @ApiModelProperty(hidden = true)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 逻辑删除标记
     */
    @ExcelIgnore
    @IgnoreSwaggerParameter
    @ApiModelProperty(hidden = true)
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer delFlag;


    /**
     * 默认构造函数
     */

    public BaseEntity() {

    }

    /**
     * 构造函数
     */
    public BaseEntity(String id) {
        this.id = id;
    }


}
