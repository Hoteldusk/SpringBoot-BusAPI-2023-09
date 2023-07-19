package com.example.demo.models;

import java.util.Objects;

public class TerDto {
    private String terId;
    private String terName;
    private String terRegion;
    private double terCoorX;
    private double terCoorY;

    public String getTerId() {
        return terId;
    }

    public void setTerId(String terId) {
        this.terId = terId;
    }

    public String getTerName() {
        return terName;
    }

    public void setTerName(String terName) {
        this.terName = terName;
    }

    public String getTerRegion() {
        return terRegion;
    }

    public void setTerRegion(String terRegion) {
        this.terRegion = terRegion;
    }

    public double getTerCoorX() {
        return terCoorX;
    }

    public void setTerCoorX(double terCoorX) {
        this.terCoorX = terCoorX;
    }

    public double getTerCoorY() {
        return terCoorY;
    }

    public void setTerCoorY(double terCoorY) {
        this.terCoorY = terCoorY;
    }

    @Override
    public String toString() {
        return "TerDto{" +
                "terId='" + terId + '\'' +
                ", terName='" + terName + '\'' +
                ", terRegion='" + terRegion + '\'' +
                ", terCoorX='" + terCoorX + '\'' +
                ", terCoorY='" + terCoorY + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TerDto dto = (TerDto) o;
        return Objects.equals(terId, dto.terId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(terId);
    }
}