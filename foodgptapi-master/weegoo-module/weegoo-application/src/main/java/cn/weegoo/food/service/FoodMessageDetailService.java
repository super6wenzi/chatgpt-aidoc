/**
 * Copyright © 2021-2025 <a href="http://www.weegoo.cn/">weegoo</a> All rights reserved.
 */
package cn.weegoo.food.service;

import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.weegoo.food.service.mapstruct.FoodMessageDetailWrapper;
import cn.weegoo.food.service.dto.FoodMessageDetailDTO;
import cn.weegoo.food.domain.FoodMessageDetail;
import cn.weegoo.food.mapper.FoodMessageDetailMapper;

/**
 * 详细聊天内容Service
 * @author xx
 * @version 2023-06-07
 */
@Service
@Transactional
public class FoodMessageDetailService extends ServiceImpl<FoodMessageDetailMapper, FoodMessageDetail> {

	/**
	* 查询列表
	* @param messageId
	* @return
	*/
	public List <FoodMessageDetailDTO> findList(String messageId) {
		return super.lambdaQuery ().eq ( FoodMessageDetail::getMessageId, messageId ).orderByAsc(FoodMessageDetail::getCreateDate).orderByAsc(FoodMessageDetail::getType).list ().stream ().map (FoodMessageDetailWrapper.INSTANCE::toDTO ).collect( Collectors.toList());
	}

	/**
	 * 查询列表
	 * @param messageId
	 * @return
	 */
	public List <FoodMessageDetailDTO> findList(String messageId, String type) {
		LambdaQueryChainWrapper<FoodMessageDetail> wrapper = super.lambdaQuery().eq(FoodMessageDetail::getMessageId, messageId);
		if(StrUtil.isNotEmpty(type)){
			wrapper.eq( FoodMessageDetail::getType, type );
		}
		return wrapper.orderByDesc(FoodMessageDetail::getCreateDate).orderByAsc(FoodMessageDetail::getType).list ().stream ().map (FoodMessageDetailWrapper.INSTANCE::toDTO ).collect( Collectors.toList());
	}

}
