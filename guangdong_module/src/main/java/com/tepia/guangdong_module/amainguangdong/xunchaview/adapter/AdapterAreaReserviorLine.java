package com.tepia.guangdong_module.amainguangdong.xunchaview.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.guangdong_module.R;
import com.example.guangdong_module.databinding.AreaReservoirListLayoutBinding;
import com.example.guangdong_module.databinding.ItemAreaLayoutBinding;
import com.tepia.base.utils.LogUtil;
import com.tepia.base.utils.ScreenUtil;
import com.tepia.base.utils.Utils;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.AreaReservoirCountBean;

import java.util.List;

/**
  * Created by      Android studio
  *
  * @author :ly (from Center Of Wuhan)
  * 创建时间 :2019-6-4
  * 更新时间 :
  * Version :1.0
  * 功能描述 :水库监测情况统计
 **/
public class AdapterAreaReserviorLine extends BaseQuickAdapter<AreaReservoirCountBean.DataBean, BaseViewHolder> {
    AreaReservoirListLayoutBinding mBinding;
    public AdapterAreaReserviorLine(int layoutResId, @Nullable List<AreaReservoirCountBean.DataBean> data) {
        super(layoutResId, data);

    }

    private int monitorCount;
    private int reportCount;
    private String allCount;
    @Override
    protected void convert(BaseViewHolder helper, AreaReservoirCountBean.DataBean item) {
        mBinding = DataBindingUtil.bind(helper.itemView);
        monitorCount = item.getMonitorCount();
        allCount = item.getAllCount()+"";
        reportCount = item.getReportCount();
        mBinding.areaTv.setText(item.getAreaName());
        mBinding.allCountTv.setText(allCount);
        mBinding.monitorCountTv.setText(monitorCount+"");
        mBinding.reportCountTv.setText(reportCount+"");

        int position = helper.getAdapterPosition();
        if(position%2 == 0){
            mBinding.rootLy.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
        }else{
            mBinding.rootLy.setBackgroundColor(ContextCompat.getColor(mContext, R.color.reportitem));

        }


    }



}
