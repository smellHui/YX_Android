package com.tepia.guangdong_module.amainguangdong.xunchaview.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.guangdong_module.R;
import com.example.guangdong_module.databinding.LvItemRainfallStatisticsBinding;
import com.example.guangdong_module.databinding.LvItemSeepageAnomalousBinding;

import java.util.List;

/**
 * Created by Android Studio
 *
 * @author : Arthur
 * Date :    2019/6/5
 * Time :    15:59
 * Describe :
 */
public class AdapterSeepageAnomalous extends BaseQuickAdapter<String, BaseViewHolder> {
    LvItemSeepageAnomalousBinding mBinding;

    public AdapterSeepageAnomalous(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        mBinding = DataBindingUtil.bind(helper.itemView);
        if (helper.getLayoutPosition() % 2 == 0) {
            helper.setBackgroundColor( R.id.rootLy, ContextCompat.getColor(mContext, R.color.white));
        } else {
            helper.setBackgroundColor(R.id.rootLy, ContextCompat.getColor(mContext, R.color.reportitem));
        }
    }
}
