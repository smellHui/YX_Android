package com.tepia.arcgismap.layer;

import android.view.MotionEvent;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.IdentifyGraphicsOverlayResult;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.tepia.arcgismap.layer.core.ICallout;
import com.tepia.arcgismap.layer.core.IMap;
import com.tepia.arcgismap.layer.core.IPoly;
import com.tepia.arcgismap.layer.observer.IMapSingleTapConfirmedObserver;
import com.tepia.arcgismap.layer.poly.callback.OnPlotListener;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Author:xch
 * Date:2019/8/1
 * Description:
 */
public class SimpleMapSingleTapObserver implements IMapSingleTapConfirmedObserver {

    private IMap map;
    private MapView mMapView;
    private List<IPoly> mPloys;
    private ICallout iCallout;

    public SimpleMapSingleTapObserver(IMap map) {
        this.map = map;
        this.mMapView = map.getMapView();
        this.mPloys = map.getPloyPlotters();
        this.iCallout = map.getiCallout();
    }

    public void onTouchMapListener(Point point){

    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        android.graphics.Point screenPoint = new android.graphics.Point(
                Math.round(e.getX()),
                Math.round(e.getY()));
        Point mapPoint = mMapView.screenToLocation(screenPoint);
        onTouchMapListener(mapPoint);

        if (mPloys.isEmpty()) return true;
        //TODO 问题待改
        IPoly poly = mPloys.get(0);
        if (poly == null) return true;
        GraphicsOverlay mGraphicsOverlay = poly.getmGraphicsOverlay();
        if (mGraphicsOverlay == null) return true;
        // identify graphics on the graphics overlay
        final ListenableFuture<IdentifyGraphicsOverlayResult> identifyGraphic =
                mMapView.identifyGraphicsOverlayAsync(mGraphicsOverlay,
                        screenPoint, 10.0, false, 2);
        identifyGraphic.addDoneListener(() -> {
            try {
                IdentifyGraphicsOverlayResult grOverlayResult = identifyGraphic.get();
                // get the list of graphics returned by identify graphic overlay
                List<Graphic> graphics = grOverlayResult.getGraphics();
                iCallout.dismiss();
                if (!graphics.isEmpty()) {
                    // get callout, set content and show
                    Map<String, Object> mAttributeMap = graphics.get(0).getAttributes();
                    if (!mAttributeMap.isEmpty()) {
                        String city = mAttributeMap.get("city").toString();
                        String country = mAttributeMap.get("country").toString();
                        iCallout.show(mapPoint, city + ", " + country);
                    } else {
                        iCallout.show(mapPoint, "i know");
                    }
                }
            } catch (InterruptedException | ExecutionException ie) {
                ie.printStackTrace();
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
        return 0;
    }
}
