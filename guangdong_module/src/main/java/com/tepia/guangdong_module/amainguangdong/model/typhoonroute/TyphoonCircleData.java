package com.tepia.guangdong_module.amainguangdong.model.typhoonroute;

import com.esri.arcgisruntime.geometry.Point;

/**
 * Created by Android Studio
 *
 * @author : Arthur
 * Date :    2019/6/5
 * Time :    18:06
 * Describe :
 */
public class TyphoonCircleData {
    private double radius;
    private String color;
    private Point point;

    public double getRadius() {
        return radius;
    }

    public TyphoonCircleData setRadius(double radius) {
        this.radius = radius;
        return this;
    }

    public String getColor() {
        return color;
    }

    public TyphoonCircleData setColor(String color) {
        this.color = color;
        return this;
    }

    public Point getPoint() {
        return point;
    }

    public TyphoonCircleData setPoint(Point point) {
        this.point = point;
        return this;
    }
}
