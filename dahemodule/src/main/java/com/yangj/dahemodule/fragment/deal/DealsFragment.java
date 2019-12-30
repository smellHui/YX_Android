package com.yangj.dahemodule.fragment.deal;

import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tepia.base.http.LoadingSubject;
import com.tepia.base.utils.ToastUtils;
import com.yangj.dahemodule.adapter.DangerAdapter;
import com.yangj.dahemodule.common.HttpManager;
import com.yangj.dahemodule.fragment.BaseListFragment;
import com.yangj.dahemodule.model.danger.DangerBean;
import com.yangj.dahemodule.model.danger.DangerDataBean;
import com.yangj.dahemodule.util.UiHelper;
import com.yangj.dahemodule.wrap.DealFeedWrap;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.yangj.dahemodule.fragment.DealFragment.DEAL_DOING;
import static com.yangj.dahemodule.fragment.DealFragment.DEAL_TYPE;

/**
 * Author:xch
 * Date:2019/8/27
 * Description:我的巡查
 */
public class DealsFragment extends BaseListFragment<DangerBean> {

    private String source;
    private int dealType;

    public static DealsFragment launch(String source, int dealType) {
        DealsFragment operatesFragment = new DealsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("source", source);
        bundle.putInt(DEAL_TYPE, dealType);
        operatesFragment.setArguments(bundle);
        return operatesFragment;
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        Bundle bundle = getArguments();
        if (bundle != null) {
            source = bundle.getString("source", "");
            dealType = bundle.getInt(DEAL_TYPE, DEAL_DOING);
        }
    }

    @Override
    protected void initRequestData() {
        getRecordList();
    }

    private void getRecordList() {
        HttpManager.getInstance().getPendingHandleList(dealType, "", getPage(), 20, "", "", source)
                .subscribe(new LoadingSubject<DangerDataBean>() {

                    @Override
                    protected void _onNext(DangerDataBean recordDataBean) {
                        success(recordDataBean.getData());
                    }

                    @Override
                    protected void _onError(String message) {
                        ToastUtils.shortToast(message);
                    }
                });
    }

    @Override
    public BaseQuickAdapter getBaseQuickAdapter() {
        return new DangerAdapter();
    }

    @Override
    public void setOnItemClickListener(BaseQuickAdapter adapter, View view, int position) {
        DangerBean dangerBean = (DangerBean) adapter.getItem(position);
        if (dangerBean == null) return;
        UiHelper.goToDangerReportDetailView(getBaseActivity(), dangerBean.getId());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(DealFeedWrap dealFeedWrap) {
        refresh();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
