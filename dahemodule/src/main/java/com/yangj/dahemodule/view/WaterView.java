package com.yangj.dahemodule.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.yangj.dahemodule.R;

/**
 * Author:xch
 * Date:2019/8/27
 * Description:水位
 */
public class WaterView extends ViewBase {
    public WaterView(Context context) {
        super(context);
    }

    public WaterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WaterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getViewId() {
        return R.layout.view_water;
    }

    @Override
    public void initData() {

    }
}
