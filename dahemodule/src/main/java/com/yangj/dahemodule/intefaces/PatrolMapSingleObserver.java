package com.yangj.dahemodule.intefaces;

import android.view.MotionEvent;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.IdentifyGraphicsOverlayResult;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.tepia.arcgismap.layer.core.IMap;
import com.tepia.arcgismap.layer.observer.IMapSingleTapConfirmedObserver;

import java.util.List;

/**
 * Author:xch
 * Date:2019/11/28
 * Description:
 */
public class PatrolMapSingleObserver implements IMapSingleTapConfirmedObserver {

    private MapView mapView;
    private PatrolMapSingleListener patrolMapSingleListener;

    public PatrolMapSingleObserver(MapView mapView, PatrolMapSingleListener patrolMapSingleListener) {
        this.mapView = mapView;
        this.patrolMapSingleListener = patrolMapSingleListener;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        android.graphics.Point screenPoint = new android.graphics.Point(Math.round(e.getX()), Math.round(e.getY()));
        ListenableFuture<List<IdentifyGraphicsOverlayResult>> listListenableFuture = mapView.identifyGraphicsOverlaysAsync(screenPoint, 5, false);
        try {
            List<IdentifyGraphicsOverlayResult> overlayResults = listListenableFuture.get();
            if (overlayResults != null && overlayResults.size() > 0) {
                List<Graphic> graphics = overlayResults.get(0).getGraphics();
                if (!graphics.isEmpty()) {
                    Graphic graphic = graphics.get(0);
                    if (patrolMapSingleListener != null)
                    patrolMapSingleListener.patrolMapSingle(graphic);
                }
            }
        } catch (Exception e1) {

        }
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
