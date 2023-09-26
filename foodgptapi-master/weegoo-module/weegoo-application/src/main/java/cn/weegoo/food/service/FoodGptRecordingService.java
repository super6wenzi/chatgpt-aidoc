/**
 * Copyright © 2021-2025 <a href="http://www.weegoo.cn/">weegoo</a> All rights reserved.
 */
package cn.weegoo.food.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.weegoo.food.domain.FoodGptRecording;
import cn.weegoo.food.mapper.FoodGptRecordingMapper;

/**
 * 生成记录表Service
 * @author weegoo
 * @version 2023-05-26
 */
@Service
@Transactional
public class FoodGptRecordingService extends ServiceImpl<FoodGptRecordingMapper, FoodGptRecording> {

}
