package cn.weegoo.food.front;

import cn.weegoo.execption.WGException;
import cn.weegoo.food.domain.FoodUser;
import cn.weegoo.food.front.vo.UserVO;
import cn.weegoo.food.service.FoodUserService;
import cn.weegoo.utils.R;
import cn.weegoo.utils.WGTokenUtil;
import cn.weegoo.utils.WxUtils;
import com.alibaba.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "用户登录接口")
@RequestMapping("/api/user")
public class ApiFoodUserController {
    @Autowired
    private FoodUserService foodUserService;
    @ApiOperation("微信授权登录")
    @GetMapping("wxLogin")
    public R wxLogin(String code) throws Exception {
        return R.data(foodUserService.wxLogin(code));
    }
    @ApiOperation("完善用户信息")
    @GetMapping("updateUserInfo")
    public R updateUserInfo(String nick, String avatar){
        foodUserService.updateUserInfo(nick,avatar);
        return R.success();
    }
    @ApiOperation("获取用户详情")
    @GetMapping("getUserInfo")
    public R<UserVO> getUserInfo(){
        return R.data(foodUserService.getUserInfo());
    }

}
