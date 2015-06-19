package com.example.y.travel_diary.Utils;

public class PlanItem {
    int pid;
    String name;
    String content;
    long sdate;
    long edate;
    boolean isalarmed;

    public PlanItem(int pid, String name, String content, long sdate, long edate, boolean isalarmed) {
        this.pid = pid;
        this.name = name;
        this.content = content;
        this.sdate = sdate;
        this.edate = edate;
        this.isalarmed = isalarmed;
    }

    public int getpid() {
        return pid;
    }

    public String getName() {
        return name;
    }

    public String getContent(){
        return content;
    }

    public long getSdate() {
        return sdate;
    }

    public long getEdate() {
        return edate;
    }

    public boolean getAlarm() {
        return isalarmed;
    }

    public void setAlarm(boolean isalarmed) { this.isalarmed = isalarmed; }
}
