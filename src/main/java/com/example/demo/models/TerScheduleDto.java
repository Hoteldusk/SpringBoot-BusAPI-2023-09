package com.example.demo.models;

public class TerScheduleDto {
    private String tes_TdId;
    private String tes_Schedule;

    public String getTes_TdId() {
        return tes_TdId;
    }

    public void setTes_TdId(String tes_TdId) {
        this.tes_TdId = tes_TdId;
    }

    public String getTes_Schedule() {
        return tes_Schedule;
    }

    public void setTes_Schedule(String tes_Schedule) {
        this.tes_Schedule = tes_Schedule;
    }

    @Override
    public String toString() {
        return "TerScheduleDto{" +
                "tes_TdId='" + tes_TdId + '\'' +
                ", tes_Schedule='" + tes_Schedule + '\'' +
                '}';
    }
}
