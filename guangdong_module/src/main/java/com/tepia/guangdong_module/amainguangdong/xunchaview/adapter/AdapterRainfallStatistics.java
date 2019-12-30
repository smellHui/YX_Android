package com.tepia.guangdong_module.amainguangdong.xunchaview.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.guangdong_module.R;
import com.example.guangdong_module.databinding.LvItemRainfallStatisticsBinding;
import com.example.guangdong_module.databinding.LvItemReservoirRegimenBinding;
import com.tepia.guangdong_module.amainguangdong.model.rainfallstatistics.RainfallItemBean;

import java.util.List;

/**
 * Created by Android Studio
 *
 * @author : Arthur
 * Date :    2019/6/5
 * Time :    15:24
 * Describe :
 */
public class AdapterRainfallStatistics extends BaseQuickAdapter<RainfallItemBean, BaseViewHolder> {

    LvItemRainfallStatisticsBinding mBinding;

    public AdapterRainfallStatistics(int layoutResId, @Nullable List<RainfallItemBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RainfallItemBean item) {
        mBinding = DataBindingUtil.bind(helper.itemView);
        if (helper.getLayoutPosition() % 2 == 0) {
            helper.setBackgroundColor(R.id.rootLy, ContextCompat.getColor(mContext, R.color.white));
        } else {
            helper.setBackgroundColor(R.id.rootLy, ContextCompat.getColor(mContext, R.color.reportitem));
        }
        mBinding.tvName.setText(item.getReservoir());
        mBinding.tvLeiji.setText(item.getDrp());
        mBinding.tvDengji.setText(TextUtils.isEmpty(item.getLevel()) ? "--" : item.getLevel());
        mBinding.tvForcast.setText(TextUtils.isEmpty(item.getForecast()) ? "--" : item.getLevel());
    }
}
