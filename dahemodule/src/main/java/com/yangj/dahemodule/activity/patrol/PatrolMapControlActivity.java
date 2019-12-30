package com.yangj.dahemodule.activity.patrol;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.IdentifyGraphicsOverlayResult;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.example.gaodelibrary.OSUtils;
import com.example.gaodelibrary.XiaomiDeviceUtil;
import com.lxj.xpopup.XPopup;
import com.tepia.arcgismap.layer.core.IPoly;
import com.tepia.base.mvp.BaseActivity;
import com.tepia.base.utils.ResUtils;
import com.tepia.base.utils.SPUtils;
import com.tepia.base.view.arcgisLayout.ArcgisLayout;
import com.tepia.base.view.floatview.CollectionsUtil;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.RouteListBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.RoutePosition;
import com.tepia.guangdong_module.amainguangdong.route.RoutepointDataBean;
import com.tepia.guangdong_module.amainguangdong.route.RoutepointDataManager;
import com.tepia.guangdong_module.amainguangdong.route.TaskBean;
import com.tepia.guangdong_module.amainguangdong.route.TaskItemBean;
import com.tepia.guangdong_module.amainguangdong.utils.ArcgisUtils;
import com.tepia.guangdong_module.amainguangdong.utils.SqlManager;
import com.tepia.guangdong_module.amainguangdong.wrap.PatroltemEvent;
import com.yangj.dahemodule.R;
import com.yangj.dahemodule.adapter.PartCardAdapter;
import com.yangj.dahemodule.intefaces.PatrolPickerCallback;
import com.yangj.dahemodule.util.GaodeHelper;
import com.yangj.dahemodule.view.ForecastView;
import com.yangj.dahemodule.view.PatrolRateView;
import com.yangj.dahemodule.view.PatrolSubmitDialog;
import com.yangj.dahemodule.view.PatrolUpControlView;
import com.yangj.dahemodule.wrap.SubmitTaskWrap;
import com.yarolegovich.discretescrollview.DSVOrientation;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Author:xch
 * Date:2019/11/8
 * Description:巡查点操作页-下
 */
