package com.express.expresssystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ai")
public class AiController {
    // 扣子长期令牌
    private static final String COZE_TOKEN = "pat_3YwEVAL6kM2cQ08Dfoig6Gnjh0W4go9Rn462VEWBH0gR7R3brjs8Bei0LvM7DRkX";
    // 机器人ID
    private static final String COZE_BOT_ID = "7661838434608168960";
    // 扣子标准接口地址
    private static final String CHAT_CREATE_URL = "https://api.coze.cn/v3/chat";
    private static final String CHAT_RETRIEVE_URL = "https://api.coze.cn/v3/chat/retrieve";
    private static final String CHAT_MSG_LIST_URL = "https://api.coze.cn/v3/chat/message/list";

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/chat")
    public Map<String, Object> chat(@RequestBody Map<String, String> param) throws InterruptedException {
        Map<String, Object> res = new HashMap<>();
        String question = param.get("question");
        if (question == null || question.trim().isEmpty()) {
            res.put("code", 400);
            res.put("msg", "提问不能为空");
            return res;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + COZE_TOKEN);
        HttpEntity<?> headerOnly = new HttpEntity<>(headers);

        Map<String, Object> reqBody = new HashMap<>();
        reqBody.put("bot_id", COZE_BOT_ID);
        reqBody.put("user_id", "user001");
        reqBody.put("stream", false);

        List<Map<String, String>> msgArr = new ArrayList<>();
        Map<String, String> userMsg = new HashMap<>();
        userMsg.put("role", "user");
        userMsg.put("content", question);
        msgArr.add(userMsg);
        reqBody.put("additional_messages", msgArr);

        HttpEntity<Map<String, Object>> createReq = new HttpEntity<>(reqBody, headers);
        ResponseEntity<Map> createResp = restTemplate.postForEntity(CHAT_CREATE_URL, createReq, Map.class);
        Map<String, Object> createBody = createResp.getBody();
        Map<String, Object> data = (Map<String, Object>) createBody.get("data");
        String chatId = (String) data.get("id");
        String convId = (String) data.get("conversation_id");

        String answer = "";
        int maxLoop = 8;
        for (int i = 0; i < maxLoop; i++) {
            Thread.sleep(1500);
            String retrieveUrl = CHAT_RETRIEVE_URL + "?chat_id=" + chatId + "&conversation_id=" + convId;
            ResponseEntity<Map> retrieveResp = restTemplate.exchange(retrieveUrl, HttpMethod.GET, headerOnly, Map.class);
            Map<String, Object> retrieveData = (Map<String, Object>) retrieveResp.getBody().get("data");
            String status = (String) retrieveData.get("status");

            if ("completed".equals(status)) {
                String msgUrl = CHAT_MSG_LIST_URL + "?chat_id=" + chatId + "&conversation_id=" + convId;
                ResponseEntity<Map> msgResp = restTemplate.exchange(msgUrl, HttpMethod.GET, headerOnly, Map.class);
                List<Map<String, Object>> msgList = (List<Map<String, Object>>) msgResp.getBody().get("data");
                // 倒序遍历，取最后一条assistant的纯文本回答
                for (int k = msgList.size() - 1; k >= 0; k--) {
                    Map<String, Object> m = msgList.get(k);
                    if ("assistant".equals(m.get("role"))) {
                        // type=answer才是AI整理后的文字
                        if ("answer".equals(m.get("type"))) {
                            answer = (String) m.get("content");
                            break;
                        }
                    }
                }
                break;
            }
        }
        if (answer.isEmpty()) {
            answer = "AI生成超时，请重新提问";
        }

        res.put("code", 200);
        res.put("msg", "success");
        res.put("data", answer);
        return res;
    }


}
