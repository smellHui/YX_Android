package com.tepia.guangdong_module.amainguangdong.xunchaview.adapter;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.guangdong_module.R;
import com.example.guangdong_module.databinding.LvPatrolWorkorderListBinding;
import com.tepia.base.AppRoutePath;
import com.tepia.base.utils.ToastUtils;
import com.tepia.base.utils.Utils;
import com.tepia.base.view.floatview.CollectionsUtil;
import com.tepia.guangdong_module.amainguangdong.route.TaskBean;
import com.tepia.guangdong_module.amainguangdong.route.TaskItemBean;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
  * Created by      Android studio
  *
  * @author :ly (from Center Of Wuhan)
  * 创建时间 :2019-5
  * 更新时间 :
  * Version :1.0
  * 功能描述 :巡查记录
 **/
public class AdapterPatrolWorkOrderList extends BaseQuickAdapter<TaskBean, BaseViewHolder> {

    LvPatrolWorkorderListBinding mBinding;
    public AdapterPatrolWorkOrderList(int layoutResId, @Nullable List<TaskBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TaskBean item) {

        mBinding = DataBindingUtil.bind(helper.itemView);

        mBinding.tvDate.setText(item.getStartTime());
        if (!TextUtils.isEmpty(item.getCreateDate())) {
            mBinding.tvDate.setText(item.getCreateDate());

        }

        mBinding.tvReservoir.setText(item.getRouteName());
        mBinding.tvExecWorker.setText(item.getExecutorName());
        mBinding.ivStatus.setVisibility(View.INVISIBLE);
        TaskBean taskBean =  DataSupport.where("workorderid=?",item.getWorkOrderId()).findFirst(TaskBean.class);
        if (taskBean != null && taskBean.getExecuteStatus() != null) {
            mBinding.ivStatus.setVisibility(View.VISIBLE);
            String executeStatus = taskBean.getExecuteStatus();
            switch (executeStatus) {

                case "2":
//                    mBinding.ivStatus.setImageResource(R.drawable.operation_not_complete);
                    mBinding.ivStatus.setBackground(ContextCompat.getDrawable(Utils.getContext(),R.drawable.bg_yunwei_status_red));
                    mBinding.ivStatus.setText("待巡查");

                    List<TaskItemBean> itemList = DataSupport.where("workorderid=? and completestatus=?",
                            taskBean.getWorkOrderId(),"0").find(TaskItemBean.class);
                    if (CollectionsUtil.isEmpty(itemList)) {
                        mBinding.ivStatus.setBackground(ContextCompat.getDrawable(Utils.getContext(),R.drawable.bg_yunwei_status_report));
                        mBinding.ivStatus.setText("待上报");
                    }
                    break;
                case "3":
//                    mBinding.ivStatus.setImageResource(R.drawable.operation_complete);
                    mBinding.ivStatus.setBackground(ContextCompat.getDrawable(Utils.getContext(),R.drawable.bg_yunwei_status_blue));
                    mBinding.ivStatus.setText("已完成");

                    break;

                default:
//                    mBinding.ivStatus.setImageResource(R.drawable.operation_complete);
                    break;
            }
        }else if(taskBean == null){
            mBinding.ivStatus.setVisibility(View.VISIBLE);
            mBinding.ivStatus.setBackground(ContextCompat.getDrawable(Utils.getContext(),R.drawable.bg_yunwei_status_blue));
            mBinding.ivStatus.setText("已完成");
        }




        if (helper.getLayoutPosition() % 2 == 0) {
            helper.setBackgroundColor(R.id.rootLy, ContextCompat.getColor(mContext, R.color.white));
        } else {
            helper.setBackgroundColor(R.id.rootLy, ContextCompat.getColor(mContext, R.color.reportitem));
        }

        //executeResultType
        List<TaskItemBean> taskItemBeanList = DataSupport.where("workorderid=? and executeResultType=? or workorderid=? and executeResultType=?",
                item.getWorkOrderId(),"1",item.getWorkOrderId(),"3").find(TaskItemBean.class);
        if (!CollectionsUtil.isEmpty(taskItemBeanList)) {
            mBinding.isTv.setText(taskItemBeanList.size()+"个");
            mBinding.isTv.setTextColor(ContextCompat.getColor(Utils.getContext(),R.color.common_red));

        }else{
            TaskBean taskBean1 =  DataSupport.where("workorderid=?",item.getWorkOrderId()).findFirst(TaskBean.class);
            if (taskBean1 != null) {
                mBinding.isTv.setText("0");
                mBinding.isTv.setTextColor(ContextCompat.getColor(Utils.getContext(),R.color.text_color_is));
            }else{
                mBinding.isTv.setText("--");
                mBinding.isTv.setTextColor(ContextCompat.getColor(Utils.getContext(),R.color.text_color_is));
            }

        }
    }
}
