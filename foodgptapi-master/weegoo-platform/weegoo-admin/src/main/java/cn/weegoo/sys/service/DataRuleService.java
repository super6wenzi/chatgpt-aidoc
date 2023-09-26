/**
 * Copyright &copy; 2021-2026 <a href="http://www.weegoo.org/">weegoo</a> All rights reserved.
 */
package cn.weegoo.sys.service;

import com.baomidou.dynamic.datasource.annotation.Master;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.weegoo.sys.constant.CacheNames;
import cn.weegoo.sys.domain.DataRule;
import cn.weegoo.sys.mapper.DataRuleMapper;
import cn.weegoo.sys.service.dto.DataRuleDTO;
import cn.weegoo.sys.service.dto.UserDTO;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 数据权限Service
 *
 * @author lgf
 * @version 2021-04-02
 */
@Service
@Transactional
@Master
public class DataRuleService extends ServiceImpl <DataRuleMapper, DataRule> {


    @Cacheable(cacheNames = CacheNames.USER_CACHE_DATA_RULE_LIST, key = "#userDTO.id")
    public List <DataRuleDTO> findByUserId(UserDTO userDTO) {
        return baseMapper.findByUserId ( userDTO );
    }

    @CacheEvict(cacheNames = CacheNames.USER_CACHE_DATA_RULE_LIST, allEntries = true)
    public void delete(String id) {
        //解除数据权限角色关联
        baseMapper.deleteRoleDataRule ( id );
        baseMapper.deleteById ( id );
    }

    @CacheEvict(cacheNames = CacheNames.USER_CACHE_DATA_RULE_LIST, allEntries = true)
    public boolean saveOrUpdate(DataRule dataRule) {
        return super.saveOrUpdate ( dataRule );
    }


}
