package com.tepia.arcgismap.layer;

import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.GeoView;
import com.esri.arcgisruntime.mapping.view.ViewpointChangedEvent;
import com.tepia.arcgismap.layer.core.IMap;
import com.tepia.arcgismap.layer.observer.IMapObserver;
import com.tepia.arcgismap.layer.observer.IMapViewpointChangedObserver;
import com.tepia.arcgismap.layer.poly.callback.OnPlotListener;
import com.tepia.base.utils.LogUtil;

import static com.esri.arcgisruntime.mapping.Viewpoint.Type.CENTER_AND_SCALE;

/**
 * Author:xch
 * Date:2019/8/1
 * Description:
 */
public class SimpleViewpointChangedObserver implements IMapViewpointChangedObserver {

    @Override
    public void viewpointChanged(ViewpointChangedEvent viewpointChangedEvent) {
        GeoView geoView = viewpointChangedEvent.getSource();
        //TODO  这有个bug就是手指一直按着屏幕慢慢移动，在松开手，地图没有惯性移动时，不往下执行方法
        if (!geoView.isNavigating()) {
            Viewpoint viewpoint = geoView.getCurrentViewpoint(CENTER_AND_SCALE);
            if (viewpoint != null) {
                Geometry geometry = viewpoint.getTargetGeometry();
                if (geometry != null) {
                    Envelope envelope = geometry.getExtent();
                    if (envelope != null) {
                        Point centerPoint = envelope.getCenter();
                        onViewpointChangedListener(centerPoint);
                    }
                }
            }
        }
    }

    public void onViewpointChangedListener(Point point){

    }

    @Override
    public void onAttach(IMap map) {

    }

    @Override
    public void onDetach() {

    }

    @Override
    public int getPriority() {
        return 1;
    }
}
