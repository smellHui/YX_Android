package com.yangj.dahemodule.fragment.operate;

import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tepia.base.http.LoadingSubject;
import com.tepia.base.utils.ToastUtils;
import com.tepia.base.view.floatview.CollectionsUtil;
import com.tepia.guangdong_module.amainguangdong.common.UserManager;
import com.tepia.guangdong_module.amainguangdong.route.TaskBean;
import com.yangj.dahemodule.adapter.OperateAdapter;
import com.yangj.dahemodule.common.HttpManager;
import com.yangj.dahemodule.fragment.BaseListFragment;
import com.yangj.dahemodule.model.xuncha.RecordBean;
import com.yangj.dahemodule.model.xuncha.RecordDataBean;
import com.yangj.dahemodule.util.UiHelper;
import com.yangj.dahemodule.wrap.SubmitTaskWrap;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:xch
 * Date:2019/8/27
 * Description:我的巡查
 */
public class OperatesFragment extends BaseListFragment<RecordBean> {

    public static final int MINE_OPERATE = 1;
    public static final int ALL_OPERATE = MINE_OPERATE >> 1;

    private int pageType;
    private String startTime, endTime;
    private String userCode;
    private List<TaskBean> toDoLocalTask;

    private List<RecordBean> localRecordList;

    public static OperatesFragment launch(int pageType) {
        OperatesFragment operatesFragment = new OperatesFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("pageType", pageType);
        operatesFragment.setArguments(bundle);
        return operatesFragment;
    }

    @Override
    protected void initData() {
//        EventBus.getDefault().register(this);

        Bundle bundle = getArguments();
        if (bundle != null) {
            pageType = bundle.getInt("pageType", MINE_OPERATE);
        }

        localRecordList = new ArrayList<>();
        userCode = UserManager.getInstance().getUserCode();
    }

    @Override
    protected void initRequestData() {
        getRecordList();
    }

    private void getRecordList() {
        HttpManager.getInstance().getRecordList(pageType, "", getPage(), 20, startTime, endTime)
                .subscribe(new LoadingSubject<RecordDataBean>() {

                    @Override
                    protected void _onNext(RecordDataBean recordDataBean) {
                        addLocalData();
                        success(recordDataBean.getData());
                    }

                    @Override
                    protected void _onError(String message) {
                        addLocalData();
                        ToastUtils.shortToast(message);
                    }
                });
    }

    private void addLocalData() {
        localRecordList.clear();
        if (pageType == MINE_OPERATE) {
            toDoLocalTask = DataSupport.where("executeStatus != 3 and hasCreated = 0")
                    .order("id desc")
                    .find(TaskBean.class);
            if (CollectionsUtil.isNotEmpty(toDoLocalTask)) {
                for (TaskBean taskBean : toDoLocalTask) {
                    RecordBean recordBean = new RecordBean();
                    recordBean.setName(taskBean.getRouteName());
                    recordBean.setCreateTime(taskBean.getStartTime());
                    recordBean.setCode(taskBean.getWorkOrderId());
                    recordBean.setRouteType(taskBean.getRouteType());
                    recordBean.setTemporary("1");
                    localRecordList.add(recordBean);
                }
            }
            if (getPage() == 1 && CollectionsUtil.isNotEmpty(localRecordList)) {
                getList().clear();
                getList().addAll(localRecordList);
            }
        }
    }

    public void refresh(String startTime, String endTime, int cate) {
        this.startTime = startTime;
        this.endTime = endTime;
        super.refresh();
    }

    @Override
    public BaseQuickAdapter getBaseQuickAdapter() {
        return new OperateAdapter(pageType);
    }

    @Override
    public void setOnItemClickListener(BaseQuickAdapter adapter, View view, int position) {
        RecordBean recordBean = (RecordBean) adapter.getItem(position);
        if (recordBean == null) return;
        boolean tem = recordBean.isTemporary();
        if (tem) {
            if (recordBean.getRouteType().equals("2")) {
                UiHelper.goToNoPathMapControlView(getBaseActivity(), recordBean.getCode());
            } else {
                UiHelper.goToPatrolMapControlView(getBaseActivity(), recordBean.getCode());
            }
        } else {
            UiHelper.goToPatrolRecordView(getBaseActivity(), recordBean.getCode());
        }
    }

    //提交工单成功
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void submitCallBack(SubmitTaskWrap submitTaskWrap) {
        refresh();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
