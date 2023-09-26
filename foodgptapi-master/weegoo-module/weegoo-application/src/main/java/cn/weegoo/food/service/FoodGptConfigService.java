/**
 * Copyright © 2021-2025 <a href="http://www.weegoo.cn/">weegoo</a> All rights reserved.
 */
package cn.weegoo.food.service;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.weegoo.execption.WGException;
import cn.weegoo.utils.R;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.plexpt.chatgpt.util.Proxys;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.weegoo.food.domain.FoodGptConfig;
import cn.weegoo.food.mapper.FoodGptConfigMapper;

import java.net.Proxy;

/**
 * gpt账号配置Service
 * @author xx
 * @version 2023-05-29
 */
@Service
@Transactional
public class FoodGptConfigService extends ServiceImpl<FoodGptConfigMapper, FoodGptConfig> {
    public String getBalance(String apiKey){
//        Proxy proxy = Proxys.http("127.0.0.1", 7890);

        String  subscription_url = "https://api.openai.com/v1/dashboard/billing/subscription";
        HttpResponse resultJson2 = HttpRequest.get(subscription_url)
                .bearerAuth(apiKey)
//                .setProxy(proxy)
                .execute();
        if (StringUtils.isNotBlank(JSONObject.parseObject( resultJson2.body()).getString("error"))){
            FoodGptConfig foodGptConfig = getOne(new LambdaQueryWrapper<FoodGptConfig>().eq(FoodGptConfig::getApiKey, apiKey));
            foodGptConfig.setIsAvailable("0");
            saveOrUpdate(foodGptConfig);
            throw new WGException("该账号已不能使用");
        }
        String price = JSONObject.parseObject( resultJson2.body()).getString("soft_limit_usd");
        return price;
    }


}
