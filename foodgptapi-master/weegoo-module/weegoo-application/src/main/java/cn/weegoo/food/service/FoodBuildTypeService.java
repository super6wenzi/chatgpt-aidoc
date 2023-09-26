/**
 * Copyright © 2021-2025 <a href="http://www.weegoo.cn/">weegoo</a> All rights reserved.
 */
package cn.weegoo.food.service;

import cn.weegoo.food.domain.FoodContentType;
import cn.weegoo.food.front.vo.BuildTypeVO;
import cn.weegoo.food.front.vo.ContentTypeVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.weegoo.food.service.dto.FoodBuildTypeDTO;
import cn.weegoo.food.domain.FoodBuildType;
import cn.weegoo.food.mapper.FoodBuildTypeMapper;

import java.net.InetAddress;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 生成类型Service
 * @author weegoo
 * @version 2023-05-26
 */
@Service
@Transactional
public class FoodBuildTypeService extends ServiceImpl<FoodBuildTypeMapper, FoodBuildType> {

    @Autowired
    private FoodContentTypeService foodContentTypeService;
    /**
     * 根据id查询
     * @param id
     * @return
     */
    public FoodBuildTypeDTO findById(String id) {
        return baseMapper.findById ( id );
    }

    /**
     * 自定义分页检索
     * @param page
     * @param queryWrapper
     * @return
     */
    public IPage <FoodBuildTypeDTO> findPage(Page <FoodBuildTypeDTO> page, QueryWrapper queryWrapper) {
        queryWrapper.eq ("a.del_flag", 0 ); // 排除已经删除
        return  baseMapper.findList (page, queryWrapper);
    }
    /**
     * 获取内容类型
     * @return
     */
    public List<ContentTypeVO> getList() {

        List<ContentTypeVO> contentTypeVOList = foodContentTypeService.list(new QueryWrapper<FoodContentType>().eq("del_flag", 0).eq("is_show","1").orderByAsc("sort")).stream().map(e -> {
            ContentTypeVO contentTypeVO = new ContentTypeVO();
            BeanUtils.copyProperties(e, contentTypeVO);
            return contentTypeVO;
        }).collect(Collectors.toList());
        for (ContentTypeVO contentTypeVO : contentTypeVOList) {
            List<BuildTypeVO> list = list(new QueryWrapper<FoodBuildType>().eq("del_flag", 0).eq("is_show","1").eq("food_content_type", contentTypeVO.getId()).orderByAsc("sort")).stream().map(e -> {
                BuildTypeVO buildTypeVO = new BuildTypeVO();
                BeanUtils.copyProperties(e, buildTypeVO);
                buildTypeVO.setIcon("https://aidoc.ggchat.com.cn"+e.getIcon());
                return buildTypeVO;
            }).collect(Collectors.toList());
            contentTypeVO.setBuildType(list);
        }
        return contentTypeVOList;
    }

}
