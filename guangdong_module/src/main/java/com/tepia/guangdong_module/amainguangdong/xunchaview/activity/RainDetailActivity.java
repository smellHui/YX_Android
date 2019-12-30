package com.tepia.guangdong_module.amainguangdong.xunchaview.activity;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.guangdong_module.R;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.data.CombinedData;
import com.tepia.base.mvp.BaseActivity;
import com.tepia.guangdong_module.amainguangdong.common.UserManager;
import com.tepia.guangdong_module.amainguangdong.model.reservoirdetail.RainDetailBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.ReservoirBean;
import com.tepia.guangdong_module.amainguangdong.mvp.reservoirDetail.ReservoirDetailContract;
import com.tepia.guangdong_module.amainguangdong.mvp.reservoirDetail.ReservoirDetailPresenter;
import com.tepia.guangdong_module.amainguangdong.utils.CombinedChartManager;
import com.tepia.guangdong_module.amainguangdong.utils.MyCombinedChartMarkView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by      Android studio
 *
 * @author :wwj (from Center Of Wuhan)
 * Date    :2019/5/28
 * Version :1.0
 * 功能描述 : 雨情详情页
 **/
public class RainDetailActivity extends BaseActivity {
    private String[] searchDateType = {"今日", "昨日"};
    private String[] searchDateTypeTwo = {"7日", "15日", "30日"};
    private CombinedChart mCombinedChart;
    private Spinner spinnerHour;
    private Spinner spinnerDay;
    private CombinedChart mCombinedChartDay;
    private List<Integer> colors;
    private CombinedChartManager combineChartManager1;
    private CombinedChartManager combineChartManagerDay;
    private TextView tvHourDate;
    private TextView tvHourDateEnd;
    private int hourPos = 0;
    private int dayPos = 0;
    private TextView tvDayDate;
    private TextView tvDayDateEnd;
    private ReservoirDetailPresenter hourPresenter;
    private ReservoirDetailPresenter dayPresenter;
    private ReservoirBean defaultReservoir;

    @Override
    public int getLayoutId() {
        return R.layout.activity_rain_detail;
    }

