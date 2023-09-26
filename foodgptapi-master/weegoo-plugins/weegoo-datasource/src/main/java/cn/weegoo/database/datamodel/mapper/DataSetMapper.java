/**
 * Copyright &copy; 2021-2026 <a href="http://www.weegoo.org/">weegoo</a> All rights reserved.
 */
package cn.weegoo.database.datamodel.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.weegoo.database.datamodel.domain.DataSet;
import cn.weegoo.database.datamodel.service.dto.DataSetDTO;
import org.apache.ibatis.annotations.Param;

/**
 * 数据模型MAPPER接口
 *
 * @author 刘高峰
 * @version 2021-08-07
 */
public interface DataSetMapper extends BaseMapper <DataSet> {

    DataSetDTO get(String id);


    /**
     * 获数据模型列表
     *
     * @param queryWrapper
     * @return
     */
    IPage <DataSetDTO> findList(Page <DataSetDTO> page, @Param(Constants.WRAPPER) QueryWrapper queryWrapper);

}
