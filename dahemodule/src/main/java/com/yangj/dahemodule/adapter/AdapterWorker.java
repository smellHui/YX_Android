package com.yangj.dahemodule.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.PersonDutyBean;
import com.yangj.dahemodule.databinding.LvTabMainWorkerItemBinding;

import java.util.List;

/**
 * Created by      android studio
 *
 * @author :      zhang xinhua
 * Date            :       2018-09-17
 * Time            :       14:33
 * Version         :       1.0
 * 功能描述        :       主页 - 首页 水库人员 适配器
 **/
public class AdapterWorker extends BaseQuickAdapter<PersonDutyBean, BaseViewHolder> {
    public AdapterWorker(int layoutResId, @Nullable List<PersonDutyBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PersonDutyBean item) {
        LvTabMainWorkerItemBinding mBinding = DataBindingUtil.bind(helper.itemView);
        mBinding.tvJobname.setText(item.getJobName());
        mBinding.tvName.setText(item.getUserName());
        if (TextUtils.isEmpty(item.getMobile())) {
            mBinding.tvTel.setText("暂无手机号码");
        } else {
            mBinding.tvTel.setText(item.getMobile());
        }
    }

}
