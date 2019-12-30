package com.tepia.arcgismap.arcmap;


import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.io.RequestConfiguration;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.layers.WebTiledLayer;
import com.esri.arcgisruntime.location.LocationDataSource;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.IdentifyGraphicsOverlayResult;
import com.esri.arcgisruntime.mapping.view.IdentifyLayerResult;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.google.gson.Gson;
import com.tepia.arcgismap.R;
import com.tepia.arcgismap.databinding.FragmentArcMapBinding;
import com.tepia.arcgismap.googlemap.GoogleTiteLayer;
import com.tepia.arcgismap.tianditu.TianDiTuMethodsClass;
//import com.tepia.base.arouter.AppRoutePath;
import com.tepia.base.AppRoutePath;
import com.tepia.base.mvp.MVPBaseFragment;
import com.tepia.base.utils.LogUtil;
import com.tepia.base.utils.ToastUtils;
import com.tepia.base.utils.Utils;
import com.tepia.base.view.floatview.CollectionsUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


/**
 * @author :       zhang xinhua
 * @Version :       1.0
 * @创建人 ：      zhang xinhua
 * @创建时间 :       2019/6/13 15:44
 * @修改人 ：
 * @修改时间 :       2019/6/13 15:44
 * @功能描述 :
 **/
//@Route(path = AppRoutePath.app_fragment_arc_map)
public class ArcMapFragment extends MVPBaseFragment<ArcMapContract.View, ArcMapPresenter> implements ArcMapContract.View {


    private FragmentArcMapBinding mBinding;
    /**
     * 天地图 影像图
     */
    private WebTiledLayer imgWebTiledLayer;
    private WebTiledLayer ciaWebTiledLayer;
    /**
     * 天地图矢量图
     */
    private WebTiledLayer vecWebTiledLayer;
    private WebTiledLayer cvaWebTiledLayer;
    /**
     * 天地图地形图
     */
    private WebTiledLayer terWebTiledLayer;
    private WebTiledLayer ctaWebTiledLayer;
    /**
     * google地图 VECTOR, IMAGE, IMAGE_ANNOTATION, URL_TERRIEN;
     */
    private WebTiledLayer googleVectorWebTiledLayer;
    private WebTiledLayer googleImageWebTiledLayer;
    private WebTiledLayer googleImageAnnotationWebTiledLayer;
    private WebTiledLayer googleUrlTerrienWebTiledLayer;
    private BaseMapType baseMapType;
    private ArcGISMap map;
    private LocationDisplay locationDisplay;
    private LocationDataSource.Location curLocatoin;
    private GraphicsOverlay addPointGraphicsLayer;
    private OnPointClickListener onPointClickListener;

    public OnPointClickListener getOnPointClickListener() {
        return onPointClickListener;
    }

    public void setOnPointClickListener(OnPointClickListener onPointClickListener) {
        this.onPointClickListener = onPointClickListener;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_arc_map;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view) {
        mBinding = DataBindingUtil.bind(view);
        initMapView();

    }

