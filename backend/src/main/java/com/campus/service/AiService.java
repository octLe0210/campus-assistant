package com.campus.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.dto.ChatRequest;
import com.campus.entity.ChatMessage;
import com.campus.mapper.ChatMessageMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class AiService {

    @Value("${deepseek.api-key}")
    private String apiKey;

    @Value("${deepseek.api-url}")
    private String apiUrl;

    @Value("${deepseek.model}")
    private String model;

    @Autowired
    private ChatMessageMapper chatMessageMapper;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String SYSTEM_PROMPT = "你是一个智能校园助手，专门为大学生提供帮助。你可以回答关于课程、学习、校园生活等方面的问题。请用友好、专业的语气回答，回答要简洁有帮助。如果遇到你不知道的问题，请诚实告知。";

    public String chat(Long userId, ChatRequest request) {
        // 保存用户消息
        ChatMessage userMsg = new ChatMessage();
        userMsg.setUserId(userId);
        userMsg.setRole("user");
        userMsg.setContent(request.getMessage());
        chatMessageMapper.insert(userMsg);

        // 调用DeepSeek API
        String reply = callDeepSeek(request);

        // 保存AI回复
        ChatMessage aiMsg = new ChatMessage();
        aiMsg.setUserId(userId);
        aiMsg.setRole("assistant");
        aiMsg.setContent(reply);
        chatMessageMapper.insert(aiMsg);

        return reply;
    }

    private String callDeepSeek(ChatRequest request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            List<Map<String, String>> messages = new ArrayList<>();
            Map<String, String> systemMsg = new HashMap<>();
            systemMsg.put("role", "system");
            systemMsg.put("content", SYSTEM_PROMPT);
            messages.add(systemMsg);

            if (request.getHistory() != null) {
                for (ChatRequest.Message msg : request.getHistory()) {
                    Map<String, String> m = new HashMap<>();
                    m.put("role", msg.getRole());
                    m.put("content", msg.getContent());
                    messages.add(m);
                }
            }

            Map<String, String> userMsg = new HashMap<>();
            userMsg.put("role", "user");
            userMsg.put("content", request.getMessage());
            messages.add(userMsg);

            Map<String, Object> body = new HashMap<>();
            body.put("model", model);
            body.put("messages", messages);
            body.put("temperature", 0.7);
            body.put("max_tokens", 2000);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, entity, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            return root.path("choices").get(0).path("message").path("content").asText();
        } catch (Exception e) {
            return "抱歉，AI服务暂时不可用，请稍后再试。错误信息：" + e.getMessage();
        }
    }

    public String generateStudyPlan(Long userId, String goal) {
        String prompt = "请根据以下学习目标，生成一份详细的学习计划。学习目标：" + goal +
                "。请按照JSON格式输出，包含以下字段：title（计划标题）、content（计划内容，使用Markdown格式，包含每日学习安排、重点内容、建议时间分配等）。";

        ChatRequest request = new ChatRequest();
        request.setMessage(prompt);
        return callDeepSeek(request);
    }

    public List<ChatMessage> getHistory(Long userId) {
        LambdaQueryWrapper<ChatMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChatMessage::getUserId, userId)
               .orderByAsc(ChatMessage::getCreatedAt);
        return chatMessageMapper.selectList(wrapper);
    }
}
