package com.example.streetlity_android.MainFragment;

import java.io.Serializable;

public class MapObject implements Serializable {

    int id;
    String name;
    float rating;
    String address;
    float lat;
    float lon;
    String note;
    float distance;
    int type; //1=fuel; 2=wc; 3=mainte; 4=atm
    int bankId;

    public MapObject(int id, String name, float rating, String address, float lat, float lon, String note, int type) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.address = address;
        this.lat = lat;
        this.lon = lon;
        this.note = note;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public float getLat() {
        return lat;
    }

    public float getLon() {
        return lon;
    }

    public String getNote() {
        return note;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getDistance() {
        return distance;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public float getRating() {
        return rating;
    }

    public String getAddress() {
        return address;
    }

    public int getType() {
        return type;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }

    public int getBankId() {
        return bankId;
    }
}
