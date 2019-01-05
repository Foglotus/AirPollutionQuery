package com.foglotus.airpollution.entity;

import java.io.Serializable;

public class City implements Serializable {
    private String cityPinyin;
    private String cityName;

    public City(String pinYin, String name) {
        this.cityPinyin = pinYin;
        this.cityName = name;
    }

    public String getCityPinyin() {
        return cityPinyin;
    }

    public void setCityPinyin(String cityPinyin) {
        this.cityPinyin = cityPinyin;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
