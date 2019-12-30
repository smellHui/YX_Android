package com.tepia.guangdong_module.amainguangdong.model.typhoonroute;

/**
 * Created by Android Studio
 *
 * @author : Arthur
 * Date :    2019/5/31
 * Time :    16:09
 * Describe :
 */
public class TyphoonRouteItemBean {

    /**
     *     tfId: 201902
     *     trong: 热带风暴
     *     lng: 154.90
     *     lat: 5.00
     *     forecast:
     *     pressure: 995
     *     peed: 20
     *     movespeed: 17
     *     radius10:
     *     radius12:
     *     radius7: 300|200|300|200
     *     power: 8
     *     time: 2019-02-20 05:00:00.0
     *
     */

    private String tfId;
    private String strong;
    private String lng;
    private String lat;
    private String forecast;
    private String pressure;
    private String speed;
    private String movespeed;
    private String radius10;
    private String radius12;
    private String radius7;
    private String power;
    private String time;

    public String getTfId() {
        return tfId;
    }

    public void setTfId(String tfId) {
        this.tfId = tfId;
    }

    public String getStrong() {
        return strong;
    }

    public void setStrong(String strong) {
        this.strong = strong;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getForecast() {
        return forecast;
    }

    public void setForecast(String forecast) {
        this.forecast = forecast;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getMovespeed() {
        return movespeed;
    }

    public void setMovespeed(String movespeed) {
        this.movespeed = movespeed;
    }

    public String getRadius10() {
        return radius10;
    }

    public void setRadius10(String radius10) {
        this.radius10 = radius10;
    }

    public String getRadius12() {
        return radius12;
    }

    public void setRadius12(String radius12) {
        this.radius12 = radius12;
    }

    public String getRadius7() {
        return radius7;
    }

    public void setRadius7(String radius7) {
        this.radius7 = radius7;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