    private void initMapView() {
        {//去水印
            ArcGISRuntimeEnvironment.setLicense("runtimelite,1000,rud7416273699,none,4N5X0H4AH7AH6XCFK036");
            mBinding.mapView.setAttributionTextVisible(false);
        }
        intBaseMap(BaseMapType.TIANDITU_V_I);
        displayDeviceLocation();

        addPointGraphicsLayer = new GraphicsOverlay();
        mBinding.mapView.getGraphicsOverlays().add(addPointGraphicsLayer);

        mBinding.mapView.setOnTouchListener(new DefaultMapViewOnTouchListener(getContext(), mBinding.mapView) {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                android.graphics.Point screenPoint = new android.graphics.Point(Math.round(e.getX()), Math.round(e.getY()));
                final Point center = mMapView.screenToLocation(screenPoint);
                final ListenableFuture<IdentifyGraphicsOverlayResult> identifyFuture = mMapView.identifyGraphicsOverlayAsync(addPointGraphicsLayer,
                        screenPoint, 10, false, 10);

                identifyFuture.addDoneListener(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // get the list of graphics returned by identify
                            List<Graphic> identifiedGraphics = identifyFuture.get().getGraphics();

                            // iterate the graphics
                            for (Graphic graphic : identifiedGraphics) {
                                // Use identified graphics as required, for example access attributes or geometry, select, build a table, etc...
                                graphic.setSelected(true);
                            }
                            if (identifiedGraphics != null && !CollectionsUtil.isEmpty(identifiedGraphics)) {

                                if (onPointClickListener != null) {
                                    onPointClickListener.onClick(identifiedGraphics.get(0).getAttributes().get("pos"));
                                }
                            }
                        } catch (InterruptedException | ExecutionException ex) {
//                            dealWithException(ex); // must deal with checked exceptions
                        }

                    }
                });
                return super.onSingleTapConfirmed(e);
            }
        });
    }

    /**
     * 初始化 底图
     */
    private void intBaseMap(BaseMapType baseMapType) {
        vecWebTiledLayer = initTianDiTuTiledLayer(TianDiTuMethodsClass.LayerType.TIANDITU_VECTOR_MERCATOR);
        cvaWebTiledLayer = initTianDiTuTiledLayer(TianDiTuMethodsClass.LayerType.TIANDITU_VECTOR_ANNOTATION_CHINESE_MERCATOR);

        imgWebTiledLayer = initTianDiTuTiledLayer(TianDiTuMethodsClass.LayerType.TIANDITU_IMAGE_MERCATOR);
        ciaWebTiledLayer = initTianDiTuTiledLayer(TianDiTuMethodsClass.LayerType.TIANDITU_IMAGE_ANNOTATION_CHINESE_MERCATOR);

        terWebTiledLayer = initTianDiTuTiledLayer(TianDiTuMethodsClass.LayerType.TIANDITU_TERRAIN_MERCATOR);
        ctaWebTiledLayer = initTianDiTuTiledLayer(TianDiTuMethodsClass.LayerType.TIANDITU_TERRAIN_ANNOTATION_CHINESE_MERCATOR);

        googleVectorWebTiledLayer = initGoogleTiledLayer(GoogleTiteLayer.Type.VECTOR);
        googleImageWebTiledLayer = initGoogleTiledLayer(GoogleTiteLayer.Type.IMAGE);
        googleImageAnnotationWebTiledLayer = initGoogleTiledLayer(GoogleTiteLayer.Type.IMAGE_ANNOTATION);
        googleUrlTerrienWebTiledLayer = initGoogleTiledLayer(GoogleTiteLayer.Type.URL_TERRIEN);
        this.baseMapType = baseMapType;
        map = new ArcGISMap();
        mBinding.mapView.setMap(map);
        switch (baseMapType) {
            case GOOGLE_V_I:
                map.getBasemap().getBaseLayers().add(googleVectorWebTiledLayer);
                map.getBasemap().getBaseLayers().add(googleImageWebTiledLayer);
                map.getBasemap().getBaseLayers().add(googleImageAnnotationWebTiledLayer);
                break;
            case GOOGLE_I:
                map.getBasemap().getBaseLayers().add(googleVectorWebTiledLayer);
                break;
            case GOOGLE_V:
                map.getBasemap().getBaseLayers().add(googleImageWebTiledLayer);
                map.getBasemap().getBaseLayers().add(googleImageAnnotationWebTiledLayer);
                break;
            case TIANDITU_V_I:
                map.getBasemap().getBaseLayers().add(vecWebTiledLayer);
                map.getBasemap().getBaseLayers().add(cvaWebTiledLayer);
                map.getBasemap().getBaseLayers().add(imgWebTiledLayer);
                map.getBasemap().getBaseLayers().add(ciaWebTiledLayer);
                break;
            case TIANDITU_I:
                map.getBasemap().getBaseLayers().add(vecWebTiledLayer);
                map.getBasemap().getBaseLayers().add(cvaWebTiledLayer);
                break;
            case TIANDITU_V:
                map.getBasemap().getBaseLayers().add(imgWebTiledLayer);
                map.getBasemap().getBaseLayers().add(ciaWebTiledLayer);
                break;
            default:
                break;
        }
    }

    /**
     * 切换 显示底图
     *
     * @param baseMapType
     */
    public void switchBaseMapType(BaseMapType baseMapType) {
        switch (baseMapType) {
            case GOOGLE_I:
                googleVectorWebTiledLayer.setVisible(true);
                googleImageWebTiledLayer.setVisible(false);
                googleImageAnnotationWebTiledLayer.setVisible(false);
                break;
            case GOOGLE_V:
                googleVectorWebTiledLayer.setVisible(false);
                googleImageWebTiledLayer.setVisible(true);
                googleImageAnnotationWebTiledLayer.setVisible(true);
                break;
            case TIANDITU_I:
                vecWebTiledLayer.setVisible(true);
                cvaWebTiledLayer.setVisible(true);
                imgWebTiledLayer.setVisible(false);
                ciaWebTiledLayer.setVisible(false);
                break;
            case TIANDITU_V:
                vecWebTiledLayer.setVisible(false);
                cvaWebTiledLayer.setVisible(false);
                imgWebTiledLayer.setVisible(true);
                ciaWebTiledLayer.setVisible(true);
                break;
            default:
                break;
        }
    }

    private void checkPermission() {
        //监听位置的变化
        locationDisplay.addDataSourceStatusChangedListener(dataSourceStatusChangedEvent -> {
            String[] reqPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            int requestCode = 2;
            if (dataSourceStatusChangedEvent.isStarted()) {
                return;
            }
            if (dataSourceStatusChangedEvent.getError() == null) {
                return;
            }
            //检查ACCESS_FINE_LOCATION权限是否允许
            boolean permissionCheck1 = ContextCompat.checkSelfPermission(getActivity(), reqPermissions[0]) ==
                    PackageManager.PERMISSION_GRANTED;
            //检查ACCESS_COARSE_LOCATION权限是否允许
            boolean permissionCheck2 = ContextCompat.checkSelfPermission(getActivity(), reqPermissions[1]) ==
                    PackageManager.PERMISSION_GRANTED;
            if (!(permissionCheck1 && permissionCheck2)) {
                ActivityCompat.requestPermissions(getActivity(), reqPermissions, requestCode);
            }
        });

    }

    /**
     * 实现定位 位置
     */
    public void displayDeviceLocation() {
        locationDisplay = mBinding.mapView.getLocationDisplay();
        locationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);
        locationDisplay.setInitialZoomScale(7000);
        locationDisplay.startAsync();
        checkPermission();

        locationDisplay.addLocationChangedListener(new LocationDisplay.LocationChangedListener() {
            @Override
            public void onLocationChanged(LocationDisplay.LocationChangedEvent locationChangedEvent) {
                curLocatoin = locationChangedEvent.getLocation();
            }
        });

    }

    //当接收到权限请求回调时调用
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // 如果请求被拒绝，结果数组是空值
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // 位置被授予许可。这可能是由于未能启动该系统而引发的。
            // LocationDisplay,再次定位
            locationDisplay.startAsync();
        } else {
            // 如果被拒绝，显示toast以告知用户所选择的内容。如果LocationDisplay再次启动，请求权限UX将被再次显示，选项应该被显示以允许不再显示UX。
            // 另一种方法是禁用功能，因此请求不再显示

        }
    }

    public void zoomIn() {
        mBinding.mapView.setViewpointScaleAsync(mBinding.mapView.getMapScale() * 0.5);
    }

    public void zoomOut() {
        mBinding.mapView.setViewpointScaleAsync(mBinding.mapView.getMapScale() * 2);
    }

    public void showCallout(PointBean pointBean, View view) {
        Point point;
        if (pointBean == null) {
            point = curLocatoin.getPosition();
        } else {
            point = transformPoint(pointBean);
        }
        Callout mCallout = mBinding.mapView.getCallout();
        mCallout.setLocation(point);
        mCallout.setContent(view);
        mCallout.getStyle().setBorderWidth(0);
        mCallout.getStyle().setBackgroundColor(0xdfffffff);
        mCallout.show();
        mBinding.mapView.setViewpointCenterAsync(point);
    }

    public void hideCallout() {

        Callout mCallout = mBinding.mapView.getCallout();

        mCallout.dismiss();

    }


    private void showCallout(Point point, View view) {
        Callout mCallout = mBinding.mapView.getCallout();
        mCallout.setLocation(point);
        mCallout.setContent(view);
        mCallout.show();
        mBinding.mapView.setViewpointCenterAsync(point);
    }

    private Point transformPoint(PointBean pointBean) {
        return new Point(pointBean.getLongitude(), pointBean.getLatitude(), SpatialReference.create(4326));
    }

    private WebTiledLayer initGoogleTiledLayer(GoogleTiteLayer.Type layerType) {
        WebTiledLayer webTiledLayer = GoogleTiteLayer.createWebTiteLayer(layerType);
        webTiledLayer.loadAsync();
        return webTiledLayer;
    }

    private WebTiledLayer initTianDiTuTiledLayer(TianDiTuMethodsClass.LayerType layerType) {
        WebTiledLayer webTiledLayer = TianDiTuMethodsClass.CreateTianDiTuTiledLayer(layerType);
        webTiledLayer.loadAsync();
        RequestConfiguration requestConfiguration = new RequestConfiguration();
        requestConfiguration.getHeaders().put("referer", "http://map.tianditu.gov.cn/");
        webTiledLayer.setRequestConfiguration(requestConfiguration);
        return webTiledLayer;
    }

    @Override
    protected void initRequestData() {

    }


    @Override
    public void onResume() {
        super.onResume();
        mBinding.mapView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mBinding.mapView.pause();
    }


    public void centerDeviceLocation() {
        mBinding.mapView.setViewpointCenterAsync(curLocatoin.getPosition());
    }

    private void centerPoint(PointBean bean) {
        mBinding.mapView.setViewpointCenterAsync(transformPoint(bean));
    }


    public void addImagePoints(List<PointBean> list) {
        addPointGraphicsLayer.getGraphics().removeAll(addPointGraphicsLayer.getGraphics());
        for (PointBean bean : list) {
            drawMapPointOne(bean);
            centerPoint(bean);
        }

    }


    public Graphic drawMapPointOne(PointBean bean) {
        if (bean == null) {
            return null;
        }
        Point point = transformPoint(bean);
        Map<String, Object> attributes = new HashMap<>(1);
        attributes.put("pos", bean.getExtra());
        PictureMarkerSymbol pictureMarkerSymbol1 = null;

        BitmapDrawable bitmapDrawable = (BitmapDrawable) ContextCompat.getDrawable(getContext(), bean.getImageRes());
        if (bitmapDrawable == null) {
            return null;
        }
        pictureMarkerSymbol1 = new PictureMarkerSymbol(bitmapDrawable);
        Graphic picGraphic = new Graphic(point, attributes, pictureMarkerSymbol1);
        addPointGraphicsLayer.getGraphics().add(picGraphic);
        return picGraphic;
    }
}
