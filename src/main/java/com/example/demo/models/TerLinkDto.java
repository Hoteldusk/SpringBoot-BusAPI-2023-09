package com.example.demo.models;

public class TerLinkDto {
    private String tl_Id;
    private String tl_DepTerId;
    private String tl_ArrTerId;


    public TerLinkDto(String tl_Id, String tl_DepTerId, String tl_ArrTerId) {
        this.tl_Id = tl_Id;
        this.tl_DepTerId = tl_DepTerId;
        this.tl_ArrTerId = tl_ArrTerId;
    }

    public TerLinkDto() {
    }

    public String getTl_Id() {
        return tl_Id;
    }

    public void setTl_Id(String tl_Id) {
        this.tl_Id = tl_Id;
    }

    public String getTl_DepTerId() {
        return tl_DepTerId;
    }

    public void setTl_DepTerId(String tl_DepTerId) {
        this.tl_DepTerId = tl_DepTerId;
    }

    public String getTl_ArrTerId() {
        return tl_ArrTerId;
    }

    public void setTl_ArrTerId(String tl_ArrTerId) {
        this.tl_ArrTerId = tl_ArrTerId;
    }

    @Override
    public String toString() {
        return "TerLinkDto{" +
                "tl_Id='" + tl_Id + '\'' +
                ", tl_DepTerId='" + tl_DepTerId + '\'' +
                ", tl_ArrTerId='" + tl_ArrTerId + '\'' +
                '}';
    }
}
