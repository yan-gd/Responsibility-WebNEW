package com.example.springboot.service;

import com.example.springboot.dto.ChatRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class AIService {

    @Value("${dashscope.api.key:}")
    private String apiKey;

    private static final String BASE_URL = "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions";
    private static final String MODEL = "qwen-vl-plus";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public AIService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public String chat(String message, List<ChatRequest.ChatMessage> history) throws Exception {
        // 构建请求体
        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("model", MODEL);

        ArrayNode messages = objectMapper.createArrayNode();

        // 添加系统提示
        ObjectNode systemMessage = objectMapper.createObjectNode();
        systemMessage.put("role", "system");
        systemMessage.put("content", "你是城市配送货运驾驶员适岗状态监管平台的智能助手。你可以帮助用户了解平台功能、查询驾驶员信息、解答关于货运监控和安全管理的问题。请用专业、友好的语气回答问题。");
        messages.add(systemMessage);

        // 添加历史消息
        if (history != null) {
            for (ChatRequest.ChatMessage msg : history) {
                ObjectNode historyMsg = objectMapper.createObjectNode();
                historyMsg.put("role", msg.getRole());
                historyMsg.put("content", msg.getContent());
                messages.add(historyMsg);
            }
        }

        // 添加当前用户消息
        ObjectNode userMessage = objectMapper.createObjectNode();
        userMessage.put("role", "user");
        userMessage.put("content", message);
        messages.add(userMessage);

        requestBody.set("messages", messages);
        requestBody.put("temperature", 0.7);
        requestBody.put("max_tokens", 2000);

        String jsonBody = objectMapper.writeValueAsString(requestBody);

        // 构建 HTTP 请求
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        // 发送请求
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("API 请求失败，状态码: " + response.statusCode() + ", 响应: " + response.body());
        }

        // 解析响应
        JsonNode responseJson = objectMapper.readTree(response.body());
        JsonNode choices = responseJson.get("choices");
        if (choices != null && choices.isArray() && choices.size() > 0) {
            JsonNode firstChoice = choices.get(0);
            JsonNode messageNode = firstChoice.get("message");
            if (messageNode != null) {
                JsonNode contentNode = messageNode.get("content");
                if (contentNode != null) {
                    return contentNode.asText();
                }
            }
        }

        throw new RuntimeException("无法解析 AI 响应");
    }
}
