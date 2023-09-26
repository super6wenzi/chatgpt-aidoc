/**
 * Copyright &copy; 2021-2026 <a href="http://www.weegoo.org/">weegoo</a> All rights reserved.
 */
package cn.weegoo.database.datamodel.domain;


import com.baomidou.mybatisplus.annotation.TableName;
import cn.weegoo.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 数据资源Entity
 *
 * @author 刘高峰
 * @version 2021-08-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("plugin_datasource_model_meta")
public class DataMeta extends BaseEntity {

    private static final long serialVersionUID = 1L;
    /**
     * 字段名
     */
    private String name;
    /**
     * 字段描述
     */
    private String label;
    /**
     * 字段类型
     */
    private String type;
    /**
     * 是否需要分析该字段
     */
    private Boolean isNeed;
    /**
     * 数据模型
     */
    private String dataSetId;
    /**
     * 排序
     */
    private int sort;
}
