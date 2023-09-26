package cn.weegoo.food.service;


import cn.hutool.core.util.StrUtil;
import cn.weegoo.execption.WGException;
import cn.weegoo.food.domain.*;
import cn.weegoo.food.front.dto.CreateMessageDTO;
import cn.weegoo.food.front.dto.FormDTO;
import cn.weegoo.food.service.dto.FoodFormDTO;
import cn.weegoo.food.service.dto.FoodMessageDTO;
import cn.weegoo.food.service.dto.FoodMessageDetailDTO;
import cn.weegoo.utils.WGTokenUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.plexpt.chatgpt.ChatGPTStream;
import com.plexpt.chatgpt.entity.chat.ChatCompletion;
import com.plexpt.chatgpt.entity.chat.Message;

import com.plexpt.chatgpt.util.Proxys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author damin
 */
@Service
@Transactional
public class SseEmitterUTF8Service extends SseEmitter {
    @Autowired
    private FoodGptConfigService foodGptConfigService;
    @Autowired
    private FoodBrandService FoodBrandService;
    @Autowired
    private FoodBuildTypeService foodBuildTypeService;
    @Autowired
    private FoodConfigService foodConfigService;
    @Autowired
    private FoodMessageService foodMessageService;
    @Autowired
    private FoodMessageDetailService foodMessageDetailService;
    @Autowired
    private FoodUserService foodUserService;


    public SseEmitterUTF8Service(Long timeout) {
        super(timeout);
    }

    public SseEmitterUTF8Service() {

    }

    @Override
    protected void extendResponse(ServerHttpResponse outputMessage) {
        super.extendResponse(outputMessage);

        HttpHeaders headers = outputMessage.getHeaders();
        headers.setContentType(new MediaType(MediaType.TEXT_EVENT_STREAM, StandardCharsets.UTF_8));
    }

    public String createMessage(CreateMessageDTO messageDTO) {
        FoodBuildType foodBuildType = foodBuildTypeService.getById(messageDTO.getFoodBuildId());
        if(foodBuildType == null){
            throw new WGException("内容类型不存在");
        }

        //校验用户次数
        FoodUser user = foodUserService.getById(WGTokenUtil.getUserId());
        if(user.getTimes() == null || user.getTimes() <= 0){
            throw new WGException("次数不足");
        }
        user.setTimes(user.getTimes() - 1);
        foodUserService.saveOrUpdate(user);

        FoodMessage message = new FoodMessage();
        message.setUserId(WGTokenUtil.getUserId());
        message.setFoodBuildId(foodBuildType.getId());
        foodMessageService.saveOrUpdate(message);

        //创建具体对话
        //首先创建内置prompt
        FoodMessageDetail preMessage = new FoodMessageDetail();
        preMessage.setType("0");
        preMessage.setMessageId(message.getId());
        preMessage.setContent(foodBuildType.getPreposition());
        foodMessageDetailService.saveOrUpdate(preMessage);
        //延迟100毫秒
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //创建系统回复
        FoodMessageDetail systemMessage = new FoodMessageDetail();
        systemMessage.setType("1");
        systemMessage.setMessageId(message.getId());
        systemMessage.setContent("我明白了");
        foodMessageDetailService.saveOrUpdate(systemMessage);
        //延迟100毫秒
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //创建用户输入
        StringBuffer content = new StringBuffer();
        for(FormDTO formDTO : messageDTO.getFormDTOList()){
            content.append(formDTO.getName() + ":" + formDTO.getValue() + "。");
        }
        content.append(foodBuildType.getKeywords());

        FoodMessageDetail userMessage = new FoodMessageDetail();
        userMessage.setType("2");
        userMessage.setMessageId(message.getId());
        userMessage.setContent(content.toString());
        foodMessageDetailService.saveOrUpdate(userMessage);

        return message.getId();
    }

