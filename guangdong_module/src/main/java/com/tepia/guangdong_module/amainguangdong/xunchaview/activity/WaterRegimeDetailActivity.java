package com.tepia.guangdong_module.amainguangdong.xunchaview.activity;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.guangdong_module.R;
import com.github.mikephil.charting.charts.LineChart;
import com.tepia.base.mvp.BaseActivity;
import com.tepia.base.utils.ToastUtils;
import com.tepia.guangdong_module.amainguangdong.common.UserManager;
import com.tepia.guangdong_module.amainguangdong.model.reservoirdetail.RainDetailBean;
import com.tepia.guangdong_module.amainguangdong.model.reservoirdetail.WaterRegimeDetailBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.ReservoirBean;
import com.tepia.guangdong_module.amainguangdong.mvp.reservoirDetail.ReservoirDetailContract;
import com.tepia.guangdong_module.amainguangdong.mvp.reservoirDetail.ReservoirDetailPresenter;
import com.tepia.guangdong_module.amainguangdong.utils.CombinedChartManager;
import com.tepia.guangdong_module.amainguangdong.utils.EmptyLayoutUtil;
import com.tepia.guangdong_module.amainguangdong.utils.MyLineChartMarkView;
import com.tepia.guangdong_module.amainguangdong.xunchaview.adapter.reservoirdetail.WaterHistoryListAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by      Android studio
 *
 * @author :wwj (from Center Of Wuhan)
 * Date    :2019/5/28
 * Version :1.0
 * 功能描述 : 水情详情
 **/
public class WaterRegimeDetailActivity extends BaseActivity {
    private Spinner spinnerHour;
    private String[] searchDateType = {"今日", "昨日"};
    private int hourPos = 0;
    private TextView tvHourDate;
    private TextView tvHourDateEnd;
    private ReservoirBean defaultReservoir;
    private ReservoirDetailPresenter hourPresenter;
    private RecyclerView rvWater;
    private List<WaterRegimeDetailBean.DataBean> waterDataList;
    private WaterHistoryListAdapter waterListAdapter;
    private LineChart mLineChart;

    @Override
    public int getLayoutId() {
        return R.layout.activity_water_regime_detail;
    }

    @Override
    public void initData() {
        defaultReservoir = UserManager.getInstance().getDefaultReservoir();
        hourPresenter = new ReservoirDetailPresenter();
        hourPresenter.attachView(new ReservoirDetailContract.View<WaterRegimeDetailBean>() {
            @Override
            public void success(WaterRegimeDetailBean dataBean) {
                waterDataList.clear();
                List<WaterRegimeDetailBean.DataBean> data = dataBean.getData();
                if (data != null && data.size() > 0) {
                    waterDataList.addAll(data);
                    waterListAdapter.notifyDataSetChanged();
                    //设置折线图
                    ArrayList<List<Float>> waterList = new ArrayList<>();
                    List<String> xValues = new ArrayList<>();
                    List<Float> yValues = new ArrayList<>();
                    List<Float> yValues2 = new ArrayList<>();
                    //汛限水位
                    List<Float> yFloodSeasonValues = new ArrayList<>();
                    //设计洪水位
                    List<Float> yDesignSeasonValues = new ArrayList<>();
                    //校核洪水位
                    List<Float> yCheckSeasonValues = new ArrayList<>();
                    for (int i = 0; i < data.size(); i++) {
                        xValues.add(data.get(data.size() - i - 1).getRrTime());
                        yValues.add((float) data.get(data.size() - i - 1).getRz());
                        float w = (float) data.get(data.size() - i - 1).getW();
                        yValues2.add(w);
                        yFloodSeasonValues.add((float) data.get(data.size() - i - 1).getFloodSeasonWaterLevel());
                        yDesignSeasonValues.add((float) data.get(data.size() - i - 1).getDesignSeasonWaterLevel());
                        yCheckSeasonValues.add((float) data.get(data.size() - i - 1).getCheckSeasonWaterLevel());
                    }
                    waterList.add(yFloodSeasonValues);
                    waterList.add(yDesignSeasonValues);
                    waterList.add(yCheckSeasonValues);

                    ArrayList<Integer> colors = new ArrayList<>();
                    colors.add(Color.parseColor("#3B96E0"));
                    colors.add(Color.parseColor("#FF7D7D"));
                    colors.add(Color.parseColor("#C23531"));
                    setLineChartData(xValues, yValues, yValues2,waterList,colors);
                } else {
                    waterListAdapter.setEmptyView(EmptyLayoutUtil.show("暂无数据"));
                    waterListAdapter.notifyDataSetChanged();
                    mLineChart.setData(null);
                    mLineChart.setNoDataText("暂无数据");
                    mLineChart.notifyDataSetChanged();
                    mLineChart.invalidate();
                }
            }

            @Override
            public void failure(String msg) {
                waterDataList.clear();
                waterListAdapter.setEmptyView(EmptyLayoutUtil.show(msg));
                waterListAdapter.notifyDataSetChanged();
                mLineChart.setData(null);
                mLineChart.setNoDataText(msg);
                mLineChart.notifyDataSetChanged();
                mLineChart.invalidate();
                ToastUtils.shortToast(msg);
            }

            @Override
            public Context getContext() {
                return null;
            }
        });


    }

    @Override
    public void initView() {
        if (defaultReservoir != null) {
            String reservoir = defaultReservoir.getReservoir();
            setCenterTitle(reservoir + "水情信息");
        } else {
            setCenterTitle("水情信息");
        }
        showBack();
        spinnerHour = findViewById(R.id.spinner_hour);
        tvHourDate = findViewById(R.id.tv_hour_date);
        tvHourDateEnd = findViewById(R.id.tv_hour_date_end);
        rvWater = findViewById(R.id.rv_water);
        mLineChart = findViewById(R.id.regimen_line_chart);
        initSpinner();
        initHourDate();
        initRecyclerView();
        initLineChart();
        requestHourRainData();
    }

    private void requestHourRainData() {
        String reservoirId = defaultReservoir.getReservoirId();
        Calendar cd = Calendar.getInstance();
        int year = cd.get(Calendar.YEAR);
        String strHour = tvHourDate.getText().toString();
        String strHourEnd = tvHourDateEnd.getText().toString();
        String startDate = year+"-"+strHour+":00";
        String endDate = year+"-"+strHourEnd+":59";
        hourPresenter.getReservoirWaterList(reservoirId, startDate, endDate, true);
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

    public static String dateToStrLong(Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
        String dateString = formatter.format(dateDate);
        return dateString;
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
    }


    private void initRecyclerView() {
        waterDataList = new ArrayList<>();
        waterListAdapter = new WaterHistoryListAdapter(R.layout.item_water_regime_detail, waterDataList);
        rvWater.setLayoutManager(new LinearLayoutManager(this));
        rvWater.setAdapter(waterListAdapter);
    }

    private void initLineChart() {
        CombinedChartManager.initLineChartTwo(mLineChart);
    }

    private void setLineChartData(List<String> xValues, List<Float> yValues, List<Float> yValues2, ArrayList<List<Float>> waterList, ArrayList<Integer> colors) {
        String[] lineNames = {"水位", "库容","汛限水位","设计洪水位","校核洪水位"};
        CombinedChartManager.setLineData(yValues, xValues, yValues2, mLineChart, lineNames,waterList,colors);
        MyLineChartMarkView mv = new MyLineChartMarkView(this, mLineChart.getXAxis().getValueFormatter());
        mv.setChartView(mLineChart);
        mLineChart.setMarker(mv);
        mLineChart.invalidate();
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initRequestData() {

    }
}
