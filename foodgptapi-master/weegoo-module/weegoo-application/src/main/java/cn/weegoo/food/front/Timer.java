package cn.weegoo.food.front;

import cn.weegoo.food.domain.FoodConfig;
import cn.weegoo.food.domain.FoodUser;
import cn.weegoo.food.service.FoodConfigService;
import cn.weegoo.food.service.FoodGptConfigService;
import cn.weegoo.food.service.FoodUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Lazy(false)
public class Timer {
    @Autowired
    private FoodConfigService foodConfigService;
    @Autowired
    private FoodUserService foodUserService;

    /**
     * 每天凌晨执行一次，重置用户次数
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void refreshTimes() {
        FoodConfig foodConfig = foodConfigService.getOne(new QueryWrapper<FoodConfig>().eq("config_key","freeTimes"));
        if (foodConfig == null) {
            return;
        }
        Integer times = Integer.valueOf(foodConfig.getConfigValue());

        List<FoodUser> foodUsers = foodUserService.list();
        for (FoodUser foodUser : foodUsers) {
            if(foodUser.getTimes() != null && foodUser.getTimes() > times){
                continue;
            }

            foodUser.setTimes(times);
            foodUserService.updateById(foodUser);
        }
    }
}
