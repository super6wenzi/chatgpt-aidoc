/**
 * Copyright © 2021-2025 <a href="http://www.weegoo.cn/">weegoo</a> All rights reserved.
 */
package cn.weegoo.food.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.extra.emoji.EmojiUtil;
import cn.weegoo.execption.WGException;
import cn.weegoo.food.domain.FoodConfig;
import cn.weegoo.food.front.vo.UserVO;
import cn.weegoo.utils.WGTokenUtil;
import cn.weegoo.utils.WxUtils;
import com.alibaba.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.weegoo.food.domain.FoodUser;
import cn.weegoo.food.mapper.FoodUserMapper;

import java.net.InetAddress;

/**
 * 用户Service
 * @author weegoo
 * @version 2023-05-26
 */
@Service
@Transactional
public class FoodUserService extends ServiceImpl<FoodUserMapper, FoodUser> {
    @Autowired
    private FoodConfigService foodConfigService;

    public String wxLogin(String code) throws Exception {
        if (StringUtils.isBlank(code)){
            throw new WGException("微信标识为空");
        }
        String openId = WxUtils.getOpenIdByCode(code);
//        String openId ="123456";
        if (StringUtils.isBlank(openId)){
            throw new WGException("获取微信标识失败");
        }
        FoodUser user = getOne(new QueryWrapper<FoodUser>().eq("openid",openId));

        if (user == null){
            user=new FoodUser();
            user.setOpenid(openId);
            user.setVip("宝餐会员");
            user.setUserType("游客");

            FoodConfig foodConfig = foodConfigService.getOne(new QueryWrapper<FoodConfig>().eq("config_key","freeTimes"));
            if (foodConfig != null) {
                user.setTimes(Integer.valueOf(foodConfig.getConfigValue()));
            }

            saveOrUpdate(user);
        }
        return  WGTokenUtil.getToken(user.getId());
    }
    /**
     * 完善用户资料
     */
    public Boolean updateUserInfo(String nick, String avatar) {
        String userId = WGTokenUtil.getUserId();
        FoodUser user = getById(userId);
        user.setAvatar(avatar);
        try {
            user.setName(nick);
            return saveOrUpdate(user);
        } catch (Exception e) {
            e.printStackTrace();
            //昵称表情过滤
            nick = EmojiUtil.toAlias(nick);
            user.setName(nick);
            return saveOrUpdate(user);
        }
    }
    /**
     * 获取用户详情
     */
    public UserVO getUserInfo() {
        String userId = WGTokenUtil.getUserId();
        FoodUser user =getById(userId);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user,userVO);
        if (StringUtils.isNotBlank(user.getAvatar())){
            userVO.setAvatar("https://aidoc.ggchat.com.cn"+ user.getAvatar());
        }
        return userVO;
    }

}
