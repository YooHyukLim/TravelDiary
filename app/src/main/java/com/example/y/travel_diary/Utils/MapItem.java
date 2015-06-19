package com.example.y.travel_diary.Utils;

public class MapItem {
    final public static int MAP_VIEW  = -1;
    int mid;
    String name;
    long longitude;
    long latitude;

    public MapItem(int mid, String name, long longitude, long latitude) {
        this.mid = mid;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public int getMid() {
        return mid;
    }

    public String getName() {
        return name;
    }

    public long getLongitude() {
        return longitude;
    }

    public long getLatitude() {
        return latitude;
    }
}
