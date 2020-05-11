package com.example.streetlity_android;

import android.app.Application;



//401 refresh

public class MyApplication extends Application { //35.240.232.218 auth server

    private String token = "";

    private String refreshToken = "";

    private int userType;

    private String username="";

    private int userId;

    public int getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    private String serviceURL = "http://35.240.207.83/";

    private String authURL = "http://35.240.232.218/";

    public void setToken(String token) {
        this.token = token;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getServiceURL() {
        return serviceURL;
    }

    public String getAuthURL() {
        return authURL;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
