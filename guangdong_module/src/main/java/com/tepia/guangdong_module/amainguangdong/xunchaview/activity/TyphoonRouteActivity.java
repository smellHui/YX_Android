package com.tepia.guangdong_module.amainguangdong.xunchaview.activity;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.Polygon;
import com.esri.arcgisruntime.geometry.Polyline;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.WebTiledLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.example.guangdong_module.R;
import com.example.guangdong_module.databinding.ActivityTyphoonRouteBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.tepia.base.mvp.MVPBaseActivity;
import com.tepia.base.utils.TimeFormatUtils;
import com.tepia.base.utils.TimePickerDialogUtil;
import com.tepia.base.utils.ToastUtils;
import com.tepia.base.utils.Utils;
import com.tepia.base.view.arcgisLayout.ArcgisLayout;
import com.tepia.base.view.arcgisLayout.GoogleMapLayer;
import com.tepia.guangdong_module.amainguangdong.model.typhoonroute.TyphoonCircleData;
import com.tepia.guangdong_module.amainguangdong.model.typhoonroute.TyphoonDetailResponse;
import com.tepia.guangdong_module.amainguangdong.model.typhoonroute.TyphoonForcastPointsBean;
import com.tepia.guangdong_module.amainguangdong.model.typhoonroute.TyphoonForcastRouteBean;
import com.tepia.guangdong_module.amainguangdong.model.typhoonroute.TyphoonListItemBean;
import com.tepia.guangdong_module.amainguangdong.model.typhoonroute.TyphoonListResponse;
import com.tepia.guangdong_module.amainguangdong.model.typhoonroute.TyphoonRouteItemBean;
import com.tepia.guangdong_module.amainguangdong.mvp.typhoonroute.TyphoonRouteContract;
import com.tepia.guangdong_module.amainguangdong.mvp.typhoonroute.TyphoonRoutePresenter;
import com.tepia.guangdong_module.amainguangdong.xunchaview.adapter.TyphoonRouteAdapter;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Android Studio
 *
 * @author : Arthur
 * Date :    2019/5/18
 * Time :    10:34
 * Describe :
 */
