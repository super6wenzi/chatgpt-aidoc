/**
 * Copyright &copy; 2021-2025 <a href="http://www.weegoo.org/">weegoo</a> All rights reserved.
 */
package cn.weegoo.sys.controller.app;

import cn.weegoo.sys.controller.LoginController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录Controller
 *
 * @author weegoo
 * @version 2021-5-31
 */
@RestController
@Api(tags = "移动端登录管理")
@RequestMapping("/app")
public class AppLoginController extends LoginController {


}