public class PatrolMapControlActivity extends BaseActivity implements
        DiscreteScrollView.OnItemChangedListener,
        DiscreteScrollView.ScrollStateChangeListener,
        BaseQuickAdapter.OnItemClickListener,
        GaodeHelper.LocationListener {
    private int[] picIds = {com.example.guangdong_module.R.drawable.ksxc_icon_map_dxc, com.example.guangdong_module.R.drawable.ksxc_icon_map_yxc, com.example.guangdong_module.R.drawable.ksxc_icon_map_ljxc};
    private final String WORK_ORDER_ID = "workorderid";
    private String workOrderId;
    private TaskBean taskBean;
    private RouteListBean routeListBean;
    private Boolean isCompleteOfTaskBean;
    private ArcgisLayout arcgisLayout;
    private List<Point> walkPoints;

    private PatrolRateView patrolRateView;
    private DiscreteScrollView partPicker;
    private MapView mapView;
    private ForecastView forecastView;
    private PartCardAdapter partCardAdapter;

    //  标准路线图层
    private GraphicsOverlay standardRouteOverlay;
    private IPoly standardPloy;
    // 实际路线图层
    private GraphicsOverlay actualRouteOverlay;
    private GraphicsOverlay routePositionOverlay;
    private GraphicsOverlay locationOverlay;
    private GraphicsOverlay textOverlay;

    private List<RoutePosition> routePositions;
    private List<Graphic> routPositionsGraphics;

    private List<Point> exeline;
    private GaodeHelper gaodeHelper;

    private ImageView locationImg;

    private Timer timer;
    private PictureMarkerSymbol bluePictureMarkerSymbol;
    private PictureMarkerSymbol greenPictureMarkerSymbol;
    private PictureMarkerSymbol yellowPictureMarkerSymbol;

    private PatrolUpControlView patrolUpControlView;
    private PatrolSubmitDialog patrolSubmitDialog;

    @Override
    public int getLayoutId() {
        return R.layout.activity_patrol_map_control;
    }

    @Override
    public void initData() {
        EventBus.getDefault().register(this);
        deviceIsMiuiTip();

        exeline = new ArrayList<>();

        Intent intent = getIntent();
        if (intent != null) {
            workOrderId = intent.getStringExtra(WORK_ORDER_ID);
            taskBean = SqlManager.getInstance().queryTaskSqlByWorkOrderId(workOrderId);
            routeListBean = SqlManager.getInstance().queryRouteSqlByWorkOrderId(workOrderId);
            routePositions = SqlManager.getInstance().queryRoutePositionsByWorkid(workOrderId);
            isCompleteOfTaskBean = taskBean.isHasExecuted();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(PatroltemEvent patrolWrap) {
        int patrolIndex = patrolWrap.getPatrolIndex();
        int troutIndex = patrolWrap.getPosition();
        TaskItemBean taskItemBean = patrolWrap.getTaskItemBean();
        //刷新弹层数据
        patrolUpControlView.controlCallback(troutIndex, taskItemBean);
        //刷新滑片数据
        updatePicker(patrolIndex);
    }

    //提交工单成功
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void submitCallBack(SubmitTaskWrap submitTaskWrap) {
        finish();
    }

    /**
     * //刷新滑片数据
     *
     * @param patrolIndex
     */
    private void updatePicker(int patrolIndex) {
        partPicker.smoothScrollToPosition(patrolIndex);
        partCardAdapter.notifyItemChanged(patrolIndex);
        patrolRateView.updateRateView();
        forecastView.setForecast(routePositions.get(patrolIndex));
    }

    @Override
    public void initView() {

        showBack();
        timer = new Timer();

        vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        patrolSubmitDialog = new PatrolSubmitDialog(getContext(), workOrderId);
        locationImg = findViewById(R.id.img_location);
        locationImg.setVisibility(View.GONE);
//        locationImg.setOnClickListener(v -> );
        patrolRateView = findViewById(R.id.view_patrol_rate);
        patrolRateView.setWorkOrderId(workOrderId, isCompleteOfTaskBean);

        forecastView = findViewById(R.id.view_forecast);
        arcgisLayout = findViewById(R.id.arcgis_layout);
        mapView = arcgisLayout.getMapView();

//        mMapLayout = new MapManager.Builder(getContext(), mapView)
//                .setArcGISMap(Basemap.Type.TOPOGRAPHIC, 34.056295, -117.195800, 16)
//                .setShowMapCenter(true)
//                .build();

        bluePictureMarkerSymbol = arcgisLayout.createPictureMarkerSymbol(picIds[0]);
        greenPictureMarkerSymbol = arcgisLayout.createPictureMarkerSymbol(picIds[1]);
        yellowPictureMarkerSymbol = arcgisLayout.createPictureMarkerSymbol(picIds[2]);

        partPicker = findViewById(R.id.part_picker);
        partPicker.setSlideOnFling(true);
        partCardAdapter = new PartCardAdapter();
        partCardAdapter.setOnItemClickListener(this);
        partPicker.setAdapter(partCardAdapter);
        partPicker.addOnItemChangedListener(this);
        partPicker.addScrollStateChangeListener(this);
        partPicker.setOrientation(DSVOrientation.HORIZONTAL);
        partPicker.setItemTransitionTimeMillis(400);
        partPicker.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());

        patrolUpControlView = new PatrolUpControlView(getContext(), isCompleteOfTaskBean, routePositions);
        patrolUpControlView.setUpdatePatrolPickListener(this::updatePicker);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mapView.setViewpointScaleAsync(4000).addDoneListener(() -> {
                    runOnUiThread(() -> {
                        if (!CollectionsUtil.isEmpty(routePositions)) {
                            initMap();
                            forecastView.setForecast(routePositions.get(0));
                            partCardAdapter.setNewData(routePositions);
                            if (!isCompleteOfTaskBean) {
                                startLocation();
                            }
                            setRoutePosition(picIds[0]);
                            initStandardRoute();
                            setArcgisMapClick();
                            drawLineOnMap();
                        }
                    });
                });
            }
        }, 800);


    }

    @Override
    protected void initListener() {
        patrolRateView.setClickListener(v -> {
            new XPopup.Builder(getContext())
                    .hasStatusBarShadow(true) //启用状态栏阴影
                    .dismissOnTouchOutside(false)
                    .asCustom(patrolSubmitDialog)
                    .show();
        });
    }

    @Override
    protected void initRequestData() {

    }

    @Override
    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder holder, int position) {
        if (holder != null) {
            forecastView.setForecast(routePositions.get(position));
            RoutePosition routePosition = routePositions.get(position);
            arcgisLayout.setCenterPoint(routePosition.parasePoint());
            twinkPoint(routPositionsGraphics.get(position));
        }
    }

    @Override
    public void onScrollStart(@NonNull RecyclerView.ViewHolder currentItemHolder, int adapterPosition) {

    }

    @Override
    public void onScrollEnd(@NonNull RecyclerView.ViewHolder holder, int position) {
    }

    @Override
    public void onScroll(float position, int currentIndex, int newIndex, @Nullable RecyclerView.ViewHolder currentHolder, @Nullable RecyclerView.ViewHolder newCurrent) {
        RoutePosition current = routePositions.get(currentIndex);
        if (newIndex >= 0 && newIndex < partCardAdapter.getItemCount()) {
            RoutePosition next = routePositions.get(newIndex);
            forecastView.onScroll(1f - Math.abs(position), current, next);
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        new XPopup.Builder(getContext())
                .setPopupCallback(new PatrolPickerCallback(patrolUpControlView, position))
                .asCustom(patrolUpControlView)
                .show();

    }

    private void startLocation() {
        gaodeHelper = new GaodeHelper(this, PatrolMapControlActivity.class, locationOverlay, this);
        //android 6.0动态申请权限
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
            }
        } else {
            gaodeHelper.startLocation();
        }
    }

    /**
     * 第一次进界面添加标准路线和待巡查点
     */
    private void initStandardRoute() {
        //第一次进界面添加标准路线和待巡查点
        if (routeListBean != null) {
            String routeContent = routeListBean.getRouteContent();
            List<Point> result = new ArrayList();
            try {
                List<Double[]> routes = JSON.parseArray(routeContent, Double[].class);
                for (Double[] route : routes) {
                    Point point = new Point(route[0], route[1], SpatialReference.create(4326));
                    result.add(point);
                }
                int color = ContextCompat.getColor(getContext(), com.example.guangdong_module.R.color.route_color_one);
                arcgisLayout.addPolylineToGraphicsOverlay(standardRouteOverlay, result, SimpleLineSymbol.Style.SOLID, color, 4);

//                if (!CollectionsUtil.isEmpty(result)) {
//                    Map map = new HashMap();
//                    arcgisLayout.addPicToGraphicsOverlay(standardRouteOverlay, com.example.guangdong_module.R.mipmap.ic_me_history_startpoint, result.get(0), map);
//                    arcgisLayout.addPicToGraphicsOverlay(standardRouteOverlay, com.example.guangdong_module.R.mipmap.ic_me_history_finishpoint, result.get(result.size() - 1), map);
//                }
//                arcgisLayout.setMapViewVisibleExtent(result);
            } catch (Exception e) {

            }
        }
    }

    /**
     * 设置地图点击事件
     */
    private void setArcgisMapClick() {
        arcgisLayout.setOnMapViewClickListener((e, mapView) -> {
            android.graphics.Point screenPoint = new android.graphics.Point(Math.round(e.getX()), Math.round(e.getY()));
            ListenableFuture<List<IdentifyGraphicsOverlayResult>> listListenableFuture = mapView.identifyGraphicsOverlaysAsync(screenPoint, 5, false);
            try {
                List<IdentifyGraphicsOverlayResult> overlayResults = listListenableFuture.get();
                if (overlayResults != null && overlayResults.size() > 0) {
                    List<Graphic> graphics = overlayResults.get(0).getGraphics();
                    if (!graphics.isEmpty()) {
                        Graphic graphic = graphics.get(0);
                        Map<String, Object> attributes = graphic.getAttributes();
                        if (!attributes.isEmpty()) {
                            int index = (int) attributes.get("index");
                            partPicker.smoothScrollToPosition(index);
                            arcgisLayout.setCenterPoint(routePositions.get(index).parasePoint());
                            twinkPoint(graphic);
                        }
                    }
                }
            } catch (Exception e1) {

            }
        });
    }


    /**
     * 选中点闪烁,让其它点不闪烁
     *
     * @param graphic
     */
    private TimerTask timerTask;
    private Graphic yellowGraphic;

    private void twinkPoint(@NonNull Graphic graphic) {
        if (this.yellowGraphic != null) {
            this.yellowGraphic.setSymbol(bluePictureMarkerSymbol);
        }
        if (timerTask != null) {
            this.yellowGraphic.setVisible(true);
            timerTask.cancel();
            timerTask = null;
        }
        this.yellowGraphic = graphic;
        this.yellowGraphic.setSymbol(yellowPictureMarkerSymbol);
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    yellowGraphic.setVisible(!graphic.isVisible());
                });
            }
        };
        timer.schedule(timerTask, 100, 500);
    }


    private void drawLineOnMap() {
        if (taskBean == null) {
            return;
        }
        if (routeListBean == null || routeListBean.getItemListByworkid(workOrderId) == null) {
            return;
        }
        arcgisLayout.removeGraphics();
        arcgisLayout.post(() -> {
            if (!TextUtils.isEmpty(taskBean.getWorkOrderRoute())) {
                if (isCompleteOfTaskBean) {
                    //已完成状态时
                    drawCompleteLine(taskBean.getWorkOrderRoute());
                } else {
                    drawNotCompleteLine();
                }
            } else {
                drawNotCompleteLine();
            }
        });
    }

    /**
     * 已完成工单画线
     *
     * @param
     */
    private void drawCompleteLine(String workOrderRoute) {
        if (TextUtils.isEmpty(workOrderRoute)) {
            return;
        }
        String temp = workOrderRoute.replaceAll("\\{", "[").replaceAll("\\}", "]");
        List<Double[]> list = JSON.parseArray(temp, Double[].class);
        if (list != null && list.size() >= 2) {
            exeline.clear();
            for (Double[] bean : list) {
                Point point1 = new Point(bean[0], bean[1], SpatialReference.create(4326));
                exeline.add(point1);
            }
            int color = ContextCompat.getColor(getContext(), com.example.guangdong_module.R.color.route_color_two);
            arcgisLayout.addPolyline(exeline, SimpleLineSymbol.Style.SOLID, color, 4);
            arcgisLayout.addPic(com.example.guangdong_module.R.mipmap.ic_me_history_startpoint, exeline.get(0), new HashMap<>());
            arcgisLayout.addPic(com.example.guangdong_module.R.mipmap.ic_me_history_finishpoint, exeline.get(exeline.size() - 1), new HashMap<>());
            arcgisLayout.setMapViewVisibleExtent(exeline);
        }
    }

    /**
     * 未完成工单划线
     */
    private void drawNotCompleteLine() {
        List<RoutepointDataBean> list = RoutepointDataManager.getInstance().getRoutePointListWithOutD(workOrderId);
        if (list != null && list.size() >= 2) {
            exeline.clear();
            for (RoutepointDataBean bean : list) {
                Point point1 = bean.parasePoint();
                exeline.add(point1);
            }
            try {
                arcgisLayout.setMapViewVisibleExtent(exeline);
                int color = ContextCompat.getColor(getContext(), com.example.guangdong_module.R.color.route_color_two);

                arcgisLayout.addPolyline(exeline, SimpleLineSymbol.Style.SOLID, color, 4);
                if (!CollectionsUtil.isEmpty(exeline)) {
                    arcgisLayout.addPic(com.example.guangdong_module.R.mipmap.ic_me_history_startpoint, exeline.get(0));
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * 设置巡查点图标
     */
    private void setRoutePosition(int imgRes) {
        if (!CollectionsUtil.isEmpty(routePositions)) {
            for (int i = 0; i < routePositions.size(); i++) {
                try {
                    RoutePosition routePosition = routePositions.get(i);
                    Point point = routePosition.parasePoint();
                    Map<String, Object> attrs = new HashMap<>();
                    attrs.put("index", i);
                    arcgisLayout.setTextMarker(point, textOverlay, routePosition.getStructureName(), R.color.text_map_bg, attrs, 28);
                    arcgisLayout.addPicToGraphicsOverlay(routePositionOverlay, imgRes, point, attrs);
                } catch (Exception e) {

                }
            }
            routPositionsGraphics = routePositionOverlay.getGraphics();
        }
    }

    /**
     * 搜索附近的巡查点
     *
     * @param mRoutePosition 巡查点集合
     * @param mPoint         当前点
     */
    private Vibrator vibrator;
    private int lastIndex = -1;

    private void searchMinRoutePoint(Point mPoint) {
        List<Double> distanceList = new ArrayList<>();
        int index = -1;
        for (RoutePosition routePosition : routePositions) {
            Point point = routePosition.parasePoint();
            double distance = ArcgisUtils.distancePoints(point, mPoint);
            routePosition.setDistance(distance);
            distanceList.add(distance);
        }
        if (!distanceList.isEmpty()) {
            Double min = Collections.min(distanceList);
            //保存500米之内最近的巡查点 min单位是千米
            if (min < 1) {
                index = distanceList.lastIndexOf(min);
            }
        }

        if (index != -1) {
            if (lastIndex != index) {
                if (vibrator.hasVibrator()) {
                    vibrator.vibrate(1000);
                }
                if (lastIndex != -1) {
                    routePositions.get(lastIndex).setIsNear(0);
                    partCardAdapter.notifyItemChanged(lastIndex);
                }

                partPicker.smoothScrollToPosition(index);
                //临近时修改卡片背景图
                routePositions.get(index).setIsNear(1);
                partCardAdapter.notifyItemChanged(index);
                lastIndex = index;
            }
        } else {
            if (lastIndex != -1) {
                //远离时修改卡片背景图
                routePositions.get(lastIndex).setIsNear(0);
                partCardAdapter.notifyItemChanged(lastIndex);
            }
        }
    }

    private void initMap() {
//        if (defaultReservoir!=null){
//            String reservoirCode = defaultReservoir.getReservoirCode();
//            String localTpkPath = Environment.getExternalStorageDirectory() + "/SKXCDownloads/tpk/"+reservoirCode+".tpk";
//            TileCache tileCache = new TileCache(localTpkPath);
//            ArcGISTiledLayer mArcGISTiledLayer = new ArcGISTiledLayer(tileCache);
//            arcGISMap.getOperationalLayers().add(mArcGISTiledLayer);
//        }
        //定位图层
        locationOverlay = arcgisLayout.getLocationOverlay();
        //标准路线
        standardRouteOverlay = new GraphicsOverlay();
        //实际路线
        actualRouteOverlay = new GraphicsOverlay();
        //巡查点
        routePositionOverlay = new GraphicsOverlay();
        //巡查点文字
        textOverlay = new GraphicsOverlay();
        mapView.getGraphicsOverlays().add(standardRouteOverlay);
        mapView.getGraphicsOverlays().add(actualRouteOverlay);
        mapView.getGraphicsOverlays().add(routePositionOverlay);
        mapView.getGraphicsOverlays().add(textOverlay);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gaodeHelper != null) {
            gaodeHelper.stopLocation();
            gaodeHelper.closeLocation();
        }
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void locationCallback(Point point) {
        searchMinRoutePoint(point);
        int length = exeline.size();
        if (length > 0) {
            if (length == 1) {
                arcgisLayout.addPic(com.example.guangdong_module.R.mipmap.ic_me_history_startpoint, exeline.get(0));
            }
            Point beforPoint = exeline.get(length - 1);
            if (beforPoint.getX() != point.getX() || beforPoint.getY() != point.getY()) {
                arcgisLayout.addPolylineToGraphicsOverlayNew(actualRouteOverlay, beforPoint, point, SimpleLineSymbol.Style.SOLID, R.color.route_color_two, 4);
                exeline.add(point);
                RoutepointDataManager.getInstance().addPoint(new RoutepointDataBean(workOrderId, point.getX() + "", point.getY() + ""));
            }
        } else {
            exeline.add(point);
            RoutepointDataManager.getInstance().addPoint(new RoutepointDataBean(workOrderId, point.getX() + "", point.getY() + ""));
        }
        //给操作taskitembean传定位坐标，便于提交给后台
        patrolUpControlView.setLocationPoint(exeline.get(exeline.size() - 1));
    }

    private void deviceIsMiuiTip() {
        boolean is_xiaomi = OSUtils.ROM_TYPE.MIUI.name().equals(OSUtils.getRomType().name());

        boolean hasset = SPUtils.getInstance(ResUtils.getContext()).getBoolean("go_set", false);

        if (is_xiaomi && !hasset) {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
            builder.setTitle(R.string.xiaomiMind);
            builder.setMessage(R.string.whiteCard);
            builder.setCancelable(false);
            builder.setPositiveButton(R.string.go_set, (dialog, which) -> {
                SPUtils.getInstance(ResUtils.getContext()).putBoolean("go_set", true);
                XiaomiDeviceUtil.toConfigApp(PatrolMapControlActivity.this, XiaomiDeviceUtil.getAppProcessName(ResUtils.getContext()), getString(R.string.app_name));
            });
            builder.setNegativeButton(R.string.cancel, (dialog, which) -> finish());
            builder.create().show();
        }
    }

}
