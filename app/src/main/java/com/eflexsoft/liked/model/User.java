package com.eflexsoft.liked.model;

import java.util.HashMap;

public class User {

    private String id;
    private String name;
    private String address;
    private String gender;
    private String age;
    private String about;
    private String email;
    private String profilePictureUrl;
    private String phoneNumber;
    private String isOnline;

    // for liking
    private boolean isLiked;

    public User() {
    }

    public User(String id, String name, String address, String gender, String age, String about, String email, String profilePictureUrl, String phoneNumber,String isOnline,boolean isLiked) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.gender = gender;
        this.age = age;
        this.about = about;
        this.email = email;
        this.profilePictureUrl = profilePictureUrl;
        this.phoneNumber = phoneNumber;
        this.isOnline = isOnline;
        this.isLiked = isLiked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

}
