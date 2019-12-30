package com.tepia.guangdong_module.amainguangdong.xunchaview.fragment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.example.guangdong_module.R;
import com.example.guangdong_module.databinding.FragmentAdminstatisticsHomeBinding;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.tepia.base.http.LoadingSubject;
import com.tepia.base.mvp.MVPBaseFragment;
import com.tepia.base.utils.LogUtil;
import com.tepia.base.utils.NetUtil;
import com.tepia.base.utils.ToastUtils;
import com.tepia.base.utils.Utils;
import com.tepia.guangdong_module.amainguangdong.common.UserManager;
import com.tepia.guangdong_module.amainguangdong.model.UserInfoBean;
import com.tepia.guangdong_module.amainguangdong.model.WeatherWarnBean;
import com.tepia.guangdong_module.amainguangdong.model.adminstatistics.InspectionStatisticsBean;
import com.tepia.guangdong_module.amainguangdong.model.adminstatistics.InspectionStatisticsResponse;
import com.tepia.guangdong_module.amainguangdong.model.adminstatistics.RealTimeMonitorBean;
import com.tepia.guangdong_module.amainguangdong.model.adminstatistics.RealTimeMonitorResponse;
import com.tepia.guangdong_module.amainguangdong.model.adminstatistics.SafetyIdentifyBean;
import com.tepia.guangdong_module.amainguangdong.model.adminstatistics.SafetyIdentifyResponse;
import com.tepia.guangdong_module.amainguangdong.model.adminstatistics.ThreeKeyPointBean;
import com.tepia.guangdong_module.amainguangdong.model.adminstatistics.ThreeKeyPointResponse;
import com.tepia.guangdong_module.amainguangdong.model.adminstatistics.ThreePersonsBean;
import com.tepia.guangdong_module.amainguangdong.model.adminstatistics.ThreePersonsResponse;
import com.tepia.guangdong_module.amainguangdong.mvp.adminstatistics.AdminStatisticsContract;
import com.tepia.guangdong_module.amainguangdong.mvp.adminstatistics.AdminStatisticsPresenter;
import com.tepia.guangdong_module.amainguangdong.xunchaview.activity.AreaReservoirCountActivity;
import com.tepia.guangdong_module.amainguangdong.xunchaview.activity.RainfallStatisticsActivity;
import com.tepia.guangdong_module.amainguangdong.xunchaview.activity.ReserviorSearchListActivity;
import com.tepia.guangdong_module.amainguangdong.xunchaview.activity.ReservoirRegimenActivity;
import com.tepia.guangdong_module.amainguangdong.xunchaview.activity.SeepageAnomalousActivity;
import com.tepia.guangdong_module.amainguangdong.xunchaview.activity.TyphoonRouteActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Android Studio
 *
 * @author: Arthur
 * Date:    2019/5/28
 * Time:    14:28
 * Describe:
 */
