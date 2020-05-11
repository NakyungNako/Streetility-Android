package com.example.streetlity_android.User.Maintainer;

public class EmergencyOrderObject {
    private int id;
    private String name;
    private String reason;
    private String phone;
    private String note;

    public EmergencyOrderObject(int id, String name, String reason, String phone, String note) {
        this.id = id;
        this.name = name;
        this.reason = reason;
        this.phone = phone;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getReason() {
        return reason;
    }

    public String getPhone() {
        return phone;
    }

    public String getNote() {
        return note;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setReason(String address) {
        this.reason = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setNote(String note) {
        this.note = note;
    }

}
