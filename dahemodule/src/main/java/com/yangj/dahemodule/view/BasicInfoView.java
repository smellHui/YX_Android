package com.yangj.dahemodule.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.yangj.dahemodule.R;
import com.yangj.dahemodule.model.main.ReservoirInfo;

/**
 * Author:xch
 * Date:2019/8/27
 * Description:基础信息
 */
public class BasicInfoView extends ViewBase {

    private TextView typeTv;
    private TextView damTv;
    private TextView damTopElevationTv;
    private TextView damBotmMaxWidthTv;
    private TextView buildDateTv;

    public BasicInfoView(Context context) {
        super(context);
    }

    public BasicInfoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BasicInfoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getViewId() {
        return R.layout.view_basic_info;
    }

    @Override
    public void initData() {
        typeTv = findViewById(R.id.tv_type);
        damTv = findViewById(R.id.tv_dam);
        damTopElevationTv = findViewById(R.id.tv_damTopElevation);
        damBotmMaxWidthTv = findViewById(R.id.tv_damBotmMaxWidth);
        buildDateTv = findViewById(R.id.tv_build_date);
    }

    public void setData(ReservoirInfo reservoirInfo) {
        if (reservoirInfo == null) return;
        typeTv.setText(reservoirInfo.getDamTypeLabel());
        damTv.setText(reservoirInfo.getDamHeight() + " * " + reservoirInfo.getDamLength() + " * " + reservoirInfo.getDamWidth());
        damBotmMaxWidthTv.setText(reservoirInfo.getMaxDamWidth());
        damTopElevationTv.setText(reservoirInfo.getDamTopElevation());
        buildDateTv.setText(subString(reservoirInfo.getStartTime()) + " / " + subString(reservoirInfo.getEndTime()));
    }

    private String subString(String time) {
        if (TextUtils.isEmpty(time)) return "-";
        String[] strs = time.split("-");
        if (strs.length != 3) return time;
        return strs[0] + "-" + strs[1];
    }
}
