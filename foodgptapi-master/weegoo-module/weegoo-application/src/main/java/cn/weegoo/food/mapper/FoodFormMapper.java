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
import cn.weegoo.food.service.dto.FoodFormDTO;
import cn.weegoo.food.domain.FoodForm;

/**
 * 表单配置MAPPER接口
 * @author xx
 * @version 2023-06-15
 */
public interface FoodFormMapper extends BaseMapper<FoodForm> {

    /**
     * 根据id获取表单配置
     * @param id
     * @return
     */
    FoodFormDTO findById(String id);

    /**
     * 获取表单配置列表
     *
     * @param queryWrapper
     * @return
     */
    IPage <FoodFormDTO> findList(Page <FoodFormDTO> page, @Param(Constants.WRAPPER) QueryWrapper queryWrapper);

}
