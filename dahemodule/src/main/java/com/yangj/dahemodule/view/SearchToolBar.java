package com.yangj.dahemodule.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.lxj.xpopup.XPopup;
import com.yangj.dahemodule.R;

/**
 * Author:xch
 * Date:2019/8/28
 * Description:
 */
public class SearchToolBar extends ViewBase implements SelectDataView.onDataSelectPickListener {

    private TextView dateTv;
    private Toolbar toolbar;
    private SelectDataView selectDataView;
    private DataSelectListener dataSelectListener;

    public void setDataSelectListener(DataSelectListener dataSelectListener) {
        this.dataSelectListener = dataSelectListener;
    }

    public SearchToolBar(Context context) {
        super(context);
    }

    public SearchToolBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchToolBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getViewId() {
        return R.layout.search_toolbar;
    }

    @Override
    public void initData() {
        dateTv = findViewById(R.id.tv_date);
        toolbar = findViewById(R.id.lo_toolbar_common);
        selectDataView = new SelectDataView(mContext, this);
        dateTv.setOnClickListener(v ->
                showChoiceDateDialog()
        );

        findViewById(R.id.iv_back).setOnClickListener(v -> {
            if (mContext instanceof Activity) {
                ((Activity) mContext).finish();
            }
        });
    }

    public void showChoiceDateDialog() {
        new XPopup.Builder(getContext())
                .hasStatusBarShadow(true) //启用状态栏阴影
                .dismissOnTouchOutside(false)
                .asCustom(selectDataView)
                .show();
    }

    public void setImmersionBar(ImmersionBar mImmersionBar) {
        mImmersionBar.titleBar(toolbar).init();
    }

    @Override
    public void onDataSelectPickListener(String startTime, String endTime, int cate) {
        if (dataSelectListener != null)
            dataSelectListener.onDataSelectPickListener(startTime, endTime, cate);
    }

    public interface DataSelectListener {
        void onDataSelectPickListener(String startTime, String endTime, int cate);
    }
}
