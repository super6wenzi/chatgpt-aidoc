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
import cn.weegoo.food.service.dto.FoodMessageDTO;
import cn.weegoo.food.domain.FoodMessage;

/**
 * 聊天记录MAPPER接口
 * @author xx
 * @version 2023-06-07
 */
public interface FoodMessageMapper extends BaseMapper<FoodMessage> {

    /**
     * 根据id获取聊天记录
     * @param id
     * @return
     */
    FoodMessageDTO findById(String id);

    /**
     * 获取聊天记录列表
     *
     * @param queryWrapper
     * @return
     */
    IPage <FoodMessageDTO> findList(Page <FoodMessageDTO> page, @Param(Constants.WRAPPER) QueryWrapper queryWrapper);

}
