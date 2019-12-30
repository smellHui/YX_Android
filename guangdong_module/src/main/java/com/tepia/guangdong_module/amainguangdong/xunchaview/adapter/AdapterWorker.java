package com.tepia.guangdong_module.amainguangdong.xunchaview.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.guangdong_module.R;
import com.example.guangdong_module.databinding.LvTabMainWorkerItemBinding;
import com.tepia.guangdong_module.amainguangdong.model.UserInfo;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.PersonDutyBean;

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
public class AdapterWorker extends BaseQuickAdapter<UserInfo, BaseViewHolder> {
    public AdapterWorker(int layoutResId, @Nullable List<UserInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserInfo item) {
        helper.setText(R.id.tv_jobname,item.getAlias());
        helper.setText(R.id.tv_name,item.getUsername());
        if (TextUtils.isEmpty(item.getPhone())) {
            helper.setText(R.id.tv_tel,"暂无手机号码");
        } else {
            helper.setText(R.id.tv_tel,item.getPhone());
        }
    }

}
