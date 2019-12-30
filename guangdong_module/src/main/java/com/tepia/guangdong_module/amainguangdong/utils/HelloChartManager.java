package com.tepia.guangdong_module.amainguangdong.utils;

import android.widget.Toast;

import com.tepia.base.utils.ToastUtils;
import com.tepia.base.view.floatview.CollectionsUtil;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;

public class HelloChartManager {
    public static HelloChartManager helloChartManager = new HelloChartManager();

    public HelloChartManager() {
    }


    private PieChartView piechart;
    private PieChartData data;         //存放数据

    private boolean hasLabels = true;                   //是否有标签
    private boolean hasLabelsOutside = false;           //标签是否在扇形外面
    private boolean hasCenterCircle = true;            //是否有中心圆
    private boolean hasCenterText1 = false;             //是否有中心的文字
    private boolean hasCenterText2 = false;             //是否有中心的文字2
    private boolean isExploded = false;                  //是否是炸开的图像
    private boolean hasLabelForSelected = false;         //选中的扇形显示标签


    public void init(PieChartView mpiechart) {
        this.piechart = mpiechart;
        initListen();
    }

    public void initListen() {

        piechart.setChartRotationEnabled(true);
        piechart.setViewportCalculationEnabled(true);
        piechart.setOnValueTouchListener(new PieChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int i, SliceValue sliceValue) {
//                ToastUtils.shortToast( ""+sliceValue.getValue());


            }

            @Override
            public void onValueDeselected() {

            }
        });

    }




    public void initDatasList(List<SliceValue> values) {

        if (CollectionsUtil.isEmpty(values)) {
            return;
        }


        data = new PieChartData(values);
        data.setHasLabels(hasLabels);
        data.setHasLabelsOnlyForSelected(hasLabelForSelected);
        data.setHasLabelsOutside(hasLabelsOutside);
        data.setHasCenterCircle(hasCenterCircle);


        if (isExploded) {
            data.setSlicesSpacing(24);
        }

        if (hasCenterText1) {
            data.setCenterText1("--");//设置中心文字1
        }

        if (hasCenterText2) {
            data.setCenterText2("--");//设置中心文字2
        }

        piechart.setPieChartData(data);

    }


    public PieChartView getPiechart() {
        return piechart;
    }

    public void setPiechart(PieChartView piechart) {
        this.piechart = piechart;
    }

    public PieChartData getData() {
        return data;
    }

    public void setData(PieChartData data) {
        this.data = data;
    }

    public boolean isHasLabels() {
        return hasLabels;
    }

    public void setHasLabels(boolean hasLabels) {
        this.hasLabels = hasLabels;
    }

    public boolean isHasLabelsOutside() {
        return hasLabelsOutside;
    }

    public void setHasLabelsOutside(boolean hasLabelsOutside) {
        this.hasLabelsOutside = hasLabelsOutside;
    }

    public boolean isHasCenterCircle() {
        return hasCenterCircle;
    }

    public void setHasCenterCircle(boolean hasCenterCircle) {
        this.hasCenterCircle = hasCenterCircle;
    }

    public boolean isHasCenterText1() {
        return hasCenterText1;
    }

    public void setHasCenterText1(boolean hasCenterText1) {
        this.hasCenterText1 = hasCenterText1;
    }

    public boolean isHasCenterText2() {
        return hasCenterText2;
    }

    public void setHasCenterText2(boolean hasCenterText2) {
        this.hasCenterText2 = hasCenterText2;
    }

    public boolean isExploded() {
        return isExploded;
    }

    public void setExploded(boolean exploded) {
        isExploded = exploded;
    }

    public boolean isHasLabelForSelected() {
        return hasLabelForSelected;
    }

    public void setHasLabelForSelected(boolean hasLabelForSelected) {
        this.hasLabelForSelected = hasLabelForSelected;
    }
}
