/**
 * Copyright &copy; 2021-2026 <a href="http://www.weegoo.org/">weegoo</a> All rights reserved.
 */
package cn.weegoo.database.datamodel.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import cn.weegoo.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 数据模型参数Entity
 *
 * @author 刘高峰
 * @version 2021-08-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("plugin_datasource_model_params")
public class DataParam extends BaseEntity {

    private static final long serialVersionUID = 1L;
    /**
     * 数据集
     */
    private String dataSetId;
    /**
     * 字段
     */
    private String field;
    /**
     * 映射关系
     */
    private String defaultValue;
    /**
     * 排序
     */
    private int sort;
}
