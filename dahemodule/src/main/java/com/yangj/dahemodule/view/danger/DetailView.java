package com.yangj.dahemodule.view.danger;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.yangj.dahemodule.R;
import com.yangj.dahemodule.view.ViewBase;

/**
 * Author:xch
 * Date:2019/10/16
 * Description:
 */
public class DetailView extends ViewBase {
    private TextView titleTv;
    private TextView createTimeTv;
    private TextView descriptionTv;

    public DetailView(Context context) {
        super(context);
    }

    public DetailView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DetailView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getViewId() {
        return R.layout.view_report_detail;
    }

    @Override
    public void initData() {
        titleTv = findViewById(R.id.tv_title);
        createTimeTv = findViewById(R.id.tv_createTime);
        descriptionTv = findViewById(R.id.tv_description);
    }

    public void setDate(String title, String createTime, String description) {
        titleTv.setText(title);
        createTimeTv.setText(createTime);
        descriptionTv.setText(String.format("险情描述：%s", description));
    }
}
