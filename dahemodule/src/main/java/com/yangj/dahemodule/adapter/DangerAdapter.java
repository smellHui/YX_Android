package com.yangj.dahemodule.adapter;

import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yangj.dahemodule.R;
import com.yangj.dahemodule.model.danger.DangerBean;
import com.yangj.dahemodule.model.xuncha.RecordBean;

/**
 * Author:xch
 * Date:2019/8/28
 * Description:
 */
public class DangerAdapter extends BaseQuickAdapter<DangerBean, BaseViewHolder> {

    public DangerAdapter() {
        super(R.layout.item_danger);
    }

    @Override
    protected void convert(BaseViewHolder helper, DangerBean item) {
        TextView statusTv = helper.getView(R.id.tv_status);
        String executeStatus = item.getProblemStatus();
        if (!TextUtils.isEmpty(executeStatus)) {
            statusTv.setText(executeStatus.equals("1") ? "待处置" : "已处置");
            statusTv.setBackgroundResource(executeStatus.equals("1") ? R.mipmap.blue : R.mipmap.grey);
        }
        helper.setText(R.id.tv_name, item.getTitle());
        helper.setText(R.id.tv_createTime, item.getCreateTime());

    }
}
