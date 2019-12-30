package com.tepia.guangdong_module.amainguangdong.xunchaview.activity;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocationClientOption;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.WebTiledLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.IdentifyGraphicsOverlayResult;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.util.ListenableList;
import com.example.gaodelibrary.GPSUtil;
import com.example.gaodelibrary.GaodeEntity;
import com.example.guangdong_module.R;
import com.example.guangdong_module.databinding.ActivityStartInspectionBinding;
import com.google.gson.Gson;
import com.tepia.base.CacheConsts;
import com.tepia.base.ConfigConsts;
import com.tepia.base.http.LoadingSubject;
import com.tepia.base.mvp.MVPBaseActivity;
import com.tepia.base.utils.DoubleClickUtil;
import com.tepia.base.utils.LogUtil;
import com.tepia.base.utils.NetUtil;
import com.tepia.base.utils.OSUtils;
import com.tepia.base.utils.ResUtils;
import com.tepia.base.utils.ScreenUtil;
import com.tepia.base.utils.ToastUtils;
import com.tepia.base.utils.Utils;
import com.tepia.base.utils.XiaomiDeviceUtil;
import com.tepia.base.view.arcgisLayout.ArcgisLayout;
import com.tepia.base.view.arcgisLayout.GoogleMapLayer;
import com.tepia.base.view.customScrollView.ContentRecyclerView;
import com.tepia.base.view.customScrollView.ScrollLayout;
import com.tepia.base.view.dialog.permissiondialog.Px2dpUtils;
import com.tepia.base.view.floatview.CollectionsUtil;
import com.tepia.guangdong_module.amainguangdong.common.CrashHandler;
import com.tepia.guangdong_module.amainguangdong.common.TjDialogFragment;
import com.tepia.guangdong_module.amainguangdong.common.UserManager;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.ReservoirBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.RouteListBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.RoutePosition;
import com.tepia.guangdong_module.amainguangdong.mvp.taskdetail.TaskDetailContract;
import com.tepia.guangdong_module.amainguangdong.mvp.taskdetail.TaskDetailPresenter;
import com.tepia.guangdong_module.amainguangdong.mvp.taskdetail.TaskManager;
import com.tepia.guangdong_module.amainguangdong.route.RoutepointDataBean;
import com.tepia.guangdong_module.amainguangdong.route.RoutepointDataManager;
import com.tepia.guangdong_module.amainguangdong.route.TaskBean;
import com.tepia.guangdong_module.amainguangdong.route.TaskDetailResponse;
import com.tepia.guangdong_module.amainguangdong.route.TaskItemBean;
import com.tepia.guangdong_module.amainguangdong.utils.ArcgisUtils;
import com.tepia.guangdong_module.amainguangdong.utils.EmptyLayoutUtil;
import com.tepia.guangdong_module.amainguangdong.xunchaview.adapter.AdapterTaskItemListNew;
import com.tepia.photo_picker.utils.SPUtils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by      Android studio
 *
 * @author :wwj (from Center Of Wuhan)
 * Date    :2019/5/6
 * update   :2019-5-6 21:46 by ly
 * Version :1.0
 * 功能描述 : 开始巡查
 **/
public class StartInspectionActivity extends MVPBaseActivity<TaskDetailContract.View, TaskDetailPresenter> implements TaskDetailContract.View {
    ActivityStartInspectionBinding mBinding;

    String workOrderId;
    TaskBean taskBean;
    private Point currentPoint;
    private boolean isFirstInitMap = false;
    private ArrayList<Point> exeline2;
    private Point positionPoint;

    AdapterTaskItemListNew adapterTaskItemList;
    private String userCode;
    private String reservoirId;
    private RouteListBean routeListBean;
    private MapView mapView;
    private List<RoutePosition> routePositions;
    /**
     * 已巡查点
     */
    private List<RoutePosition> yxcRoutePositions;
    /**
     * 待巡查点
     */
    private List<RoutePosition> dxcRoutePositions;

    private boolean isFirstLocation = true;
    private GraphicsOverlay locationOverlay;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    /**
     * 标准路线图层
     */
    private GraphicsOverlay standardRouteOverlay;
    /**
     * 实际路线图层
     */
    private GraphicsOverlay actualRouteOverlay;
    private GraphicsOverlay dxcOverlay;
    private GraphicsOverlay yxcOverlay;
    private GraphicsOverlay ljxcOverlay;
    private RoutePosition nearbyRoutePosition;

    private int[] picIds = {R.drawable.ksxc_icon_map_dxc, R.drawable.ksxc_icon_map_yxc, R.drawable.ksxc_icon_map_ljxc};
    private ArcgisLayout alMapview;
    private boolean isStartPatrol;

    String startTime;
    ReservoirBean defaultReservoir;
    String routeName;

    String noticeStr = "巡检项已全部提交，最后的工单提交失败，请重试";
    /**
     * 记录上一个巡查点，避免列表一直刷新
     */
    private RoutePosition lastRoutePosition;
    /**
     * 是否开启自动查找定位点功能
     */
    private boolean isOpenAuto;
    /**
     * 工单是否完成
     */
    private boolean isCompleteOfTaskBean = false;
    private LinearLayout llDxc;
    private LinearLayout llLjxc;
    private GraphicsOverlay textGraphics;

    private int mapHeight;
    private ScrollLayout scroll_down_layout;
    private TextView text_foot;
    private ContentRecyclerView rvStartInspection;
    private Vibrator vibrator;
    private ImageView viewById;

    public boolean isCompleteOfTaskBean() {
        return isCompleteOfTaskBean;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_start_inspection;
    }

    @Override
    public void initData() {
        yxcRoutePositions = new ArrayList<>();
        dxcRoutePositions = new ArrayList<>();
        defaultReservoir = UserManager.getInstance().getDefaultReservoir();
        userCode = UserManager.getInstance().getUserCode();
        reservoirId = UserManager.getInstance().getReservoirId();
        mBinding = DataBindingUtil.bind(mRootView);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(ConfigConsts.Workorderid)) {
            workOrderId = getIntent().getStringExtra(ConfigConsts.Workorderid);
            taskBean = DataSupport.where("workOrderId=?", workOrderId).findFirst(TaskBean.class);
            if ("3".equals(taskBean.getExecuteStatus())) {
                userCode = taskBean.getUserCode();
            }
            startTime = taskBean.getStartTime();
            workOrderId = taskBean.getWorkOrderId();

            routeListBean = DataSupport.where("workorderid=?", workOrderId).findFirst(RouteListBean.class);
            LogUtil.i(StartInspectionActivity.class.getName(), "workOrderId:" + workOrderId);

            if (routeListBean == null) {
                workOrderId = " ";
                routeName = " ";
                startTime = " ";
                return;
            }
            routeName = routeListBean.getRouteName();

        }

