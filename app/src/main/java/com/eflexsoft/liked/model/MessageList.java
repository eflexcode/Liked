package com.eflexsoft.liked.model;

public class MessageList {

    private String id;
    private String lastMessage;
    private String chatId;

    public MessageList() {
    }

    public MessageList(String id, String lastMessage,String chatId) {
        this.id = id;
        this.lastMessage = lastMessage;
        this.chatId = chatId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }
}
