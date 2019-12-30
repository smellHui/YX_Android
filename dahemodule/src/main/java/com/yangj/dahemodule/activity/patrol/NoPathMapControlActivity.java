package com.yangj.dahemodule.activity.patrol;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.lxj.xpopup.XPopup;
import com.tepia.arcgismap.Util;
import com.tepia.arcgismap.layer.LocationManager;
import com.tepia.arcgismap.layer.MapManager;
import com.tepia.arcgismap.layer.core.IPoly;
import com.tepia.base.mvp.BaseActivity;
import com.tepia.base.utils.ImageLoaderUtil;
import com.tepia.base.view.floatview.CollectionsUtil;
import com.tepia.guangdong_module.amainguangdong.route.RoutepointDataBean;
import com.tepia.guangdong_module.amainguangdong.route.RoutepointDataManager;
import com.tepia.guangdong_module.amainguangdong.route.TaskBean;
import com.tepia.guangdong_module.amainguangdong.route.TaskItemBean;
import com.tepia.guangdong_module.amainguangdong.utils.SqlManager;
import com.yangj.dahemodule.R;
import com.yangj.dahemodule.common.HttpManager;
import com.yangj.dahemodule.observer.PatrolLocationObserver;
import com.yangj.dahemodule.observer.PatrolPointTouchObserver;
import com.yangj.dahemodule.util.UiHelper;
import com.yangj.dahemodule.view.NoPathTimeView;
import com.yangj.dahemodule.view.PatrolSubmitDialog;
import com.yangj.dahemodule.wrap.PatrolAddProblemWrap;
import com.yangj.dahemodule.wrap.SubmitTaskWrap;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import cc.shinichi.library.tool.image.ImageUtil;

import static com.yangj.dahemodule.util.UiHelper.WORK_ORDER_ID;

/**
 * Author:xch
 * Date:2019/12/17
 * Description:无线路巡查
 */
public class NoPathMapControlActivity extends BaseActivity {

    private static final String PROBLEM_ID = "problem_id";
    private MapView mapView;
    private NoPathTimeView noPathTimeView;
    private PatrolSubmitDialog patrolSubmitDialog;
    private MapManager mapLayout;
    //定位点图层
    private IPoly locationPoly;
    private IPoly routePoly;
    private IPoly problemPoly;

    private Graphic scanGraphic;

    private LocationManager locationManager;
    private Point problemPoint, locationPoint;
    private List<Point> exeRoutePoints;
    private List<Point> lastPoints;
    private String workOrderId;
    private TaskBean taskBean;
    private boolean isXC;

    @Override
    public int getLayoutId() {
        return R.layout.activity_no_path_map_control;
    }

    @Override
    public void initData() {

        EventBus.getDefault().register(this);
        isXC = HttpManager.getInstance().isXC();
        exeRoutePoints = new ArrayList<>();
        lastPoints = new ArrayList<>();

        Intent intent = getIntent();
        if (intent != null) {
            workOrderId = intent.getStringExtra(WORK_ORDER_ID);
            taskBean = SqlManager.getInstance().queryTaskSqlByWorkOrderId(workOrderId);
        }
    }

    @Override
    public void initView() {
        showBack();
        noPathTimeView = findViewById(R.id.view_no_path_time);
        patrolSubmitDialog = new PatrolSubmitDialog(getContext(), workOrderId);
        mapView = findViewById(R.id.map_view);
        locationManager = new LocationManager(getContext(), this.getClass(), R.mipmap.logo);
        mapLayout = new MapManager.Builder(getContext(), mapView)
                .setLocationManager(locationManager)
                .build();
        locationPoly = mapLayout.addPloyPlotter();
        routePoly = mapLayout.addPloyPlotter();
        problemPoly = mapLayout.addPloyPlotter();
        //定位
        mapLayout.addObserver(new PatrolLocationObserver(locationPoly) {
            @Override
            public void onLocation(Point point) {
                RoutepointDataManager.getInstance().addPoint(new RoutepointDataBean(workOrderId, point.getX() + "", point.getY() + ""));
                locationPoint = point;
                drawLine(point);
                mapLayout.center(point);
            }
        });
        mapLayout.addObserver(new PatrolPointTouchObserver(problemPoly) {
            @Override
            public void onTouchPatrolTouch(Graphic graphic, Map<String, Object> attributes) {
                scanGraphic = graphic;
                String problemId = (String) attributes.get(PROBLEM_ID);
                UiHelper.goToPatrolFeedBackView(getContext(), workOrderId, problemId);
            }
        });

        if (isXC && !taskBean.isHasExecuted()) {
            locationManager.startLocation();
            noPathTimeView.startTime();
            setEndBtnEnabled();
        } else {
            noPathTimeView.setBtnGone();
            drawCompleteLine(taskBean.getWorkOrderRoute());
        }

        List<TaskItemBean> taskItemBeans = SqlManager.getInstance().queryTaskItem(workOrderId);
        if (!CollectionsUtil.isEmpty(taskItemBeans)) {
            for (TaskItemBean taskItemBean : taskItemBeans) {
                if (taskItemBean == null) continue;
                Point point = new Point(Double.parseDouble(taskItemBean.getExcuteLatitude()), Double.parseDouble(taskItemBean.getExcuteLongitude()));
                drawPatrolPoint(point, taskItemBean.getItemId());
                mapLayout.center(point);
            }
        }
    }

