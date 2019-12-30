package com.tepia.arcgismap.layer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.util.ListenableList;
import com.tepia.arcgismap.Util;
import com.tepia.arcgismap.layer.core.IArcGis;
import com.tepia.arcgismap.layer.core.ICallout;
import com.tepia.arcgismap.layer.core.IMap;
import com.tepia.arcgismap.layer.core.IPoly;
import com.tepia.arcgismap.layer.impl.ArcGisImpl;
import com.tepia.arcgismap.layer.observer.IMapLocationChangedObserver;
import com.tepia.arcgismap.layer.observer.IMapMapRotationChangedObserver;
import com.tepia.arcgismap.layer.observer.IMapObserver;
import com.tepia.arcgismap.layer.observer.IMapSingleTapConfirmedObserver;
import com.tepia.arcgismap.layer.observer.IMapUpObserver;
import com.tepia.arcgismap.layer.observer.IMapViewDrawStatusChangedObserver;
import com.tepia.arcgismap.layer.observer.IMapViewpointChangedObserver;
import com.tepia.arcgismap.layer.poly.PolyPlotter;
import com.tepia.base.utils.LogUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;

/**
 * Author:xch
 * Date:2019/7/23
 * Description:
 */
public class MapManager implements IMap {

    private static final double DEFAULT_SCALE = 3000.0d;

    private Context mContext;
    private MapView mMapView;
    private IArcGis mArcGISMap;
    private ICallout iCallout;
    private LocationManager locationManager;
    private double scale;

    //是否清楚印记
    private boolean clearSign = true;
    //是否显示地图中心点
    private boolean showMapCenter = false;

    private TreeSet<IMapObserver> mObservers;
    private List<IPoly> mPloys = new ArrayList<>();
    private SpatialReference spatialReference;

    private MapManager(Builder builder) {
        mObservers = new TreeSet<>(new MapObserverComparator());
        this.mContext = builder.mContext;
        this.mMapView = builder.mMapView;
        this.mArcGISMap = builder.mArcGISMap;
        this.scale = builder.scale;
        this.locationManager = builder.locationManager;
        this.clearSign = builder.clearSign;
        this.showMapCenter = builder.showMapCenter;
        this.spatialReference = builder.spatialReference;

        if (this.mArcGISMap == null)
            this.mArcGISMap = new ArcGisImpl();

        mMapView.setMap(mArcGISMap.getArcGISMap());

        iCallout = new CallOutManager(this);

        initEvents();

        if (this.clearSign) {
            clearLog();
        }
    }

