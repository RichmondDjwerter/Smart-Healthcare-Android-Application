package com.example.modernizedshapp.doctor.Diagnosis.db;

public class ChatMessage {
    private int id;
    private int chatId;
    private String message;
    private int isUserMessage;

    public ChatMessage(int id, int chatId, String message, boolean isUserMessage) {
        this.id = id;
        this.chatId = chatId;
        this.message = message;
        this.isUserMessage = isUserMessage ? 1 : 0;
    }

    public boolean getIsUserMessage() {
        return isUserMessage == 1;
    }

    public void setIsUserMessage(boolean isUserMessage) {
        this.isUserMessage = isUserMessage ? 1 : 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
