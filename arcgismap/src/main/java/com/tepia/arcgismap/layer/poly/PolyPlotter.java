package com.tepia.arcgismap.layer.poly;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.Polygon;
import com.esri.arcgisruntime.geometry.Polyline;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.IdentifyGraphicsOverlayResult;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.Symbol;
import com.esri.arcgisruntime.util.ListenableList;
import com.tepia.arcgismap.layer.core.IMap;
import com.tepia.arcgismap.layer.core.IPoly;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.esri.arcgisruntime.loadable.LoadStatus.LOADED;

/**
 * Author:xch
 * Date:2019/7/26
 * Description:折线，多边形绘制类。
 */
public class PolyPlotter implements IPoly {

    private static final String markImgUri = "http://sampleserver6.arcgisonline.com/arcgis/rest/services/Recreation/FeatureServer/0/images/e82f744ebb069bb35b234b3fea46deae";
    private Context mContext;
    private IMap map;
    private SpatialReference spatialReference;
    private GraphicsOverlay mGraphicsOverlay;

    private float pictureWidth = 18f;
    private float pictureHeight = 18f;

    private int polyColor = Color.BLUE;
    private float polyWidth = 3.0f;
    private SimpleLineSymbol.Style style = SimpleLineSymbol.Style.SOLID;

    public PolyPlotter(IMap map) {
        this.mContext = map.getContext();
        this.spatialReference = map.getSpatialReference();
        this.mGraphicsOverlay = new GraphicsOverlay();
        this.map = map;
        map.getGraphicsOverlays().add(mGraphicsOverlay);
    }

    public GraphicsOverlay getmGraphicsOverlay() {
        return mGraphicsOverlay;
    }

    @Override
    public Graphic createPicturePointGraphics(Point point) {
        return this.createPicturePointGraphics(point, markImgUri);
    }

    @Override
    public Graphic createPicturePointGraphics(Point point, Map<String, Object> attributes) {
        return this.createPicturePointGraphics(point, markImgUri, attributes);
    }

    @Override
    public Graphic createPicturePointGraphics(Point point, @DrawableRes int imgPath) {
        return this.createPicturePointGraphics(point, imgPath, 0, 0);
    }

    @Override
    public Graphic createPicturePointGraphics(Point point, @DrawableRes int imgPath, Map<String, Object> attributes) {
        return this.createPicturePointGraphics(point, imgPath, attributes,0,0);
    }

    @Override
    public Graphic createPicturePointGraphics(Point point, @DrawableRes int imgPath, float width, float height) {
        return this.createPicturePointGraphics(point, imgPath, null, width, height);
    }

    @Override
    public Graphic createPicturePointGraphics(Point point, @DrawableRes int imgPath, Map<String, Object> attributes, float width, float height) {
        try {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) ContextCompat.getDrawable(mContext, imgPath);
            if (bitmapDrawable == null) return null;
            ListenableFuture<PictureMarkerSymbol> pictureMarkerSymbolListenableFuture = PictureMarkerSymbol.createAsync(bitmapDrawable);
            PictureMarkerSymbol pointSymbol = pictureMarkerSymbolListenableFuture.get();
            if (width != 0f || height != 0f) {
                pointSymbol.setWidth(width);
                pointSymbol.setHeight(height);
            }
            //可以设置图片的偏移，使图片符号的基点与几何点对齐
            pointSymbol.setOffsetY(11);
            Graphic pointGraphic = createGraphic(point, pointSymbol, attributes);
            pictureMarkerSymbolListenableFuture.addDoneListener(() -> {
                addGraphic(pointGraphic);
            });
            return pointGraphic;
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Graphic createGraphic(Point point, Symbol symbol, Map<String, Object> attributes) {
        Graphic pointGraphic;
        if (attributes == null || attributes.isEmpty()) {
            pointGraphic = new Graphic(point, symbol);
        } else {
            pointGraphic = new Graphic(point, attributes, symbol);
        }
        return pointGraphic;
    }

    @Override
    public Graphic createPicturePointGraphics(Point point, String url) {
        return this.createPicturePointGraphics(point, url, null);
    }

    @Override
    public Graphic createPicturePointGraphics(Point point, String url, Map<String, Object> attributes) {
        return this.createPicturePointGraphics(point, url, attributes, pictureWidth, pictureHeight);
    }

    @Override
    public Graphic createPicturePointGraphics(Point point, String url, float width, float height) {
        return createPicturePointGraphics(point, url, null, width, height);
    }

    @Override
    public Graphic createPicturePointGraphics(Point point, String url, Map<String, Object> attributes, float width, float height) {
        PictureMarkerSymbol pointSymbol = new PictureMarkerSymbol(url);
        pointSymbol.setWidth(width);
        pointSymbol.setHeight(height);
        pointSymbol.loadAsync();
        Graphic pointGraphic = createGraphic(point, pointSymbol, attributes);
        pointSymbol.addDoneLoadingListener(() -> {
            if (pointSymbol.getLoadStatus() == LOADED) {
                addGraphic(pointGraphic);
            } else {
                //TODO 待处理
            }
        });
        return pointGraphic;
    }

    /**
     * 划点
     *
     * @param point
     */
    @Override
    public Graphic createSimplePointGraphics(Point point) {
        return this.createSimplePointGraphics(point, SimpleMarkerSymbol.Style.CIRCLE, Color.rgb(226, 119, 40), 10.0f);
    }

    @Override
    public Graphic createSimplePointGraphics(Point point, Map<String, Object> attributes) {
        return null;
    }

    @Override
    public Graphic createSimplePointGraphics(Point point, SimpleMarkerSymbol.Style style, int color, float size) {
        SimpleMarkerSymbol pointSymbol = new SimpleMarkerSymbol(style, color, size);
        pointSymbol.setOutline(new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 2.0f));
        Graphic pointGraphic = new Graphic(point, pointSymbol);
        addGraphic(pointGraphic);
        return pointGraphic;
    }

