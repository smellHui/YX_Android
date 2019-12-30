package com.tepia.guangdong_module.amainguangdong.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.example.guangdong_module.R;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.tepia.base.utils.LogUtil;

import java.util.List;

/**
 * Created by      Android studio
 *
 * @author :wwj (from Center Of Wuhan)
 * Date    :2019/5/30
 * Version :1.0
 * 功能描述 :  组合图
 **/
public class MyCombinedChartMarkView extends MarkerView {

    private TextView tvDate;
    private TextView tvValue0;
    private TextView tvValue1;

    public MyCombinedChartMarkView(Context context) {
        super(context, R.layout.layout_line_markview);
        tvDate = (TextView) findViewById(R.id.tv_date);
        tvValue0 = (TextView) findViewById(R.id.tv_value);
        tvValue1 = (TextView) findViewById(R.id.tv_value2);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        tvValue0.setText("");
        tvValue1.setText("");
        try {
            Chart chart = getChartView();
            if (chart instanceof CombinedChart) {
                CombinedChart combinedChart = (CombinedChart) chart;
                XAxis xAxis = combinedChart.getXAxis();
                tvDate.setText(xAxis.getValueFormatter().getFormattedValue(e.getX(), null));
                CombinedData combinedData = combinedChart.getCombinedData();
                LineData lineData1 = combinedData.getLineData();
                List<ILineDataSet> lineDataSets = lineData1.getDataSets();
                LineDataSet lineDataSet = (LineDataSet) lineDataSets.get(0);
                float y = lineDataSet.getValues().get((int) e.getX()).getY();
                tvValue0.setText(lineDataSet.getLabel() + "：" + y);
                BarData barData = combinedData.getBarData();
                List<IBarDataSet> barDataSets = barData.getDataSets();
                BarDataSet barDataSet = (BarDataSet) barDataSets.get(0);
                float barY = barDataSet.getValues().get((int) e.getX()).getY();
                tvValue1.setText(barDataSet.getLabel() + "：" + barY);
            }
        } catch (Exception ex) {
        }
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
