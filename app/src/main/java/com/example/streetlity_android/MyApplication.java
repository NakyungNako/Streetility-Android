package com.example.streetlity_android;

import android.app.Application;

//eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1OTQ0NDUzMzIsImlkIjoibXJYWVpaemFidiJ9.0Hd4SpIELulSuTxGAeuCPl_A33X-KoPUpRmgK4dTphk

//401 refresh

//1:common
//7:maintenance

public class MyApplication extends Application { //35.240.232.218 auth server

    private String token = "";

    private String refreshToken = "";

    private String deviceToken = "";

    private int userType;

    private String username="";

    private String userId;

    public String getUserId() {
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

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
