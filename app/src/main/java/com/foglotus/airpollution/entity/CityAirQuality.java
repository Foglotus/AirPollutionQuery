package com.foglotus.airpollution.entity;

public class CityAirQuality {
    private String aqi;//指数
    private String area;//地区
    private String primaryPollution;//主要污染物
    private String positionName;//地点名称
    private String quality;//质量
    private String stationCode;//位置代码
    private String timePoint;//时间点

    public CityAirQuality(int aqi, String area, String primaryPollution,String positionName, String quality, String stationCode, String timePoint) {
        this.area = area;
        this.quality = quality;
        this.stationCode = stationCode;
        this.timePoint = timePoint;
        this.positionName = positionName;
        setPrimaryPollution(primaryPollution);
        setTimePoint(timePoint);
        setAqi(aqi);
    }

    public String getAqi() {
        return aqi;
    }

    public void setAqi(int aqi) {
        this.aqi = String.valueOf(aqi);
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPrimaryPollution() {
        return primaryPollution;
    }

    public void setPrimaryPollution(String primaryPollution) {
        if(primaryPollution.equals("null") || primaryPollution.equals(""))
            primaryPollution = "无";

        primaryPollution = primaryPollution.replace(",","\n");
        this.primaryPollution = primaryPollution;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public String getTimePoint() {
        return timePoint;
    }

    public void setTimePoint(String timePoint) {
        this.timePoint = timePoint.replace("T"," ").replace("Z","").substring(0,16);
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String postionName) {
        this.positionName = postionName;
    }

    @Override
    public String toString() {
        return "CityQuality{" +
                "aqi='" + aqi + '\'' +
                ", area='" + area + '\'' +
                ", primaryPollutant='" + primaryPollution + '\'' +
                ", positionName='" + positionName + '\'' +
                ", quality='" + quality + '\'' +
                ", stationCode='" + stationCode + '\'' +
                ", timePoint='" + timePoint + '\'' +
                '}';
    }
}
