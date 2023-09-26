package cn.weegoo.quartz.controller;


import cn.weegoo.quartz.server.ServerOS;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 系统监控Controller
 *
 * @author weegoo
 * @version 2021-09-01
 */
@RestController
@RequestMapping("/monitor")
public class MonitorController {
    @GetMapping("/server/info")
    public ResponseEntity info() {

        return ResponseEntity.ok ( ServerOS.getInfo ( ) );
    }

}
