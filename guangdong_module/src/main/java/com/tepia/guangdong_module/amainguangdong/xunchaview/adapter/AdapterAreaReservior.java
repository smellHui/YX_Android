package com.tepia.guangdong_module.amainguangdong.xunchaview.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.guangdong_module.databinding.ItemAreaLayoutBinding;
import com.example.guangdong_module.databinding.LvTabMainWorkerItemBinding;
import com.tepia.base.utils.LogUtil;
import com.tepia.base.utils.ScreenUtil;
import com.tepia.base.utils.Utils;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.AreaReservoirCountBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.PersonDutyBean;

import java.util.List;

import okhttp3.internal.Util;

/**
  * Created by      Android studio
  *
  * @author :ly (from Center Of Wuhan)
  * 创建时间 :2019-6-4
  * 更新时间 :
  * Version :1.0
  * 功能描述 :水库监测情况统计
 **/
public class AdapterAreaReservior extends BaseQuickAdapter<AreaReservoirCountBean.DataBean, BaseViewHolder> {
    private int unitDp = 0;
    ItemAreaLayoutBinding mBinding;
    private int maxNum;
    public AdapterAreaReservior(int layoutResId, @Nullable List<AreaReservoirCountBean.DataBean> data) {
        super(layoutResId, data);

    }

    public void setUnitDp(int mMaxNum){
        maxNum = mMaxNum;
        int widthScreen = ScreenUtil.getScreenWidthPix(Utils.getContext());
        unitDp = widthScreen*5/6/mMaxNum - 20 ;
        if(unitDp < 0){
            unitDp = 0;
        }
        LogUtil.e("-----------widthScreen:"+widthScreen+",unitDp:"+unitDp);
    }

    private Integer monitorCount;
    private Integer reportCount;
    private String allCount;
    @Override
    protected void convert(BaseViewHolder helper, AreaReservoirCountBean.DataBean item) {
        mBinding = DataBindingUtil.bind(helper.itemView);
        monitorCount = item.getMonitorCount();
        allCount = item.getAllCount()+"";
        reportCount = item.getReportCount();
        mBinding.nameOfareaTv.setText(item.getAreaName());
        mBinding.allOfnumTv.setText(allCount);
        mBinding.monitorOfnumTv.setText(monitorCount+"");
        mBinding.reportOfnumTv.setText(reportCount+"");

        setData(mBinding.allCountTv,Integer.valueOf(allCount),
                mBinding.monitorCountTv,monitorCount.intValue(),
                mBinding.reportCountTv,reportCount.intValue());

        int margin = 10;
        LinearLayout.LayoutParams maxlayoutparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        int widthScreen = ScreenUtil.getScreenWidthPix(Utils.getContext());
        maxlayoutparams.width = widthScreen*5/6;
        maxlayoutparams.weight = 1;
        maxlayoutparams.topMargin = margin;
        maxlayoutparams.bottomMargin = margin;
//        maxlayoutparams.gravity = Gravity.FILL_VERTICAL;

        mBinding.allLy.setLayoutParams(maxlayoutparams);
        mBinding.monitorLy.setLayoutParams(maxlayoutparams);
        mBinding.reportLy.setLayoutParams(maxlayoutparams);


    }

    private void setData(TextView textViewAll,int allCountWidth,TextView textViewMonitor,int monitorCountWidth,TextView textViewReport,int reportCountWidth){
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        LinearLayout.LayoutParams layoutParamsOfall = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        layoutParamsOfall.width = allCountWidth*unitDp;
        textViewAll.setLayoutParams(layoutParamsOfall);



        LinearLayout.LayoutParams layoutParamsOfmonitor = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        layoutParamsOfmonitor.width = monitorCountWidth*unitDp;
        /*layoutParamsOfmonitor.weight = 1;
        layoutParamsOfmonitor.topMargin = margin;
        layoutParamsOfmonitor.bottomMargin = margin;
        layoutParamsOfmonitor.gravity = Gravity.FILL_VERTICAL;*/

        textViewMonitor.setLayoutParams(layoutParamsOfmonitor);

        LinearLayout.LayoutParams layoutParamsOfreport = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        layoutParamsOfreport.width = reportCountWidth*unitDp;
       /* layoutParamsOfreport.weight = 1;
        layoutParamsOfreport.topMargin = margin;
        layoutParamsOfreport.bottomMargin = margin;
        layoutParamsOfreport.gravity = Gravity.FILL_VERTICAL;*/
        textViewReport.setLayoutParams(layoutParamsOfreport);

    }

}
