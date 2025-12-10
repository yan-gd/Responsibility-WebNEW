package com.example.springboot.controller;

import com.example.springboot.dto.ChatRequest;
import com.example.springboot.dto.ChatResponse;
import com.example.springboot.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class AIController {

    @Autowired
    private AIService aiService;

    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody ChatRequest request) {
        try {
            String response = aiService.chat(request.getMessage(), request.getHistory());
            return ChatResponse.success(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ChatResponse.error("AI 服务请求失败: " + e.getMessage());
        }
    }
}
