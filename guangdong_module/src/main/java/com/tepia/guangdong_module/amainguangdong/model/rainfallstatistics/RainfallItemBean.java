package com.tepia.guangdong_module.amainguangdong.model.rainfallstatistics;

/**
 * Created by Android Studio
 *
 * @author : Arthur
 * Date :    2019/6/17
 * Time :    20:44
 * Describe :
 */
public class RainfallItemBean {
    private String drp;
    private String address;
    private String level;
    private String reservoir;
    private String forecast;

    public String getDrp() {
        return drp;
    }

    public void setDrp(String drp) {
        this.drp = drp;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getReservoir() {
        return reservoir;
    }

    public void setReservoir(String reservoir) {
        this.reservoir = reservoir;
    }

    public String getForecast() {
        return forecast;
    }

    public void setForecast(String forecast) {
        this.forecast = forecast;
    }
}
