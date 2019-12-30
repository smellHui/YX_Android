package com.tepia.guangdong_module.amainguangdong.xunchaview.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.guangdong_module.R;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.ReservoirBean;
import com.tepia.guangdong_module.amainguangdong.utils.InspectionDateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
  * Created by      Android studio
  *
  * @author :wwj (from Center Of Wuhan)
  * Date    :2019/5/5
  * Version :1.0
  * 功能描述 : 切换水库
 **/

public class MyChangeReservoirAdapter extends BaseQuickAdapter<ReservoirBean, BaseViewHolder> {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Context mContext;
    private OnLocationClickListener mOnLocationClickListener;

    public interface OnLocationClickListener{
        void onClick(ReservoirBean item);
    }

    public OnLocationClickListener getOnLocationClickListener() {
        return mOnLocationClickListener;
    }

    public void setOnLocationClickListener(OnLocationClickListener mOnLocationClickListener) {
        this.mOnLocationClickListener = mOnLocationClickListener;
    }

    public MyChangeReservoirAdapter(int layoutResId, @Nullable List<ReservoirBean> data, Context context) {
        super(layoutResId, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ReservoirBean item) {
        TextView tvReservoirName = helper.getView(R.id.tv_reservoir_name);
        tvReservoirName.setText(item.getReservoir());
        TextView tvDate = helper.getView(R.id.tv_date);
        TextView tvInspectionStatus = helper.getView(R.id.tv_inspection_status);
        TextView tvDistance = helper.getView(R.id.tv_distance);
        FrameLayout flLocation = helper.getView(R.id.fl_location);
        flLocation.setOnClickListener(v -> {
            if (mOnLocationClickListener!=null){
                mOnLocationClickListener.onClick(item);
            }
        });
        double distance = item.getDistance();
        if (distance>0){
            double distance2 = (double) Math.round(distance * 100) / 100;
            tvDistance.setText("直线距离:" + distance2 + "千米");
        }
        String lastTime = item.getLastTime();
        String nowTime = InspectionDateUtils.getNowDate();
        try {
            if (null==lastTime||"".equals(lastTime)){
                tvDate.setVisibility(View.GONE);
                tvInspectionStatus.setText("建议巡查");
//                tvInspectionStatus.setBackgroundResource(R.drawable.bg_reservoir_status);
                tvInspectionStatus.setBackgroundColor(Color.parseColor("#68ABFA"));
            }else {
                Date lastDate = sdf.parse(lastTime);
                Date nowDate = sdf.parse(nowTime);
                int days = InspectionDateUtils.differentDays(lastDate, nowDate);
                if (0<days&&days<=3){
                   tvInspectionStatus.setText("无需巡查");
//                    tvInspectionStatus.setBackgroundResource(R.drawable.bg_reservoir_status_two);
                    tvInspectionStatus.setBackgroundColor(Color.parseColor("#4FB97E"));
                    tvDate.setVisibility(View.VISIBLE);
                    tvDate.setText("距上次巡查"+days+"天");
                }
                if (days==0){
                    tvInspectionStatus.setText("今日已巡查");
//                    tvInspectionStatus.setBackgroundResource(R.drawable.bg_reservoir_status_two);
                    tvInspectionStatus.setBackgroundColor(Color.parseColor("#4FB97E"));
                    tvDate.setVisibility(View.GONE);
                }
                if (days>3){
                    tvInspectionStatus.setText("建议巡查");
//                    tvInspectionStatus.setBackgroundResource(R.drawable.bg_reservoir_status);
                    tvInspectionStatus.setBackgroundColor(Color.parseColor("#68ABFA"));
                    tvDate.setVisibility(View.VISIBLE);
                    tvDate.setText("距上次巡查"+days+"天");
                }
                if (days<0){
                    tvInspectionStatus.setText("建议巡查");
//                    tvInspectionStatus.setBackgroundResource(R.drawable.bg_reservoir_status);
                    tvInspectionStatus.setBackgroundColor(Color.parseColor("#68ABFA"));
                    tvDate.setVisibility(View.VISIBLE);
                    tvDate.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
//            e.printStackTrace();
            tvDate.setVisibility(View.GONE);
            tvInspectionStatus.setText("建议巡查");
//            tvInspectionStatus.setBackgroundResource(R.drawable.bg_reservoir_status);
            tvInspectionStatus.setBackgroundColor(Color.parseColor("#68ABFA"));
        }


    }
}
