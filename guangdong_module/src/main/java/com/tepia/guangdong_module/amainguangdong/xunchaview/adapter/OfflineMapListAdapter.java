package com.tepia.guangdong_module.amainguangdong.xunchaview.adapter;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.guangdong_module.R;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.ReservoirBean;

import java.util.List;

/**
 * Created by      Android studio
 *
 * @author :wwj (from Center Of Wuhan)
 * Date    :2018/10/16
 * Version :1.0
 * 功能描述 :离线地图包管理
 **/

public class OfflineMapListAdapter extends BaseQuickAdapter<ReservoirBean, BaseViewHolder> {
    private OnDownClickListener mOnDownClickListener;

    public interface OnDownClickListener{
        void onClick(ReservoirBean item);
    }

    public OnDownClickListener getmOnDownClickListener() {
        return mOnDownClickListener;
    }

    public void setmOnDownClickListener(OnDownClickListener mOnDownClickListener) {
        this.mOnDownClickListener = mOnDownClickListener;
    }

    public OfflineMapListAdapter(int layoutResId, @Nullable List<ReservoirBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ReservoirBean item) {
        helper.setText(R.id.tv_reservoir_name, item.getReservoir()+"地图包");

        TextView downTv = helper.getView(R.id.downTv);
        if (item.isOfflineMap()){
            downTv.setText("更新");
        }else {
            downTv.setText("下载");
        }
        downTv.setEnabled(true);
        downTv.setClickable(true);
        downTv.setOnClickListener(v -> {
            if (mOnDownClickListener!=null){
                mOnDownClickListener.onClick(item);
            }
        });
    }


}
