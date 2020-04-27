package com.example.streetlity_android;

import android.app.Application;

public class MyApplication extends Application { //35.240.232.218 auth server

    private String token = "";

    private int userType;

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
}
