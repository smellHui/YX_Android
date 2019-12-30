package com.tepia.arcgismap.arcmap;

/**
 * Created by      android studio
 *
 * @author :      zhang xinhua
 * Date            :       2019/6/18
 * Time            :       8:57
 * Version         :       1.0
 * 功能描述        :
 **/
public class PointBean {
    private double longitude;
    private double latitude;
    private double height;
    private Integer ImageRes;
    private Object extra;


    public PointBean(double longitude, double latitude, Object extra) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.extra = extra;
    }

    public PointBean(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Integer getImageRes() {
        return ImageRes;
    }

    public void setImageRes(Integer imageRes) {
        ImageRes = imageRes;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public Object getExtra() {
        return extra;
    }

    public void setExtra(Object extra) {
        this.extra = extra;
    }
}
