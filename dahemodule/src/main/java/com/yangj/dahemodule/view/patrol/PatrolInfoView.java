package com.yangj.dahemodule.view.patrol;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.yangj.dahemodule.R;
import com.yangj.dahemodule.view.ViewBase;

/**
 * Author:xch
 * Date:2019/12/24
 * Description:
 */
public class PatrolInfoView extends ViewBase {

    private TextView tv_title;
    private TextView tv_createTime;
    private TextView tv_reservoir_name;

    public PatrolInfoView(Context context) {
        super(context);
    }

    public PatrolInfoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PatrolInfoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getViewId() {
        return R.layout.view_patrol_info;
    }

    @Override
    public void initData() {
        tv_title = findViewById(R.id.tv_title);
        tv_createTime = findViewById(R.id.tv_createTime);
        tv_reservoir_name = findViewById(R.id.tv_reservoir_name);
    }

    public void setDate(String title,String time,String reservoirName){
        tv_title.setText(title);
        tv_createTime.setText(time);
        tv_reservoir_name.setText(reservoirName);
    }
}
