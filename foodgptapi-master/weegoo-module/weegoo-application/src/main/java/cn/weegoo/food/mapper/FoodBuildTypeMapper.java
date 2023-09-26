/**
 * Copyright © 2021-2025 <a href="http://www.weegoo.cn/">weegoo</a> All rights reserved.
 */
package cn.weegoo.food.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.weegoo.food.service.dto.FoodBuildTypeDTO;
import cn.weegoo.food.domain.FoodBuildType;

/**
 * 生成类型MAPPER接口
 * @author weegoo
 * @version 2023-05-26
 */
public interface FoodBuildTypeMapper extends BaseMapper<FoodBuildType> {

    /**
     * 根据id获取生成类型
     * @param id
     * @return
     */
    FoodBuildTypeDTO findById(String id);

    /**
     * 获取生成类型列表
     *
     * @param queryWrapper
     * @return
     */
    IPage <FoodBuildTypeDTO> findList(Page <FoodBuildTypeDTO> page, @Param(Constants.WRAPPER) QueryWrapper queryWrapper);

}