    @Override
    public void initData() {
        defaultReservoir = UserManager.getInstance().getDefaultReservoir();
        hourPresenter = new ReservoirDetailPresenter();
        hourPresenter.attachView(new ReservoirDetailContract.View<RainDetailBean>() {
            @Override
            public void success(RainDetailBean data) {
                if (data != null) {
                    RainDetailBean.DataBean dataBean = data.getData();
                    if (dataBean != null) {
                        List<RainDetailBean.DataBean.StPptnRsBean> dataList = dataBean.getStPptnRs();
                        if (dataList != null && dataList.size() > 0) {
                            //x轴数据
                            List<String> xData = new ArrayList<>();
                            //y轴柱状图数
                            List<Float> yBarData = new ArrayList<>();
                            //y轴折线图数
                            List<Float> yLineData = new ArrayList<>();
                            for (int i = 0; i < dataList.size(); i++) {
                                String tm = dataList.get(i).getTm();
                                xData.add(tm == null ? "" : tm);
                                double drp = dataList.get(i).getDrp();
                                double sumDrp = 0;
                                for (int j = 0; j <= i; j++) {
                                    sumDrp += dataList.get(j).getDrp();
                                }
                                yBarData.add((float) drp);
                                yLineData.add((float) sumDrp);
                            }
                            combineChartManager1.showCombinedChart(getApplication(),xData, yBarData, yLineData,
                                    "小时雨量", "累计雨量", colors.get(0), colors.get(1),true);
                        } else {
                            mCombinedChart.setData(null);
                            mCombinedChart.setNoDataText("暂无数据");
                            mCombinedChart.notifyDataSetChanged();
                            mCombinedChart.invalidate();
                        }
                    } else {
                        mCombinedChart.setData(null);
                        mCombinedChart.setNoDataText("暂无数据");
                        mCombinedChart.notifyDataSetChanged();
                        mCombinedChart.invalidate();
                    }
                } else {
                    mCombinedChart.setData(null);
                    mCombinedChart.setNoDataText("暂无数据");
                    mCombinedChart.notifyDataSetChanged();
                    mCombinedChart.invalidate();
                }
            }

            @Override
            public void failure(String msg) {
                mCombinedChart.setData(null);
                mCombinedChart.setNoDataText(msg);
                mCombinedChart.notifyDataSetChanged();
                mCombinedChart.invalidate();
            }

            @Override
            public Context getContext() {
                return null;
            }
        });

        dayPresenter = new ReservoirDetailPresenter();
        dayPresenter.attachView(new ReservoirDetailContract.View<RainDetailBean>() {
            @Override
            public void success(RainDetailBean data) {
                if (data != null) {
                    RainDetailBean.DataBean dataBean = data.getData();
                    if (dataBean != null) {
                        List<RainDetailBean.DataBean.StPptnRsBean> dataList = dataBean.getStPptnRs();
                        if (dataList != null && dataList.size() > 0) {
                            //x轴数据
                            List<String> xData = new ArrayList<>();
                            //y轴柱状图数
                            List<Float> yBarData = new ArrayList<>();
                            //y轴折线图数
                            List<Float> yLineData = new ArrayList<>();
                            for (int i = 0; i < dataList.size(); i++) {
                                String tm = dataList.get(i).getTm();
                                xData.add(tm == null ? "" : tm);
                                double drp = dataList.get(i).getDrp();
                                double sumDrp = 0;
                                for (int j = 0; j <= i; j++) {
                                    sumDrp += dataList.get(j).getDrp();
                                }
                                yBarData.add((float) drp);
                                yLineData.add((float) sumDrp);
                            }
                            combineChartManagerDay.showCombinedChart(getApplicationContext(),xData, yBarData, yLineData,
                                    "日雨量", "累计雨量", colors.get(0), colors.get(1),false);
                        } else {
                            mCombinedChartDay.setData(null);
                            mCombinedChartDay.setNoDataText("暂无数据");
                            mCombinedChartDay.notifyDataSetChanged();
                            mCombinedChartDay.invalidate();
                        }
                    } else {
                        mCombinedChartDay.setData(null);
                        mCombinedChartDay.setNoDataText("暂无数据");
                        mCombinedChartDay.notifyDataSetChanged();
                        mCombinedChartDay.invalidate();
                    }
                } else {
                    mCombinedChartDay.setData(null);
                    mCombinedChartDay.setNoDataText("暂无数据");
                    mCombinedChartDay.notifyDataSetChanged();
                    mCombinedChartDay.invalidate();
                }
            }

            @Override
            public void failure(String msg) {
                mCombinedChartDay.setData(null);
                mCombinedChartDay.setNoDataText(msg);
                mCombinedChartDay.notifyDataSetChanged();
                mCombinedChartDay.invalidate();
            }

            @Override
            public Context getContext() {
                return null;
            }
        });
    }

    @Override
    public void initView() {
        //颜色集合
        colors = new ArrayList<>();
        colors.add(Color.parseColor("#3B96E0"));
        colors.add(Color.parseColor("#FF7D7D"));
        colors.add(Color.parseColor("#C23531"));
        colors.add(Color.parseColor("#E38800"));
        if (defaultReservoir!=null){
            String reservoir = defaultReservoir.getReservoir();
            setCenterTitle(reservoir+"雨情信息");
        }else {
            setCenterTitle("雨情信息");
        }
        showBack();
        spinnerHour = findViewById(R.id.spinner_hour);
        spinnerDay = findViewById(R.id.spinner_day);
        mCombinedChart = findViewById(R.id.manager_chart);
        mCombinedChartDay = findViewById(R.id.manager_chart_day);
        tvHourDate = findViewById(R.id.tv_hour_date);
        tvHourDateEnd = findViewById(R.id.tv_hour_date_end);
        tvDayDate = findViewById(R.id.tv_day_date);
        tvDayDateEnd = findViewById(R.id.tv_day_date_end);
        initBarTitle();
        initCombinedChart();
        initSpinner();
        initHourDate();
        initDayDate();
        initRequestResponse();
    }

