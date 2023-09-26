/**
 * Copyright © 2021-2025 <a href="http://www.weegoo.cn/">weegoo</a> All rights reserved.
 */
package cn.weegoo.food.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.weegoo.food.domain.FoodConfig;
import cn.weegoo.food.mapper.FoodConfigMapper;

/**
 * model配置Service
 * @author weegoo
 * @version 2023-06-01
 */
@Service
@Transactional
public class FoodConfigService extends ServiceImpl<FoodConfigMapper, FoodConfig> {

}
