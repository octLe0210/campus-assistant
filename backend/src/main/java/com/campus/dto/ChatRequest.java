package com.campus.dto;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class ChatRequest {
    private String message;
    private List<Message> history = new ArrayList<>();
    private Long planId;

    @Data
    public static class Message {
        private String role;
        private String content;
    }
}
