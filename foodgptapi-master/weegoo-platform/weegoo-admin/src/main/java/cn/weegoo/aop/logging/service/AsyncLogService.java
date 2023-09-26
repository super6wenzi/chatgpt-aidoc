/**
 * Copyright &copy; 2021-2026 <a href="http://www.weegoo.org/">weegoo</a> All rights reserved.
 */
package cn.weegoo.aop.logging.service;

import cn.weegoo.sys.domain.Log;
import cn.weegoo.sys.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 异步日志服务
 *
 * @author weegoo
 * @version 2021-06-11
 */
@Service
public class AsyncLogService {

    @Autowired
    private LogService logService;

    /**
     * 异步保存日志
     *
     * @throws Throwable
     */
    @Async
    public void saveLog(Log log) {
        logService.save ( log );
    }


}
