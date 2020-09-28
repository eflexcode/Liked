package com.eflexsoft.liked.model;

public class Chat {

    private String senderId;
    private String receiverId;
    private String message;
    private String date;
    private boolean isSeen;
    private String imageUrl;

    public Chat() {
    }

    public Chat(String senderId, String receiverId, String message, String date, String imageUrl, boolean isSeen) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.date = date;
        this.isSeen = isSeen;
        this.imageUrl = imageUrl;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public boolean getIsSeen() {
        return isSeen;
    }

    public void setIsSeen(boolean isSeen) {
        this.isSeen = isSeen;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