public class AdminStatisticsFragment extends MVPBaseFragment<AdminStatisticsContract.View, AdminStatisticsPresenter>
        implements AdminStatisticsContract.View, View.OnClickListener {

    FragmentAdminstatisticsHomeBinding mBinding;
    /**
     * 顶部右侧天气按钮
     */
    private TextView tv_right_tianqi;
    /**
     * 顶部标题
     */
    private TextView tv_center_text;
    /**
     * 顶部返回按钮
     */
    private TextView tv_left_text;

    /**
     * 实时监测模板
     */
    private String contentTimeMonitor;
    private RealTimeMonitorBean realTimeMonitorBean;

    /**
     * 巡查统计模板
     */
    private String contentInspection;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_adminstatistics_home;
    }

    @Override
    protected void initData() {
        mBinding = FragmentAdminstatisticsHomeBinding.bind(mRootView);

        contentTimeMonitor =
                "<font color=\"#373737\">水库总数：</font><font color=\"#3951ad\">%1$s</font><font color=\"#373737\">宗　</font>"
                        + "<font color=\"#373737\">监测：</font><font color=\"#3951ad\">%2$s</font><font color=\"#373737\">宗　</font>"
                        + "<font color=\"#373737\">数据上报：</font><font color=\"#36b23\">%3$s</font><font color=\"#373737\">宗</font>";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            mBinding.includeMonitor.tvDesDataJiance.setText(
                    Html.fromHtml(String.format(contentTimeMonitor, "--", "--", "--"), Html.FROM_HTML_MODE_LEGACY));
        } else {
            mBinding.includeMonitor.tvDesDataJiance
                    .setText(Html.fromHtml(String.format(contentTimeMonitor, "--", "--", "--")));
        }

        contentInspection =
                "<font color=\"#373737\">应巡：</font><font color=\"#3951ad\">%1$s</font><font color=\"#373737\">宗　</font>"
                        + "<font color=\"#373737\">实巡：</font><font color=\"#3951ad\">%2$s</font><font color=\"#373737\">宗　</font>"
                        + "<font color=\"#373737\">有效巡查：</font><font color=\"#36b23\">%3$s</font><font color=\"#373737\">宗</font>";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            mBinding.includePatrol.tvDesDataXucha.setText(
                    Html.fromHtml(String.format(contentInspection, "--", "--", "--"), Html.FROM_HTML_MODE_LEGACY));
        } else {
            mBinding.includePatrol.tvDesDataXucha
                    .setText(Html.fromHtml(String.format(contentInspection, "--", "--", "--")));
        }

        // 动态设置背景色
        /*************    实时监测部分      ************/
        setViewColor(mBinding.includeMonitor.tvDataCxq, "#98C9F2", 12);
        setViewColor(mBinding.includeMonitor.tvDataYlgj, "#FCA072", 12);
        setViewColor(mBinding.includeMonitor.tvDataSlyc, "#EE888B", 12);
        setViewColor(mBinding.includeMonitor.tvDataRwsz, "#EDC558", 12);

        /*************    巡查统计部分    ************/
        setViewColor(mBinding.includePatrol.tvDataXqgj, "#E6232A", 12);
        setViewColor(mBinding.includePatrol.tvDataZdyh, "#FF9800", 12);
        setViewColor(mBinding.includePatrol.tvDataYbyh, "#EDC558", 12);

        /*************    三个责任人部分      ************/
        setViewColor(mBinding.includePersonIncharge.viewZrrQb, "#619FA7", 4);
        setViewColor(mBinding.includePersonIncharge.viewZrrBf, "#334755", 4);
        setViewColor(mBinding.includePersonIncharge.viewZrrW, "#D13430", 4);
        setViewColor(mBinding.includePersonIncharge.rlZrrXz, "#FFAA89", 112);
        setViewColor(mBinding.includePersonIncharge.rlZrrJs, "#8C6AFC", 112);
        setViewColor(mBinding.includePersonIncharge.rlZrrXc, "#21A1FF", 112);

        /*************    三个重点环节部分      ************/
        setViewColor(mBinding.includeKeylinks.viewZdhjQb, "#98C9F2", 4);
        setViewColor(mBinding.includeKeylinks.viewZdhjBf, "#FF9800", 4);
        setViewColor(mBinding.includeKeylinks.viewZdhjW, "#E6232A", 4);
        setViewColor(mBinding.includeKeylinks.rlZdhjJc, "#9ACBF4", 112);
        setViewColor(mBinding.includeKeylinks.rlZdhjDd, "#F9D77C", 112);
        setViewColor(mBinding.includeKeylinks.rlZdhjYj, "#F28B8F", 112);

        /*************    安全鉴定部分      ************/
        setViewColor(mBinding.includeSafetyappraisal.viewAqjdQb, "#98C9F2", 4);
        setViewColor(mBinding.includeSafetyappraisal.viewAqjdBf, "#FF9800", 4);
        setViewColor(mBinding.includeSafetyappraisal.viewAqjdW, "#E6232A", 4);
        initListen();
    }

    @Override
    protected void initView(View view) {
        tv_right_tianqi = view.findViewById(R.id.tv_right_tianqi);
        tv_right_tianqi.setVisibility(View.VISIBLE);
        tv_center_text = view.findViewById(R.id.tv_center_text);
        tv_center_text.setText("首页");
        tv_left_text = view.findViewById(R.id.tv_left_text);
        tv_left_text.setVisibility(View.GONE);

        initPieChartDataZrr();
        initPieChartDataZdhj();
        initPieChartDataAqjd();

    }

    @Override
    protected void initRequestData() {


    }


    @Override
    public void onResume(){
        super.onResume();
        mPresenter.getRealTimeMonitor(true, "正在获取数据");
        mPresenter.getInspectionStatistics(false, "");
        mPresenter.getThreePersons(false, "");
        mPresenter.getThreeKeyPoints(false, "");
        mPresenter.getSafetyIndentify(false, "");
        // 获取用户信息
        getUserInfo();
        // 获取天气预警
        getWeatherWarn();
    }

    /**
     * 三个责任人图表控件初始化
     */
    private void initPieChartDataZrr() {
        mBinding.includePersonIncharge.piechartZrr.setUsePercentValues(true);
        mBinding.includePersonIncharge.piechartZrr.getDescription().setEnabled(false);
        mBinding.includePersonIncharge.piechartZrr.setExtraOffsets(5, 10, 5, 5);

        mBinding.includePersonIncharge.piechartZrr.setDragDecelerationFrictionCoef(0.95f);

        // 设置中间文件
        // mBinding.includePersonIncharge.piechartZrr.setCenterText("131宗");

        mBinding.includePersonIncharge.piechartZrr.setDrawHoleEnabled(true);
        mBinding.includePersonIncharge.piechartZrr.setHoleColor(Color.WHITE);

        mBinding.includePersonIncharge.piechartZrr.setTransparentCircleColor(Color.WHITE);
        mBinding.includePersonIncharge.piechartZrr.setTransparentCircleAlpha(110);

        mBinding.includePersonIncharge.piechartZrr.setHoleRadius(58f);
        mBinding.includePersonIncharge.piechartZrr.setTransparentCircleRadius(61f);

        mBinding.includePersonIncharge.piechartZrr.setDrawCenterText(true);

        mBinding.includePersonIncharge.piechartZrr.setRotationAngle(0);
        // 触摸旋转
        mBinding.includePersonIncharge.piechartZrr.setRotationEnabled(false);
        mBinding.includePersonIncharge.piechartZrr.setHighlightPerTapEnabled(true);

        mBinding.includePersonIncharge.piechartZrr.animateY(1400, Easing.EaseInOutQuad);

        Legend lengend = mBinding.includePersonIncharge.piechartZrr.getLegend();
        lengend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        lengend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        lengend.setOrientation(Legend.LegendOrientation.VERTICAL);
        lengend.setDrawInside(false);
        lengend.setEnabled(false);
        lengend.setXEntrySpace(7f);
        lengend.setYEntrySpace(0f);
        lengend.setYOffset(0f);

        // 输入标签样式
        mBinding.includePersonIncharge.piechartZrr.setEntryLabelColor(Color.WHITE);
        mBinding.includePersonIncharge.piechartZrr.setEntryLabelTextSize(12f);

        // // 设置数据
        // setPersonInchargeData();

    }

    /**
     * 三个重点环节图表控件初始化
     */
    private void initPieChartDataZdhj() {
        mBinding.includeKeylinks.piechartZdhj.setUsePercentValues(true);
        mBinding.includeKeylinks.piechartZdhj.getDescription().setEnabled(false);
        mBinding.includeKeylinks.piechartZdhj.setExtraOffsets(5, 10, 5, 5);

        mBinding.includeKeylinks.piechartZdhj.setDragDecelerationFrictionCoef(0.95f);
        // 设置中间文件
        // mBinding.includeKeylinks.piechartZdhj.setCenterText("131宗");

        mBinding.includeKeylinks.piechartZdhj.setDrawHoleEnabled(true);
        mBinding.includeKeylinks.piechartZdhj.setHoleColor(Color.WHITE);

        mBinding.includeKeylinks.piechartZdhj.setTransparentCircleColor(Color.WHITE);
        mBinding.includeKeylinks.piechartZdhj.setTransparentCircleAlpha(110);

        mBinding.includeKeylinks.piechartZdhj.setHoleRadius(58f);
        mBinding.includeKeylinks.piechartZdhj.setTransparentCircleRadius(61f);

        mBinding.includeKeylinks.piechartZdhj.setDrawCenterText(true);

        mBinding.includeKeylinks.piechartZdhj.setRotationAngle(0);
        // 触摸旋转
        mBinding.includeKeylinks.piechartZdhj.setRotationEnabled(true);
        mBinding.includeKeylinks.piechartZdhj.setHighlightPerTapEnabled(true);

        mBinding.includeKeylinks.piechartZdhj.animateY(1400, Easing.EaseInOutQuad);

        Legend lengend = mBinding.includeKeylinks.piechartZdhj.getLegend();
        lengend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        lengend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        lengend.setOrientation(Legend.LegendOrientation.VERTICAL);
        lengend.setDrawInside(false);
        lengend.setEnabled(false);
        lengend.setXEntrySpace(7f);
        lengend.setYEntrySpace(0f);
        lengend.setYOffset(0f);

        // 输入标签样式
        mBinding.includeKeylinks.piechartZdhj.setEntryLabelColor(Color.WHITE);
        mBinding.includeKeylinks.piechartZdhj.setEntryLabelTextSize(12f);

    }

    /**
     * 安全鉴定图表控件初始化
     */
    private void initPieChartDataAqjd() {
        mBinding.includeSafetyappraisal.piechartAqjd.setUsePercentValues(true);
        mBinding.includeSafetyappraisal.piechartAqjd.getDescription().setEnabled(false);
        mBinding.includeSafetyappraisal.piechartAqjd.setExtraOffsets(5, 10, 5, 5);

        mBinding.includeSafetyappraisal.piechartAqjd.setDragDecelerationFrictionCoef(0.95f);
        // 设置中间文件
        // mBinding.includeSafetyappraisal.piechartAqjd.setCenterText("131宗");

        mBinding.includeSafetyappraisal.piechartAqjd.setDrawHoleEnabled(true);
        mBinding.includeSafetyappraisal.piechartAqjd.setCenterTextSize(16);
        mBinding.includeSafetyappraisal.piechartAqjd.setHoleColor(Color.WHITE);

        mBinding.includeSafetyappraisal.piechartAqjd.setTransparentCircleColor(Color.WHITE);
        mBinding.includeSafetyappraisal.piechartAqjd.setTransparentCircleAlpha(110);

        mBinding.includeSafetyappraisal.piechartAqjd.setHoleRadius(58f);
        mBinding.includeSafetyappraisal.piechartAqjd.setTransparentCircleRadius(61f);

        mBinding.includeSafetyappraisal.piechartAqjd.setDrawCenterText(true);

        mBinding.includeSafetyappraisal.piechartAqjd.setRotationAngle(0);
        // 触摸旋转
        mBinding.includeSafetyappraisal.piechartAqjd.setRotationEnabled(true);
        mBinding.includeSafetyappraisal.piechartAqjd.setHighlightPerTapEnabled(true);

        mBinding.includeSafetyappraisal.piechartAqjd.animateY(1400, Easing.EaseInOutQuad);

        Legend lengend = mBinding.includeSafetyappraisal.piechartAqjd.getLegend();
        lengend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        lengend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        lengend.setOrientation(Legend.LegendOrientation.VERTICAL);
        lengend.setDrawInside(false);
        lengend.setEnabled(false);
        lengend.setXEntrySpace(7f);
        lengend.setYEntrySpace(0f);
        lengend.setYOffset(0f);

        // 输入标签样式
        mBinding.includeSafetyappraisal.piechartAqjd.setEntryLabelColor(Color.WHITE);
        mBinding.includeSafetyappraisal.piechartAqjd.setEntryLabelTextSize(12f);

    }

    @Override
    public void showRealTimeMonitor(RealTimeMonitorResponse data) {
        RealTimeMonitorBean bean = data.getData();
        if (bean != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                realTimeMonitorBean = bean;
                mBinding.includeMonitor.tvDesDataJiance
                        .setText(Html.fromHtml(String.format(contentTimeMonitor, bean.getAllCount(),
                                bean.getMonitorCount(), bean.getReportCount()), Html.FROM_HTML_MODE_LEGACY));
            } else {
                mBinding.includeMonitor.tvDesDataJiance.setText(Html.fromHtml(String.format(contentTimeMonitor,
                        bean.getAllCount(), bean.getMonitorCount(), bean.getReportCount())));
            }
            mBinding.includeMonitor.tvDataCxq.setText(bean.getOverLevelConut());
            mBinding.includeMonitor.tvDataYlgj.setText(bean.getPpWarnCount());
            mBinding.includeMonitor.tvDataSlyc.setText(bean.getSeepageCount());
            mBinding.includeMonitor.tvDataRwsz.setText(bean.getArtificialCount());
        }

    }

    @Override
    public void showInspectionStatistics(InspectionStatisticsResponse data) {
        InspectionStatisticsBean bean = data.getData();
        if (bean != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                mBinding.includePatrol.tvDesDataXucha.setText(Html.fromHtml(String.format(contentInspection,
                        bean.getAllCount(), bean.getRelCount(), bean.getEffectiveCount()), Html.FROM_HTML_MODE_LEGACY));
            } else {
                mBinding.includePatrol.tvDesDataXucha.setText(Html.fromHtml(String.format(contentInspection,
                        bean.getAllCount(), bean.getRelCount(), bean.getEffectiveCount())));
            }
            mBinding.includePatrol.tvDataXqgj.setText(bean.getDangerWarnCount());
            mBinding.includePatrol.tvDataZdyh.setText(bean.getSeriousAllCount());
            mBinding.includePatrol.tvDataYbyh.setText(bean.getGeneralAllCount());
            mBinding.includePatrol.tvUndealXqgj.setText(bean.getUnprocessDangerCount() + "宗未处理");
            mBinding.includePatrol.tvUndealZdyh.setText(bean.getSeriousCount() + "宗未处理");
            mBinding.includePatrol.tvUndealYbyh.setText(bean.getGeneralCount() + "宗未处理");
        }
    }

    @Override
    public void showThreePersons(ThreePersonsResponse data) {
        ThreePersonsBean bean = data.getData();
        if (bean != null) {
            mBinding.includePersonIncharge.piechartZrr.setCenterText(bean.getAllCount() + "宗");
            List<PieEntry> pieEntryList = new ArrayList<>();
            // 全部落实数量
            mBinding.includePersonIncharge.tvZrrQb.setText(bean.getAllUserCount() + "宗全部落实");
            pieEntryList.add(new PieEntry(bean.getAllUserCount(), ""));
            // 部分落实数量
            int part = bean.getAllCount() - bean.getAllUserCount() - bean.getNoUserCount();
            mBinding.includePersonIncharge.tvZrrBf.setText(part + "宗部分落实");
            pieEntryList.add(new PieEntry(part, ""));
            // 未落实数量
            mBinding.includePersonIncharge.tvZrrW.setText(bean.getNoUserCount() + "宗未落实");
            pieEntryList.add(new PieEntry(bean.getNoUserCount(), ""));
            String[] colors = { "#619FA7", "#334755", "#D13430" };
            setPieData(mBinding.includePersonIncharge.piechartZrr, pieEntryList, colors);

            // 行政责任人数据
            mBinding.includePersonIncharge.tvZrrXz.setText(bean.getXzUserCount() + "人("
                    + calculatePercent(bean.getXzReservoirCount(), bean.getAllCount()) + "%)");
            // 技术责任人数据
            mBinding.includePersonIncharge.tvZrrJs.setText(bean.getJsUserCount() + "人("
                    + calculatePercent(bean.getJsReservoirCount(), bean.getAllCount()) + "%)");
            // 巡查责任人数据
            mBinding.includePersonIncharge.tvZrrXc.setText(bean.getXcUserCount() + "人("
                    + calculatePercent(bean.getXcReservoirCount(), bean.getAllCount()) + "%)");

        }
    }

    @Override
    public void showThreeKeyPoints(ThreeKeyPointResponse data) {
        ThreeKeyPointBean bean = data.getData();
        if (bean != null) {
            mBinding.includeKeylinks.piechartZdhj.setCenterText(bean.getAllCount() + "宗");
            List<PieEntry> pieEntryList = new ArrayList<>();
            // 全部落实数量
            mBinding.includeKeylinks.tvZdhjQb.setText(bean.getAllPointCount() + "宗全部落实");
            pieEntryList.add(new PieEntry(bean.getAllPointCount(), ""));
            // 部分落实数量
            int part = bean.getAllCount() - bean.getAllPointCount() - bean.getNoPointCount();
            mBinding.includeKeylinks.tvZdhjBf.setText(part + "宗部分落实");
            pieEntryList.add(new PieEntry(part, ""));
            // 未落实数量
            mBinding.includeKeylinks.tvZdhjW.setText(bean.getNoPointCount() + "宗未落实");
            pieEntryList.add(new PieEntry(bean.getNoPointCount(), ""));
            String[] colors = { "#98C9F2", "#FF9800", "#E6232A" };
            setPieData(mBinding.includeKeylinks.piechartZdhj, pieEntryList, colors);

            // 监测设施数据
            mBinding.includeKeylinks.tvZdhjJc.setText(bean.getDeviceCount() + "个("
                    + calculatePercent(bean.getDeviceReservoirCount(), bean.getAllCount()) + "%)");
            // 调度方案数据
            mBinding.includeKeylinks.tvZdhjDd.setText(bean.getDdPlanCount() + "个("
                    + calculatePercent(bean.getDdReservoirCount(), bean.getAllCount()) + "%)");
            // 应急预案数据
            mBinding.includeKeylinks.tvZdhjYj.setText(bean.getYjPlanCount() + "个("
                    + calculatePercent(bean.getYjReservoirCount(), bean.getAllCount()) + "%)");
        }
    }

    /**
     *
     * 计算百分比
     *
     * @param data1
     * @param data2
     * @return
     */
    private String calculatePercent(int data1, int data2) {
        BigDecimal bg1 = new BigDecimal(data1);
        BigDecimal bg2 = new BigDecimal(data2);
        BigDecimal result = bg1.divide(bg2, 0, BigDecimal.ROUND_UP).multiply(new BigDecimal(100));
        return result.toString();
    }

    @Override
    public void showSafetyIndentify(SafetyIdentifyResponse data) {
        SafetyIdentifyBean bean = data.getData();
        if (bean != null) {
            mBinding.includeSafetyappraisal.piechartAqjd.setCenterText(bean.getAllCount() + "宗");
            List<PieEntry> pieEntryList = new ArrayList<>();
            // 全部落实数量
            mBinding.includeSafetyappraisal.tvAqjdQb.setText(bean.getAllIdentifyCount() + "宗全部落实");
            pieEntryList.add(new PieEntry(bean.getAllIdentifyCount(), ""));
            // 部分落实数量
            int part = bean.getAllCount() - bean.getAllIdentifyCount() - bean.getAllNoCount();
            mBinding.includeSafetyappraisal.tvAqjdBf.setText(part + "宗部分落实");
            pieEntryList.add(new PieEntry(part, ""));
            // 未落实数量
            mBinding.includeSafetyappraisal.tvAqjdW.setText(bean.getAllNoCount() + "宗未落实");
            String[] colors = { "#98C9F2", "#FF9800", "#E6232A" };
            pieEntryList.add(new PieEntry(bean.getAllNoCount(), ""));
            setPieData(mBinding.includeSafetyappraisal.piechartAqjd, pieEntryList, colors);
        }

    }

    @Override
    public void onFail(String msg) {
        ToastUtils.shortToast(msg);
    }

    /**
     *
     *  设置圆角背景
     *
     * @param view
     * @param color
     * @param radius
     */
    private void setViewColor(View view, String color, float radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(radius);
        drawable.setColor(Color.parseColor(color));
        view.setBackground(drawable);
    }

    /**
     *
     * 设置piechart数据
     *
     * @param pieChart PieChart实体对象
     * @param entries 数据
     * @param colors 数据颜色
     */
    private void setPieData(PieChart pieChart, List<PieEntry> entries, String[] colors) {
        boolean isAllDataZero = true;
        for (PieEntry entry : entries) {
            if (entry.getValue() != 0) {
                isAllDataZero = false;
                break;
            }
        }
        if (!isAllDataZero) {
            PieDataSet dataSet = new PieDataSet(entries, "");
            dataSet.setSliceSpace(0f);
            dataSet.setSelectionShift(5f);

            // 数据和颜色
            ArrayList<Integer> colorsList = new ArrayList<Integer>();
            for (String color : colors) {
                colorsList.add(Color.parseColor(color));
            }
            dataSet.setColors(colorsList);

            dataSet.setDrawValues(false);
            PieData data = new PieData(dataSet);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(11f);
            data.setValueTextColor(Color.WHITE);
            pieChart.setData(data);
            pieChart.highlightValues(null);
            // 刷新
            pieChart.invalidate();
        } else {
            entries = new ArrayList<>();
            entries.add(new PieEntry(1, ""));
            PieDataSet dataSet = new PieDataSet(entries, "");
            dataSet.setSliceSpace(0f);
            dataSet.setSelectionShift(5f);

            // 数据和颜色
            ArrayList<Integer> colorsList = new ArrayList<Integer>();
            colorsList.add(Color.parseColor("#333333"));
            dataSet.setColors(colorsList);

            dataSet.setDrawValues(false);
            PieData data = new PieData(dataSet);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(11f);
            data.setValueTextColor(Color.WHITE);
            pieChart.setData(data);
            pieChart.highlightValues(null);
            // 刷新
            pieChart.invalidate();
        }
    }

    /**
     * 获取用户信息
     */
    private void getUserInfo() {
        UserManager.getInstance_ADMIN().getLoginUser().subscribe(new LoadingSubject<UserInfoBean>(true, "") {
            @Override
            protected void _onNext(UserInfoBean userInfoBean) {
                if (userInfoBean != null) {
                    if (userInfoBean.getCode() == 0) {
                        String roleName = "";
                        if (TextUtils.isEmpty(roleName)) {
                            mBinding.tvPosition.setText(userInfoBean.getData().getOfficeName());

                        } else {
                            mBinding.tvPosition.setText(roleName);

                        }
                        mBinding.tvUsername.setText(userInfoBean.getData().getUserName());

                    } else {
                        ToastUtils.longToast(userInfoBean.getMsg());

                    }
                }
            }

            @Override
            protected void _onError(String message) {
                UserInfoBean userInfoBean = UserManager.getInstance().getUserBean();
                if (userInfoBean != null) {
                    mBinding.tvUsername.setText(userInfoBean.getData().getUserName());
                }
                LogUtil.e("getLoginUser:获取用户信息失败-----");
            }
        });
    }

    /**
     * 获取天气预警
     */
    private void getWeatherWarn() {
        UserManager.getInstance_Monitor().getWeatherWarn().subscribe(new LoadingSubject<WeatherWarnBean>() {
            @Override
            protected void _onNext(WeatherWarnBean weatherWarnBean) {
                // 2019/5/10日晚测试没有预警
                if (weatherWarnBean.getCode() == 0) {
                    if (weatherWarnBean != null && weatherWarnBean.getData() != null) {
                        mBinding.tianqiLy.setVisibility(View.VISIBLE);
                        mBinding.wuTv.setVisibility(View.GONE);

                        WeatherWarnBean.DataBean dataBean = weatherWarnBean.getData();
                        // contentOfWeather = dataBean.getCategoryCnname()+dataBean.getDepartmentlevelCnname();
                        mBinding.tianqiTv.setText(dataBean.getContent());
                        mBinding.tianqiTimeTv.setText("发布时间：" + dataBean.getPublishdate());
                        if (!TextUtils.isEmpty(dataBean.getIcon())) {
                            int identifier = getResources().getIdentifier("a" + dataBean.getIcon(), "mipmap",
                                    "com.tepia.reservoirxuncha");
                            if (identifier != 0) {
                                mBinding.tianqiOfpicIv.setImageResource(identifier);
                                // setClickOftianqi(dataBean);
                            } else {
                                mBinding.tianqiOfpicIv.setImageResource(R.mipmap.a11);
                            }
                        } else {
                            mBinding.tianqiOfpicIv.setImageResource(R.mipmap.a11);

                        }

                    } else {
                        mBinding.tianqiLy.setVisibility(View.GONE);
                        mBinding.wuTv.setVisibility(View.VISIBLE);

                    }
                } else {
                    mBinding.tianqiLy.setVisibility(View.GONE);
                    mBinding.wuTv.setVisibility(View.VISIBLE);

                }

            }

            @Override
            protected void _onError(String message) {
                mBinding.tianqiLy.setVisibility(View.GONE);
                mBinding.wuTv.setVisibility(View.VISIBLE);
                LogUtil.e(MainFragment.class.getName(), message + " 天气");
                // contentOfWeather = "";
            }
        });
    }

    @Override
    public void onClick(View v) {
        // 水库检索
        if (v.getId() == R.id.tv_search) {
            Intent intent = new Intent();
            intent.setClass(getBaseActivity(), ReserviorSearchListActivity.class);
            startActivity(intent);
        }
        // 实时监测
        else if (v.getId() == R.id.areaReserviorRy) {
            Intent intent = new Intent();
            intent.setClass(getBaseActivity(), AreaReservoirCountActivity.class);
            if (realTimeMonitorBean != null) {
                intent.putExtra("title", String.format(contentTimeMonitor, realTimeMonitorBean.getAllCount(),
                        realTimeMonitorBean.getMonitorCount(), realTimeMonitorBean.getReportCount()));
            }else{
                intent.putExtra("title", String.format(contentTimeMonitor, "--",
                        "--", "--"));
            }

            startActivity(intent);
        }
        // 查看台风信息
        else if (v.getId() == R.id.tianqiLy || v.getId() == R.id.wuTv) {
            // 无网络状态
            if (!NetUtil.isNetworkConnected(Utils.getContext())) {
                ToastUtils.shortToast("功能需在有网络情况下使用，以便更新获取台风实时信息。");
            } else {
                startActivity(new Intent(getActivity(), TyphoonRouteActivity.class));
            }
        }
        // 超汛限
        else if (v.getId() == R.id.ll_cxx) {
            Intent intent_cxx = new Intent(getActivity(), ReservoirRegimenActivity.class);
//            intent_cxx.putExtra("");
//            intent_cxx.putExtra("")
            startActivity(intent_cxx);
        }
        // 雨量告警
        else if (v.getId() == R.id.ll_ylgj){
            startActivity(new Intent(getActivity(), RainfallStatisticsActivity.class));
        }
        // 渗流异常
        else if (v.getId() == R.id.ll_slyc){
            startActivity(new Intent(getActivity(), SeepageAnomalousActivity.class));
        }

    }

    /**
     * 点击事件添加
     */
    private void initListen() {
        mBinding.tvSearch.setOnClickListener(this);
        mBinding.includeMonitor.areaReserviorRy.setOnClickListener(this);
        mBinding.tianqiLy.setOnClickListener(this);
        mBinding.wuTv.setOnClickListener(this);
        mBinding.includeMonitor.llCxx.setOnClickListener(this);
        mBinding.includeMonitor.llYlgj.setOnClickListener(this);
//        mBinding.includeMonitor.llSlyc.setOnClickListener(this);
    }
}