        SPUtils.getInstance().putBoolean(CacheConsts.isOpenAuto, true);
        if (SPUtils.getInstance().getBoolean(CacheConsts.isOpenAuto, true)) {
            SPUtils.getInstance().putBoolean(CacheConsts.isOpenAuto, true);
            mBinding.checkbox.setChecked(true);
        } else {
            mBinding.checkbox.setChecked(false);
            SPUtils.getInstance().putBoolean(CacheConsts.isOpenAuto, false);
        }
        mBinding.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                SPUtils.getInstance().putBoolean(CacheConsts.isOpenAuto, true);

            } else {
                SPUtils.getInstance().putBoolean(CacheConsts.isOpenAuto, false);
            }
        });

        isCompleteOfTaskBean = stopClick();
    }

    /**
     * 刷新统计列表
     */
    public void refreshTitle() {
        if (isCompleteOfTaskBean) {
            List<TaskItemBean> taskItemBeanList = DataSupport.where("workorderid=? and executeResultType=? or workorderid=? and executeResultType=?",
                    workOrderId, "1", workOrderId, "3").find(TaskItemBean.class);
            mBinding.nearCheckLinearLy.setVisibility(View.GONE);
            mBinding.hasCheckLinearLy.setVisibility(View.GONE);
            mBinding.executeResultTypeLinearLy.setVisibility(View.VISIBLE);

            mBinding.executeResultTypeTv.setText("" + taskItemBeanList.size());
        } else {
            mBinding.nearCheckLinearLy.setVisibility(View.VISIBLE);
            mBinding.hasCheckLinearLy.setVisibility(View.VISIBLE);
            mBinding.executeResultTypeLinearLy.setVisibility(View.GONE);

            int notnum = routeListBean.getItemListHasNotCheck(workOrderId, "0");
            int hasnumber = routeListBean.getItemListHasNotCheck(workOrderId, "1");
            mBinding.nearCheckTv.setText(notnum + "");
            mBinding.hasCheckTv.setText(hasnumber + "");

            if (notnum == 0) {
                mBinding.btComplete.setVisibility(View.VISIBLE);
            } else {
                mBinding.btComplete.setVisibility(View.GONE);

            }
        }
    }

    @Override
    public void initView() {
        CrashHandler.getInstance().init(this);
        vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        LinearLayout llMapDescribe = findViewById(R.id.ll_map_describe);
        scroll_down_layout = findViewById(R.id.scroll_down_layout);
        text_foot = findViewById(R.id.text_foot);
        llDxc = llMapDescribe.findViewById(R.id.ll_dxc);
        llLjxc = llMapDescribe.findViewById(R.id.ll_ljxc);

        viewById = findViewById(R.id.iv_alarm);
        rvStartInspection = findViewById(R.id.rv_start_inspection);

        showTitle();

        if (routeListBean == null) {
            return;
        }
        refreshTitle();

        if (isCompleteOfTaskBean) {
            viewById.setVisibility(View.GONE);
            mBinding.checkbox.setVisibility(View.INVISIBLE);
            mBinding.btComplete.setVisibility(View.GONE);
            rvStartInspection.setEnabled(false);
        }

        rvStartInspection.setLayoutManager(new LinearLayoutManager(this));
        adapterTaskItemList = new AdapterTaskItemListNew(R.layout.rv_start_inspection_item, null, this);
        rvStartInspection.setAdapter(adapterTaskItemList);

        emptviewShow();

        initMap();
        initListen();
    }

    /**
     * 标题展示
     */
    private void showTitle() {
        if (defaultReservoir != null) {
            String executorName = taskBean.getExecutorName();
            String roleName = taskBean.getRoleName();
            if (!TextUtils.isEmpty(roleName)) {
                mBinding.executorNameTv.setVisibility(View.VISIBLE);
                mBinding.executorNameTv.setText("巡查人：" + executorName + "（" + roleName + "）");
            } else {
                if (isCompleteOfTaskBean) {
                    mBinding.executorNameTv.setVisibility(View.VISIBLE);

                } else {
                    mBinding.executorNameTv.setVisibility(View.GONE);
                }
                mBinding.executorNameTv.setText("巡查人：" + executorName);

            }
            mBinding.titleTv.setText(defaultReservoir.getReservoir() + startTime + routeName);
        }

        List<TaskItemBean> itemList = DataSupport.where("workorderid=?",
                workOrderId).find(TaskItemBean.class);
        if (!CollectionsUtil.isEmpty(itemList)) {
            int totalCheckNum = itemList.size();
            mBinding.benciTotalTv.setText(totalCheckNum + "");

        }
    }

    /**
     * 判读recycleView列表显示什么
     */
    private void emptviewShow() {
        if (!isCompleteOfTaskBean) {
            adapterTaskItemList.setEmptyView(EmptyLayoutUtil.showTop("附近没有巡查点，请按上方地图路线" + "\n" + "对各巡查点进行检查"));
            int notnum = routeListBean.getItemListHasNotCheck(workOrderId, "0");
            if (notnum == 0) {
                adapterTaskItemList.setEmptyView(EmptyLayoutUtil.showTop("巡检已完成，可以直接点击按钮提交"));
            }

            List<TaskItemBean> localTaskItemBeanList = adapterTaskItemList.getLocalData(workOrderId);
            if (CollectionsUtil.isEmpty(localTaskItemBeanList)) {
                if (taskBean.getExecuteStatus().equals("2")) {
                    adapterTaskItemList.setEmptyView(EmptyLayoutUtil.showTop(noticeStr));

                }
            }
        }
    }

    /**
     * 初始化各类监听
     */
    private void initListen() {
        viewById.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(StartInspectionActivity.this, DangerReportActivity.class);
            startActivity(intent);
        });

        adapterTaskItemList.setOnGetCurrentClickListener(taskItemBean -> {
            if (currentPoint != null) {
                taskItemBean.setExcuteLatitude(currentPoint.getY() + "");
                taskItemBean.setExcuteLongitude(currentPoint.getX() + "");
            }
            //刷新待巡查点和已巡查点
            setYxcListAndDxcList();
            setRoutePosition(dxcRoutePositions, picIds[0], dxcOverlay);
            setRoutePosition(yxcRoutePositions, picIds[1], yxcOverlay);
        });

        mBinding.btComplete.setOnClickListener(v -> {
            int notnum = routeListBean.getItemListHasNotCheck(workOrderId, "0");
            if (notnum == 0) {

                if (DoubleClickUtil.isFastDoubleClick()) {
                    return;
                }
                showTjDialog();
            } else {
                ToastUtils.shortToast("还有待完成项，请继续巡查");
            }
        });
        ViewTreeObserver viewTreeObserver = alMapview.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                alMapview.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mapHeight = alMapview.getHeight();
