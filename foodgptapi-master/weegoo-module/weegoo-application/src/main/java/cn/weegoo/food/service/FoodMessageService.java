/**
 * Copyright © 2021-2025 <a href="http://www.weegoo.cn/">weegoo</a> All rights reserved.
 */
package cn.weegoo.food.service;

import cn.weegoo.sys.constant.CommonConstants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.weegoo.food.service.dto.FoodMessageDTO;
import cn.weegoo.food.service.dto.FoodMessageDetailDTO;
import cn.weegoo.food.service.mapstruct.FoodMessageWrapper;
import cn.weegoo.food.service.mapstruct.FoodMessageDetailWrapper;
import cn.weegoo.food.domain.FoodMessage;
import cn.weegoo.food.domain.FoodMessageDetail;
import cn.weegoo.food.mapper.FoodMessageMapper;

/**
 * 聊天记录Service
 * @author xx
 * @version 2023-06-07
 */
@Service
@Transactional
public class FoodMessageService extends ServiceImpl<FoodMessageMapper, FoodMessage> {
	/**
	* 子表service
	*/
	@Autowired
	private FoodMessageDetailService foodMessageDetailService;

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	public FoodMessageDTO findById(String id) {
		FoodMessageDTO foodMessageDTO = baseMapper.findById ( id );
		foodMessageDTO.setFoodMessageDetailDTOList(foodMessageDetailService.findList(id));
		return foodMessageDTO;
	}

	/**
	 * 自定义分页检索
	 * @param page
	 * @param queryWrapper
	 * @return
	 */
	public IPage <FoodMessageDTO> findPage(Page <FoodMessageDTO> page, QueryWrapper queryWrapper) {
		queryWrapper.eq ("a.del_flag", 0 ); // 排除已经删除
		return  baseMapper.findList (page, queryWrapper);
	}

	/**
	* 保存或者更新
	* @param  foodMessageDTO
	* @return
	*/
	public void saveOrUpdate(FoodMessageDTO foodMessageDTO) {
		FoodMessage foodMessage =  FoodMessageWrapper.INSTANCE.toEntity ( foodMessageDTO );
		super.saveOrUpdate (foodMessage);
		for (FoodMessageDetailDTO foodMessageDetailDTO : foodMessageDTO.getFoodMessageDetailDTOList ()){
			if ( CommonConstants.DELETED.equals ( foodMessageDetailDTO.getDelFlag()) ){
				foodMessageDetailService.removeById ( foodMessageDetailDTO.getId () );
			}else{
				FoodMessageDetail foodMessageDetail = FoodMessageDetailWrapper.INSTANCE.toEntity ( foodMessageDetailDTO );
				foodMessageDetail.setMessageId ( foodMessage.getId () );
				foodMessageDetailService.saveOrUpdate ( foodMessageDetail );
			}
		}
	}

	/**
	 * 删除
	 * @param  id
	 * @return
	 */
	public void removeById(String id) {
		super.removeById ( id );
		foodMessageDetailService.lambdaUpdate ().eq ( FoodMessageDetail::getMessageId, id ).remove ();
	}

}
