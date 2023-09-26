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
import cn.weegoo.food.service.dto.FoodBrandDTO;
import cn.weegoo.food.domain.FoodBrand;

/**
 * 品牌MAPPER接口
 * @author weegoo
 * @version 2023-05-26
 */
public interface FoodBrandMapper extends BaseMapper<FoodBrand> {

    /**
     * 根据id获取品牌
     * @param id
     * @return
     */
    FoodBrandDTO findById(String id);

    /**
     * 获取品牌列表
     *
     * @param queryWrapper
     * @return
     */
    IPage <FoodBrandDTO> findList(Page <FoodBrandDTO> page, @Param(Constants.WRAPPER) QueryWrapper queryWrapper);

}
