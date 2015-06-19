package com.example.y.travel_diary.Utils;

public class TravelItem {
    int _id;
    String name;
    long sdate;
    long edate;

    public TravelItem(int _id, String name, long sdate, long edate) {
        this._id = _id;
        this.name = name;
        this.sdate = sdate;
        this.edate = edate;
    }

    public int get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public long getSdate() {
        return sdate;
    }

    public long getEdate() {
        return edate;
    }
}