//                Log.i("距离顶部的高度:",viewRect.toString());
////                Log.i("屏幕高度:",""+ ScreenUtil.getScreenHeightPix(mContext));
                Rect viewRect = new Rect();
                mBinding.checkbox.getGlobalVisibleRect(viewRect);
                int checkbox_top = viewRect.top;
                initScrollLayout(checkbox_top);
            }
        });
    }

    private boolean isWatingForUpload() {

        List<TaskItemBean> itemList = DataSupport.where("workorderid=? and completestatus=?",
                taskBean.getWorkOrderId(), "0").find(TaskItemBean.class);
        if (CollectionsUtil.isEmpty(itemList)) {
            showAlertOfWhenisWatingForUpload();
            return true;
        } else {
            return false;
        }
    }

    private void showAlertOfWhenisWatingForUpload() {
        new AlertDialog.Builder(this)
                .setMessage("此次巡查工作已完成，是否提交巡查结果?")
                .setCancelable(false)
                .setPositiveButton("提交结果", (dialog, which) -> {

                    if (DoubleClickUtil.isFastDoubleClick()) {
                        return;
                    }
                    showTjDialog();
                })
                .setNegativeButton("继续巡查", (dialog, which) -> {
                    if (DoubleClickUtil.isFastDoubleClick()) {
                        return;
                    }
                    startLocation();
                }).show();
    }

    private void startLocation() {
        getGaoDeLocation();
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
                //                gaodeEntity.startLocation();
                //开始轨迹
                gaodeEntity.startTrace();
                //                gaodeEntity.setTransportMode(2000);
            }
        }
    }

    private Boolean isMoveUp = false;
    private Boolean isMoveDown = true;

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
//                    mBinding.checkbox
                    float translationY1 = mBinding.checkbox.getTranslationY();
                    ObjectAnimator animator = ObjectAnimator.ofFloat(mBinding.checkbox, "translationY", -mapHeight / 2, 0);
                    animator.setDuration(500);
                    animator.start();
                }
                isMoveUp = false;
            } else if (currentStatus == ScrollLayout.Status.OPENED) {
                if (!isMoveUp) {
                    moveMap(true);
                    isMoveUp = true;
//                    float translationY1 = mBinding.checkbox.getTranslationY();
                    ObjectAnimator animator = ObjectAnimator.ofFloat(mBinding.checkbox, "translationY", 0, -mapHeight / 2);
                    animator.setDuration(300);
                    animator.start();
                }
                isMoveDown = false;
            }
        }

        @Override
        public void onChildScroll(int top) {

        }
    };

    private void moveMap(Boolean status) {
        android.graphics.Point screenPoint = new android.graphics.Point(0, 0);
        if (mapView.screenToLocation(screenPoint) != null) {
            double mapScale = mapView.getMapScale();
            if (status) {
                mapView.setViewpointScaleAsync(mapScale * 0.5).addDoneListener(() -> {
                    double y = mapView.screenToLocation(screenPoint).getY();
                    android.graphics.Point bottomPoint = new android.graphics.Point(0, mapHeight);
                    double bottomY = mapView.screenToLocation(bottomPoint).getY();
                    double translationY = (bottomY - y) / 2 - (bottomY - y) / 4;
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
                double translationY = (bottomY - y) / 2 - (bottomY - y) / 4;
//                mapView.visibleArea   返回一个Polygon，表示当前在MapView中可见的ArcGISMap区域。
                Viewpoint viewPoint = mapView.getCurrentViewpoint(Viewpoint.Type.CENTER_AND_SCALE);
                Point centerPoint = viewPoint.getTargetGeometry().getExtent().getCenter();
                mapView.setViewpointScaleAsync(mapScale * 2).addDoneListener(() -> {
                    //下移
                    mapView.setViewpointCenterAsync(new Point(centerPoint.getX(), centerPoint.getY() - translationY));
                });
            }
        }
    }

    private void initScrollLayout(int checkBoxTop) {
        LinearLayout llListTitle = findViewById(R.id.ll_list_title);
        //设置列表滑动布局
        //关闭状态时最上方预留高度
        scroll_down_layout.setMinOffset(Px2dpUtils.dip2px(getContext(), 100));
        //打开状态时内容显示区域的高度
        scroll_down_layout.setMaxOffset(mapHeight / 2);
        //最低部退出状态时可看到的高度，0为不可见
        int height = mBinding.checkbox.getHeight();
        scroll_down_layout.setExitOffset(ScreenUtil.getScreenHeightPix(getContext()) - checkBoxTop - Px2dpUtils.dip2px(getContext(), 15) - ScreenUtil.getStatusBarHeight() - height);
        //解决recycleView底部显示不全的问题
//        rvStartInspection.setPadding(0, 0, 0, Px2dpUtils.dip2px(getContext(), 100));
        //是否支持下滑退出，支持会有下滑到最底部时的回调
        scroll_down_layout.setIsSupportExit(true);
        //是否支持横向滚动
        scroll_down_layout.setAllowHorizontalScroll(true);
        scroll_down_layout.setOnScrollChangedListener(mOnScrollChangedListener);
        scroll_down_layout.setToExit();
        scroll_down_layout.getBackground().setAlpha(0);
        text_foot.setOnClickListener(v -> scroll_down_layout.scrollToOpen());
        llListTitle.setOnClickListener(v -> {
        });
//        new Handler().postDelayed(() -> {
//            scroll_down_layout.scrollToOpen();
//        },2000);
    }

    private boolean stopClick() {
        TaskBean taskBean = DataSupport.where("workOrderId=?", workOrderId).findFirst(TaskBean.class);
        if (taskBean != null) {
            if ("3".equals(taskBean.getExecuteStatus())) {
                return true;
            }
        }
        return false;

    }

    /**
     * 初始化地图相关方法
     */
    private void initMap() {
        initAndSetMapView();

        if (!isCompleteOfTaskBean) {
            boolean isWatingForUpload = isWatingForUpload();
            if (!isWatingForUpload) {
                startLocation();
            }
        } else {
            //隐藏地图描述
            llDxc.setVisibility(View.GONE);
            //隐藏地图图标
            dxcOverlay.setVisible(false);
        }


        initStandardRoute();
        setArcgisMapClick();
        refreshMapView();
        newTaskTimeOfNearPosition();
    }

    private void initAndSetMapView() {
        exeline2 = new ArrayList<>();
        alMapview = findViewById(R.id.al_mapview);
        mapView = alMapview.getMapView();
        //google地图
        WebTiledLayer titleLayer = GoogleMapLayer.createWebTiteLayer(GoogleMapLayer.Type.VECTOR);
        //google影像图
        WebTiledLayer imgTitleLayer = GoogleMapLayer.createWebTiteLayer(GoogleMapLayer.Type.IMAGE);
        WebTiledLayer webTitleLayer = GoogleMapLayer.createWebTiteLayer(GoogleMapLayer.Type.IMAGE_ANNOTATION);
        Basemap basemap = new Basemap();
        ArcGISMap arcGISMap = new ArcGISMap(basemap);
        arcGISMap.getOperationalLayers().add(imgTitleLayer);
        arcGISMap.getOperationalLayers().add(webTitleLayer);
//        if (defaultReservoir!=null){
//            String reservoirCode = defaultReservoir.getReservoirCode();
//            String localTpkPath = Environment.getExternalStorageDirectory() + "/SKXCDownloads/tpk/"+reservoirCode+".tpk";
//            TileCache tileCache = new TileCache(localTpkPath);
//            ArcGISTiledLayer mArcGISTiledLayer = new ArcGISTiledLayer(tileCache);
//            arcGISMap.getOperationalLayers().add(mArcGISTiledLayer);
//        }
        arcGISMap.setMinScale(ArcgisLayout.minScale);
        Point point1 = new Point(1.1992433073197773E7, 4885139.106039485, SpatialReference.create(3857));
        Point point2 = new Point(1.3532164647994766E7, 2329702.2487083403, SpatialReference.create(3857));
        Envelope initEnvelope = new Envelope(point1, point2);
        arcGISMap.setInitialViewpoint(new Viewpoint(initEnvelope));
        mapView.setMap(arcGISMap);
        locationOverlay = alMapview.getLocationOverlay();
        //标准路线
        standardRouteOverlay = new GraphicsOverlay();
        //实际路线
        actualRouteOverlay = new GraphicsOverlay();
        //待巡查点
        dxcOverlay = new GraphicsOverlay();
        //已巡查点
        yxcOverlay = new GraphicsOverlay();
        //临近巡查点
        ljxcOverlay = new GraphicsOverlay();
        //巡查点文字
        textGraphics = new GraphicsOverlay();
        mapView.getGraphicsOverlays().add(standardRouteOverlay);
        mapView.getGraphicsOverlays().add(actualRouteOverlay);
        mapView.getGraphicsOverlays().add(dxcOverlay);
        mapView.getGraphicsOverlays().add(yxcOverlay);
        mapView.getGraphicsOverlays().add(ljxcOverlay);
        mapView.getGraphicsOverlays().add(textGraphics);
    }

    /**
     * 定时闪烁临近巡查点
     */
    private void newTaskTimeOfNearPosition() {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                isLjxcVisible = !isLjxcVisible;
                ljxcOverlay.setVisible(isLjxcVisible);
                //每隔1s循环执行run方法
                handler.postDelayed(this, 500);
            }
        };
        //延时一秒
        handler.postDelayed(runnable, 1000);
    }

    /**
     * 设置地图点击事件
     */
    private void setArcgisMapClick() {
        alMapview.setOnMapViewClickListener((e, mapView) -> {
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
            try {
                List<IdentifyGraphicsOverlayResult> overlayResults = listListenableFuture.get();
                if (overlayResults != null && overlayResults.size() > 0) {
                    List<Graphic> graphics = overlayResults.get(0).getGraphics();
                    if (graphics != null && graphics.size() > 0) {
                        Map<String, Object> attributes = graphics.get(0).getAttributes();
                        if (attributes != null) {
                            String positionId = (String) attributes.get("positionId");
                            if (routePositions != null && routePositions.size() > 0) {
                                for (RoutePosition routePosition : routePositions) {
                                    String positionId1 = routePosition.getPositionId();
                                    if (positionId.equals(positionId1)) {
                                        locationPoint = routePosition.parasePoint();
                                        searchNearbyRoutePosition(routePositions, locationPoint);
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e1) {

            }
            //刷新待巡查点和已巡查点
            setYxcListAndDxcList();
            setRoutePosition(dxcRoutePositions, picIds[0], dxcOverlay);
            setRoutePosition(yxcRoutePositions, picIds[1], yxcOverlay);

        });
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
//                mBinding.alMapview.addPolyline(exeline2, SimpleLineSymbol.Style.SOLID, Color.RED, 6);
                int color = ContextCompat.getColor(getContext(), R.color.route_color_one);
                alMapview.addPolylineToGraphicsOverlay(standardRouteOverlay, result, SimpleLineSymbol.Style.SOLID, color, 4);
                if (!CollectionsUtil.isEmpty(result)) {
                    Map map = new HashMap();
                    alMapview.addPicToGraphicsOverlay(standardRouteOverlay, R.mipmap.ic_me_history_startpoint, result.get(0), map);
                    alMapview.addPicToGraphicsOverlay(standardRouteOverlay, R.mipmap.ic_me_history_finishpoint, result.get(result.size() - 1), map);
                }
                alMapview.setMapViewVisibleExtent(result);
            } catch (Exception e) {

            }
            //第一次进界面添加待巡查点
            routePositions = routeListBean.getRoutePositionsByworkid(workOrderId);
            //刷新待巡查点和已巡查点
            setYxcListAndDxcList();
            setRoutePosition(dxcRoutePositions, picIds[0], dxcOverlay);
            setRoutePosition(yxcRoutePositions, picIds[1], yxcOverlay);
            //            searchNearbyRoutePosition(routePositions,locationPoint);
            //巡查点添加文字app/patrol/add
            if (routePositions != null && routePositions.size() > 0) {
                for (RoutePosition routePosition : routePositions) {
                    try {
                        List<TaskItemBean> itemList = DataSupport.where("usercode=? and reservoirid=? and workorderid=? and positionid=?"
                                , userCode, reservoirId, workOrderId, routePosition.getPositionId()).find(TaskItemBean.class);
                        Point point = routePosition.parasePoint();
                        int color = ContextCompat.getColor(getContext(), R.color.text_map_bg);
                        if (!CollectionsUtil.isEmpty(itemList)) {
                            TaskItemBean taskItemBean = itemList.get(0);
                            String positionTreeNames = taskItemBean.getPositionTreeNames();
                            Map<String, Object> attrs = new HashMap<>();
                            attrs.put("positionId", routePosition.getPositionId());
//                            ArcgisLayout.setTextMarker(point, textGraphics, positionTreeNames, color, attrs, 28);
                        }
                    } catch (Exception e) {

                    }
                }
            }
            setRoutePosition(yxcRoutePositions, picIds[1], yxcOverlay);
        }
    }

    private boolean isLjxcVisible;

    /**
     * 设置已巡查点集合和待巡查点集合
     */
    private void setYxcListAndDxcList() {
        List<RoutePosition> routePositionList = routePositions;
        dxcRoutePositions.clear();
        yxcRoutePositions.clear();
        if (routePositions != null && routePositions.size() > 0) {
            //得到待巡查点集合和已巡查点集合
            for (RoutePosition routePosition : routePositionList) {
                List<TaskItemBean> itemList = DataSupport.where("usercode=? and reservoirid=? and workorderid=? and positionid=?",
                        userCode, reservoirId, workOrderId, routePosition.getPositionId()).find(TaskItemBean.class);
                boolean isYxc = false;
                for (TaskItemBean taskItemBean : itemList) {
                    //0未完成
                    if ("0".equals(taskItemBean.getCompleteStatus())) {
                        dxcRoutePositions.add(routePosition);
                        isYxc = false;
                        break;
                    } else {
                        isYxc = true;
                    }
                }
                if (isYxc) {
                    yxcRoutePositions.add(routePosition);
                }
            }
        }
    }

    /**
     * 设置巡查点图标
     *
     * @param mRoutePosition
     */
    private void setRoutePosition(List<RoutePosition> mRoutePosition, int pid, GraphicsOverlay overlay) {
        overlay.getGraphics().clear();
        if (mRoutePosition != null && mRoutePosition.size() > 0) {
            for (RoutePosition routePosition : mRoutePosition) {
                try {
                    Point point = routePosition.parasePoint();
                    Map<String, Object> attrs = new HashMap<>();
                    attrs.put("positionId", routePosition.getPositionId());
                    alMapview.addPicToGraphicsOverlay(overlay, pid, point, attrs);
                } catch (Exception e) {

                }
            }
        }

    }

    private String nearDxcPositionStr = "";

    /**
     * 搜索附近的巡查点
     *
     * @param mRoutePosition 巡查点集合
     * @param mPoint         当前点
     */
    private void searchNearbyRoutePosition(List<RoutePosition> mRoutePosition, Point mPoint) {
        List<Double> distanceList = new ArrayList<>();
        if (mPoint != null) {
            if (mRoutePosition != null && mRoutePosition.size() > 0 && mPoint != null) {
                distanceList.clear();
                for (RoutePosition routePosition : mRoutePosition) {
                    try {
                        Point point = routePosition.parasePoint();
                        double distance = ArcgisUtils.distancePoints(point, mPoint);
                        routePosition.setDistance(distance);
                        distanceList.add(distance);
                    } catch (Exception ex) {
                        routePosition.setDistance(Integer.MAX_VALUE + 0.0);
                        distanceList.add(Integer.MAX_VALUE + 0.0);
                    }
                }
                if (distanceList != null && distanceList.size() > 0) {
                    Double min = Collections.min(distanceList);
                    int i = distanceList.lastIndexOf(min);
                    //保存500米之内最近的巡查点 min单位是千米
                    if (i < mRoutePosition.size() && min < 1) {
                        nearbyRoutePosition = mRoutePosition.get(i);
                    } else {
                        nearbyRoutePosition = null;
                    }
                } else {
                    nearbyRoutePosition = null;
                }
            }
            if (nearbyRoutePosition != null) {
                Point point = nearbyRoutePosition.parasePoint();
                Map<String, Object> attrs = new HashMap<>();
                attrs.put("positionId", nearbyRoutePosition.getPositionId());
//            ToastUtils.longToast("最近点距离:"+nearbyRoutePosition.getDistance()*1000+"米");
                ljxcOverlay.getGraphics().clear();
                alMapview.addPicToGraphicsOverlay(ljxcOverlay, picIds[2], point, attrs);
                isStartPatrol = true;
            } else {
                ljxcOverlay.getGraphics().clear();
            }
//            Graphic graphic = ljxcOverlay.getGraphics().get(0);
            refreshList(nearbyRoutePosition);

            List<Double> dcxDistanceList = new ArrayList<>();
            //靠近最近的待巡查点震动
            if (dxcRoutePositions != null && dxcRoutePositions.size() > 0 && mPoint != null) {
                dcxDistanceList.clear();
                for (RoutePosition routePosition : dxcRoutePositions) {
                    try {
                        Point point = routePosition.parasePoint();
                        double distance = ArcgisUtils.distancePoints(point, mPoint);
                        routePosition.setDistance(distance);
                        dcxDistanceList.add(distance);
                    } catch (Exception ex) {
                        routePosition.setDistance(Integer.MAX_VALUE + 0.0);
                        dcxDistanceList.add(Integer.MAX_VALUE + 0.0);
                    }
                }
                if (dcxDistanceList != null && dcxDistanceList.size() > 0) {
                    Double min = Collections.min(dcxDistanceList);
                    int i = dcxDistanceList.lastIndexOf(min);
                    //保存500米之内最近的巡查点 min单位是千米
                    if (i < dxcRoutePositions.size() && min < 1) {
                        RoutePosition routePosition = dxcRoutePositions.get(i);
                        String positionId = routePosition.getPositionId();
                        if (positionId != null) {
                            if (!nearDxcPositionStr.equals(positionId)) {
                                if (vibrator.hasVibrator()) {
                                    vibrator.vibrate(1000);
                                }
                                nearDxcPositionStr = positionId;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 刷新列表
     *
     * @param routePosition
     */
    private void refreshList(RoutePosition routePosition) {
        //是否刷新列表
        boolean isRefreshList = false;
        if (lastRoutePosition != null) {
            String positionId = lastRoutePosition.getPositionId();
            String nearPositionId = routePosition.getPositionId();
            try {
                if (!positionId.equals(nearPositionId)) {
                    isRefreshList = true;
                } else {
                    isRefreshList = false;
                }
            } catch (Exception e) {
                isRefreshList = true;
            }
        } else {
            lastRoutePosition = routePosition;
            isRefreshList = true;
        }
        if (routeListBean != null) {
            routePositions = routeListBean.getRoutePositionsByworkid(workOrderId);
            if (!CollectionsUtil.isEmpty(routePositions)) {
                if (routePosition != null) {
                    String positionId = routePosition.getPositionId();

                    List<TaskItemBean> taskItemBeans = DataSupport.where("positionid=? and userCode=? and reservoirId=? and workorderid=?",
                            positionId, userCode, reservoirId, workOrderId).find(TaskItemBean.class);
                    adapterTaskItemList.setNewData(taskItemBeans);
                    if (CollectionsUtil.isEmpty(taskItemBeans)) {
                        adapterTaskItemList.setEmptyView(EmptyLayoutUtil.showTop("暂无数据"));
                    }
                } else {
                    adapterTaskItemList.setNewData(null);
                    adapterTaskItemList.setEmptyView(EmptyLayoutUtil.showTop("附近没有巡查点，请按上方地图路线" + "\n" + "对各巡查点进行检查"));

                    int notnum = routeListBean.getItemListHasNotCheck(workOrderId, "0");
                    if (notnum == 0) {
                        adapterTaskItemList.setEmptyView(EmptyLayoutUtil.showTop("巡检已完成，可以直接点击按钮提交"));
                    }

                }

            }
        }
    }

    /**
     * 添加定位图标
     *
     * @param graphicsOverlay
     * @param id
     * @param point
     */
    Bitmap bitmapLocation;

    public void addLocationPic(GraphicsOverlay graphicsOverlay, int id, Point point) {
        PictureMarkerSymbol pictureMarkerSymbol1 = null;
        try {
            if (bitmapLocation == null) {
                bitmapLocation = BitmapFactory.decodeResource(getResources(), id);
            }
            if (bitmapLocation == null) {
                return;
            }
            pictureMarkerSymbol1 = PictureMarkerSymbol.createAsync(new BitmapDrawable(getResources(), bitmapLocation)).get();
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

    @Override
    protected void initListener() {
        boolean is_xiaomi = OSUtils.ROM_TYPE.MIUI.name().equals(OSUtils.getRomType().name());
        boolean hasset = SPUtils.getInstance(ResUtils.getContext()).getBoolean("go_set", false);

        if (is_xiaomi && !hasset) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.xiaomiMind);
            builder.setMessage(R.string.whiteCard);
            builder.setCancelable(false);
            builder.setPositiveButton(R.string.go_set, (dialog, which) -> {
                SPUtils.getInstance(ResUtils.getContext()).putBoolean("go_set", true);
                XiaomiDeviceUtil.toConfigApp(StartInspectionActivity.this, XiaomiDeviceUtil.getAppProcessName(ResUtils.getContext()), getString(R.string.app_name));
            });
            builder.setNegativeButton(R.string.cancel, (dialog, which) -> finish());
            builder.create().show();

        }

    }

    @Override
    protected void initRequestData() {

    }

    /**
     * 高德地图定位
     */
    private GaodeEntity gaodeEntity;
    private int colorOfLine = 0;
    /**
     * 控制加载的点
     */
    private int initCount = 0;

    private void getGaoDeLocation() {
        if (gaodeEntity == null) {
            gaodeEntity = new GaodeEntity(this, StartInspectionActivity.class, R.mipmap.logo);
            boolean offline = SPUtils.getInstance().getBoolean(CacheConsts.OFFLINE, false);
            if (offline) {
                //支持离线模式定位
                ToastUtils.shortToast("无网模式定位已开启");
                gaodeEntity.getLocationClient().setLocationOption(getDefaultOption(5000));

            }
        }
//        if (taskBean != null && !TextUtils.isEmpty(taskBean.getExecuteStatus()) && UserManager.getInstance().getUserBean().getData().getUserCode().equals(taskBean.getExecuteId())) {
        gaodeEntity.setLocationListen(aMapLocation -> {
            if (gaodeEntity != null) {
                if (aMapLocation.getErrorCode() == 14) {
//                    ToastUtils.shortToast("设备当前 GPS 状态差,请持设备到相对开阔的露天场所再次尝试");
                    new AlertDialog.Builder(getBaseContext())
                            .setMessage("设备当前 GPS 状态差,请持设备到相对开阔的露天场所再次尝试")
                            .setCancelable(true)
                            .setPositiveButton("好的", (dialog, which) -> {

                            })
                            .show();

                }
                if (aMapLocation.getErrorCode() != 0) {
//                    ToastUtils.shortToast(aMapLocation.getErrorInfo()+aMapLocation.getLocationDetail());
                    gaodeEntity.getLocationClient().setLocationOption(getDefaultOption(5000));
                    if (aMapLocation.getErrorCode() == 4) {
                        ToastUtils.shortToast("当前网络较差，已自动切换为无网模式定位");

                    }

                }


//                refreshTitle();

                double[] temp = GPSUtil.gcj02_To_Gps84(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                double latitude = temp[0];
                double longitude = temp[1];
                LogUtil.e(StartInspectionActivity.class.getName(), "经度：" + longitude);
//                ToastUtils.shortToast("经度：" + longitude);
                if (latitude == 0 || longitude == 0) {
                    return;
                }
                initCount++;
                currentPoint = new Point(longitude, latitude, SpatialReference.create(4326));

                if (SPUtils.getInstance().getBoolean(CacheConsts.isOpenAuto, false)) {
                    setYxcListAndDxcList();
                    setRoutePosition(dxcRoutePositions, picIds[0], dxcOverlay);
                    setRoutePosition(yxcRoutePositions, picIds[1], yxcOverlay);
                    searchNearbyRoutePosition(routePositions, currentPoint);
                    if (scroll_down_layout.getCurrentStatus() != ScrollLayout.Status.EXIT) {
                        android.graphics.Point screenPoint = new android.graphics.Point(0, 0);
                        if (mapView.screenToLocation(screenPoint) != null) {
                            double y = mapView.screenToLocation(screenPoint).getY();
                            android.graphics.Point bottomPoint = new android.graphics.Point(0, mapHeight);
                            double bottomY = mapView.screenToLocation(bottomPoint).getY();
                            double translationY = (bottomY - y) / 2 - (bottomY - y) / 4;
//                mapView.visibleArea   返回一个Polygon，表示当前在MapView中可见的ArcGISMap区域。
                            Viewpoint viewPoint = mapView.getCurrentViewpoint(Viewpoint.Type.CENTER_AND_SCALE);
                            Point point = ArcgisLayout.transformationPoint(currentPoint.getX(), currentPoint.getY());
                            Point point2 = new Point(point.getX(), point.getY() + translationY, mapView.getSpatialReference());
//                    initCallout(point2, item.getReservoir());
                            mapView.setViewpointCenterAsync(point2);
                        }
                    } else {
                        alMapview.setCenterPoint(currentPoint, alMapview.getMapView().getMapScale());
                    }
                    LogUtil.e("缩放比例------" + alMapview.getMapView().getMapScale());

                }
                //添加定位图标
                Point locationPoint = new Point(aMapLocation.getLongitude(), aMapLocation.getLatitude(), SpatialReference.create(4326));
                addLocationPic(locationOverlay, R.drawable.google_location, locationPoint);
                if (isFirstLocation) {
                    isFirstLocation = false;
                    alMapview.setCenterPoint(currentPoint, 5000);

                }
                if ("2".equals(taskBean.getExecuteStatus())) {
                    if (initCount > 3) {
                        RoutepointDataManager.getInstance().addPoint(new RoutepointDataBean(workOrderId, longitude + "", latitude + ""));
                    }

                    if (exeline2 != null) {
                        if (colorOfLine == 0) {
                            colorOfLine = ContextCompat.getColor(getContext(), R.color.route_color_two);
                        }
//                            mBinding.alMapview.addPolyline(exeline2, SimpleLineSymbol.Style.SOLID, color, 6);
//                        Point pointTrans = ArcgisLayout.transformationPoint(currentPoint.getX(), currentPoint.getY());
//                        alMapview.addPolylineToGraphicsOverlay(actualRouteOverlay,exeline2,SimpleLineSymbol.Style.SOLID, colorOfLine, 4);
                        LogUtil.e("点个数-------------" + exeline2.size());
                        if (exeline2.size() > 0) {
                            if (exeline2.size() == 1) {
                                mBinding.alMapview.addPic(R.mipmap.ic_me_history_startpoint, exeline2.get(0));
                            }
                            alMapview.addPolylineToGraphicsOverlayNew(actualRouteOverlay, exeline2.get(exeline2.size() - 1), currentPoint, SimpleLineSymbol.Style.SOLID, colorOfLine, 4);
                        }
//                        ToastUtils.shortToast("点个数-------------"+exeline2.size());
                        if (initCount > 3) {
                            exeline2.add(currentPoint);
                        }

                    }
                }
            }
        });
//        }

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

    private void refreshMapViewPoint() {
        List<Point> exeLine = new ArrayList<>();
        if (routeListBean != null) {

            for (TaskItemBean taskItemBean : routeListBean.getItemListByworkid(workOrderId)) {
                if (TextUtils.isEmpty(taskItemBean.getExcuteLongitude()) || TextUtils.isEmpty(taskItemBean.getExcuteLatitude())) {
                } else {
                    double longitude = Double.parseDouble(taskItemBean.getExcuteLongitude());
                    double Latitude = Double.parseDouble(taskItemBean.getExcuteLatitude());
                    Point point1 = new Point(longitude, Latitude, SpatialReference.create(4326));
//                Point point = (Point) GeometryEngine.project(point1, SpatialReferences.getWebMercator());
                    Map<String, Object> attrs = new HashMap<>();
                    attrs.put("taskItemBean", new Gson().toJson(taskItemBean));
//                    mBinding.alMapview.addPic(R.drawable.icon_taskitem_exe, point1, attrs);
                    positionPoint = point1;
                    exeLine.add(point1);
                }
                if (TextUtils.isEmpty(taskItemBean.getPositionLongitude()) || TextUtils.isEmpty(taskItemBean.getPositionLatitude())) {
                } else {
                    double longitude = Double.parseDouble(taskItemBean.getPositionLongitude());
                    double Latitude = Double.parseDouble(taskItemBean.getPositionLatitude());
                    Point point1 = new Point(longitude, Latitude, SpatialReference.create(4326));
//                Point point = (Point) GeometryEngine.project(point1, SpatialReferences.getWebMercator());
                    Map<String, Object> attrs = new HashMap<>();
                    attrs.put("taskItemBean", new Gson().toJson(taskItemBean));
                    positionPoint = point1;
//                    mBinding.alMapview.addPic(R.drawable.icon_taskitem_temp, point1, attrs);
                }
            }
        }

    }


    /**
     * 刷新地图相关逻辑
     */
    private void refreshMapView() {
        if (taskBean == null) {
            return;
        }
        if (routeListBean == null || routeListBean.getItemListByworkid(workOrderId) == null) {
            return;
        }
        mBinding.alMapview.removeGraphics();
        mBinding.alMapview.post(() -> {
            if (!TextUtils.isEmpty(taskBean.getWorkOrderRoute())) {
                if (TextUtils.isEmpty(taskBean.getExecuteStatus())) {
                    refreshMapViewList();
                    return;
                }
                if ("3".equals(taskBean.getExecuteStatus())) {
                    //已完成状态时
                    refreshMapViewList2(taskBean.getWorkOrderRoute());
                    //默认第一个巡查点
                    if (routePositions != null && routePositions.size() > 0) {
                        RoutePosition routePosition = routePositions.get(0);
                        Point locationPoint = routePosition.parasePoint();
                        searchNearbyRoutePosition(routePositions, locationPoint);
                    }
                } else {
                    refreshMapViewList();
                }
            } else {
                refreshMapViewList();
            }
            refreshMapViewPoint();
        });


    }

    /**
     * 已完成工单画线
     *
     * @param
     */
    private void refreshMapViewList2(String workOrderRoute) {
        if (TextUtils.isEmpty(workOrderRoute)) {
            return;
        }
        String temp = workOrderRoute.replaceAll("\\{", "[").replaceAll("\\}", "]");
        List<Double[]> list = JSON.parseArray(temp, Double[].class);
        if (list != null && list.size() >= 2) {
            ArrayList<Point> exeline = new ArrayList();
            for (Double[] bean : list) {
                Point point1 = new Point(bean[0], bean[1], SpatialReference.create(4326));
                exeline.add(point1);
            }
            int color = ContextCompat.getColor(getContext(), R.color.route_color_two);
            mBinding.alMapview.addPolyline(exeline, SimpleLineSymbol.Style.SOLID, color, 4);
            if (exeline != null && exeline.size() > 0) {
                mBinding.alMapview.setMapViewVisibleExtent(exeline);
                mBinding.alMapview.addPic(R.mipmap.ic_me_history_startpoint, exeline.get(0), new HashMap<>());
                mBinding.alMapview.addPic(R.mipmap.ic_me_history_finishpoint, exeline.get(exeline.size() - 1), new HashMap<>());
            }
        }
    }

    /**
     * 未完成工单划线
     */
    private void refreshMapViewList() {
        List<RoutepointDataBean> list = RoutepointDataManager.getInstance().getRoutePointListWithOutD(workOrderId);
        if (list != null && list.size() >= 2) {
            exeline2 = new ArrayList();
            for (RoutepointDataBean bean : list) {
                Point point1 = bean.parasePoint();
                exeline2.add(point1);
            }
            try {
                mBinding.alMapview.setMapViewVisibleExtent(exeline2);
                int color = ContextCompat.getColor(getContext(), R.color.route_color_two);

                mBinding.alMapview.addPolyline(exeline2, SimpleLineSymbol.Style.SOLID, color, 4);
                if (exeline2 != null && exeline2.size() > 0) {
                    mBinding.alMapview.addPic(R.mipmap.ic_me_history_startpoint, exeline2.get(0));
                }
            } catch (Exception e) {
            }
        }
    }


    private void showTjDialog() {
        SpannableString spannableString;
        int size = adapterTaskItemList.getAbnormalityList(workOrderId).size();
        List<TaskItemBean> localTaskItemBeanList = adapterTaskItemList.getLocalData(workOrderId);
        if (size > 0) {
            String title = "有 " + size + " 项异常项是否确定提交？  ";
            if (!CollectionsUtil.isEmpty(localTaskItemBeanList)) {
                title = title + "\n有" + localTaskItemBeanList.size() + "项离线数据没有提交，请在网络较好的情况下完成此操作";
            }
            spannableString = new SpannableString(title);
            //设置颜色
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FE6026")), 2, 2 + (size + "").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            String title = "没有异常项是否确定提交？";
            if (!CollectionsUtil.isEmpty(localTaskItemBeanList)) {
                title = title + "\n有" + localTaskItemBeanList.size() + "项离线数据没有提交，请在网络较好的情况下完成此操作";
            }
            spannableString = new SpannableString(title);
        }

        if (CollectionsUtil.isEmpty(localTaskItemBeanList)) {
            if (taskBean != null && taskBean.getExecuteStatus().equals("2")) {
                spannableString = new SpannableString(noticeStr);
            }
        }

        TjDialogFragment tjDialogFragment = new TjDialogFragment();
        tjDialogFragment.tip = spannableString;
        tjDialogFragment.setListener(v -> {
            if (DoubleClickUtil.isFastDoubleClick()) {
                return;
            }
            tjDialogFragment.dismiss();
            commitTotal();
        }, v -> {
            if (DoubleClickUtil.isFastDoubleClick()) {
                return;
            }
            tjDialogFragment.dismiss();
            finish();

        });
        tjDialogFragment.show(getSupportFragmentManager(), "cb");

    }

    /**
     * 最终提交
     */
    private void commitTotal() {
        boolean offline = SPUtils.getInstance().getBoolean(CacheConsts.OFFLINE, false);
        if (true) {
            if (!NetUtil.isNetworkConnected(Utils.getContext())) {
                ToastUtils.shortToast(R.string.no_network);
                return;
            }

            TaskBean taskBean = DataSupport.where("workOrderId=?", workOrderId).findFirst(TaskBean.class);
            if (taskBean != null) {
                if (taskBean.isHasCreated()) {
                    //如果已经新建过工单了则直接提交
//                        List<TaskItemBean> taskItemBeanList = DataSupport.where("workOrderId=?", workOrderId).find(TaskItemBean.class);
                    List<TaskItemBean> localTaskItemBeanList = adapterTaskItemList.getLocalData(workOrderId);
                    if (!CollectionsUtil.isEmpty(localTaskItemBeanList)) {
                        mPresenter.commitTotal(localTaskItemBeanList, StartInspectionActivity.this);
                    } else {
                        if (taskBean.getExecuteStatus().equals("2")) {
                            String temp = RoutepointDataManager.getInstance().getRoutePointListString(workOrderId);
                            taskBean.setWorkOrderRoute(temp);
                            taskBean.updateAll("workorderid=?", taskBean.getWorkOrderId());
                            /**
                             * APP 巡检人员工单执行完成提交 （App 端使用）
                             */
                            mPresenter.endExecute(workOrderId, temp, true, "正在提交完成");
                        } else {
                            finish();
                        }
                    }
                } else {
                    newStartExecute(taskBean.getWorkOrderId(), taskBean.getRouteId(), taskBean.getStartTime());
                }
            }
        }
    }

    /**
     * 新建工单
     */
    public void newStartExecute(String workOrderId, String routeId, String startTime) {
//        startTime += ":00";
        TaskManager.getInstance().newStartExecute(workOrderId, routeId, startTime).safeSubscribe(new LoadingSubject<TaskDetailResponse>(true, "正在新建工单...") {
            @Override
            protected void _onNext(TaskDetailResponse taskDetailResponse) {
                /**
                 * 新建功工单成功则开始上传
                 */
                if (taskDetailResponse != null) {
                    if (taskDetailResponse.getCode() == 0) {
                        List<TaskBean> templist = DataSupport.where("workOrderId=?", StartInspectionActivity.this.workOrderId).find(TaskBean.class);
                        if (!CollectionsUtil.isEmpty(templist)) {
                            TaskBean taskBean = templist.get(0);
                            if (taskBean != null) {
                                /*taskBean.setHasStartExecute(true);
                                taskBean.update();*/
                                ContentValues values = new ContentValues();
                                values.put("hasCreated", true);
                                DataSupport.updateAll(TaskBean.class, values, "workOrderId = ?", StartInspectionActivity.this.workOrderId);
                            }
                        }
                        mPresenter.commitTotal(adapterTaskItemList.getLocalData(workOrderId), StartInspectionActivity.this);
                    }
                }
            }

            @Override
            protected void _onError(String message) {
                if (message != null && message.contains("重复请求")) {
                    LogUtil.e("工单已创建");
                    List<TaskBean> templist = DataSupport.where("workOrderId=?", StartInspectionActivity.this.workOrderId).find(TaskBean.class);
                    if (!CollectionsUtil.isEmpty(templist)) {
                        TaskBean taskBean = templist.get(0);
                        if (taskBean != null) {
                            if (taskBean.isHasCreated()) {
                                //如果已经新建过工单了则直接提交
                                mPresenter.commitTotal(adapterTaskItemList.getLocalData(workOrderId), StartInspectionActivity.this);
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     * 展示数据
     *
     * @param data
     */
    private void showData(TaskBean data) {
        this.taskBean = data;
//        refreshView();
        getGaoDeLocation();
    }

    @Override
    public void startExecuteSucess() {
        if (!TextUtils.isEmpty(workOrderId)) {
            List<TaskBean> templist = DataSupport.where("workOrderId=?", workOrderId).find(TaskBean.class);
            if (!CollectionsUtil.isEmpty(templist)) {
                showData(templist.get(0));
            }
        }
    }

    /**
     * 工单完成之后的最后操作：更新工单状态
     */
    @Override
    public void endExecuteSucess() {
        DataSupport.deleteAll(TaskBean.class,"workOrderId=?", workOrderId);
        DataSupport.deleteAll(TaskItemBean.class,"workOrderId=?", workOrderId);
        ToastUtils.shortToast("提交成功");
        finish();
    }

    @Override
    public void appReservoirWorkOrderItemCommitOneByOneSuccess() {
        String temp = RoutepointDataManager.getInstance().getRoutePointListString(workOrderId);
        taskBean.setWorkOrderRoute(temp);
        taskBean.updateAll("workorderid=?", taskBean.getWorkOrderId());
        /**
         * APP 巡检人员工单执行完成提交 （App 端使用）
         */
        mPresenter.endExecute(workOrderId, temp, true, ResUtils.getString(R.string.data_loading));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length >= 1) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (gaodeEntity != null) {
                        gaodeEntity.startTrace();//开始定位
                    }
                } else {
                    new AlertDialog.Builder(this)
                            .setMessage("定位权限被拒绝,将导致定位功能无法正常使用，需要到设置页面手动授权")
                            .setPositiveButton("去授权", (dialog, which) -> {
                                //引导用户至设置页手动授权
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            })
                            .setNegativeButton("取消", (dialog, which) -> {
                                //引导用户手动授权，权限请求失败
                                ToastUtils.longToast("拒绝了定位权限，将不能使用定位功能！！！");
                            }).setOnCancelListener(dialog -> {
                        //引导用户手动授权，权限请求失败
                        ToastUtils.longToast("拒绝了定位权限，将不能使用定位功能！！！");
                    }).show();
                    // Permission Denied
                }
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        refreshTitle();
        if (alMapview != null) {
            alMapview.onMapResume();
        }
        if (adapterTaskItemList != null) {
            refreshList(nearbyRoutePosition);
        }
    }

  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1000) {
            if (adapterTaskItemList != null) {
                refreshList(nearbyRoutePosition);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }*/

    @Override
    protected void onPause() {
        super.onPause();
        if (alMapview != null) {
            alMapview.onMapPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gaodeEntity != null) {
            gaodeEntity.stopTrace();
            gaodeEntity.closeLocation();
        }
        if (alMapview != null) {
            alMapview.onMapDestroy();
        }
        if (bitmapLocation != null) {
            bitmapLocation.recycle();
            bitmapLocation = null;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!isCompleteOfTaskBean) {
                if (taskBean != null) {
                    String temp = RoutepointDataManager.getInstance().getRoutePointListString(workOrderId);
                    taskBean.setWorkOrderRoute(temp);
                    taskBean.updateAll("workorderid=?", taskBean.getWorkOrderId());
                }

                ToastUtils.shortToast("数据已保存");
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
