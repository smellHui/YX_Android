package com.yangj.dahemodule.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.TextView;

import com.tepia.base.http.LoadingSubject;
import com.tepia.base.utils.ResUtils;
import com.tepia.base.utils.ToastUtils;
import com.tepia.base.view.floatview.CollectionsUtil;
import com.tepia.guangdong_module.amainguangdong.mvp.taskdetail.TaskDetailPresenter;
import com.tepia.guangdong_module.amainguangdong.mvp.taskdetail.TaskManager;
import com.tepia.guangdong_module.amainguangdong.route.RoutepointDataManager;
import com.tepia.guangdong_module.amainguangdong.route.TaskBean;
import com.tepia.guangdong_module.amainguangdong.route.TaskDetailResponse;
import com.tepia.guangdong_module.amainguangdong.route.TaskItemBean;
import com.tepia.guangdong_module.amainguangdong.utils.SqlManager;
import com.yangj.dahemodule.R;
import com.yangj.dahemodule.wrap.SubmitTaskWrap;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Author:xch
 * Date:2019/11/25
 * Description:
 */
public class PatrolRateView extends ViewBase {

    private TextView tv_rate;
    private TextView tv_abnormal_num;
    private Button btn_submit;

    private String workOrderId;

    public void setClickListener(OnClickListener clickListener) {
        btn_submit.setOnClickListener(clickListener);
    }

    public PatrolRateView(Context context) {
        super(context);
    }

    public PatrolRateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PatrolRateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getViewId() {
        return R.layout.view_patrol_rate;
    }

    @Override
    public void initData() {
        tv_rate = findViewById(R.id.tv_rate);
        tv_abnormal_num = findViewById(R.id.tv_abnormal_num);
        btn_submit = findViewById(R.id.btn_submit);
    }

    @SuppressLint("DefaultLocale")
    public void setWorkOrderId(String workOrderId, boolean isCompleteOfTaskBean) {
        int abnormalNum = SqlManager.getInstance().getNormalTaskById(workOrderId, false);
        if (isCompleteOfTaskBean) {
            tv_rate.setText("100%");
            tv_abnormal_num.setText(String.format("%d项", abnormalNum));
            btn_submit.setVisibility(GONE);
        } else {
            this.workOrderId = workOrderId;
            int totalNum = SqlManager.getInstance().getTotalTaskById(workOrderId);
            int completedNum = SqlManager.getInstance().getTotalTaskByStatus(workOrderId, "1");
            DecimalFormat df = new DecimalFormat("0.00");//格式化小数
            String num = df.format((float) completedNum * 100 / totalNum);//返回的是String类型
            tv_rate.setText(num + "%");
            tv_abnormal_num.setText(String.format("%d项", abnormalNum));
            btn_submit.setVisibility(completedNum == totalNum ? VISIBLE : GONE);
        }
    }

    public void updateRateView() {
        setWorkOrderId(workOrderId, false);
    }

}
