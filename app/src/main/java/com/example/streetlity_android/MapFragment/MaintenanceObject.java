package com.example.streetlity_android.MapFragment;

public class MaintenanceObject {

    int id;
    String name;
    float lat;
    float lon;
    String note;
    String address;

    public MaintenanceObject(int id, String name, float lat, float lon, String note, String address) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.note = note;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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

    public String getAddress() {
        return address;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setAddress(String address) {
        this.address = address;
    }
}