    /**
     * 设置小时降雨量时间段时间段
     */
    private void initHourDate() {
        //得到一个Calendar的实例
        Calendar ca = Calendar.getInstance();
        //设置时间为当前时间
        ca.setTime(new Date());
        //昨天减1
        if (hourPos == 0) {
            //今天
            ca.add(Calendar.DATE, 0);
        } else {
            //昨天
            ca.add(Calendar.DATE, -1);
        }
        Date startDate = ca.getTime();
        String hourStr = dateToStrLong(startDate);
        tvHourDate.setText(hourStr + " 00:00");
        tvHourDateEnd.setText(hourStr + " 23:59");
    }

    /**
     * 设置日雨量时间段
     */
    private void initDayDate() {
        //设置日雨量时间
        //得到一个Calendar的实例
        Calendar caDay = Calendar.getInstance();
        //设置时间为当前时间
        caDay.setTime(new Date());

        Date startDayDate = caDay.getTime();
        String hourDayStr = dateToStrLongDay(startDayDate);
        tvDayDateEnd.setText(hourDayStr);
        //昨天减1
        if (dayPos == 0) {
            //7日
            caDay.add(Calendar.DATE, -7);
        } else if (dayPos == 1) {
            //15日
            caDay.add(Calendar.DATE, -15);
        } else {
            caDay.add(Calendar.DATE, -30);
        }
        Date startDayDateEnd = caDay.getTime();
        String hourDayStrEnd = dateToStrLongDay(startDayDateEnd);
        tvDayDate.setText(hourDayStrEnd);
    }