    @Override
    public Graphic createSimplePointGraphics(Point point, SimpleMarkerSymbol.Style style, Map<String, Object> attributes, int color, float size) {
        SimpleMarkerSymbol pointSymbol = new SimpleMarkerSymbol(style, color, size);
        pointSymbol.setOutline(new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 2.0f));
        Graphic pointGraphic;
        if (attributes == null) {
            pointGraphic = new Graphic(point, pointSymbol);
        } else {
            pointGraphic = new Graphic(point, attributes, pointSymbol);
        }
        addGraphic(pointGraphic);
        return pointGraphic;
    }

    /**
     * 划折线
     *
     * @param points
     */
    @Override
    public Graphic createPolylineGraphics(Iterable<Point> points) {
        return this.createPolylineGraphics(points, style, polyColor, polyWidth);
    }

    @Override
    public Graphic createPolylineGraphics(Iterable<Point> points, int color) {
        return this.createPolylineGraphics(points, style, color, polyWidth);
    }

    @Override
    public Graphic createPolylineGraphics(Iterable<Point> points, float width) {
        return this.createPolylineGraphics(points, style, polyColor, width);
    }

    @Override
    public Graphic createPolylineGraphics(Iterable<Point> points, int color, float width) {
        return this.createPolylineGraphics(points, style, color, width);
    }

    @Override
    public Graphic createPolylineGraphics(Iterable<Point> points, SimpleLineSymbol.Style style, int color, float width) {
        PointCollection polylinePoints = new PointCollection(points, SpatialReferences.getWebMercator());
        Polyline polyline = new Polyline(polylinePoints);
        SimpleLineSymbol polylineSymbol = new SimpleLineSymbol(style, color, width);
        Graphic polylineGraphic = new Graphic(polyline, polylineSymbol);
        addGraphic(polylineGraphic);
        return polylineGraphic;
    }

    /**
     * 划多边形
     *
     * @param points
     */
    @Override
    public Graphic createPolygonGraphics(Iterable<Point> points) {
        PointCollection polygonPoints = new PointCollection(points, this.spatialReference);
        Polygon polygon = new Polygon(polygonPoints);
        SimpleFillSymbol polygonSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, Color.rgb(226, 119, 40),
                new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 2.0f));
        Graphic polygonGraphic = new Graphic(polygon, polygonSymbol);
        addGraphic(polygonGraphic);
        return polygonGraphic;
    }

    @Override
    public void removeGraphic(Graphic graphic) {
        getGraphics().remove(graphic);
    }

    @Override
    public void addGraphic(Graphic graphic) {
        getGraphics().add(graphic);
    }

    @Override
    public void clearGraphics() {
        getGraphics().clear();
    }


    @Override
    public ListenableList<Graphic> getGraphics() {
        return mGraphicsOverlay.getGraphics();
    }

    @Override
    public ListenableFuture<IdentifyGraphicsOverlayResult> identifyGraphicsOverlayAsync(android.graphics.Point screenPoint, double tolerance, boolean returnPopupsOnly, int maximumResults) {
        return map.getMapView().identifyGraphicsOverlayAsync(mGraphicsOverlay,
                screenPoint, 10.0, false, 2);
    }

}
