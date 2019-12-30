package com.tepia.guangdong_module.amainguangdong.utils;

import com.github.mikephil.charting.charts.PieChart;

/**
 * Created by Android Studio
 *
 * @author : Arthur
 * Date :    2019/6/4
 * Time :    10:29
 * Describe :
 */
public class PieChartUtils {

    private PieChart pieChart;

    public PieChartUtils(PieChart pie){
    }

    public PieChart getPieChart() {
        return pieChart;
    }

    public PieChartUtils setPieChart(PieChart pieChart) {
        this.pieChart = pieChart;
        return this;
    }

    public class Builder{

    }
}
