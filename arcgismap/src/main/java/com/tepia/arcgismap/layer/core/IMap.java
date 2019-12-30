package com.tepia.arcgismap.layer.core;

import android.content.Context;
import android.view.View;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.util.ListenableList;
import com.tepia.arcgismap.layer.poly.callback.OnPlotListener;

import java.util.List;

/**
 * Author:xch
 * Date:2019/7/26
 * Description:
 */
public interface IMap {

    Context getContext();

    MapView getMapView();

    List<IPoly> getPloyPlotters();

    void clearLog();

    void startLocation();

    void center(Point point);

    void zoomin();

    void zoomout();

    SpatialReference getSpatialReference();

    ListenableList<GraphicsOverlay> getGraphicsOverlays();

    Callout getCallout();

    ICallout getiCallout();
}
