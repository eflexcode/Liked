package com.eflexsoft.liked.model;

public class User {

    private String id;
    private String name;
    private double longitude;
    private double latitude;
    private String gender;
    private String age;
    private String about;
    private String email;
    private String profilePictureUrl;
    private String phoneNumber;
    private String isOnline;

    private String displayImage1;
    private String displayImage2;
    private String displayImage3;

    private long timeStamp;

    // for liking
    private boolean isLiked;

    public User() {
    }


    public User(String id, String name, double longitude, double latitude, String gender, String age, String about, String email, String profilePictureUrl, String phoneNumber, String isOnline, String displayImage1, String displayImage2, String displayImage3, long timeStamp, boolean isLiked) {
        this.id = id;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.gender = gender;
        this.age = age;
        this.about = about;
        this.email = email;
        this.profilePictureUrl = profilePictureUrl;
        this.phoneNumber = phoneNumber;
        this.isOnline = isOnline;
        this.displayImage1 = displayImage1;
        this.displayImage2 = displayImage2;
        this.displayImage3 = displayImage3;
        this.timeStamp = timeStamp;
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

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
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

    public String getDisplayImage1() {
        return displayImage1;
    }

    public void setDisplayImage1(String displayImage1) {
        this.displayImage1 = displayImage1;
    }

    public String getDisplayImage2() {
        return displayImage2;
    }

    public void setDisplayImage2(String displayImage2) {
        this.displayImage2 = displayImage2;
    }

    public String getDisplayImage3() {
        return displayImage3;
    }

    public void setDisplayImage3(String displayImage3) {
        this.displayImage3 = displayImage3;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
