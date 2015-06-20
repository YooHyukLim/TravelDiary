package com.example.y.travel_diary.Utils;

public class MainItem {
    final public static int MAP = 0;
    final public static int PLAN = 1;
    final public static int BUCKET = 2;

    int type;
    String text1;
    String text2;

    public MainItem(int type) {
        this.type = type;
    }

    public MainItem(int type, String text) {
        this.type = type;
        this.text1 = text;
    }

    public MainItem(int type, String text1, String text2) {
        this.type = type;
        this.text1 = text1;
        this.text2 = text2;
    }

    public int getType() {
        return type;
    }

    public String getText1() {
        return text1;
    }

    public String getText2() {
        return text2;
    }
}