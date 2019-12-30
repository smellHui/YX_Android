package com.tepia.guangdong_module.amainguangdong.xunchaview.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.ReservoirBean;

import java.util.List;

/**
  * Created by      Android studio
  *
  * @author :wwj (from Center Of Wuhan)
  * Date    :2019/5/5
  * Version :1.0
  * 功能描述 : 巡查水库
 **/

public class MyInspectionReservoirAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public MyInspectionReservoirAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

    }
}