    public void setScale(double scale) {
        ListenableFuture<Boolean> viewpointSetFuture = mMapView.setViewpointScaleAsync(scale);
        viewpointSetFuture.addDoneListener(() -> {
            try {
                boolean completed = viewpointSetFuture.get();
                if (completed) {

                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public SpatialReference getSpatialReference() {
        return spatialReference;
    }

    public List<IPoly> getPloyPlotters() {
        return mPloys;
    }

    public void addObserver(IMapObserver observer) {
        boolean success = mObservers.add(observer);
        if (!success) return;
        observer.onAttach(this);
    }

    public boolean hasObserver(IMapObserver observer) {
        return mObservers.contains(observer);
    }

    public void removeObserver(IMapObserver observer) {
        boolean result = mObservers.remove(observer);
        if (!result)
            return;
        observer.onDetach();
    }

    public IPoly addPloyPlotter() {
        IPoly poly = new PolyPlotter(this);
        mPloys.add(poly);
        return poly;
    }

    public ICallout getiCallout() {
        return iCallout;
    }

    /**
     * 设置事件
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initEvents() {
        if (locationManager != null) {
            //定位
            locationManager.setLocationListen(aMapLocation -> {
                for (IMapObserver o : mObservers) {
                    if (o instanceof IMapLocationChangedObserver) {
                        ((IMapLocationChangedObserver) o).onLocationChanged(aMapLocation);
                    }
                }
            });
        }
        mMapView.setOnTouchListener(new DefaultMapViewOnTouchListener(mContext, mMapView) {

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                for (IMapObserver o : mObservers) {
                    if (o instanceof IMapSingleTapConfirmedObserver) {
                        if (((IMapSingleTapConfirmedObserver) o).onSingleTapConfirmed(e)) {
                            return true;
                        }
                    }
                }
                return false;
            }

            @Override
            public boolean onUp(MotionEvent e) {
                for (IMapObserver o : mObservers) {
                    if (o instanceof IMapUpObserver) {
                        if (((IMapUpObserver) o).onUp(e)) {
                            return false;
                        }
                    }
                }
                return true;
            }
        });

        mMapView.addMapRotationChangedListener(mapRotationChangedEvent -> {
            for (IMapObserver o : mObservers) {
                if (o instanceof IMapMapRotationChangedObserver) {
                    ((IMapMapRotationChangedObserver) o).mapRotationChanged(mapRotationChangedEvent);
                }
            }
        });

        mMapView.addMapScaleChangedListener(mapScaleChangedEvent -> {
            LogUtil.i("---mapScaleChangedEvent");
        });

        mArcGISMap.getBasemap().addLoadStatusChangedListener(loadStatusChangedEvent -> {
            LogUtil.i("---loadStatusChangedEvent");
        });

        mArcGISMap.getBasemap().addDoneLoadingListener(() -> {
            LogUtil.i("---addDoneLoadingListener");
        });
        mMapView.addNavigationChangedListener(navigationChangedEvent -> {
            LogUtil.i("---addNavigationChangedListener");
        });

        //监控图层加载
        mMapView.addLayerViewStateChangedListener(layerViewStateChangedEvent -> {
            LogUtil.i("---addLayerViewStateChangedListener");

        });

        mMapView.addViewpointChangedListener(viewpointChangedEvent -> {
            LogUtil.i("---addViewpointChangedListener");
            for (IMapObserver o : mObservers) {
                if (o instanceof IMapViewpointChangedObserver) {
                    ((IMapViewpointChangedObserver) o).viewpointChanged(viewpointChangedEvent);
                }
            }
        });

        //监控地图绘制
        mMapView.addDrawStatusChangedListener(drawStatusChangedEvent -> {
            LogUtil.i("---addViewpointChangedListener");
            for (IMapObserver o : mObservers) {
                if (o instanceof IMapViewDrawStatusChangedObserver) {
                    ((IMapViewDrawStatusChangedObserver) o).viewDrawStatusChanged(drawStatusChangedEvent);
                }
            }
        });
    }

    @Override
    public Context getContext() {
        return this.mContext;
    }

    /**
     * 去掉水印
     */
    @Override
    public void clearLog() {
        ArcGISRuntimeEnvironment.setLicense("runtimelite,1000,rud7416273699,none,4N5X0H4AH7AH6XCFK036");
        mMapView.setAttributionTextVisible(false);
    }

    @Override
    public void startLocation() {
//        LocationDisplay locationDisplay = mMapView.getLocationDisplay();
//        locationDisplay.setShowLocation(false);//显示当前位置符号
//        // 设置定位模式
//        /**
//         *  LocationDisplayManager.AutoPanMode:
//         // (1) COMPASS：定位到你所在的位置（作为中心位置显示）并按照手机所指向的方向旋转地图（非行驶状态）。
//         // （2）LOCATION：自动定位到你的位置（作为中心位置显示）
//         // （3）NAVIGATION:默认情况下，这将图标放置在屏幕底部，并将地图旋转至行驶的方向。
//         // （4）OFF：不会自动定位，它只会简单地显示地图（默认）
//         */
//        locationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.OFF);
//        locationDisplay.setShowPingAnimation(true);
//        locationDisplay.addLocationChangedListener(locationChangedEvent -> {
//            for (IMapObserver o : mObservers) {
//                if (o instanceof IMapLocationChangedObserver) {
//                    ((IMapLocationChangedObserver) o).onLocationChanged(locationChangedEvent);
//                }
//            }
//        });
//        locationDisplay.startAsync();
    }

    /**
     * 将地图居中到指定的点
     *
     * @param point
     */
    @Override
    public void center(Point point) {
        point = Util.transformationPoint(point);
        Viewpoint viewpoint = new Viewpoint(point, scale);
        ListenableFuture<Boolean> viewpointSetFuture = mMapView.setViewpointAsync(viewpoint, 2);
        viewpointSetFuture.addDoneListener(() -> {
            try {
                boolean completed = viewpointSetFuture.get();
//                if (completed)
//                    ToastUtils.shortToast("Animation completed successfully");
            } catch (InterruptedException e) {
//                ToastUtils.shortToast("Animation interrupted");
            } catch (ExecutionException e) {
                // Deal with exception during animation...
            }
        });

    }

    /**
     * 放大地图
     */
    @Override
    public void zoomin() {
        mMapView.setViewpointScaleAsync(mMapView.getMapScale() * 0.5);
    }

    /**
     * 缩小地图
     */
    @Override
    public void zoomout() {
        mMapView.setViewpointScaleAsync(mMapView.getMapScale() * 2);
    }

    @Override
    public Callout getCallout() {
        return mMapView.getCallout();
    }

    @Override
    public MapView getMapView() {
        return mMapView;
    }

    public ListenableList<GraphicsOverlay> getGraphicsOverlays() {
        return mMapView.getGraphicsOverlays();
    }

    private class MapObserverComparator implements Comparator<IMapObserver> {

        @Override
        public int compare(IMapObserver o1, IMapObserver o2) {
            return o2.getPriority() - o1.getPriority();
        }
    }

    public static class Builder {
        private Context mContext;
        private MapView mMapView;
        private IArcGis mArcGISMap;
        private double scale;
        private LocationManager locationManager;
        private boolean clearSign = true;
        private boolean showMapCenter = false;
        private SpatialReference spatialReference;

        public Builder(@NonNull Context mContext, MapView mMapView) {
            this.mContext = mContext;
            this.mMapView = mMapView;
            this.scale = DEFAULT_SCALE;
            this.spatialReference = SpatialReferences.getWgs84();
        }

        public Builder setLocationManager(LocationManager locationManager) {
            this.locationManager = locationManager;
            return this;
        }

        public Builder setScale(double scale) {
            this.scale = scale;
            return this;
        }

        public Builder setArcGISMap(IArcGis mArcGISMap) {
            this.mArcGISMap = mArcGISMap;
            return this;
        }

        public Builder setClearSign(boolean clearSign) {
            this.clearSign = clearSign;
            return this;
        }

        public Builder setSpatialReference(SpatialReference spatialReference) {
            this.spatialReference = spatialReference;
            return this;
        }

        public Builder setShowMapCenter(boolean showMapCenter) {
            this.showMapCenter = showMapCenter;
            return this;
        }

        public MapManager build() {
            return new MapManager(this);
        }
    }
}
