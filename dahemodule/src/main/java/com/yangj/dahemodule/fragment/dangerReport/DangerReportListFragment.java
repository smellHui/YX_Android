package com.yangj.dahemodule.fragment.dangerReport;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tepia.base.http.LoadingSubject;
import com.tepia.base.utils.ToastUtils;
import com.yangj.dahemodule.adapter.ReportAdapter;
import com.yangj.dahemodule.common.HttpManager;
import com.yangj.dahemodule.fragment.BaseListFragment;
import com.yangj.dahemodule.model.report.ReportBean;
import com.yangj.dahemodule.model.report.ReportDataBean;
import com.yangj.dahemodule.util.UiHelper;

/**
 * Author:xch
 * Date:2019/9/4
 * Description:
 */
public class DangerReportListFragment extends BaseListFragment<ReportBean> {

    private String startTime;
    private String endTime;

    public static DangerReportListFragment newInstance() {
        DangerReportListFragment fragment = new DangerReportListFragment();
        return fragment;
    }

    @Override
    protected void initRequestData() {
        HttpManager.getInstance().getReportList("", getPage(), 20, startTime, endTime)
                .subscribe(new LoadingSubject<ReportDataBean>() {

                    @Override
                    protected void _onNext(ReportDataBean recordDataBean) {
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
        return new ReportAdapter();
    }

    public void refresh(String startTime, String endTime, int cate) {
        this.startTime = startTime;
        this.endTime = endTime;
        super.refresh();
    }

    @Override
    public void setOnItemClickListener(BaseQuickAdapter adapter, View view, int position) {
        ReportBean reportBean = (ReportBean) adapter.getItem(position);
        if (reportBean == null) return;
        UiHelper.goToDangerReportDetailView(getContext(), reportBean.getId());
    }
}
