/**
 * Copyright &copy; 2021-2026 <a href="http://www.weegoo.org/">weegoo</a> All rights reserved.
 */
package cn.weegoo.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.weegoo.sys.domain.Log;
import cn.weegoo.sys.mapper.LogMapper;
import cn.weegoo.sys.service.dto.LogDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 日志Service
 *
 * @author weegoo
 * @version 2021-05-16
 */
@Service
@Transactional
public class LogService extends ServiceImpl <LogMapper, Log> {


    /**
     * 自定义分页检索
     *
     * @param page
     * @param queryWrapper
     * @return
     */
    public IPage <LogDTO> findPage(Page <LogDTO> page, QueryWrapper queryWrapper) {
        queryWrapper.eq ( "a.del_flag", 0 ); // 排除已经删除
        return baseMapper.findList ( page, queryWrapper );


    }
}
