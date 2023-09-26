package cn.weegoo.food.front;

import cn.weegoo.food.front.dto.CreateMessageDTO;
import cn.weegoo.food.service.SseEmitterUTF8Service;
import cn.weegoo.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @author damin
 */
@Api(tags = "gpt接口")
@RestController
@RequestMapping("/api/chatGPT")
public class ApiSseEmitterController {
    @Autowired
    private SseEmitterUTF8Service sseEmitterUTF8Service;

    /**
     * @return
     */
    @ApiOperation("创建对话，返回对话id")
    @PostMapping("createMessage")
    public R<String> createMessage(@RequestBody CreateMessageDTO messageDTO){

        return R.data(sseEmitterUTF8Service.createMessage(messageDTO));
    }

    /**
     *
     * @return
     */
    @ApiOperation("生成gpt,第一次对话时content可为空")
    @GetMapping("sse")
    public SseEmitter sse(String messageId, String content, String userId){

        return sseEmitterUTF8Service.SseEmitter(messageId, content, userId);
    }
}
