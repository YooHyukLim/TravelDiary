package com.example.y.travel_diary.Utils;

public class BucketItem {
    int bid;
    String name;
    boolean done;

    public BucketItem(int bid, String name, boolean done) {
        this.bid = bid;
        this.name = name;
        this.done = done;
    }

    public void setDone(boolean done) { this.done = done; }

    public int getBid() {
        return bid;
    }

    public String getName() {
        return name;
    }

    public boolean isDone() {
        return done;
    }
}