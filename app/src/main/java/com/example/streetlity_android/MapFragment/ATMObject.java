package com.example.streetlity_android.MapFragment;

public class ATMObject {
    int id;
    String type;
    float lat;
    float lon;
    String note;

    public ATMObject(int id, String type, float lat, float lon, String note) {
        this.id = id;
        this.type = type;
        this.lat = lat;
        this.lon = lon;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
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

    public void setType(String type) {
        this.type = type;
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
}
