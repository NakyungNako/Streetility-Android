package com.example.streetlity_android.User.Maintainer;

public class OrderObject {
    private int id;
    private String name;
    private String address;
    private String phone;
    private String note;
    private String time;

    public OrderObject(int id, String name, String address, String phone, String note, String time) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.note = note;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getNote() {
        return note;
    }

    public String getTime() {
        return time;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
