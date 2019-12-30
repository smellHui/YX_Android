package com.tepia.guangdong_module.amainguangdong.utils;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by      Android studio
 *
 * @author :wwj (from Center Of Wuhan)
 * Date    :2019/5/28
 * Version :1.0
 * 功能描述 : 组合图
 **/
public class CombinedChartManager {
    private CombinedChart mCombinedChart;
    private YAxis leftAxis;
    private YAxis rightAxis;
    private XAxis xAxis;

    public CombinedChartManager(CombinedChart combinedChart) {
        this.mCombinedChart = combinedChart;
        leftAxis = mCombinedChart.getAxisLeft();
        rightAxis = mCombinedChart.getAxisRight();
        xAxis = mCombinedChart.getXAxis();
        initChart();
    }

    /**
     * 初始化Chart
     */
    private void initChart() {
        //不显示描述内容
        mCombinedChart.getDescription().setEnabled(false);
        mCombinedChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR,
                CombinedChart.DrawOrder.LINE
        });
        mCombinedChart.setNoDataText("图表暂无数据");
        mCombinedChart.setBackgroundColor(Color.WHITE);
        mCombinedChart.setDrawGridBackground(false);
        mCombinedChart.setDrawBarShadow(false);
        mCombinedChart.setHighlightFullBarEnabled(false);
        mCombinedChart.setScaleXEnabled(true);
        mCombinedChart.setScaleYEnabled(false);
        //显示边界
        mCombinedChart.setDrawBorders(false);
        //图例说明
        Legend legend = mCombinedChart.getLegend();
        legend.setWordWrapEnabled(true);

        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        //Y轴设置
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f);
        rightAxis.setTextColor(Color.parseColor("#FF7D7D"));
        leftAxis.setDrawGridLines(true);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setTextColor(Color.parseColor("#37A2DA"));

        mCombinedChart.animateX(1000); // 立即执行的动画,x轴
    }

    /**
     * 设置X轴坐标值
     *
     * @param xAxisValues x轴坐标集合
     */
    public void setXAxis(final List<String> xAxisValues, boolean isHour) {

        //设置X轴在底部
        XAxis xAxis = mCombinedChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);

        xAxis.setLabelCount(xAxisValues.size() - 1, false);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                String xResult = xAxisValues.get((int) value % xAxisValues.size());
                try {
                    if (isHour) {
                        String[] split = xResult.split("-", 2);
                        String s = split[1];
                        String[] split1 = s.split(":");
                        String s1 = split1[0];
                        xResult = s1 + "时";
                    } else {
                        String[] split = xResult.split("-", 2);
                        String s = split[1];
                        String[] split1 = s.split(" ");
                        String s1 = split1[0];
                        xResult = s1;
                    }
                } catch (Exception e) {

                }
                return xResult;
            }
        });
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(4);
        mCombinedChart.invalidate();
    }

    /**
     * 得到折线图(一条)
     *
     * @param lineChartY 折线Y轴值
     * @param lineName   折线图名字
     * @param lineColor  折线颜色
     * @return
     */
    private LineData getLineData(List<Float> lineChartY, String lineName, int lineColor) {
        LineData lineData = new LineData();

        ArrayList<Entry> yValue = new ArrayList<>();
        for (int i = 0; i < lineChartY.size(); i++) {
            yValue.add(new Entry(i, lineChartY.get(i)));
        }
        LineDataSet dataSet = new LineDataSet(yValue, lineName);

        dataSet.setColor(lineColor);
        dataSet.setCircleColor(lineColor);
        dataSet.setValueTextColor(lineColor);

        dataSet.setCircleSize(1);
        //显示值
        dataSet.setDrawValues(false);
        dataSet.setValueTextSize(10f);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setCircleRadius(3);
        //设置数据对应的是左边y轴还是右边Y轴
        dataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
        lineData.addDataSet(dataSet);
        return lineData;
    }

    /**
     * 得到折线图(多条)
     *
     * @param lineChartYs 折线Y轴值
     * @param lineNames   折线图名字
     * @param lineColors  折线颜色
     * @return
     */
    private LineData getLineData(List<List<Float>> lineChartYs, List<String> lineNames, List<Integer> lineColors) {
        LineData lineData = new LineData();

        for (int i = 0; i < lineChartYs.size(); i++) {
            ArrayList<Entry> yValues = new ArrayList<>();
            for (int j = 0; j < lineChartYs.get(i).size(); j++) {
                yValues.add(new Entry(j, lineChartYs.get(i).get(j)));
            }
            LineDataSet dataSet = new LineDataSet(yValues, lineNames.get(i));
            dataSet.setColor(lineColors.get(i));
            dataSet.setCircleColor(lineColors.get(i));
            dataSet.setValueTextColor(lineColors.get(i));

            dataSet.setCircleSize(1);
            dataSet.setDrawValues(true);
            dataSet.setValueTextSize(10f);
            dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
            lineData.addDataSet(dataSet);
        }
        return lineData;
    }

    /**
     * 得到柱状图
     *
     * @param barChartY Y轴值
     * @param barName   柱状图名字
     * @param barColor  柱状图颜色
     * @return
     */

    private BarData getBarData(List<Float> barChartY, String barName, int barColor) {
        BarData barData = new BarData();
        ArrayList<BarEntry> yValues = new ArrayList<>();
        for (int i = 0; i < barChartY.size(); i++) {
            yValues.add(new BarEntry(i, barChartY.get(i)));
        }

        BarDataSet barDataSet = new BarDataSet(yValues, barName);
        barDataSet.setColor(barColor);
        barDataSet.setValueTextSize(10f);
        barDataSet.setValueTextColor(barColor);
        //设置数据对应的是左边y轴还是右边Y轴
        barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        barData.addDataSet(barDataSet);

        //以下是为了解决 柱状图 左右两边只显示了一半的问题 根据实际情况 而定
        xAxis.setAxisMinimum(-0.5f);
        xAxis.setAxisMaximum((float) (barChartY.size() - 0.5));
        return barData;
    }

    /**
     * 得到柱状图(多条)
     *
     * @param barChartYs Y轴值
     * @param barNames   柱状图名字
     * @param barColors  柱状图颜色
     * @return
     */

    private BarData getBarData(List<List<Float>> barChartYs, List<String> barNames, List<Integer> barColors) {
        List<IBarDataSet> lists = new ArrayList<>();
        for (int i = 0; i < barChartYs.size(); i++) {
            ArrayList<BarEntry> entries = new ArrayList<>();

            for (int j = 0; j < barChartYs.get(i).size(); j++) {
                entries.add(new BarEntry(j, barChartYs.get(i).get(j)));
            }
            BarDataSet barDataSet = new BarDataSet(entries, barNames.get(i));

            barDataSet.setColor(barColors.get(i));
            barDataSet.setValueTextColor(barColors.get(i));
            barDataSet.setValueTextSize(10f);
            barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
            lists.add(barDataSet);
        }
        BarData barData = new BarData(lists);

        int amount = barChartYs.size(); //需要显示柱状图的类别 数量
        float groupSpace = 0.12f; //柱状图组之间的间距
        float barSpace = (float) ((1 - 0.12) / amount / 10); // x4 DataSet
        float barWidth = (float) ((1 - 0.12) / amount / 10 * 9); // x4 DataSet

        // (0.2 + 0.02) * 4 + 0.12 = 1.00 即100% 按照百分百布局
        //柱状图宽度
        barData.setBarWidth(barWidth);
        //(起始点、柱状图组间距、柱状图之间间距)
        barData.groupBars(0, groupSpace, barSpace);
        return barData;
    }

    /**
     * 显示混合图(柱状图+折线图)
     *
     * @param xAxisValues X轴坐标
     * @param barChartY   柱状图Y轴值
     * @param lineChartY  折线图Y轴值
     * @param barName     柱状图名字
     * @param lineName    折线图名字
     * @param barColor    柱状图颜色
     * @param lineColor   折线图颜色
     */
    public void showCombinedChart(Context context,
                                  List<String> xAxisValues, List<Float> barChartY, List<Float> lineChartY
            , String barName, String lineName, int barColor, int lineColor, boolean isHour) {
        try {
            Float max = Collections.max(barChartY);
            Float min = Collections.min(barChartY);
            float v1 = max - min;
            //重置所有限制线,以避免重叠线
            leftAxis.removeAllLimitLines();
            //y轴最大
            leftAxis.setAxisMaximum(max + v1 / 10);
            //y轴最小
            leftAxis.setAxisMinimum(0);
            leftAxis.setValueFormatter((value, axis) -> Math.round(value) + "");
            Float max2 = Collections.max(lineChartY);
            Float min2 = Collections.min(lineChartY);
            float v2 = max2 - min2;
            if (v2 < 1) {
                max2 = max2 + 1;
                min2 = min2 - 1;
            } else {
                Float len = (max2 - min2) / 8;
                max2 = max2 + len;
                min2 = min2 - len * 3;
            }
            //重置所有限制线,以避免重叠线
            rightAxis.removeAllLimitLines();
            //y轴最大
            rightAxis.setAxisMaximum(max2 + v2 / 10);
            //y轴最小
            rightAxis.setAxisMinimum(0);
            rightAxis.setValueFormatter((value, axis) -> Math.round(value) + "");
            setXAxis(xAxisValues, isHour);

            CombinedData combinedData = new CombinedData();

            combinedData.setData(getBarData(barChartY, barName, barColor));
            combinedData.setData(getLineData(lineChartY, lineName, lineColor));
            mCombinedChart.setData(combinedData);
            mCombinedChart.animateX(1000); // 立即执行的动画,x轴
            mCombinedChart.notifyDataSetChanged();
            MyCombinedChartMarkView mv = new MyCombinedChartMarkView(context);
            mv.setChartView(mCombinedChart);
            mCombinedChart.setMarker(mv);
            mCombinedChart.invalidate();
        } catch (Exception e) {

        }
    }

    /**
     * 显示混合图(柱状图+折线图)
     *
     * @param xAxisValues X轴坐标
     * @param barChartYs  柱状图Y轴值
     * @param lineChartYs 折线图Y轴值
     * @param barNames    柱状图名字
     * @param lineNames   折线图名字
     * @param barColors   柱状图颜色
     * @param lineColors  折线图颜色
     */

    public void showCombinedChart(
            List<String> xAxisValues, List<List<Float>> barChartYs, List<List<Float>> lineChartYs,
            List<String> barNames, List<String> lineNames, List<Integer> barColors, List<Integer> lineColors) {
        initChart();
        setXAxis(xAxisValues, true);

        CombinedData combinedData = new CombinedData();

        combinedData.setData(getBarData(barChartYs, barNames, barColors));
        combinedData.setData(getLineData(lineChartYs, lineNames, lineColors));

        mCombinedChart.setData(combinedData);
        mCombinedChart.invalidate();
    }

    public static void initLineChartTwo(LineChart lineChart) {

        // no description text
        lineChart.getDescription().setEnabled(false);
        lineChart.setNoDataText("图表暂无数据");
        // enable touch gestures
        lineChart.setTouchEnabled(true);

        lineChart.setDragDecelerationFrictionCoef(0.9f);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        lineChart.setMaxVisibleValueCount(40);

        // enable scaling and dragging
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setScaleYEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setHighlightPerDragEnabled(true);
// 向左偏移15dp，抵消y轴向右偏移的30dp
        lineChart.setExtraLeftOffset(-15);
        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(true);

        // set an alternative background color
//        lineChart.setBackgroundColor(Color.LTGRAY);

        lineChart.animateY(1000);

        // get the legend (only possible after setting data)
        Legend l = lineChart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTextSize(11f);
        l.setTextColor(Color.BLACK);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
//        l.setYOffset(11f);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setTextSize(8f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(3);
        xAxis.setXOffset(15);
        //设置x轴间隔尺寸
        xAxis.setGranularity(1f);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);
        // 设置y轴数据偏移量
        leftAxis.setXOffset(15);
        // 限制数据(而不是背后的线条勾勒出了上面)
        leftAxis.setDrawLimitLinesBehindData(true);

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawZeroLine(false);
        rightAxis.setGranularityEnabled(false);
    }

    public static void setLineData(List<Float> yAxisValue, List<String> xValues, List<Float> yAxisValueTwo, LineChart lineChart, String[] lineNames, ArrayList<List<Float>> waterList, ArrayList<Integer> colors) {
        try {
            XAxis xLabels = lineChart.getXAxis();
            xLabels.setLabelCount(4);
            //修改x轴显示文字  monthList.get((int)value%monthList.size())
            xLabels.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    String xResult = xValues.get((int) value % xValues.size());
                    try {
                        String[] split = xResult.split("-", 2);
                        String s = split[1];
                        String[] split1 = s.split(":");
                        String s1 = split1[0];
                        xResult = s1 + "时";
                    } catch (Exception e) {

                    }
                    return xResult;
                }
            });
            Float max = Collections.max(yAxisValue);
            Float min = Collections.min(yAxisValue);
            float v = max - min;
            if (v < 1) {
                max = max + 1;
                min = min - 1;
            } else {
                Float len = (max - min) / 8;
                max = max + len;
                min = min - len * 3;
            }
            YAxis leftAxis = lineChart.getAxisLeft();
            //重置所有限制线,以避免重叠线
            leftAxis.removeAllLimitLines();