    @CrossOrigin
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public SseEmitter SseEmitter(String messageId, String content, String userId) {
        String key =getRandomKey();
        //模版配置
        FoodConfig foodConfig =foodConfigService.getOne(new QueryWrapper<FoodConfig>().eq("config_key","foodModel"));
        //国内需要代理 国外不需要
//        Proxy proxy = Proxys.http("127.0.0.1", 7890);
        ChatGPTStream chatGPTStream = ChatGPTStream.builder()
                .timeout(600)
                .apiKey(key)
//                .proxy(proxy)
                .apiHost("https://api.openai.com/")
                .build()
                .init();
        SseEmitterUTF8Service sseEmitter = new SseEmitterUTF8Service(-1L);
        GPTEventSourceListener listener = new GPTEventSourceListener(sseEmitter);

        //创建用户新一次输入
        if(StrUtil.isNotBlank(content)){
            FoodMessageDetail foodMessageDetail = new FoodMessageDetail();
            foodMessageDetail.setMessageId(messageId);
            foodMessageDetail.setType("2");
            foodMessageDetail.setContent(content);
            foodMessageDetailService.saveOrUpdate(foodMessageDetail);
        }

        //获取历史对话
        List<Message> messageList = new ArrayList<>();
        FoodMessageDTO messageDTO = foodMessageService.findById(messageId);
        FoodMessage foodMessage = new FoodMessage();
        foodMessage.setId(messageId);
        foodMessage.setUserId(userId);
        foodMessageService.saveOrUpdate(foodMessage);

        for(FoodMessageDetailDTO detailDTO : messageDTO.getFoodMessageDetailDTOList()){
            Message message = new Message();
            message.setContent(detailDTO.getContent());
            if("0".equals(detailDTO.getType())){
                message.setRole(Message.Role.ASSISTANT.getValue());
            }else if("1".equals(detailDTO.getType())){
                message.setRole(Message.Role.SYSTEM.getValue());
            }else if("2".equals(detailDTO.getType())){
                message.setRole(Message.Role.USER.getValue());
            }

            messageList.add(message);
        }

        ChatCompletion chatCompletion = ChatCompletion.builder()
                        .model(foodConfig.getConfigValue()).messages(messageList).build();

        listener.setOnComplate(msg -> {
            //回答完成，保留对话信息
            System.out.println("获取输出内容"+msg);
            FoodMessageDetail messageDetail = new FoodMessageDetail();
            messageDetail.setMessageId(messageId);
            messageDetail.setType("1");
            messageDetail.setContent(msg);
            try {
                foodMessageDetailService.saveOrUpdate(messageDetail);
            }catch (Exception e){
                e.printStackTrace();
            }

            //回答完成，可以做一些事情
            FoodGptConfig foodGptConfig = foodGptConfigService.getOne(new LambdaQueryWrapper<FoodGptConfig>().eq(FoodGptConfig::getApiKey, key));
            foodGptConfig.setFrequency(foodGptConfig.getFrequency() + 1);
            foodGptConfigService.saveOrUpdate(foodGptConfig);
            complete();
        });

        listener.setOnError(e ->{
            if(e.contains("invalid_api_key")){
                onErr(key);
            }
        });

        listener.setOnErrorComplate(msg ->{
            System.out.println("中断输出" + msg);

            //检测是否输入过相同数据
            FoodMessageDetail temp = foodMessageDetailService.getOne(new LambdaQueryWrapper<FoodMessageDetail>().eq(FoodMessageDetail::getContent, msg).eq(FoodMessageDetail::getMessageId, messageId));
            if(temp != null){
                return;
            }

            FoodMessageDetail messageDetail = new FoodMessageDetail();
            messageDetail.setMessageId(messageId);
            messageDetail.setType("1");
            messageDetail.setContent(msg);
            try {
                foodMessageDetailService.saveOrUpdate(messageDetail);
            }catch (Exception e){
                e.printStackTrace();
            }
        });

        chatGPTStream.streamChatCompletion(chatCompletion, listener);
        return sseEmitter;
    }

    public void onErr(String key){
        FoodGptConfig foodGptConfig = foodGptConfigService.getOne(new LambdaQueryWrapper<FoodGptConfig>().eq(FoodGptConfig::getApiKey, key));
        foodGptConfig.setIsAvailable("0");
        foodGptConfigService.saveOrUpdate(foodGptConfig);
        throw new WGException("该key无法使用");
    }

    public String getRandomKey(){
        List<FoodGptConfig> list = foodGptConfigService.list(new LambdaQueryWrapper<FoodGptConfig>().eq(FoodGptConfig::getIsAvailable,1));
        if (list.size()<1){
            throw new WGException("没有可用的Key,请联系管理员！") ;
        }
        List<String> stringList = new ArrayList();
        for (FoodGptConfig foodConfig : list) {
            stringList.add(foodConfig.getApiKey());
        }
        Random random = new Random();
        return stringList.get(random.nextInt(stringList.size()));
    }

}
