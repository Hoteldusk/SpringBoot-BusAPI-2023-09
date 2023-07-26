package com.example.demo.models;

import java.util.ArrayList;
import java.util.List;

public class TerScheduleVO {
    private String td_ID;
    private String td_TlID;
    private String td_Interval;
    private String td_WasteTime;
    private int td_Fare;
    private List<TerScheduleDto> td_schedule;

    public String getTd_ID() {
        return td_ID;
    }

    public void setTd_ID(String td_ID) {
        this.td_ID = td_ID;
    }

    public String getTd_TlID() {
        return td_TlID;
    }

    public void setTd_TlID(String td_TlID) {
        this.td_TlID = td_TlID;
    }

    public String getTd_Interval() {
        return td_Interval;
    }

    public void setTd_Interval(String td_Interval) {
        this.td_Interval = td_Interval;
    }

    public String getTd_WasteTime() {
        return td_WasteTime;
    }

    public void setTd_WasteTime(String td_WasteTime) {
        this.td_WasteTime = td_WasteTime;
    }

    public int getTd_Fare() {
        return td_Fare;
    }

    public void setTd_Fare(int td_Fare) {
        this.td_Fare = td_Fare;
    }

    public List<TerScheduleDto> getTd_schedule() {
        return td_schedule;
    }

    public void setTd_schedule(List<TerScheduleDto> td_schedule) {
        this.td_schedule = td_schedule;
    }

    @Override
    public String toString() {
        return "TerScheduleVO{" +
                "td_ID='" + td_ID + '\'' +
                ", td_TlID='" + td_TlID + '\'' +
                ", td_Interval='" + td_Interval + '\'' +
                ", td_WasteTime='" + td_WasteTime + '\'' +
                ", td_Fare=" + td_Fare +
                ", td_schedule=" + td_schedule +
                '}';
    }
}
