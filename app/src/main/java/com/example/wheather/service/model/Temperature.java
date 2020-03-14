package com.example.wheather.service.model;

public class Temperature {
    private double temp;
    private double minTemp;
    private double maxTemp;

    public Temperature(double temp, double minTemp, double maxTemp) {
        this.temp = temp;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
    }

    public double getTemp() {
        return temp;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public double getMaxTemp() {
        return maxTemp;
    }
}

