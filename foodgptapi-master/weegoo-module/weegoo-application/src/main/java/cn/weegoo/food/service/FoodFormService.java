/**
 * Copyright © 2021-2025 <a href="http://www.weegoo.cn/">weegoo</a> All rights reserved.
 */
package cn.weegoo.food.service;

import cn.hutool.core.bean.BeanUtil;
import cn.weegoo.food.front.vo.FormSelectVO;
import cn.weegoo.food.front.vo.FormVO;
import cn.weegoo.sys.constant.CommonConstants;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.weegoo.food.service.dto.FoodFormDTO;
import cn.weegoo.food.service.dto.FoodFormSelectDTO;
import cn.weegoo.food.service.mapstruct.FoodFormWrapper;
import cn.weegoo.food.service.mapstruct.FoodFormSelectWrapper;
import cn.weegoo.food.domain.FoodForm;
import cn.weegoo.food.domain.FoodFormSelect;
import cn.weegoo.food.mapper.FoodFormMapper;

import java.util.List;

/**
 * 表单配置Service
 * @author xx
 * @version 2023-06-15
 */
@Service
@Transactional
public class FoodFormService extends ServiceImpl<FoodFormMapper, FoodForm> {
	/**
	* 子表service
	*/
	@Autowired
	private FoodFormSelectService foodFormSelectService;

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	public FoodFormDTO findById(String id) {
		FoodFormDTO foodFormDTO = baseMapper.findById ( id );
		foodFormDTO.setFoodFormSelectDTOList(foodFormSelectService.findList(id));
		return foodFormDTO;
	}

	/**
	 * 自定义分页检索
	 * @param page
	 * @param queryWrapper
	 * @return
	 */
	public IPage <FoodFormDTO> findPage(Page <FoodFormDTO> page, QueryWrapper queryWrapper) {
		queryWrapper.eq ("a.del_flag", 0 ); // 排除已经删除
		return  baseMapper.findList (page, queryWrapper);
	}

	/**
	* 保存或者更新
	* @param  foodFormDTO
	* @return
	*/
	public void saveOrUpdate(FoodFormDTO foodFormDTO) {
		FoodForm foodForm =  FoodFormWrapper.INSTANCE.toEntity ( foodFormDTO );
		super.saveOrUpdate (foodForm);
		for (FoodFormSelectDTO foodFormSelectDTO : foodFormDTO.getFoodFormSelectDTOList ()){
			if ( CommonConstants.DELETED.equals ( foodFormSelectDTO.getDelFlag()) ){
				foodFormSelectService.removeById ( foodFormSelectDTO.getId () );
			}else{
				FoodFormSelect foodFormSelect = FoodFormSelectWrapper.INSTANCE.toEntity ( foodFormSelectDTO );
				foodFormSelect.setFormId ( foodForm.getId () );
				foodFormSelectService.saveOrUpdate ( foodFormSelect );
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
		foodFormSelectService.lambdaUpdate ().eq ( FoodFormSelect::getFormId, id ).remove ();
	}

	public List<FormVO> getByBuildTypeId(String buildTypeId) {
		List<FoodForm> foodFormList = list(new LambdaQueryWrapper<>(FoodForm.class).eq(FoodForm::getBuildTypeId, buildTypeId).orderByAsc(FoodForm::getSort));

		List<FormVO> formVOList = foodFormList.stream().map(foodForm -> {
			FormVO formVO = BeanUtil.copyProperties(foodForm, FormVO.class);

			if("2".equals(foodForm.getType())) {
				List<FoodFormSelectDTO> foodFormSelectList = foodFormSelectService.findList(foodForm.getId());
				formVO.setSelectVOList(BeanUtil.copyToList(foodFormSelectList, FormSelectVO.class));
			}

			return formVO;
		}).collect(java.util.stream.Collectors.toList());

		return formVOList;
	}
}
