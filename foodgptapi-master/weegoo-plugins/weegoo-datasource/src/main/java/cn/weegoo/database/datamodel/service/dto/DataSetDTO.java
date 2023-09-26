/**
 * Copyright &copy; 2021-2026 <a href="http://www.weegoo.org/">weegoo</a> All rights reserved.
 */
package cn.weegoo.database.datamodel.service.dto;


import com.google.common.collect.Lists;
import cn.weegoo.core.query.Query;
import cn.weegoo.core.service.dto.BaseDTO;
import cn.weegoo.database.datalink.domain.DataSource;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 数据模型Entity
 *
 * @author 刘高峰
 * @version 2021-08-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DataSetDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;
    /**
     * 目标数据库
     */
    @Query(tableColumn = "dataSource.name", javaField = "dataSource.name")
    private DataSource dataSource;
    /**
     * 数据源名称
     */
    @Query(tableColumn = "a.name")
    private String name;
    /**
     * sql语句
     */
    private String sqlCmd;
    /**
     * 关联数据集
     */
    private List <DataMetaDTO> columnList = Lists.newArrayList ( );
    /**
     * 关联参数
     */
    private List <DataParamDTO> params = Lists.newArrayList ( );
}