    public static String dateToStrLong(Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    public static String dateToStrLongDay(Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    private void initRequestResponse() {
        requestHourRainData();
        requestDayRainData();
    }

    /**
     * 获取小时雨量
     */
    private void requestHourRainData() {
        String reservoirId = defaultReservoir.getReservoirId();
        Calendar cd = Calendar.getInstance();
        int year = cd.get(Calendar.YEAR);
        String strHour = tvHourDate.getText().toString();
        String strHourEnd = tvHourDateEnd.getText().toString();
        String startDate = year+"-"+strHour+":00";
        String endDate = year+"-"+strHourEnd+":59";
        hourPresenter.getRainDetailList(reservoirId, startDate, endDate, "time");
    }

    /**
     * 获取日雨量
     */
    private void requestDayRainData() {
        String reservoirId = defaultReservoir.getReservoirId();
        Calendar cd = Calendar.getInstance();
        int year = cd.get(Calendar.YEAR);
        String strDay = tvDayDate.getText().toString();
        String strDayEnd = tvDayDateEnd.getText().toString();
        String startDate = strDay+" 00:00:00";
        String endDate = strDayEnd+" 23:59:59";
        dayPresenter.getRainDetailList(reservoirId, startDate, endDate, "day");
    }

    private void initSpinner() {
        ArrayAdapter spinnerAdapter = new ArrayAdapter<String>(this, R.layout.spinner_rain_detail, searchDateType);
        //设置下拉列表的风格
        spinnerAdapter.setDropDownViewResource(R.layout.dropdown_stytle);
        //将adapter2 添加到spinner中
        spinnerHour.setAdapter(spinnerAdapter);
        //添加事件Spinner事件监听
        spinnerHour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                hourPos = pos;
                initHourDate();
                requestHourRainData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
        ArrayAdapter spinnerDayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_rain_detail, searchDateTypeTwo);
        //设置下拉列表的风格
        spinnerDayAdapter.setDropDownViewResource(R.layout.dropdown_stytle);
        //将adapter2 添加到spinner中
        spinnerDay.setAdapter(spinnerDayAdapter);
        //添加事件Spinner事件监听
        spinnerDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                dayPos = pos;
                initDayDate();
                requestDayRainData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }

    /**
     * 初始化组合图表
     */
    private void initCombinedChart() {
        //x轴数据
        List<String> xData = new ArrayList<>();
        for (int i = 0; i <= 20; i++) {
            xData.add(String.valueOf(i));
        }
        //y轴数据集合
        List<List<Float>> yBarDatas = new ArrayList<>();
        //4种直方图
        for (int i = 0; i < 4; i++) {
            //y轴数
            List<Float> yData = new ArrayList<>();
            for (int j = 0; j <= 20; j++) {
                yData.add((float) (Math.random() * 100));
            }
            yBarDatas.add(yData);
        }
        //y轴数据集合
        List<List<Float>> yLineDatas = new ArrayList<>();
        //4种直方图
        for (int i = 0; i < 4; i++) {
            //y轴数
            List<Float> yData = new ArrayList<>();
            for (int j = 0; j <= 20; j++) {
                yData.add((float) (Math.random() * 200));
            }
            yLineDatas.add(yData);
        }
        //管理类
        combineChartManager1 = new CombinedChartManager(mCombinedChart);
     /*   combineChartManager1.showCombinedChart(xData, yBarDatas.get(0), yLineDatas.get(0),
                "小时雨量", "累计雨量", colors.get(0), colors.get(1));*/

        combineChartManagerDay = new CombinedChartManager(mCombinedChartDay);
   /*     combineChartManagerDay.showCombinedChart(xData, yBarDatas.get(0), yLineDatas.get(0),
                "日雨量", "累计雨量", colors.get(2), colors.get(3));*/
    }

    private void initBarTitle() {
        TextView tvHourRain = findViewById(R.id.tv_hour_rain);
        TextView tvHourTotalRain = findViewById(R.id.tv_hour_total_rain);
        TextView tvDayRain = findViewById(R.id.tv_day_rain);
        TextView tvDayTotalRain = findViewById(R.id.tv_day_total_rain);
        String strHour = "小时雨量mm";
        String strTotal = "累计雨量mm";
        int fstart = strHour.indexOf("mm");
        int fend = fstart + "mm".length();
        SpannableStringBuilder style = new SpannableStringBuilder(strHour);
        style.setSpan(new ForegroundColorSpan(Color.parseColor("#37A2DA")), fstart, fend, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tvHourRain.setText(style);
        int fTotalstart = strTotal.indexOf("mm");
        int fTotalend = fTotalstart + "mm".length();
        SpannableStringBuilder styleTotal = new SpannableStringBuilder(strTotal);
        styleTotal.setSpan(new ForegroundColorSpan(Color.parseColor("#FF7D7D")), fTotalstart, fTotalend, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tvHourTotalRain.setText(styleTotal);
        String strDay = "日雨量mm";
        String strDayTotal = "累计雨量mm";
        int fDayStart = strDay.indexOf("mm");
        int fDayEnd = fDayStart + "mm".length();
        SpannableStringBuilder styleDay = new SpannableStringBuilder(strDay);
        styleDay.setSpan(new ForegroundColorSpan(Color.parseColor("#37A2DA")), fDayStart, fDayEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tvDayRain.setText(styleDay);
        int fDayTotalstart = strDayTotal.indexOf("mm");
        int fDayTotalend = fDayTotalstart + "mm".length();
        SpannableStringBuilder styleDayTotal = new SpannableStringBuilder(strDayTotal);
        styleDayTotal.setSpan(new ForegroundColorSpan(Color.parseColor("#FF7D7D")), fDayTotalstart, fDayTotalend, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tvDayTotalRain.setText(styleDayTotal);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initRequestData() {

    }
}
