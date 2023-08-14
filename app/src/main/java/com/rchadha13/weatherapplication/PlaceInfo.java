package com.rchadha13.weatherapplication;

public class PlaceInfo {
    private String name;
    private double latitude;
    private double longitude;

    public PlaceInfo(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