public class TyphoonRouteActivity extends MVPBaseActivity<TyphoonRouteContract.View, TyphoonRoutePresenter>
        implements TyphoonRouteContract.View, View.OnClickListener, OnDateSetListener {

    ActivityTyphoonRouteBinding mBinding;

    private static double minScale = 7.335451152802595E7 * 0.5 * 0.5;

    /**
     * 普通图层
     */
    private GraphicsOverlay mGraphicsOverlay;

    /**
     * 路径点图层
     */
    private GraphicsOverlay mGraphicsOverlayPoints;

    /**
     * 当前选中的路径点
     */
    private TyphoonListItemBean currentTyphoonData;

    /**
     * 点集合
     */
    private PointCollection mPointCollection = new PointCollection(SpatialReferences.getWebMercator());

    /**
     * 台风等级颜色
     */
    private String[] typhoonColors = { "#FF3030", "#FF8C00", "#FFFF00", "#000099", "#32CD32", "#7FFF00" };

    /**
     * 风圈颜色,黄橙红
     */
    private String[] circleColors = { "#33FFD700", "#33FF8247", "#33FF0000" };

    /**
     * 台风预测路线颜色
     */
    private String[] forcastLineColors = {"#6959CD","#4876FF","#8B3A3A", "#CD2626", "#8B6914","#7CFC00","#0000EE"};

    /**
     * 时间选择器
     */
    private TimePickerDialogUtil timePickerDialogUtil;

    /**
     * 选择的时间
     */
    private String choosedTime;

    @Override
    public int getLayoutId() {
        return R.layout.activity_typhoon_route;
    }

    @Override
    public void initData() {
        mBinding = DataBindingUtil.bind(mRootView);
        ArcGISRuntimeEnvironment.setLicense("runtimelite,1000,rud4163659509,none,1JPJD4SZ8L4HC2EN0229");
    }

    @Override
    public void initView() {
        Basemap basemap = new Basemap();
        ArcGISMap arcGISMap = new ArcGISMap(basemap);
        mBinding.alMapview.setAttributionTextVisible(false);
        WebTiledLayer imgTitleLayer = GoogleMapLayer.createWebTiteLayer(GoogleMapLayer.Type.IMAGE);
        WebTiledLayer webTitleLayer = GoogleMapLayer.createWebTiteLayer(GoogleMapLayer.Type.IMAGE_ANNOTATION);
        arcGISMap.getOperationalLayers().add(imgTitleLayer);
        arcGISMap.getOperationalLayers().add(webTitleLayer);
        arcGISMap.setMinScale(ArcgisLayout.minScale);
        arcGISMap.setMaxScale(ArcgisLayout.maxScale);
        Point point1 = new Point(1.1992433073197773E7, 4885139.106039485,
                SpatialReference.create(3857));
        Point point2 = new Point(1.3532164647994766E7, 2329702.2487083403, SpatialReference.create(3857));
        Envelope initEnvelope = new Envelope(point1, point2);
        arcGISMap.setInitialViewpoint(new Viewpoint(initEnvelope));
        arcGISMap.setMinScale(minScale);
        mBinding.alMapview.setMap(arcGISMap);
        mGraphicsOverlay = new GraphicsOverlay();
        mGraphicsOverlayPoints = new GraphicsOverlay();
        mBinding.alMapview.getGraphicsOverlays().add(mGraphicsOverlay);
        mBinding.alMapview.getGraphicsOverlays().add(mGraphicsOverlayPoints);
        mBinding.rvList.setLayoutManager(new LinearLayoutManager(this));
        initTimePicker();
    }

    @Override
    protected void initListener() {
        mBinding.ivBack.setOnClickListener(this);
        mBinding.rlChooseYear.setOnClickListener(this);
        mBinding.tvSearch.setOnClickListener(this);
    }

    @Override
    protected void initRequestData() {
        mPresenter.getTyphoonList(choosedTime, true, "正在获取台风数据");
    }

    /**
     * 展示台风列表
     */
    @Override
    public void showTyphoonList(TyphoonListResponse response) {
        mBinding.rvList.setAdapter(new TyphoonRouteAdapter(R.layout.item_typhoon_route, response.getData(), bean -> {
            mPresenter.getTyphoonDetails(bean.getTfId(), true, "正在获取路径数据");
            currentTyphoonData = bean;
            StringBuilder builder = new StringBuilder();
            builder.append(bean.getTfId()).append(bean.getName()).append("路径图");
            mBinding.tvTyphoonTittle.setText(builder.toString());
        }, this));
        // 初始化获取到台风列表后展示最新的台风数据
        if (response.getData() != null && response.getData().size() > 0) {
            StringBuilder builder = new StringBuilder();
            builder.append(response.getData().get(0).getTfId()).append(response.getData().get(0).getName())
                    .append("路径图");
            mBinding.tvTyphoonTittle.setText(builder.toString());
            currentTyphoonData = response.getData().get(0);
            mPresenter.getTyphoonDetails(response.getData().get(0).getTfId(), true, "正在获取路径数据");
        }
    }

    /**
     * 在地图上绘制台风路径
     */
    @Override
    public void showTyphoonRoute(TyphoonDetailResponse response) {
        List<TyphoonRouteItemBean> data = response.getData();
        // 清理掉已绘制的图层
        mGraphicsOverlay.getGraphics().clear();
        mGraphicsOverlayPoints.getGraphics().clear();
        if (data.size() > 0) {
            // 台风实际路线
            List<Point> pointsReal = new ArrayList<>();
            // 生成路径点数据
            for (int i = 0, count = data.size(); i < count; i++) {
                Point point = new Point(Double.valueOf(data.get(i).getLng()), Double.valueOf(data.get(i).getLat()),
                        SpatialReference.create(4326));
                pointsReal.add(point);
                // 绘制路径上的各过境点
                drawVertices(Double.valueOf(data.get(i).getLng()), Double.valueOf(data.get(i).getLat()),
                        data.get(i).getStrong());
            }
            // 绘制起点坐标说明
            try {
                drawMarker(pointsReal.get(0), currentTyphoonData.getTfId() + currentTyphoonData.getName());
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 将中心移到台风最近过境点处
            mBinding.alMapview.setViewpointCenterAsync(pointsReal.get(pointsReal.size() - 1),
                    mBinding.alMapview.getMapScale());
            // 绘制路径线
            addPolyline(pointsReal, SimpleLineSymbol.Style.SOLID, Color.parseColor("#598AF9"), 1.5f);
            // 处理最新台风过境点数据
            try {
                dealNewestPoint(data.get(data.size() - 1));
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFail(String msg) {
        ToastUtils.shortToast(msg);
    }

    @Override
    public void onClick(View v) {
        // 返回按钮
        if (v.getId() == R.id.iv_back) {
            finish();
        }
        // 时间选择
        else if (v.getId() == R.id.rl_choose_year) {
            if (timePickerDialogUtil.startDialog != null) {
                timePickerDialogUtil.startDialog = null;
            }
            timePickerDialogUtil.builder.setTitleStringId("请选择要查询年份");
            timePickerDialogUtil.startDialog = timePickerDialogUtil.builder.build();
            timePickerDialogUtil.startDialog.show(getSupportFragmentManager(), "all");
        }
        // 查询按钮
        else if (v.getId() == R.id.tv_search){
            mPresenter.getTyphoonList(choosedTime, true, "正在获取台风数据");
        }
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
       choosedTime = timePickerDialogUtil.getDateToString(millseconds);
       mBinding.tvYear.setText(choosedTime);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mBinding.alMapview != null) {
            mBinding.alMapview.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mBinding.alMapview != null) {
            mBinding.alMapview.resume();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBinding.alMapview != null) {
            mBinding.alMapview.dispose();
        }
    }

    /**
     * 初始化时间选择器
     */
    public void initTimePicker() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy", Locale.getDefault());
        timePickerDialogUtil = new TimePickerDialogUtil(Utils.getContext(), sf);
        timePickerDialogUtil.initTimePicker(this, com.jzxiang.pickerview.data.Type.YEAR);
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        choosedTime = formatter.format(currentTime);
        mBinding.tvYear.setText(choosedTime);
    }

    /**
     * 添加线
     *
     * @param points 路径集合    统一4326坐标系
     * @param style  线的样式
     * @param color  线的颜色
     * @param width  线的宽度
     */
    public void addPolyline(List<Point> points, SimpleLineSymbol.Style style, int color, float width) {
        try {
            if (points.size() > 0) {
                PointCollection coloradoCorners = new PointCollection(SpatialReference.create(4326));
                coloradoCorners.addAll(points);
                Polyline polyline = new Polyline(coloradoCorners);
                SimpleLineSymbol lineSymbol = new SimpleLineSymbol(style, color, width);
                mGraphicsOverlay.getGraphics().add(new Graphic(polyline, lineSymbol));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     *  绘制台风风圈
     *
     * @param data 风圈信息
     */
    private void drawCircle(TyphoonCircleData data) {
        Point[] points = getPoints(data.getPoint(), data.getRadius());
        mPointCollection.clear();
        for (Point p : points) {
            mPointCollection.add(p);
        }
        Polygon polygon = new Polygon(mPointCollection);
        // 风圈范围外部圆环
        SimpleLineSymbol lineSymbol =
                new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.parseColor("#FC8145"), 1.0f);
        // 风圈范围
        SimpleFillSymbol simpleFillSymbol =
                new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, Color.parseColor(data.getColor()), lineSymbol);
        Graphic graphic = new Graphic(polygon, simpleFillSymbol);
        mGraphicsOverlay.getGraphics().add(graphic);
    }

    /**
     * 获取台风风圈环上的坐标
     *
     * @param center 中心点
     * @param radius 半径
     * @return
     */
    private Point[] getPoints(Point center, double radius) {
        Point[] points = new Point[50];
        double sin;
        double cos;
        double x;
        double y;
        for (double i = 0; i < 50; i++) {
            sin = Math.sin(Math.PI * 2 * i / 50);
            cos = Math.cos(Math.PI * 2 * i / 50);
            x = center.getX() + radius * sin;
            y = center.getY() + radius * cos;
            points[(int) i] = new Point(x, y, SpatialReference.create(3857));
        }
        return points;
    }

    /**
     * 处理台风近过境点
     * @param bean 最近过境点数据
     */
    private void dealNewestPoint(TyphoonRouteItemBean bean) throws ExecutionException, InterruptedException, ParseException {
        String json = bean.getForecast();
        Point point =
                new Point(Double.valueOf(bean.getLng()), Double.valueOf(bean.getLat()), SpatialReference.create(4326));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date date;
        date = format.parse(currentTyphoonData.getEndTime());
        SimpleDateFormat format1 = new SimpleDateFormat("MM月dd日 HH时", Locale.CHINA);
        String newtime = format1.format(date);
        // 绘制最新过境点坐标说明
        drawMarker(point, currentTyphoonData.getName() + newtime);

        // 地图坐标转换
        Point mapPoint = (Point) GeometryEngine.project(point, SpatialReference.create(3857));
        // 添加台风中心图标
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.typhoon_center);
        if (bitmap == null) {
            return;
        }
        Bitmap result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(bitmap, 0, 0, null);
        PictureMarkerSymbol pictureMarkerSymbol1;
        pictureMarkerSymbol1 = PictureMarkerSymbol.createAsync(new BitmapDrawable(getResources(), result)).get();
        Graphic pointGraphic = new Graphic(point, pictureMarkerSymbol1);
        mGraphicsOverlay.getGraphics().add(pointGraphic);
        // 默认风圈半径200km
        double radius = 200;
        // 默认风圈颜色,黄色
        String color = circleColors[0];
        String radius7 = bean.getRadius7();
        List<TyphoonCircleData> radiusList = new ArrayList<>();
        if (!TextUtils.isEmpty(radius7)) {
            radius7 = radius7.replace("|", ",");
            String[] radiusArray = radius7.split(",");
            radius = getMax(radiusArray);

            TyphoonCircleData data = new TyphoonCircleData();
            data.setPoint(mapPoint);
            data.setRadius(radius*1000);
            data.setColor(circleColors[0]);
            radiusList.add(data);
        }
        String radius10 = bean.getRadius10();
        if (!TextUtils.isEmpty(radius10)) {
            radius10 = radius10.replace("|", ",");
            String[] radiusArray = radius10.split(",");
            radius = getMax(radiusArray);

            TyphoonCircleData data = new TyphoonCircleData();
            data.setPoint(mapPoint);
            data.setRadius(radius*1000);
            data.setColor(circleColors[1]);
            radiusList.add(data);
        }

        String radius12 = bean.getRadius12();
        if (!TextUtils.isEmpty(radius12)) {
            radius12 = radius12.replace("|", ",");
            String[] radiusArray = radius12.split(",");
            radius = getMax(radiusArray);

            TyphoonCircleData data = new TyphoonCircleData();
            data.setPoint(mapPoint);
            data.setRadius(radius*1000);
            data.setColor(circleColors[2]);
            radiusList.add(data);
        }

        for (int i=0;i<radiusList.size();i++){
            // 绘制台风半径
            drawCircle(radiusList.get(i));
        }

        // 处理台风预测路径
        Type type = new TypeToken<List<TyphoonForcastRouteBean>>() {
        }.getType();
        List<TyphoonForcastRouteBean> list = new Gson().fromJson(json, type);
        for (int i = 0; i < list.size(); i++) {
            List<TyphoonForcastPointsBean> listroute = list.get(i).getForecastpoints();
            // 台风实际路线
            List<Point> pointsForcast = new ArrayList<>();
            // 生成路径点数据
            for (int j = 0, count = listroute.size(); j < count; j++) {
                Point pointForcast = new Point(Double.valueOf(listroute.get(j).getLng()),
                        Double.valueOf(listroute.get(j).getLat()), SpatialReference.create(4326));
                pointsForcast.add(pointForcast);
                // 绘制路径上的各过境点
                drawVertices(Double.valueOf(listroute.get(j).getLng()), Double.valueOf(listroute.get(j).getLat()),
                        listroute.get(j).getStrong());
            }
            if (forcastLineColors.length > i){
                // 绘制预测台风路径线
                addPolyline(pointsForcast, SimpleLineSymbol.Style.DASH, Color.parseColor(forcastLineColors[i]), 1.5f);
            }else {
                int pos = i%forcastLineColors.length;
                // 绘制预测台风路径线
                addPolyline(pointsForcast, SimpleLineSymbol.Style.DASH, Color.parseColor(forcastLineColors[pos]), 1.5f);
            }
        }
    }

    /**
     *
     * 绘制仓库风路径上的过境点
     *
     * @param lng 坐标点经度
     * @param lat 坐标点纬度
     * @param strong 台风强度
     */
    private void drawVertices(double lng, double lat, String strong) {
        // 根据台风强度绘制路径点上的圆点，不同强度台风圆点颜色不同
        // 热带低压(白色),热带风暴(绿色),强热带风暴(蓝色),台风(黄色)，强台风(橙色)，超强台风(红色)
        String color;
        // 超强台风
        if ("超强台风".equals(strong)) {
            color = typhoonColors[0];
        }
        // 强台风
        else if ("强台风".equals(strong)) {
            color = typhoonColors[1];
        }
        // 强热带风暴
        else if ("强热带风暴".equals(strong)) {
            color = typhoonColors[2];
        }
        // 台风
        else if ("台风".equals(strong)) {
            color = typhoonColors[3];
        }
        // 执带风暴
        else if ("热带风暴".equals(strong)) {
            color = typhoonColors[4];
        }
        // 热带低压
        else {
            color = typhoonColors[5];
        }
        // 路径点4326坐标
        Point point = new Point(lng, lat, SpatialReference.create(4326));
        SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.parseColor(color), 6);
        Graphic graphic = new Graphic(point, symbol);
        mGraphicsOverlayPoints.getGraphics().add(graphic);
    }

    /**
     * 取数组中最大值
     * @param array 数组
     * @return 最大值
     */
    private double getMax(String[] array) {
        String max = array[0];
        for (int i = 0; i < array.length; i++) {
            // 判断最大值
            if (Double.valueOf(array[i]) > Double.valueOf(max)) {
                max = array[i];
            }
        }
        return Double.valueOf(max);
    }

    /**
     *
     * 地图绘制过境点说明
     *
     * @param point 过境点数据
     */
    private void drawMarker(Point point, String name) throws ExecutionException, InterruptedException {
        View view = LayoutInflater.from(this).inflate(R.layout.view_typhoon_marker, null);
        view.setBackgroundResource(android.R.color.transparent);
        TextView tvTyphoonname = view.findViewById(R.id.tv_typhoonname);
        tvTyphoonname.setText(name);
        // 测量使得view指定大小
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(100, View.MeasureSpec.UNSPECIFIED);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(50, View.MeasureSpec.UNSPECIFIED);
        view.measure(measuredWidth, measuredHeight);
        // 调用layout方法布局后，可以得到view的尺寸大小
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        Bitmap bmp = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        c.drawColor(Color.WHITE);
        view.draw(c);

        PictureMarkerSymbol pictureMarkerSymbol = PictureMarkerSymbol.createAsync(new BitmapDrawable(bmp)).get();
        pictureMarkerSymbol.setOffsetX(70);
        Graphic graphic = new Graphic(point, pictureMarkerSymbol);
        mGraphicsOverlay.getGraphics().add(graphic);
    }

}
