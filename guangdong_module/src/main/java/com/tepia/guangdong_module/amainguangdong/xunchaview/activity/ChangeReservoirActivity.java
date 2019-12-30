package com.tepia.guangdong_module.amainguangdong.xunchaview.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClientOption;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.TileCache;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.layers.WebTiledLayer;
import com.esri.arcgisruntime.location.LocationDataSource;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.IdentifyGraphicsOverlayResult;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.util.ListenableList;
import com.example.gaodelibrary.GPSUtil;
import com.example.gaodelibrary.GaodeEntity;
import com.example.gaodelibrary.OnGaodeLibraryListen;
import com.example.guangdong_module.R;
import com.google.gson.Gson;
import com.tepia.base.CacheConsts;
import com.tepia.base.mvp.BaseActivity;
import com.tepia.base.utils.LogUtil;
import com.tepia.base.utils.ScreenUtil;
import com.tepia.base.utils.ToastUtils;

import com.tepia.base.view.arcgisLayout.ArcgisLayout;
import com.tepia.base.view.arcgisLayout.GoogleMapLayer;
import com.tepia.base.view.customScrollView.ContentRecyclerView;
import com.tepia.base.view.customScrollView.ScrollLayout;
import com.tepia.base.view.dialog.permissiondialog.Px2dpUtils;
import com.tepia.guangdong_module.MainActivity;
import com.tepia.guangdong_module.amainguangdong.common.CrashHandler;
import com.tepia.guangdong_module.amainguangdong.common.UserManager;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.ReservoirBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.RoutePosition;
import com.tepia.guangdong_module.amainguangdong.route.RoutepointDataBean;
import com.tepia.guangdong_module.amainguangdong.route.RoutepointDataManager;
import com.tepia.guangdong_module.amainguangdong.route.TaskItemBean;
import com.tepia.guangdong_module.amainguangdong.utils.ArcgisUtils;
import com.tepia.guangdong_module.amainguangdong.utils.EmptyLayoutUtil;
import com.tepia.guangdong_module.amainguangdong.utils.InspectionDateUtils;
import com.tepia.guangdong_module.amainguangdong.xunchaview.adapter.MyChangeReservoirAdapter;
import com.tepia.photo_picker.utils.SPUtils;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by      Android studio
 *
 * @author :wwj (from Center Of Wuhan)
 * Date    :2019/5/5
 * Version :1.0
 * 功能描述 :  切换水库
 **/
public class ChangeReservoirActivity extends BaseActivity {
    public static final int resultCode = 1001;

    private List<ReservoirBean> reservoirs = new ArrayList<>();
    private List<ReservoirBean> localReservoirList = new ArrayList<>();
    private List<ReservoirBean> dxcReservoirList = new ArrayList<>();
    private List<ReservoirBean> fiveKmReservoirList = new ArrayList<>();
    private List<ReservoirBean> tenKmReservoirList = new ArrayList<>();
    private MapView mapView;
    private WebTiledLayer titleLayer;
    private WebTiledLayer imgTitleLayer;
    private GraphicsOverlay locationOverlay;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private boolean isFirstLocation = true;
    public static String isKeyBack = "isKeyBack";
    private int picId = R.drawable.reservoir_pic;
    private Callout callout;
    private int mapHeight;

    /**
     * 4326坐标系当前点为点
     */
    private Point currentPoint;
    private MyChangeReservoirAdapter myChangeReservoirAdapter;
    private ContentRecyclerView rvSelectReservoir;
    private GraphicsOverlay reservoirOverlay;

    private ReservoirBean defaultReservoir;

    private ScrollLayout scroll_down_layout;
    private TextView text_foot;
    private RadioGroup rg;
    private CheckBox checkbox;

    @Override
    public int getLayoutId() {
        return R.layout.activity_change_reservoir;
    }

    @Override
    public void initData() {
        localReservoirList = UserManager.getInstance().getLocalReservoirList();
        defaultReservoir = UserManager.getInstance().getDefaultReservoir();
    }

    @Override
    public void initView() {
        CrashHandler.getInstance().init(this);
        scroll_down_layout = findViewById(R.id.scroll_down_layout);
        ImageView ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra(isKeyBack, true);
            ChangeReservoirActivity.this.setResult(resultCode, intent);
            finish();
        });
        text_foot = findViewById(R.id.text_foot);
        mapView = findViewById(R.id.mapView);
        initRecyclerView();
        initRadioGroup();
        initGaoDeLocation();