//        //y轴最大
//        leftAxis.setAxisMaximum(max);
//        //y轴最小
            leftAxis.setAxisMinimum(0f);
            leftAxis.setTextColor(Color.parseColor("#376fff"));

            ArrayList<Entry> yVals1 = new ArrayList<Entry>();

            for (int i = 0; i < yAxisValue.size(); i++) {
                yVals1.add(new Entry(i, yAxisValue.get(i)));
            }

            ArrayList<Entry> yVals2 = new ArrayList<Entry>();
            boolean isShowYTwo = true;
            for (int i = 0; i < yAxisValue.size(); i++) {
                Float aFloat = yAxisValueTwo.get(i);
                if (i == 0) {
                    if (aFloat == 0) {
                        isShowYTwo = false;
                    }
                }
                if (aFloat == 0 && i != 0) {
                    Float aFloat1 = null;
                    for (int i1 = i; i1 >= 0; i1--) {
                        aFloat1 = yAxisValueTwo.get(i1);
                        if (aFloat1 != 0) {
                            break;
                        }
                    }
                    aFloat = aFloat1;
                }
                yVals2.add(new Entry(i, aFloat));
                yAxisValueTwo.set(i, aFloat);
//            if(i == 10) {
//                yVals2.add(new Entry(i, val + 50));
//            }
            }
            Float max2 = Collections.max(yAxisValueTwo);
            Float min2 = Collections.min(yAxisValueTwo);
            float v1 = max2 - min2;
            if (v1 < 1) {
                max2 = max2 + 1;
                min2 = min2 - 1;
            } else {
                Float len2 = (max2 - min2) / 8;
                max2 = max2 + len2;
                min2 = min2 - len2 * 3;
            }
            LineDataSet set1, set2;

