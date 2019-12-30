package com.tepia.guangdong_module.amainguangdong.utils;

import com.esri.arcgisruntime.geometry.AngularUnit;
import com.esri.arcgisruntime.geometry.AngularUnitId;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.GeodeticCurveType;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.LinearUnit;
import com.esri.arcgisruntime.geometry.LinearUnitId;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.tepia.base.view.arcgisLayout.ArcgisGpsUtils;

import java.util.ArrayList;
import java.util.List;

/**
  * Created by      Android studio
  *
  * @author :wwj (from Center Of Wuhan)
  * Date    :2019/5/7
  * Version :1.0
  * 功能描述 :  arcgis工具
 **/
public class ArcgisUtils {

    /**
     * @param p1 轨迹点1
     * @param p2 轨迹点2
     * @return 千米
     */
    public static double distancePoints(Point p1, Point p2) {
        LinearUnit linearUnit = new LinearUnit(LinearUnitId.KILOMETERS);//距离单位
        AngularUnit angularUnit = new AngularUnit(AngularUnitId.DEGREES);//角度单位
        return GeometryEngine.distanceGeodetic(p1, p2, linearUnit, angularUnit, GeodeticCurveType.GEODESIC).getDistance();
    }


    /**
     * 设置地图的可见范围
     *
     * @param points   统一4326坐标系
     */
    public static void setMapViewVisibleExtent(List<Point> points, MapView mapView) {
        try {
            if (points != null && points.size() > 0) {
                List<Point>  pointList = new ArrayList<>();
                for (int i = 0; i < points.size(); i++) {
                    Point point = points.get(i);
                    Point point1 = transformationPoint(point.getX(), point.getY());
                    pointList.add(point1);
                }
                double numx = (double) pointList.get(0).getX();
                double numy = (double) pointList.get(0).getY();
                double minx = (double) pointList.get(0).getX();
                double miny = (double) pointList.get(0).getY();
                for (int i = 0; i < pointList.size(); i++) {
                    double x = (double) pointList.get(i).getX();
                    double y = (double) pointList.get(i).getY();
                    numx = x < numx ? numx : x;
                    numy = y < numy ? numy : y;
                    minx = x > minx ? minx : x;
                    miny = y > miny ? miny : y;
                }
                double xcen = (numx - minx) > 0 ? (numx - minx) : 0;
                double ycen = (numy - miny) > 0 ? (numy - miny) : 0;
//            Envelope envelope = new Envelope();
//            envelope.setXMin(minPoint.getX() - xcen / 10);
//            envelope.setYMin(minPoint.getY() - ycen / 10);
//            envelope.setXMax(maxPoint.getX() + xcen / 10);
//            envelope.setYMax(maxPoint.getY() + ycen / 10);
//            mapView.setExtent(envelope);
                Envelope envelope = new Envelope(minx - xcen / 10, miny- ycen / 10, numx+ xcen / 10, numy+ ycen / 10, SpatialReference.create(3857));
                mapView.setViewpointGeometryAsync(envelope);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 地图坐标转换
     *
     * @param lgtd
     * @param lttd
     * @return
     */
    public static Point transformationPoint(double lgtd, double lttd) {
        Point point1 = new Point(lgtd, lttd, SpatialReference.create(4326));
//        Google地图偏移
        double[] doubles = ArcgisGpsUtils.gps84_To_Gcj02(lttd, lgtd);
        Point point = (Point) GeometryEngine.project(new Point(doubles[1],doubles[0],SpatialReference.create(4326)), SpatialReferences.getWebMercator());
//        Point point = (Point) GeometryEngine.project(point1, SpatialReferences.getWebMercator());
        return point;
    }

}
