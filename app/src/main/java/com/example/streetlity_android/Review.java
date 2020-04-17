package com.example.streetlity_android;

public class Review {
    public String username;
    public String reviewBody;
    public float rating;

    public Review(String username, String reviewBody, float rating) {
        this.username = username;
        this.reviewBody = reviewBody;
        this.rating = rating;
    }
}
