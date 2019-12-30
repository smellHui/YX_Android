package com.yangj.dahemodule.view.patrol;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.tepia.base.utils.TimeFormatUtils;
import com.yangj.dahemodule.R;
import com.yangj.dahemodule.common.HttpManager;
import com.yangj.dahemodule.model.user.SysUserBean;
import com.yangj.dahemodule.view.ViewBase;

/**
 * Author:xch
 * Date:2019/12/26
 * Description:
 */
public class PatrolReportInfoView extends ViewBase {

    private TextView tv_reporter;
    private TextView tv_created_time;
    private SysUserBean sysUser;

    public PatrolReportInfoView(Context context) {
        super(context);
    }

    public PatrolReportInfoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PatrolReportInfoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getViewId() {
        return R.layout.view_patrol_report_info;
    }

    @Override
    public void initData() {
        tv_reporter = findViewById(R.id.tv_reporter);
        tv_created_time = findViewById(R.id.tv_created_time);
        sysUser = HttpManager.getInstance().getSysUser();
        tv_reporter.setText(String.format("上报人：%s", sysUser.getUsername()));
    }

    public void setData(String routeName) {
        tv_created_time.setText(TimeFormatUtils.getNowShortDate() + routeName);
    }

}
