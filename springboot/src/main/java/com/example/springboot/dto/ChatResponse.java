package com.example.springboot.dto;

public class ChatResponse {
    private String content;
    private boolean success;
    private String error;

    public ChatResponse() {}

    public ChatResponse(String content) {
        this.content = content;
        this.success = true;
    }

    public static ChatResponse success(String content) {
        ChatResponse response = new ChatResponse();
        response.setContent(content);
        response.setSuccess(true);
        return response;
    }

    public static ChatResponse error(String error) {
        ChatResponse response = new ChatResponse();
        response.setError(error);
        response.setSuccess(false);
        return response;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
