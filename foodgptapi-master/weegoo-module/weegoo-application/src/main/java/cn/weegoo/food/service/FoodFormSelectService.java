/**
 * Copyright © 2021-2025 <a href="http://www.weegoo.cn/">weegoo</a> All rights reserved.
 */
package cn.weegoo.food.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.weegoo.food.service.mapstruct.FoodFormSelectWrapper;
import cn.weegoo.food.service.dto.FoodFormSelectDTO;
import cn.weegoo.food.domain.FoodFormSelect;
import cn.weegoo.food.mapper.FoodFormSelectMapper;

/**
 * 表单下拉选项Service
 * @author xx
 * @version 2023-06-15
 */
@Service
@Transactional
public class FoodFormSelectService extends ServiceImpl<FoodFormSelectMapper, FoodFormSelect> {

	/**
	* 查询列表
	* @param formId
	* @return
	*/
	public List <FoodFormSelectDTO> findList(String formId) {
		return super.lambdaQuery ().eq ( FoodFormSelect::getFormId, formId ).orderByAsc(FoodFormSelect::getSort).list ().stream ().map (FoodFormSelectWrapper.INSTANCE::toDTO ).collect( Collectors.toList());
	}

}
