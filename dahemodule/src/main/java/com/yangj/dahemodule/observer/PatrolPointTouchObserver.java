package com.yangj.dahemodule.observer;

import android.graphics.Point;
import android.view.MotionEvent;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.geometry.GeometryType;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.IdentifyGraphicsOverlayResult;
import com.tepia.arcgismap.layer.core.IMap;
import com.tepia.arcgismap.layer.core.IPoly;
import com.tepia.arcgismap.layer.observer.IMapSingleTapConfirmedObserver;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Author:xch
 * Date:2019/12/20
 * Description:
 */
public abstract class PatrolPointTouchObserver implements IMapSingleTapConfirmedObserver {

    private IPoly iPoly;

    public PatrolPointTouchObserver(IPoly iPoly) {
        this.iPoly = iPoly;
    }

    public abstract void onTouchPatrolTouch(Graphic graphic, Map<String, Object> attributes);

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        Point screenPoint = new android.graphics.Point(Math.round(motionEvent.getX()), Math.round(motionEvent.getY()));
        ListenableFuture<IdentifyGraphicsOverlayResult> identifyGraphic = iPoly.identifyGraphicsOverlayAsync(screenPoint, 1.0, false, 2);
        identifyGraphic.addDoneListener(() -> {
            try {
                IdentifyGraphicsOverlayResult grOverlayResult = identifyGraphic.get();
                List<Graphic> graphics = grOverlayResult.getGraphics();
                if (!graphics.isEmpty()) {
                    Graphic graphic = graphics.get(0);
                    GeometryType geometryType = graphic.getGeometry().getGeometryType();
                    //点击的是点要素
                    if (GeometryType.POINT.equals(geometryType)) {
                        Map<String, Object> attributes = graphic.getAttributes();
                        if (!attributes.isEmpty()) {
                            onTouchPatrolTouch(graphic, attributes);
                        }
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
        return true;
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