//        initArcgisLocation();
        //android 6.0动态申请权限
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        } else {
            //开始定位
            if (gaodeEntity != null) {
                gaodeEntity.startLocation();
            }
           /* if (mLocationDisplay != null && !mLocationDisplay.isStarted()) {
                mLocationDisplay.startAsync();//开始定位
            }*/
        }
        Button btChange = findViewById(R.id.bt_change);
        checkbox = findViewById(R.id.checkbox);
        ViewTreeObserver viewTreeObserver = mapView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mapHeight = mapView.getHeight();
//                Log.i("距离顶部的高度:",viewRect.toString());
////                Log.i("屏幕高度:",""+ ScreenUtil.getScreenHeightPix(mContext));
                Rect viewRect = new Rect();
                checkbox.getGlobalVisibleRect(viewRect);
                int checkbox_top = viewRect.top;
                initScrollLayout(checkbox_top);
            }
        });
        initMap();
        new Handler().postDelayed(this::setReservoirMarkers,1500);
    }

    private Boolean isMoveUp = false;
    private Boolean isMoveDown = true;

    /**
     * 列表滑动监听
     */
    private ScrollLayout.OnScrollChangedListener mOnScrollChangedListener = new ScrollLayout.OnScrollChangedListener() {
        @Override
        public void onScrollProgressChanged(float currentProgress) {
            if (text_foot.getVisibility() == View.VISIBLE) {
                text_foot.setVisibility(View.GONE);
            }
        }

        @Override
        public void onScrollFinished(ScrollLayout.Status currentStatus) {
            if (currentStatus == ScrollLayout.Status.EXIT) {
                text_foot.setVisibility(View.VISIBLE);
                if (!isMoveDown) {
                    moveMap(false);
                    isMoveDown = true;
//                    ObjectAnimator objectAnimator = translationAnimator(llZoom, (mapHeight / 2 - Px2dpUtils.dip2px(mContext, 110)), 0);
//                    objectAnimator.start();
                }
                isMoveUp = false;
            } else if (currentStatus == ScrollLayout.Status.OPENED) {
                if (!isMoveUp) {
                    moveMap(true);
                    isMoveUp = true;
//                    ObjectAnimator objectAnimator = translationAnimator(llZoom, -(mapHeight / 2 - Px2dpUtils.dip2px(mContext, 110)), 0);
//                    objectAnimator.start();
                }
                isMoveDown = false;
            }
        }

        @Override
        public void onChildScroll(int top) {

        }
    };

    private double translationY;

    /**
     * 地图滑动
     *
     * @param status true上移  false 下移
     */
    private void moveMap(Boolean status) {
        android.graphics.Point screenPoint = new android.graphics.Point(0, 0);
        if (mapView.screenToLocation(screenPoint) != null) {
            double mapScale = mapView.getMapScale();
            if (status) {
                mapView.setViewpointScaleAsync(mapScale*0.5).addDoneListener(() -> {
                    double y = mapView.screenToLocation(screenPoint).getY();
                    android.graphics.Point bottomPoint = new android.graphics.Point(0, mapHeight);
                    double bottomY = mapView.screenToLocation(bottomPoint).getY();
                    translationY = (bottomY - y) / 2 - (bottomY - y) / 4;
//                mapView.visibleArea   返回一个Polygon，表示当前在MapView中可见的ArcGISMap区域。
                    Viewpoint viewPoint = mapView.getCurrentViewpoint(Viewpoint.Type.CENTER_AND_SCALE);
                    Point centerPoint = viewPoint.getTargetGeometry().getExtent().getCenter();
                    //中心点上移
                    mapView.setViewpointCenterAsync(new Point(centerPoint.getX(), centerPoint.getY() + translationY));
                });
            } else {
                double y = mapView.screenToLocation(screenPoint).getY();
                android.graphics.Point bottomPoint = new android.graphics.Point(0, mapHeight);
                double bottomY = mapView.screenToLocation(bottomPoint).getY();
                translationY = (bottomY - y) / 2 - (bottomY - y) / 4;
//                mapView.visibleArea   返回一个Polygon，表示当前在MapView中可见的ArcGISMap区域。
                Viewpoint viewPoint = mapView.getCurrentViewpoint(Viewpoint.Type.CENTER_AND_SCALE);
                Point centerPoint = viewPoint.getTargetGeometry().getExtent().getCenter();
                mapView.setViewpointScaleAsync(mapScale*2).addDoneListener(() -> {
                    //下移
                    mapView.setViewpointCenterAsync(new Point(centerPoint.getX(), centerPoint.getY() - translationY));
                });
            }
        }
    }

    /**
     *初始化滑动布局
     */
    private void initScrollLayout(int checkBoxTop) {
        LinearLayout llListTitle = findViewById(R.id.ll_list_title);
        LinearLayout llScrollTitle = findViewById(R.id.ll_scroll_title);
        //设置列表滑动布局
        //关闭状态时最上方预留高度
        scroll_down_layout.setMinOffset(Px2dpUtils.dip2px(getContext(), 100));
        //打开状态时内容显示区域的高度
        scroll_down_layout.setMaxOffset(mapHeight / 2);
        //最低部退出状态时可看到的高度，0为不可见
        int height = checkbox.getHeight();
        scroll_down_layout.setExitOffset(ScreenUtil.getScreenHeightPix(getContext()) - checkBoxTop - Px2dpUtils.dip2px(getContext(), 15) - ScreenUtil.getStatusBarHeight()-height);
        //解决recycleView底部显示不全的问题
        rvSelectReservoir.setPadding(0, 0, 0, Px2dpUtils.dip2px(getContext(), 60));
        //是否支持下滑退出，支持会有下滑到最底部时的回调
        scroll_down_layout.setIsSupportExit(true);
        //是否支持横向滚动
        scroll_down_layout.setAllowHorizontalScroll(true);
        scroll_down_layout.setOnScrollChangedListener(mOnScrollChangedListener);
        scroll_down_layout.setToExit();
        scroll_down_layout.getBackground().setAlpha(0);
        text_foot.setOnClickListener(v -> scroll_down_layout.scrollToOpen());
        llListTitle.setOnClickListener(v -> {});
        llScrollTitle.setOnClickListener(v -> {
            rvSelectReservoir.scrollToPosition(0);
            scroll_down_layout.scrollToOpen();
        });
        new Handler().postDelayed(() -> {
            scroll_down_layout.scrollToOpen();
        },1500);
    }

    /**
     * 地图添加水库
     */
    private void setReservoirMarkers() {
        if (localReservoirList != null && localReservoirList.size() > 0) {
            for (int i = 0; i < localReservoirList.size(); i++) {
                ReservoirBean reservoirBean = localReservoirList.get(i);
                try {
                    String reservoir = reservoirBean.getReservoir();
                    String reservoirLongitude = reservoirBean.getReservoirLongitude();
                    String reservoirLatitude = reservoirBean.getReservoirLatitude();
                    double lgtd = Double.parseDouble(reservoirLongitude);
                    double lttd = Double.parseDouble(reservoirLatitude);
                    Point point = new Point(lgtd, lttd, SpatialReference.create(4326));
                    Map<String, Object> attrs = new HashMap<>(1);
                    String reservoirBeanStr = new Gson().toJson(reservoirBean);
                    attrs.put("bean",reservoirBeanStr);
                    addPic(picId, point,attrs);
                    //水库图标上方添加文字
                    int color = ContextCompat.getColor(getContext(), R.color.text_map_bg);
//                    ArcgisLayout.setTextMarker(point, reservoirOverlay, reservoir, color,attrs,18);
                } catch (Exception e) {

                }
            }
        }
    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 设置筛选数据
     */
    private void initRadioGroup() {
        rg = findViewById(R.id.rg);
        rg.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_0) {
                //全部
                if (isFirstLocation) {
                    setNearReservoir();
                }
                reservoirs.clear();
                reservoirs.addAll(localReservoirList);
                myChangeReservoirAdapter.notifyDataSetChanged();
            } else if (checkedId == R.id.rb_1) {
                //5Km内
                if (fiveKmReservoirList == null || fiveKmReservoirList.size() == 0) {
                    setNearReservoir();
                }
                reservoirs.clear();
                reservoirs.addAll(fiveKmReservoirList);
                myChangeReservoirAdapter.notifyDataSetChanged();
            } else if (checkedId == R.id.rb_2) {
                //10Km内
                if (tenKmReservoirList == null || tenKmReservoirList.size() == 0) {
                    setNearReservoir();
                }
                reservoirs.clear();
                reservoirs.addAll(tenKmReservoirList);
                myChangeReservoirAdapter.notifyDataSetChanged();
            } else if (checkedId == R.id.rb_3) {
                //待巡查
                if (dxcReservoirList == null || dxcReservoirList.size() == 0) {
                    setNearReservoir();
                }
                reservoirs.clear();
                reservoirs.addAll(dxcReservoirList);
                myChangeReservoirAdapter.notifyDataSetChanged();
            }
        });
    }


    /**
     * 设置附近水库
     */
    private void setNearReservoir() {
        if (currentPoint != null && currentPoint.getX() != 0 && currentPoint.getY() != 0) {
            if (null != localReservoirList && localReservoirList.size() > 0) {
                fiveKmReservoirList.clear();
                tenKmReservoirList.clear();
                dxcReservoirList.clear();
                for (int i = 0; i < localReservoirList.size(); i++) {
                    ReservoirBean reservoirBean = localReservoirList.get(i);
                    try {
                        String reservoirLongitude = reservoirBean.getReservoirLongitude();
                        String reservoirLatitude = reservoirBean.getReservoirLatitude();
                        double lgtd = Double.parseDouble(reservoirLongitude);
                        double lttd = Double.parseDouble(reservoirLatitude);
                        Point point = new Point(lgtd, lttd, SpatialReference.create(4326));
                        double distance = ArcgisUtils.distancePoints(point, currentPoint);
                        reservoirBean.setDistance(distance);
                        String lastTime = reservoirBean.getLastTime();
                        boolean inspectionStatues = getInspectionStatues(lastTime);
                        if (inspectionStatues) {
                            dxcReservoirList.add(reservoirBean);
                        }
                        if (distance < 5) {
                            fiveKmReservoirList.add(reservoirBean);
                        }
                        if (distance < 10) {
                            tenKmReservoirList.add(reservoirBean);
                        }
//                        Log.i("distance:",distance+"km");
                    } catch (Exception e) {
                        e.printStackTrace();
                        reservoirBean.setDistance(-1);
                    }
                }
            }
        }
    }

    /**
     * 得到水库巡查状态
     *
     * @param lastTime
     * @return
     */
    private boolean getInspectionStatues(String lastTime) {
        boolean inspectionStatues;
        String nowTime = InspectionDateUtils.getNowDate();
        try {
            if (null == lastTime || "".equals(lastTime)) {
                inspectionStatues = true;
            } else {
                Date lastDate = sdf.parse(lastTime);
                Date nowDate = sdf.parse(nowTime);
                int days = InspectionDateUtils.differentDays(lastDate, nowDate);
                if (days > 3) {
                    inspectionStatues = true;
                } else {
                    inspectionStatues = false;
                }
            }
        } catch (Exception e) {
            inspectionStatues = false;
        }
        return inspectionStatues;
    }

    private void initMap() {
        ImageView btZoonIn = findViewById(com.tepia.base.R.id.zoomBtnIn);
        ImageView btZoonOut = findViewById(com.tepia.base.R.id.zoomBtnOut);
        //去水印
        ArcGISRuntimeEnvironment.setLicense("runtimelite,1000,rud4163659509,none,1JPJD4SZ8L4HC2EN0229");
        mapView.setAttributionTextVisible(false);
        //google地图
        titleLayer = GoogleMapLayer.createWebTiteLayer(GoogleMapLayer.Type.VECTOR);
        //google影像图
        imgTitleLayer = GoogleMapLayer.createWebTiteLayer(GoogleMapLayer.Type.IMAGE);
        WebTiledLayer webTitleLayer = GoogleMapLayer.createWebTiteLayer(GoogleMapLayer.Type.IMAGE_ANNOTATION);
        Basemap basemap = new Basemap();
        ArcGISMap arcGISMap = new ArcGISMap(basemap);
        arcGISMap.getOperationalLayers().add(imgTitleLayer);
        arcGISMap.getOperationalLayers().add(webTitleLayer);
        if (defaultReservoir != null) {
            String reservoirCode = defaultReservoir.getReservoirCode();
            String localTpkPath = Environment.getExternalStorageDirectory() + "/SKXCDownloads/tpk/" + reservoirCode + ".tpk";
            TileCache tileCache = new TileCache(localTpkPath);
            ArcGISTiledLayer mArcGISTiledLayer = new ArcGISTiledLayer(tileCache);
            arcGISMap.getOperationalLayers().add(mArcGISTiledLayer);
        }
        arcGISMap.setMinScale(ArcgisLayout.minScale);
        Point point1 = new Point(1.1992433073197773E7, 4885139.106039485, SpatialReference.create(3857));
        Point point2 = new Point(1.3532164647994766E7, 2329702.2487083403, SpatialReference.create(3857));
        Envelope initEnvelope = new Envelope(point1, point2);
        arcGISMap.setInitialViewpoint(new Viewpoint(initEnvelope));
        mapView.setMap(arcGISMap);
        //水库图层
        reservoirOverlay = new GraphicsOverlay();
        mapView.getGraphicsOverlays().add(reservoirOverlay);
        //模拟定位图标
        locationOverlay = new GraphicsOverlay();
        mapView.getGraphicsOverlays().add(locationOverlay);
        btZoonIn.setOnClickListener(this::bottomBtnClick);
        btZoonOut.setOnClickListener(this::bottomBtnClick);
        callout = mapView.getCallout();
        mapView.setOnTouchListener(new DefaultMapViewOnTouchListener(getContext(), mapView) {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (callout.isShowing()) {
                    callout.dismiss();
                }
                handleSingleTapEvent(e);
                return super.onSingleTapConfirmed(e);
            }
        });
    }

    /**
     * 地图点击事件
     * @param e
     */
    private void handleSingleTapEvent(MotionEvent e) {
        android.graphics.Point screenPoint = new android.graphics.Point(Math.round(e.getX()), Math.round(e.getY()));
        Point clickPoint = mapView.screenToLocation(screenPoint);
//            LogUtil.e("坐标:", "坐标:x:" + clickPoint.getX() + "y:" + clickPoint.getY());
        SpatialReference spatialReference = mapView.getSpatialReference();
//            LogUtil.e("坐标系", "坐标系:" + spatialReference.getWKText());
        double mapScale = mapView.getMapScale();
//            LogUtil.e("缩放比例", "缩放比例:" + mapScale);
        Point locationPoint = (Point) GeometryEngine.project(clickPoint, SpatialReference.create(4326));
//            LogUtil.e("转换之后坐标:", "坐标:x:" + locationPoint.getX() + "y:" + locationPoint.getY());

        ListenableFuture<List<IdentifyGraphicsOverlayResult>> listListenableFuture = mapView.identifyGraphicsOverlaysAsync(screenPoint, 5, false);
        try{
            List<IdentifyGraphicsOverlayResult> overlayResults = listListenableFuture.get();
            if (overlayResults != null && overlayResults.size() > 0) {
                List<Graphic> graphics = overlayResults.get(0).getGraphics();
                if (graphics != null && graphics.size() > 0) {
                    Map<String, Object> attributes = graphics.get(0).getAttributes();
                    if (attributes != null) {
                        String beanStr = (String) attributes.get("bean");
                        ReservoirBean reservoirBean = new Gson().fromJson(beanStr, ReservoirBean.class);
                        if (reservoirBean!=null){
                            String reservoir = reservoirBean.getReservoir();
                            if (!TextUtils.isEmpty(reservoir)){
                                new AlertDialog.Builder(getContext())
//                                        .setTitle("是否重新下载离线地图包?")
                                        .setMessage("是否切换到"+reservoir+"?")
                                        .setCancelable(true)
                                        .setPositiveButton("确定", (dialog, which) -> {
                                            Intent intent = new Intent();
                                            String reservoirId = reservoirBean.getReservoirId();
                                            intent.putExtra("reservoirId", reservoirId);
                                            intent.putExtra("reservoir", reservoir);
                                            ChangeReservoirActivity.this.setResult(resultCode, intent);
                                            UserManager.getInstance().saveDefaultReservoir(reservoirBean);
                                            finish();
                                        })
                                        .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                                        .show();
                            }
                        }
                    }
                }
            }
        }catch (Exception el){

        }
    }

    private void bottomBtnClick(View view) {
        int i = view.getId();
        if (i == com.tepia.base.R.id.zoomBtnIn) {
            if (mapView != null) {
                //放大
                mapViewZoomIn();
            }
        } else if (i == com.tepia.base.R.id.zoomBtnOut) {
            if (mapView != null) {
                //缩小
                mapViewZoomOut();
            }
        }
    }

    /**
     * 添加图片
     *
     * @param id    图片id
     * @param point 坐标点  统一4326坐标系
     */
    public void addPic(int id, Point point, Map<String, Object> attributes) {
        PictureMarkerSymbol pictureMarkerSymbol1 = null;
        try {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), id);
            pictureMarkerSymbol1 = PictureMarkerSymbol.createAsync(new BitmapDrawable(getResources(), bitmap)).get();
            Point point1 = transformationPoint(point.getX(), point.getY());
            Graphic picGraphic = new Graphic(point1,attributes,pictureMarkerSymbol1);
            reservoirOverlay.getGraphics().add(picGraphic);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * 缩小地图
     */
    public void mapViewZoomOut() {
        if (mapView != null) {
            //缩小
            double mScale = mapView.getMapScale();
            try {
                mapView.setViewpointScaleAsync(mScale * 2);
            } catch (Exception e) {
//                e.printStackTrace();
            }
        }
    }

    /**
     * 放大地图
     */
    public void mapViewZoomIn() {
        if (mapView != null) {
            try {
                //放大
                double mScale = mapView.getMapScale();
                mapView.setViewpointScaleAsync(mScale * 0.5);
            } catch (Exception e) {
//                e.printStackTrace();
            }
        }
    }

    private LocationDisplay mLocationDisplay;

    private void initArcgisLocation() {
        //定位
        mLocationDisplay = mapView.getLocationDisplay();
        mLocationDisplay.addLocationChangedListener(locationChangedEvent -> {
            //getMapLocation获取的点是基于当前地图坐标系的点
            //getPosition是获取基于GPS的位置信息，再获取的点是基于WGS84的经纬度坐标。
            LocationDataSource.Location location = locationChangedEvent.getLocation();
            Point point = mLocationDisplay.getMapLocation();
            Point position = location.getPosition();
//            LogUtil.i(" LocationDataSource.Location:"+location.getPosition());
//            LogUtil.i("point:"+point.toString());
            Point locationPoint = transformationPoint(position.getX(), position.getY());
            Log.i("position:", "x:" + position.getX() + "y:" + position.getY());
            addLocationPic(locationOverlay, R.drawable.google_location, locationPoint);
            currentPoint = new Point(position.getX(), position.getY(), SpatialReference.create(4326));
            if (isFirstLocation) {
                setNearReservoir();
                reservoirs.clear();
                reservoirs.addAll(localReservoirList);
                myChangeReservoirAdapter.notifyDataSetChanged();
                isFirstLocation = false;
            }
        });
        mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.OFF);
        mLocationDisplay.setShowLocation(false);//设置不显示定位图标
    }

    /**
     * 地图坐标转换
     *
     * @param lgtd
     * @param lttd
     * @return
     */
    public Point transformationPoint(double lgtd, double lttd) {
        Point point1 = new Point(lgtd, lttd, SpatialReference.create(4326));
//        Google地图偏移
        double[] doubles = GPSUtil.gps84_To_Gcj02(lttd, lgtd);
        Point point = (Point) GeometryEngine.project(new Point(doubles[1], doubles[0], SpatialReference.create(4326)), SpatialReferences.getWebMercator());
//        Point point = (Point) GeometryEngine.project(point1, SpatialReferences.getWebMercator());
        return point;
    }

    /**
     * 高德地图定位
     */
    private GaodeEntity gaodeEntity;

    private void initGaoDeLocation() {
        if (gaodeEntity == null) {
            gaodeEntity = new GaodeEntity(this, ChangeReservoirActivity.class, R.mipmap.logo);
            gaodeEntity.initLocation();
            boolean offline = SPUtils.getInstance().getBoolean(CacheConsts.OFFLINE, false);
            if (offline) {
                //支持离线模式定位
                gaodeEntity.getLocationClient().setLocationOption(getDefaultOption(5000));

            }
        }
        gaodeEntity.setLocationListen(aMapLocation -> {
            if (gaodeEntity != null) {
                if (aMapLocation.getErrorCode() == 4) {
                    ToastUtils.shortToast("当前网络较差，已自动切换为无网模式定位");

                    gaodeEntity.getLocationClient().setLocationOption(getDefaultOption(5000));

                }
                double[] temp = GPSUtil.gcj02_To_Gps84(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                double latitude = temp[0];
                double longitude = temp[1];
                LogUtil.e(StartInspectionActivity.class.getName(), "经度：" + longitude);
                if (latitude == 0 || longitude == 0) {
                    return;
                }
                currentPoint = new Point(longitude, latitude, SpatialReference.create(4326));
//                Point locationPoint = gaoDeTransformationPoint(longitude, latitude);
                Point locationPoint = new Point(aMapLocation.getLongitude(), aMapLocation.getLatitude(), SpatialReference.create(4326));

                addLocationPic(locationOverlay, R.drawable.google_location, locationPoint);
                if (isFirstLocation) {
                    try {
                        Point point1 = transformationPoint(currentPoint.getX(), currentPoint.getY());
                        mapView.setViewpointCenterAsync(point1, 20000);
                    } catch (Exception e) {
//            e.printStackTrace();
                    }
//                    mapView.setViewpointCenterAsync(locationPoint);
//                    alMapview.setCenterPoint(currentPoint, 20000);
                    setNearReservoir();
                    reservoirs.clear();
                    reservoirs.addAll(localReservoirList);
                    myChangeReservoirAdapter.notifyDataSetChanged();
                    isFirstLocation = false;
                }
            }
        });
    }

    /**
     * 添加定位图标
     *
     * @param graphicsOverlay
     * @param id
     * @param point
     */
    public void addLocationPic(GraphicsOverlay graphicsOverlay, int id, Point point) {
        PictureMarkerSymbol pictureMarkerSymbol1 = null;
        try {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), id);
            if (bitmap == null) {
                return;
            }
            Bitmap result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight() * 2, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);
            canvas.drawBitmap(bitmap, 0, 0, null);
