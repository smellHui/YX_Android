package com.tepia.guangdong_module.amainguangdong.xunchaview.adapter.reservoirdetail;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.guangdong_module.R;
import com.tepia.guangdong_module.amainguangdong.model.reservoirdetail.WaterRegimeDetailBean;

import java.util.List;

/**
  * Created by      Android studio
  *
  * @author :wwj (from Center Of Wuhan)
  * Date    :2019/5/30
  * Version :1.0
  * 功能描述 :
 **/
public class WaterHistoryListAdapter extends BaseQuickAdapter<WaterRegimeDetailBean.DataBean, BaseViewHolder> {
    public WaterHistoryListAdapter(int layoutResId, @Nullable List<WaterRegimeDetailBean.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WaterRegimeDetailBean.DataBean item) {
////        TextView tvReservoirName = helper.getView(R.id.tv_reservoir_name);
        TextView tvDate = helper.getView(R.id.tv_date);
        TextView tvWaterLevel = helper.getView(R.id.tv_water_level);
        TextView tvW = helper.getView(R.id.tv_w);
////        tvReservoirName.setText(item.getStnm());
        tvDate.setText(item.getRrTime());
        tvWaterLevel.setText(item.getRz()+"");
        if (TextUtils.isEmpty(item.getW()+"")||item.getW()==0) {
            tvW.setText(mContext.getString(R.string.setting_t_null));
        }else{
            tvW.setText(item.getW()+"");
        }
        if (helper.getLayoutPosition() % 2 == 0) {
            helper.setBackgroundColor(R.id.ll_root, ContextCompat.getColor(mContext, R.color.white));
        } else {
            helper.setBackgroundColor(R.id.ll_root, Color.parseColor("#F7F9FC"));
        }
    }
}
