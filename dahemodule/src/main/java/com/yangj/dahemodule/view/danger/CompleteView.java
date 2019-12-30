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
public class CompleteView extends ViewBase {
    private TextView tv_statue_title;

    public CompleteView(Context context) {
        super(context);
    }

    public CompleteView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CompleteView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getViewId() {
        return R.layout.view_complete;
    }

    @Override
    public void initData() {
        tv_statue_title = findViewById(R.id.tv_type);
    }

    public void setData(String statue) {
        tv_statue_title.setText(statue);
    }
}