//        if (lineChart.getData() != null &&
//                lineChart.getData().getDataSetCount() > 0) {
//            set1 = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
//            set1.setValues(yVals1);
//            if (isShowYTwo){
//                set2 = (LineDataSet) lineChart.getData().getDataSetByIndex(1);
//                set2.setValues(yVals2);
//            }
//            lineChart.getData().notifyDataChanged();
//            lineChart.notifyDataSetChanged();
//        } else {
//
//        }
            // create a dataset and give it a type
            set1 = new LineDataSet(yVals1, lineNames[0]);
            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setColor(Color.parseColor("#376fff"));
            set1.setCircleColor(Color.parseColor("#376fff"));
            set1.setLineWidth(2f);
            set1.setDrawCircles(true);
            set1.setCircleRadius(2f);
            set1.setFillAlpha(65);
//          set1.setFillColor(ColorTemplate.getHoloBlue());
            set1.setHighLightColor(Color.parseColor("#376fff"));
//          set1.setDrawCircleHole(false);
            //set1.setFillFormatter(new MyFillFormatter(0f));
            //set1.setDrawHorizontalHighlightIndicator(false);
            //set1.setVisible(false);
            //set1.setCircleHoleColor(Color.WHITE);
            // create a dataset and give it a type
            LineData data;
            YAxis rightAxis = lineChart.getAxisRight();
            rightAxis.setAxisMaximum(max2);
            rightAxis.setAxisMinimum(min2);
            rightAxis.setDrawGridLines(false);
            rightAxis.setDrawZeroLine(false);
            rightAxis.setGranularityEnabled(false);
            rightAxis.setDrawAxisLine(true);
            rightAxis.setDrawLabels(true);
            rightAxis.setTextColor(Color.rgb(10, 184, 180));
            if (isShowYTwo) {
                set2 = new LineDataSet(yVals2, lineNames[1]);
                set2.setAxisDependency(YAxis.AxisDependency.RIGHT);
                set2.setColor(Color.rgb(10, 184, 180));
                set2.setCircleColor(Color.rgb(10, 184, 180));
                set2.setLineWidth(2f);
                set2.setDrawCircles(true);
                set2.setCircleRadius(2f);
                set2.setFillAlpha(65);
//              set1.setFillColor(ColorTemplate.getHoloBlue());
//              set1.setDrawCircleHole(false);
                set2.setHighLightColor(Color.rgb(10, 184, 180));
                //set2.setFillFormatter(new MyFillFormatter(900f));

                // create a data object with the datasets
                data = new LineData(set1, set2);
            } else {
                rightAxis.setDrawAxisLine(false);
                rightAxis.setDrawLabels(false);
                data = new LineData(set1);
            }
