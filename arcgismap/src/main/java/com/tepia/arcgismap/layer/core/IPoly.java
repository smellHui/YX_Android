package com.tepia.arcgismap.layer.core;

import android.support.annotation.DrawableRes;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.IdentifyGraphicsOverlayResult;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.util.ListenableList;

import java.util.Map;

/**
 * Author:xch
 * Date:2019/7/30
 * Description:
 */
public interface IPoly {

    GraphicsOverlay getmGraphicsOverlay();

    Graphic createPicturePointGraphics(Point point);

    Graphic createPicturePointGraphics(Point point, Map<String, Object> attributes);

    //通过APP资源创建picture marker symbol
    Graphic createPicturePointGraphics(Point point, @DrawableRes int imgPath);

    Graphic createPicturePointGraphics(Point point, @DrawableRes int imgPath, Map<String, Object> attributes);

    Graphic createPicturePointGraphics(Point point, @DrawableRes int imgPath, float width, float height);

    Graphic createPicturePointGraphics(Point point, @DrawableRes int imgPath, Map<String, Object> attributes, float width, float height);

    //放网络图标
    Graphic createPicturePointGraphics(Point point, String url);

    Graphic createPicturePointGraphics(Point point, String url, Map<String, Object> attributes);

    Graphic createPicturePointGraphics(Point point, String url, float width, float height);

    Graphic createPicturePointGraphics(Point point, String url, Map<String, Object> attributes, float width, float height);

    //放色块
    Graphic createSimplePointGraphics(Point point);

    Graphic createSimplePointGraphics(Point point, Map<String, Object> attributes);

    Graphic createSimplePointGraphics(Point point, SimpleMarkerSymbol.Style style, int color, float size);

    Graphic createSimplePointGraphics(Point point, SimpleMarkerSymbol.Style style, Map<String, Object> attributes, int color, float size);

    //划线
    Graphic createPolylineGraphics(Iterable<Point> points);

    Graphic createPolylineGraphics(Iterable<Point> points, int color);

    Graphic createPolylineGraphics(Iterable<Point> points, float width);

    Graphic createPolylineGraphics(Iterable<Point> points, int color, float width);

    Graphic createPolylineGraphics(Iterable<Point> points, SimpleLineSymbol.Style style, int color, float width);

    Graphic createPolygonGraphics(Iterable<Point> points);

    void clearGraphics();

    void removeGraphic(Graphic graphic);

    void addGraphic(Graphic graphic);

    ListenableList<Graphic> getGraphics();

    ListenableFuture<IdentifyGraphicsOverlayResult> identifyGraphicsOverlayAsync(android.graphics.Point screenPoint, double tolerance, boolean returnPopupsOnly, int maximumResults);
}
