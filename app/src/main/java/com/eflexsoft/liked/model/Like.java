package com.eflexsoft.liked.model;

public class Like {

    private String messageId;
    private String userId;

    public Like() {
    }

    public Like(String messageId, String userId) {
        this.messageId = messageId;
        this.userId = userId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