//            data.setValueTextColor(Color.BLACK);
//            data.setValueTextSize(9f);
            //设置汛限水位,设计洪水位,校核洪水位
            if (waterList != null && waterList.size() > 0) {
                for (int i = 0; i < waterList.size(); i++) {
                    List<Float> floats = waterList.get(i);
                    LineDataSet lineDataSet = getLineDataSet(floats, lineNames[i + 2], colors.get(i));
                    data.addDataSet(lineDataSet);
                }
            }
            //曲线图上不显示值
            data.setDrawValues(false);
            // set data
            lineChart.setData(data);
            List<ILineDataSet> setsHorizontalCubic = lineChart.getData().getDataSets();
            if (setsHorizontalCubic != null && setsHorizontalCubic.size() > 0) {
                for (ILineDataSet iSet : setsHorizontalCubic) {
                    LineDataSet set = (LineDataSet) iSet;
                    set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                }
            }
            lineChart.invalidate();
            lineChart.animateX(1000);
            lineChart.notifyDataSetChanged();
        } catch (Exception e) {

        }
    }

    private static LineDataSet getLineDataSet(List<Float> yAxisValue, String title1, int lineColor) {
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        for (int i = 0; i < yAxisValue.size(); i++) {
            yVals1.add(new Entry(i, yAxisValue.get(i)));
        }
        LineDataSet set1 = new LineDataSet(yVals1, title1);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(lineColor);
        set1.setCircleColor(lineColor);
        set1.setLineWidth(2f);
        set1.setDrawCircles(true);
        set1.setCircleRadius(2f);
        set1.setFillAlpha(65);
//        set1.setFillColor(ColorTemplate.getHoloBlue());
        set1.setHighLightColor(lineColor);
//        set1.setDrawCircleHole(false);
        return set1;
    }
}
