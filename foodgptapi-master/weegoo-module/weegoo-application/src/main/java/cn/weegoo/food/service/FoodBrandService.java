/**
 * Copyright © 2021-2025 <a href="http://www.weegoo.cn/">weegoo</a> All rights reserved.
 */
package cn.weegoo.food.service;

import cn.weegoo.food.front.dto.BrandDTO;
import cn.weegoo.food.front.vo.BrandVO;
import cn.weegoo.utils.WGTokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.weegoo.food.service.dto.FoodBrandDTO;
import cn.weegoo.food.domain.FoodBrand;
import cn.weegoo.food.mapper.FoodBrandMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 品牌Service
 * @author weegoo
 * @version 2023-05-26
 */
@Service
@Transactional
public class FoodBrandService extends ServiceImpl<FoodBrandMapper, FoodBrand> {

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	public FoodBrandDTO findById(String id) {
		return baseMapper.findById ( id );
	}

	/**
	 * 自定义分页检索
	 * @param page
	 * @param queryWrapper
	 * @return
	 */
	public IPage <FoodBrandDTO> findPage(Page <FoodBrandDTO> page, QueryWrapper queryWrapper) {
		queryWrapper.eq ("a.del_flag", 0 ); // 排除已经删除
		return  baseMapper.findList (page, queryWrapper);
	}

    /**
     * 获取列别
     * @param current 分页参数
     * @param size 分页参数
     * @return
     */
    public Page<BrandVO> getList(int current, int size){
        System.out.println(WGTokenUtil.getUserId());
        IPage<FoodBrand> food = page(new Page<>(current,size),new QueryWrapper<FoodBrand>().eq("del_flag", "0").eq("food_user", WGTokenUtil.getUserId()));
        List<BrandVO> list= food.getRecords().stream().map(e ->{
            BrandVO brandVO =new BrandVO();
            BeanUtils.copyProperties(e,brandVO);
            return brandVO;
        }).collect(Collectors.toList());
        Page<BrandVO> page =new Page();
        BeanUtils.copyProperties(food,page);
        page.setRecords(list);
        return page;
    }
    /**
     * 添加或修改品牌
     * @return
     */
    public String addOrSaveBrand(BrandDTO brandDTO){
        FoodBrand foodBrand =new FoodBrand();
        if (StringUtils.isBlank(brandDTO.getId())){
            BeanUtils.copyProperties(brandDTO,foodBrand);
            foodBrand.setFoodUserId(WGTokenUtil.getUserId());
            saveOrUpdate(foodBrand);
        }else {
            BeanUtils.copyProperties(brandDTO,foodBrand);
            saveOrUpdate(foodBrand);
        }
        return foodBrand.getId();
    }

    public BrandVO getInfo(String id){
        FoodBrand foodBrand =getById(id);
        BrandVO brandVO =new BrandVO();
        BeanUtils.copyProperties(foodBrand, brandVO);
        return brandVO;
    }
}
