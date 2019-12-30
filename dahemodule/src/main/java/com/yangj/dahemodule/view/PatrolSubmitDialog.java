package com.yangj.dahemodule.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.lxj.xpopup.impl.ConfirmPopupView;
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

import java.util.List;

/**
 * Author:xch
 * Date:2019/12/23
 * Description:
 */
public class PatrolSubmitDialog extends ConfirmPopupView implements View.OnClickListener {

    private String workOrderId;
    private TextView tv_title, tv_content, tv_cancel, tv_confirm;

    @Override
    protected int getImplLayoutId() {
        return R.layout._xpopup_center_impl_confirm;
    }

    public PatrolSubmitDialog(@NonNull Context context, String workOrderId) {
        super(context);
        this.workOrderId = workOrderId;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        tv_title = findViewById(R.id.tv_title);
        tv_content = findViewById(R.id.tv_content);
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_confirm = findViewById(R.id.tv_confirm);
        tv_title.setText("提示");
        tv_content.setText("确认提交吗？");
        tv_title.setVisibility(VISIBLE);
        tv_content.setVisibility(VISIBLE);
        tv_cancel.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == tv_cancel) {
            dismiss();
        }
        if (v == tv_confirm) {
            dismiss();
            TaskBean taskBean = SqlManager.getInstance().queryTaskSqlByWorkOrderId(workOrderId);
            if (taskBean != null) {
                if (taskBean.isHasCreated()) {
                    uploadForm();
                } else {
                    createOrder(taskBean);
                }
            }
        }
    }

    private void createOrder(TaskBean taskBean) {
        TaskManager.getInstance().newStartExecute(workOrderId, taskBean.getRouteId(), taskBean.getStartTime()).safeSubscribe(new LoadingSubject<TaskDetailResponse>(true, "正在新建工单...") {

            @Override
            protected void _onNext(TaskDetailResponse taskDetailResponse) {
                if (taskDetailResponse == null) return;
                if (taskDetailResponse.getCode() != 0) return;
                taskBean.setHasCreated(true);
                SqlManager.getInstance().updateTaskAsyn(taskBean, null);
                uploadForm();
            }


            @Override
            protected void _onError(String message) {
                ToastUtils.shortToast(message);
            }
        });
    }

    private void uploadForm() {
        List<TaskItemBean> taskItemBeans = SqlManager.getInstance().queryLocalData(workOrderId);
        if (CollectionsUtil.isEmpty(taskItemBeans)) {
            SubmitFormCallback();
        } else {
            new TaskDetailPresenter(this::SubmitFormCallback).commitTotal(taskItemBeans, getContext());
        }
    }

    private void SubmitFormCallback() {
        String temp = RoutepointDataManager.getInstance().getRoutePointListString(workOrderId);
        new TaskDetailPresenter(this::endFormCallback).endExecute(workOrderId, temp, false, ResUtils.getString(com.example.guangdong_module.R.string.data_loading));
    }

    private void endFormCallback() {
        SqlManager.getInstance().deleteTask(workOrderId);
        ToastUtils.shortToast("提交成功");
        EventBus.getDefault().post(new SubmitTaskWrap());
    }
}