    private void setEndBtnEnabled() {
        List<Graphic> graphics = problemPoly.getGraphics();
        noPathTimeView.setEndBtnEnabled(CollectionsUtil.isNotEmpty(graphics));
        List<TaskItemBean> taskItemBeans = SqlManager.getInstance().queryTaskItem(workOrderId);
        noPathTimeView.setAbNormalNum(taskItemBeans.size());
    }

    @Override
    protected void initListener() {
        noPathTimeView.setNoPathTimeListener(new NoPathTimeView.NoPathTimeListener() {
            @Override
            public void endAbnormalClick() {
                new XPopup.Builder(getContext())
                        .hasStatusBarShadow(true) //启用状态栏阴影
                        .dismissOnTouchOutside(false)
                        .asCustom(patrolSubmitDialog)
                        .show();
            }

            @Override
            public void uploadAbnormalClick() {
                problemPoint = new Point(locationPoint.getX(), locationPoint.getY());
                UiHelper.goToPatrolFeedBackView(getContext(), workOrderId, problemPoint.getX() + "", problemPoint.getY() + "");
            }
        });
    }

    @Override
    protected void initRequestData() {

    }

    /**
     * 在地图上打上报点
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addPatrolProblemWrap(PatrolAddProblemWrap patrolAddProblemWrap) {
        if (patrolAddProblemWrap == null) return;
        if (patrolAddProblemWrap.isToDelete()) {
            problemPoly.removeGraphic(scanGraphic);
            setEndBtnEnabled();
            return;
        }
        drawPatrolPoint(problemPoint, patrolAddProblemWrap.getProblemId());
    }

    //提交成功关闭页面
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void submitSucessCallBack(SubmitTaskWrap submitTaskWrap) {
        finish();
    }

    private void drawLine(Point point) {
        try {
            lastPoints.clear();
            exeRoutePoints.add(point);
            int size = exeRoutePoints.size();
            if (size > 1) {
                lastPoints.add(Util.transformationPoint(exeRoutePoints.get(size - 2)));
                lastPoints.add(Util.transformationPoint(exeRoutePoints.get(size - 1)));
                drawLine(lastPoints);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawLine(@NonNull List<Point> points) {
        int size = points.size();
        if (size > 1) {
            try {
                routePoly.createPolylineGraphics(points, getResources().getColor(R.color.color_67b4ff));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
        List<Point> webPoints = new ArrayList<>();
        if (list != null && list.size() >= 2) {
            exeRoutePoints.clear();
            for (Double[] bean : list) {
                Point point1 = new Point(bean[0], bean[1], SpatialReference.create(4326));
                webPoints.add(Util.transformationPoint(point1));
                exeRoutePoints.add(point1);
            }
            drawLine(webPoints);
            routePoly.createPicturePointGraphics(webPoints.get(0), com.example.guangdong_module.R.mipmap.ic_me_history_startpoint);
            routePoly.createPicturePointGraphics(webPoints.get(webPoints.size() - 1), com.example.guangdong_module.R.mipmap.ic_me_history_finishpoint);
        }
    }

    public void drawPatrolPoint(Point problemPoint, @NonNull String itemId) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put(PROBLEM_ID, itemId);
        problemPoly.createPicturePointGraphics(Util.transformationPoint(problemPoint), R.mipmap.icon_map_problem, attributes);
        setEndBtnEnabled();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.stopLocation();
        noPathTimeView.closeTime();
        EventBus.getDefault().unregister(this);
    }

}
