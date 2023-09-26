/**
 * Copyright &copy; 2021-2026 <a href="http://www.weegoo.org/">weegoo</a> All rights reserved.
 */
package cn.weegoo.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import cn.weegoo.aop.logging.annotation.ApiLog;
import cn.weegoo.core.query.QueryWrapperGenerator;
import cn.weegoo.sys.service.LogService;
import cn.weegoo.sys.service.dto.LogDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 日志Controller
 *
 * @author weegoo
 * @version 2021-6-2
 */

@RestController
@RequestMapping("/sys/log")
public class LogController {

    @Autowired
    private LogService logService;

    /**
     * 获取日志列表
     *
     * @param logDTO
     * @param page
     * @return
     * @throws Exception
     */
    @ApiLog("日志列表")
    @PreAuthorize("hasAuthority('sys:log:list')")
    @GetMapping("list")
    public ResponseEntity data(LogDTO logDTO, Page <LogDTO> page) throws Exception {
        QueryWrapper <LogDTO> queryWrapper = QueryWrapperGenerator.buildQueryCondition ( logDTO, LogDTO.class );
        IPage <LogDTO> result = logService.findPage ( page, queryWrapper );
        return ResponseEntity.ok ( result );
    }

    /**
     * 我的日志
     *
     * @param logDTO
     * @param page
     * @return
     * @throws Exception
     */
    @ApiLog("我的日志")
    @GetMapping("data/mine")
    public ResponseEntity mine(LogDTO logDTO, Page <LogDTO> page) throws Exception {
        QueryWrapper <LogDTO> queryWrapper = QueryWrapperGenerator.buildQueryCondition ( logDTO, LogDTO.class );
        IPage <LogDTO> result = logService.findPage ( page, queryWrapper );
        return ResponseEntity.ok ( result );
    }


    /**
     * 删除日志
     *
     * @param ids
     * @return
     */
    @ApiLog("删除日志")
    @PreAuthorize("hasAuthority('sys:log:del')")
    @DeleteMapping("delete")
    public ResponseEntity delete(String ids) {
        logService.removeByIds ( Lists.newArrayList ( ids.split ( "," ) ) );
        return ResponseEntity.ok ( "删除日志成功！" );
    }

    /**
     * 清空日志
     *
     * @return
     */
    @ApiLog("清空日志")
    @PreAuthorize("hasAuthority('sys:log:del')")
    @DeleteMapping("empty")
    public ResponseEntity empty() {
        logService.remove ( new QueryWrapper <> ( ) );
        return ResponseEntity.ok ( "清空日志成功!" );
    }
}
