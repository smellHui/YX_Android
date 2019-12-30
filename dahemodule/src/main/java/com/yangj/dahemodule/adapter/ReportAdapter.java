package com.yangj.dahemodule.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yangj.dahemodule.R;
import com.yangj.dahemodule.model.report.ReportBean;

/**
 * Author:xch
 * Date:2019/8/28
 * Description:
 */
public class ReportAdapter extends BaseQuickAdapter<ReportBean, BaseViewHolder> {

    public ReportAdapter() {
        super(R.layout.item_report);
    }

    @Override
    protected void convert(BaseViewHolder helper, ReportBean item) {
        TextView statusTv = helper.getView(R.id.tv_status);
        boolean isComplete = item.isComplete();
        statusTv.setText(isComplete ? "已完结" : "待反馈");
        statusTv.setBackgroundResource(isComplete ? R.mipmap.grey : R.mipmap.blue);
        helper.setText(R.id.tv_name, item.getTitle());
        helper.setText(R.id.tv_createTime, item.getCreateTime());
    }
}
