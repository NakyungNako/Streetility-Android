package com.example.streetlity_android.User.Maintainer;

public class HistoryObject {
    private int id;
    private String name;
    private String date;
    private String address;

    public HistoryObject(int id, String name, String date, String address) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.address = address;
    }

    public int getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getAddress() {
        return address;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
