package com.campus.controller;

import com.campus.common.Result;
import com.campus.dto.ChatRequest;
import com.campus.service.AiService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    @Autowired
    private AiService aiService;

    @PostMapping("/chat")
    public Result<?> chat(@RequestBody ChatRequest request, HttpServletRequest req) {
        Long userId = (Long) req.getAttribute("userId");
        String reply = aiService.chat(userId, request);
        return Result.ok(reply);
    }

    @GetMapping("/history")
    public Result<?> history(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.ok(aiService.getHistory(userId));
    }
}
