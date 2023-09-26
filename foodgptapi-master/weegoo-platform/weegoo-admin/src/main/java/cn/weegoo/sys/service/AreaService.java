/**
 * Copyright &copy; 2021-2026 <a href="http://www.weegoo.org/">weegoo</a> All rights reserved.
 */
package cn.weegoo.sys.service;

import cn.weegoo.core.service.TreeService;
import cn.weegoo.sys.constant.CacheNames;
import cn.weegoo.sys.domain.Area;
import cn.weegoo.sys.mapper.AreaMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 区域Service
 *
 * @author weegoo
 * @version 2021-05-16
 */
@Service
@Transactional
public class AreaService extends TreeService<AreaMapper, Area> {

    @Cacheable(cacheNames = CacheNames.SYS_CACHE_AREA_LIST)
    public List <Area> findAll() {
        return super.list ( );
    }

    @CacheEvict(cacheNames = CacheNames.SYS_CACHE_AREA_LIST, allEntries = true)
    public boolean saveOrUpdate(Area area) {
        return super.saveOrUpdate ( area );
    }

    @CacheEvict(cacheNames = CacheNames.SYS_CACHE_AREA_LIST, allEntries = true)
    public boolean removeWithChildrenById(String id) {
        return super.removeWithChildrenById ( id );
    }

}
