package com.yangj.dahemodule.view.danger;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.google.common.base.Strings;
import com.yangj.dahemodule.R;
import com.yangj.dahemodule.view.ImageListView;
import com.yangj.dahemodule.view.ViewBase;

import java.util.List;

/**
 * Author:xch
 * Date:2019/10/16
 * Description:
 */
public class ReportedView extends ViewBase {
    private TextView tv_report;
    private TextView tv_type;
    private TextView tv_createdTime;
    private TextView tv_resultDes;
    private ImageListView view_imgs;

    public ReportedView(Context context) {
        super(context);
    }

    public ReportedView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ReportedView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getViewId() {
        return R.layout.view_reported;
    }

    @Override
    public void initData() {
        tv_type = findViewById(R.id.tv_type);
        tv_report = findViewById(R.id.tv_report);
        tv_createdTime = findViewById(R.id.tv_createdTime);
        tv_resultDes = findViewById(R.id.tv_resultDes);
        view_imgs = findViewById(R.id.view_imgs);

    }

    public void setDate(String type, String reporter, String createTime, String resultDes, List<String> imgs) {
        tv_type.setText(type);
        tv_report.setVisibility(Strings.isNullOrEmpty(reporter) ? GONE : VISIBLE);
        tv_createdTime.setVisibility(Strings.isNullOrEmpty(createTime) ? GONE : VISIBLE);
        tv_resultDes.setVisibility(Strings.isNullOrEmpty(resultDes) ? GONE : VISIBLE);
        tv_report.setText(reporter);
        tv_createdTime.setText(String.format("上报时间：%s", createTime));
        tv_resultDes.setText(String.format("反馈内容：%s", resultDes));
        view_imgs.addImages(imgs);
    }
}
