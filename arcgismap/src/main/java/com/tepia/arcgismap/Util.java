package com.tepia.arcgismap;

import com.amap.api.location.AMapLocation;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.example.gaodelibrary.GPSUtil;
import com.tepia.base.view.arcgisLayout.ArcgisGpsUtils;

/**
 * Author:xch
 * Date:2019/12/19
 * Description:
 */
public class Util {

    public static Point transformationPoint(Point point) {
//        Google地图偏移
        double[] doubles = ArcgisGpsUtils.gps84_To_Gcj02(point.getY(), point.getX());
        return (Point) GeometryEngine.project(new Point(doubles[1], doubles[0], SpatialReference.create(4326)), SpatialReferences.getWebMercator());
    }

    public static Point gdToArcgis(AMapLocation point) {
        double[] temp = GPSUtil.gcj02_To_Gps84(point.getLatitude(), point.getLongitude());
        double latitude = temp[0];
        double longitude = temp[1];
        if (latitude == 0 || longitude == 0) {
            return null;
        }
        return to_4326(latitude, longitude);
    }

    public static Point to_4326(Point point) {
        return new Point(point.getX(), point.getY(), SpatialReference.create(4326));
    }

    public static Point to_4326(AMapLocation aMapLocation) {
        return new Point(aMapLocation.getLongitude(), aMapLocation.getLatitude(), SpatialReference.create(4326));
    }

    public static Point to_4326(double latitude, double longitude) {
        return new Point(longitude, latitude, SpatialReference.create(4326));
    }
}
