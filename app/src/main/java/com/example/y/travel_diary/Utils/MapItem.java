package com.example.y.travel_diary.Utils;

public class MapItem {
    final public static int MAP_VIEW  = -1;
    int mid;
    String name;
    String address;
    double longitude;
    double latitude;

    public MapItem(int mid, String name, String address, double longitude, double latitude) {
        this.mid = mid;
        this.name = name;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public int getMid() {
        return mid;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
