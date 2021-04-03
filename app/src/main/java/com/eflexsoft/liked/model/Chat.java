package com.eflexsoft.liked.model;

public class Chat {

    private String firstId;
    private String secondId;
    private String message;
    private String date;
    private boolean firstIdSeen;
    private boolean secondIdSeen;
    private String imageUrl;
    private String videoUrl;
    private long chatId;

    public Chat() {
    }

    public Chat(String firstId, String secondId, String message, String date, boolean firstIdSeen, boolean secondIdSeen, String imageUrl, String videoUrl, long chatId) {
        this.firstId = firstId;
        this.secondId = secondId;
        this.message = message;
        this.date = date;
        this.firstIdSeen = firstIdSeen;
        this.secondIdSeen = secondIdSeen;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
        this.chatId = chatId;
    }

    public String getFirstId() {
        return firstId;
    }

    public void setFirstId(String firstId) {
        this.firstId = firstId;
    }

    public String getSecondId() {
        return secondId;
    }

    public void setSecondId(String secondId) {
        this.secondId = secondId;
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

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isFirstIdSeen() {
        return firstIdSeen;
    }

    public void setFirstIdSeen(boolean firstIdSeen) {
        this.firstIdSeen = firstIdSeen;
    }

    public boolean isSecondIdSeen() {
        return secondIdSeen;
    }

    public void setSecondIdSeen(boolean secondIdSeen) {
        this.secondIdSeen = secondIdSeen;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }
}
