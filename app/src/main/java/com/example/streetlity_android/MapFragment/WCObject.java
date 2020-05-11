package com.example.streetlity_android.MapFragment;

public class WCObject {

    int id;
    float lat;
    float lon;
    String note;

    public WCObject(int id, float lat, float lon, String note) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.note = note;
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
}
