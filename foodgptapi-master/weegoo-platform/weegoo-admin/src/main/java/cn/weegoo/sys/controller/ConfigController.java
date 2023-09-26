/**
 * Copyright &copy; 2021-2026 <a href="http://www.weegoo.org/">weegoo</a> All rights reserved.
 */
package cn.weegoo.sys.controller;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import cn.weegoo.aop.demo.annotation.DemoMode;
import cn.weegoo.aop.logging.annotation.ApiLog;
import cn.weegoo.common.utils.ResponseUtil;
import cn.weegoo.sys.domain.SysConfig;
import cn.weegoo.sys.domain.vo.SysConfigVo;
import cn.weegoo.sys.service.SysConfigService;
import cn.weegoo.sys.service.mapstruct.SysConfigWrapper;
import cn.weegoo.sys.utils.SmsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 系统配置Controller
 *
 * @author 刘高峰
 * @version 2021-09-18
 */
@RestController
@RequestMapping("/sys/sysConfig")
public class ConfigController {

    @Autowired
    private SysConfigService sysConfigService;

    /**
     * 查询系统配置信息
     *
     * @return
     */
    @ApiLog("查询系统配置信息")
    @GetMapping("queryById")
    public ResponseEntity queryById() {
        SysConfig config = sysConfigService.getById ( "1" );
        return ResponseEntity.ok ( config );
    }

    /**
     * 返回给前台的系统配置信息
     *
     * @return
     */
    @ApiLog("查询系统配置信息")
    @GetMapping("getConfig")
    public ResponseEntity getConfig() {
        SysConfig sysConfig = sysConfigService.getById ( "1" );
        SysConfigVo vo = SysConfigWrapper.INSTANCE.toVo ( sysConfig );
        return ResponseEntity.ok ( vo );
    }

    /**
     * 测试短信
     *
     * @param tel
     * @return
     * @throws ClientException
     */
    @GetMapping("testSms")
    public ResponseEntity testSms(@RequestParam("tel") String tel) throws ClientException {
        SendSmsResponse response = SmsUtils.sendSms ( tel, "{code:123}" );
        if ( response.getCode ( ) != null && response.getCode ( ).equals ( "OK" ) ) {
            return ResponseEntity.ok ( "短信发送成功!" );
        } else {
            return ResponseEntity.badRequest ( ).body ( "短信发送失败!" + response.getMessage ( ) );
        }
    }

    /**
     * 保存系统配置
     */
    @DemoMode
    @ApiLog("更新系统配置")
    @PostMapping("save")
    public ResponseEntity save(@RequestBody SysConfig config) {
        if ( config.getMultiAccountLogin ( ) == null ) {
            config.setMultiAccountLogin ( "1" );
        }
        config.setId ( "1" );
        sysConfigService.updateById ( config );
        SysConfig target = sysConfigService.getById ( "1" );
        return ResponseUtil.newInstance ( ).add ( "config", target ).ok ( "保存系统配置成功" );
    }

}