//            canvas.drawBitmap(bitmap, bitmap.getHeight(), 0, null);
            pictureMarkerSymbol1 = PictureMarkerSymbol.createAsync(new BitmapDrawable(getResources(), result)).get();
            Graphic picGraphic = new Graphic(point, pictureMarkerSymbol1);
            ListenableList<Graphic> graphics = graphicsOverlay.getGraphics();
            if (graphics != null && graphics.size() > 0) {
                graphics.set(0, picGraphic);
            } else {
                graphicsOverlay.getGraphics().add(picGraphic);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * 高德地图坐标转换
     *
     * @param lgtd
     * @param lttd
     * @return
     */
    public Point gaoDeTransformationPoint(double lgtd, double lttd) {
        Point point1 = new Point(lgtd, lttd, SpatialReference.create(4326));
//        Google地图偏移
//        double[] doubles = GPSUtil.gps84_To_Gcj02(lttd, lgtd);
        Point point = (Point) GeometryEngine.project(new Point(lgtd, lttd, SpatialReference.create(4326)), SpatialReferences.getWebMercator());
//        Point point = (Point) GeometryEngine.project(point1, SpatialReferences.getWebMercator());
        return point;
    }

    public AMapLocationClientOption getDefaultOption(int interval) {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        //高德地图说明，来自高德android开发常见问题：
        //GPS模块无法计算出定位结果的情况多发生在卫星信号被阻隔时，在室内环境卫星信号会被墙体、玻璃窗阻隔反射，在“城市峡谷”（高楼林立的狭长街道）地段卫星信号也会损失比较多。
        ////强依赖GPS模块的定位模式——如定位SDK的仅设备定位模式，需要在室外环境进行，多用于行车、骑行、室外运动等场景。
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        /*mOption.setGpsFirst(true);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setInterval(interval);//可选，设置定位间隔。默认为2秒
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差*/
        mOption.setGpsFirst(true);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(interval);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }

    private void initRecyclerView() {
        rvSelectReservoir = findViewById(R.id.rv_select_reservoir);
        rvSelectReservoir.setLayoutManager(new LinearLayoutManager(this));
        myChangeReservoirAdapter = new MyChangeReservoirAdapter(R.layout.rv_change_reservoir_item, reservoirs, this);
        rvSelectReservoir.setAdapter(myChangeReservoirAdapter);
        myChangeReservoirAdapter.addFooterView(EmptyLayoutUtil.getFootView());
        if (null != localReservoirList) {
            reservoirs.clear();
            reservoirs.addAll(localReservoirList);
        }
        myChangeReservoirAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent();
            ReservoirBean reservoirBean = reservoirs.get(position);
            String reservoirId = reservoirBean.getReservoirId();
            String reservoir = reservoirBean.getReservoir();
            intent.putExtra("reservoirId", reservoirId);
            intent.putExtra("reservoir", reservoir);
            ChangeReservoirActivity.this.setResult(resultCode, intent);
            UserManager.getInstance().saveDefaultReservoir(reservoirBean);
            finish();
        });
        myChangeReservoirAdapter.setOnLocationClickListener(item -> {
            try {
                if (scroll_down_layout.getCurrentStatus() != ScrollLayout.Status.OPENED) {
                                scroll_down_layout.scrollToOpen();
                }
                String reservoirLongitude = item.getReservoirLongitude();
                String reservoirLatitude = item.getReservoirLatitude();
                double lgtd = Double.parseDouble(reservoirLongitude);
                double lttd = Double.parseDouble(reservoirLatitude);
                Point point = new Point(lgtd, lttd, SpatialReference.create(4326));
                Point point1 = transformationPoint(point.getX(), point.getY());
                android.graphics.Point screenPoint = new android.graphics.Point(0, 0);
                if (mapView.screenToLocation(screenPoint) != null) {
                    double y = mapView.screenToLocation(screenPoint).getY();
                    android.graphics.Point bottomPoint = new android.graphics.Point(0, mapHeight);
                    double bottomY = mapView.screenToLocation(bottomPoint).getY();
                    translationY = (bottomY - y) / 2 - (bottomY - y) / 4;
//                mapView.visibleArea   返回一个Polygon，表示当前在MapView中可见的ArcGISMap区域。
                    Viewpoint viewPoint = mapView.getCurrentViewpoint(Viewpoint.Type.CENTER_AND_SCALE);
                    Point point2 = new Point(point1.getX(), point1.getY() + translationY, mapView.getSpatialReference());
//                    initCallout(point2, item.getReservoir());
                    mapView.setViewpointCenterAsync(point2);
                }
            } catch (Exception e) {

            }
        });
    }

    /**
     * 地图CallOut
     *
     * @param point
     */
    private void initCallout(Point point, String name) {
        TextView tv = new TextView(getApplicationContext());
        tv.setTextColor(Color.BLACK);
        tv.setText(name);
        if (callout==null){
            callout = mapView.getCallout();
        }
        callout.setContent(tv);
        callout.setLocation(point);
        callout.show();
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initRequestData() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length >= 1) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (gaodeEntity != null) {
                        gaodeEntity.startLocation();//开始定位
                    }
                    if (mLocationDisplay != null && !mLocationDisplay.isStarted()) {
                        mLocationDisplay.startAsync();//开始定位
                    }
                } else {
                    new AlertDialog.Builder(this)
                            .setMessage("定位权限被拒绝,将导致定位功能无法正常使用，需要到设置页面手动授权")
                            .setPositiveButton("去授权", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //引导用户至设置页手动授权
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                                    intent.setData(uri);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("取消", (dialog, which) -> {
                                //引导用户手动授权，权限请求失败
                                ToastUtils.longToast("拒绝了定位权限，将不能使用定位功能！！！");
                            }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            //引导用户手动授权，权限请求失败
                            ToastUtils.longToast("拒绝了定位权限，将不能使用定位功能！！！");
                        }
                    }).show();
                    // Permission Denied
                }
            }
            return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gaodeEntity != null) {
            gaodeEntity.closeLocation();
        }
    }
}
