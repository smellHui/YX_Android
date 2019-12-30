package com.tepia.guangdong_module.amainguangdong.xunchaview.weatherforecast;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.guangdong_module.databinding.LvItemTempHourlyBinding;
import com.tepia.guangdong_module.amainguangdong.model.weather.Hour24Bean;

import java.util.List;

/**
 * Created by      android studio
 *
 * @author :      zhang xinhua
 * Date            :       2018-09-12
 * Time            :       21:56
 * Version         :       1.0
 * 功能描述        :
 **/
public class AdapterTempHourly extends BaseQuickAdapter<Hour24Bean.HourListBean,BaseViewHolder> {
    public AdapterTempHourly(int layoutResId, @Nullable List<Hour24Bean.HourListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Hour24Bean.HourListBean item) {
        LvItemTempHourlyBinding mBinding = DataBindingUtil.bind(helper.itemView);
        mBinding.tvHour.setText(item.getTime().substring(item.getTime().length()-4,item.getTime().length()-2)+":"+item.getTime().substring(item.getTime().length()-2));
        mBinding.tvTempHour.setText(item.getTemperature()+"℃");
        mBinding.ivTempStatus.setImageResource(WeatherImgManager.getInstance().getImgRes(item.getWeather_code()));
    }
}
