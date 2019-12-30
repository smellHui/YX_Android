package com.yangj.dahemodule.observer;

import com.amap.api.location.AMapLocation;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.tepia.arcgismap.Util;
import com.tepia.arcgismap.layer.core.IMap;
import com.tepia.arcgismap.layer.core.IPoly;
import com.tepia.arcgismap.layer.observer.IMapLocationChangedObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:xch
 * Date:2019/12/19
 * Description:
 */
public class PatrolLocationObserver implements IMapLocationChangedObserver {

    private IPoly iPoly;
    private Point beforePoint;
    private Graphic beforLocationGraphic;

    public PatrolLocationObserver(IPoly iPoly) {
        this.iPoly = iPoly;
    }

    public void onLocation(Point point) {

    }

    @Override
    public void onAttach(IMap map) {
    }

    @Override
    public void onDetach() {

    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        Point point = Util.gdToArcgis(aMapLocation);
        if (point == null) return;

        if (beforePoint != null) {
            if (beforePoint.getX() == point.getX() && beforePoint.getY() == point.getY())
                return;
        }
        beforePoint = point;
        onLocation(point);
        addLocationImg(point);
    }

    private void addLocationImg(Point point) {
        Point locationPoint = Util.transformationPoint(point);
        iPoly.removeGraphic(beforLocationGraphic);
        beforLocationGraphic = iPoly.createPicturePointGraphics(locationPoint, com.example.guangdong_module.R.drawable.google_location);
    }

}
