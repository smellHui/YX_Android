package com.tepia.guangdong_module.amainguangdong.xunchaview.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.guangdong_module.R;
import com.tepia.base.utils.TimeFormatUtils;
import com.tepia.guangdong_module.amainguangdong.route.TaskItemBean;
import com.tepia.guangdong_module.amainguangdong.xunchaview.activity.DangerReportActivity;
import com.tepia.guangdong_module.amainguangdong.xunchaview.activity.StartInspectionActivity;
import com.tepia.guangdong_module.amainguangdong.xunchaview.activity.TroubleRecordActivity;
import com.tepia.photo_picker.entity.CheckTaskPicturesBean;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
  * Created by      Android studio
  *
  * @author :wwj (from Center Of Wuhan)
  * Date    :2019/5/5
  * Version :1.0
  * 功能描述 :
 **/

public class AdapterTaskItemListNew extends BaseQuickAdapter<TaskItemBean, BaseViewHolder> {
    private StartInspectionActivity mactivity;

    public AdapterTaskItemListNew(int layoutResId, @Nullable List<TaskItemBean> data, StartInspectionActivity activity) {
        super(layoutResId, data);
        this.mactivity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, TaskItemBean item) {
        View view = helper.getView(R.id.rootRyOfitem);
        view.setEnabled(false);
        Button editBtn = helper.getView(R.id.editBtn);
        Button btAbnormal = helper.getView(R.id.bt_abnormal);
        Button normalBtn = helper.getView(R.id.normalBtn);
        Button hasNormalBtn = helper.getView(R.id.hasNormalBtn);
        Button stillAbBtn = helper.getView(R.id.stillAbBtn);
        Button faxianyichangBtn = helper.getView(R.id.faxianyichangBtn);
        Button querenNormalBtn = helper.getView(R.id.querenNormalBtn);
        ImageView normalIv = helper.getView(R.id.normalIv);
        ImageView errorIv = helper.getView(R.id.errorIv);
        TextView tvInspectionName = helper.getView(R.id.tv_inspection_name);
        tvInspectionName.setText(item.getPositionTreeNames());
        TextView tvInspectionDescribe = helper.getView(R.id.tv_inspection_describe);
        tvInspectionDescribe.setText(item.getSuperviseItemContent());
        TextView tvInspectionDate = helper.getView(R.id.tv_inspection_date);
        TextView lastExcuteDateTv = helper.getView(R.id.lastExcuteDateTv);
        String lastExcuteDate = item.getLastExcuteDate();

        String executeResultDescription = item.getExecuteResultDescription();
        String lastExecuteResultDescription = item.getLastExecuteResultDescription();



        if (!TextUtils.isEmpty(lastExecuteResultDescription)){
            tvInspectionDate.setText(lastExecuteResultDescription);
            tvInspectionDate.setVisibility(View.VISIBLE);

        }else {
//            --记录异常
            tvInspectionDate.setText("--");
            tvInspectionDate.setVisibility(View.INVISIBLE);

        }
        if (!TextUtils.isEmpty(executeResultDescription)){
            tvInspectionDate.setText(executeResultDescription);
            tvInspectionDate.setVisibility(View.VISIBLE);

        }else {
//            --记录异常
            tvInspectionDate.setText("--");
            tvInspectionDate.setVisibility(View.INVISIBLE);
        }


        String completeStatus = item.getCompleteStatus();
        TextView completeStatusTv = helper.getView(R.id.completeStatusTv);
        if ("1".equals(completeStatus)) {
            completeStatusTv.setText("该项巡查已完成");
            completeStatusTv.setTextColor(ContextCompat.getColor(mContext,R.color.common_blue));
        }else {
            completeStatusTv.setText("待巡查");
            completeStatusTv.setTextColor(ContextCompat.getColor(mContext,R.color.common_red));
        }

        String operationLevel = item.getOperationLevel();
        //1：一般项 2：报警项
        ImageView iv_alarm_2 = helper.getView(R.id.iv_alarm_2);
        ImageView iv_alarm_1 = helper.getView(R.id.iv_alarm_1);
        iv_alarm_1.setVisibility(View.GONE);
        iv_alarm_2.setVisibility(View.GONE);
        if ("1".equals(operationLevel)) {
            iv_alarm_1.setVisibility(View.VISIBLE);
            iv_alarm_2.setVisibility(View.GONE);
        }else if("2".equals(operationLevel)){
            iv_alarm_2.setVisibility(View.VISIBLE);
            iv_alarm_1.setVisibility(View.GONE);
        }



        String current = item.getExecuteResultType();
        String last = item.getLastExcuteResultType();
        faxianyichangBtn.setVisibility(View.GONE);
        querenNormalBtn.setVisibility(View.GONE);
        hasNormalBtn.setVisibility(View.GONE);
        stillAbBtn.setVisibility(View.GONE);
        btAbnormal.setVisibility(View.GONE);
        normalBtn.setVisibility(View.GONE);

        errorIv.setVisibility(View.INVISIBLE);
        normalIv.setVisibility(View.INVISIBLE);





        if (TextUtils.isEmpty(last) || "0".equals(last)) {
            hasNormalBtn.setVisibility(View.GONE);
            stillAbBtn.setVisibility(View.GONE);
            btAbnormal.setVisibility(View.VISIBLE);
            normalBtn.setVisibility(View.VISIBLE);


            if ("0".equals(current)) {
                normalIv.setVisibility(View.VISIBLE);
                errorIv.setVisibility(View.GONE);
                faxianyichangBtn.setVisibility(View.VISIBLE);

                hasNormalBtn.setVisibility(View.GONE);
                stillAbBtn.setVisibility(View.GONE);
                btAbnormal.setVisibility(View.GONE);
                normalBtn.setVisibility(View.GONE);
            }else if("1".equals(current)){
                errorIv.setVisibility(View.VISIBLE);
                goTrouble(view,item);

                normalIv.setVisibility(View.GONE);
                querenNormalBtn.setVisibility(View.VISIBLE);

                hasNormalBtn.setVisibility(View.GONE);
                stillAbBtn.setVisibility(View.GONE);
                btAbnormal.setVisibility(View.GONE);
                normalBtn.setVisibility(View.GONE);
            }

        }else if("1".equals(last) || "3".equals(last)){
            hasNormalBtn.setVisibility(View.VISIBLE);
            stillAbBtn.setVisibility(View.VISIBLE);
            btAbnormal.setVisibility(View.GONE);
            normalBtn.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(lastExcuteDate)) {
                if("1".equals(last)) {
                    lastExcuteDateTv.setText(lastExcuteDate + " " + "记录异常");
                }
                if("3".equals(last)) {
                    lastExcuteDateTv.setText(lastExcuteDate + " " + "仍然异常");
                }

            }
            if ("2".equals(current)) {
                normalIv.setVisibility(View.VISIBLE);
                errorIv.setVisibility(View.GONE);
                stillAbBtn.setVisibility(View.VISIBLE);

                hasNormalBtn.setVisibility(View.GONE);
                btAbnormal.setVisibility(View.GONE);
                normalBtn.setVisibility(View.GONE);


            }else if ("3".equals(current)) {
                errorIv.setVisibility(View.VISIBLE);
                goTrouble(view,item);

                normalIv.setVisibility(View.GONE);
                hasNormalBtn.setVisibility(View.VISIBLE);

                stillAbBtn.setVisibility(View.GONE);
                btAbnormal.setVisibility(View.GONE);
                normalBtn.setVisibility(View.GONE);

            }


        }else if("2".equals(last)){
            normalBtn.setVisibility(View.VISIBLE);
            stillAbBtn.setVisibility(View.VISIBLE);
            hasNormalBtn.setVisibility(View.GONE);
            btAbnormal.setVisibility(View.GONE);

            if (!TextUtils.isEmpty(lastExcuteDate)) {
                if("2".equals(last)) {
                    lastExcuteDateTv.setText(lastExcuteDate + "  " + "更新正常");
                }


            }

            if ("0".equals(current)) {
                normalIv.setVisibility(View.VISIBLE);
                errorIv.setVisibility(View.GONE);
                stillAbBtn.setVisibility(View.VISIBLE);

                hasNormalBtn.setVisibility(View.GONE);
                btAbnormal.setVisibility(View.GONE);
                normalBtn.setVisibility(View.GONE);


            }else if("3".equals(current)) {
                errorIv.setVisibility(View.VISIBLE);
                goTrouble(view,item);

                normalIv.setVisibility(View.GONE);
                querenNormalBtn.setVisibility(View.VISIBLE);

                hasNormalBtn.setVisibility(View.GONE);
                stillAbBtn.setVisibility(View.GONE);
                btAbnormal.setVisibility(View.GONE);
                normalBtn.setVisibility(View.GONE);
            }

        }


        if ("0".equals(item.getCompleteStatus())) {
            editBtn.setVisibility(View.GONE);
        }else{
            editBtn.setVisibility(View.VISIBLE);
            faxianyichangBtn.setVisibility(View.GONE);
            querenNormalBtn.setVisibility(View.GONE);
            hasNormalBtn.setVisibility(View.GONE);
            stillAbBtn.setVisibility(View.GONE);
            btAbnormal.setVisibility(View.GONE);
            normalBtn.setVisibility(View.GONE);
        }




        TextView showExecuteResultTypeTv = helper.getView(R.id.showExecuteResultTypeTv);
        showExecuteResultTypeTv.setVisibility(View.GONE);
        if (mactivity != null && mactivity.isCompleteOfTaskBean()) {
//            goTrouble(view,item);
            showExecuteResultTypeTv.setVisibility(View.VISIBLE);
            faxianyichangBtn.setVisibility(View.GONE);
            querenNormalBtn.setVisibility(View.GONE);
            hasNormalBtn.setVisibility(View.GONE);
            stillAbBtn.setVisibility(View.GONE);
            btAbnormal.setVisibility(View.GONE);
            normalBtn.setVisibility(View.GONE);
            editBtn.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(current)) {
                switch (current){
                    case "0":
                        showExecuteResultTypeTv.setText("");
                        break;
                    case "1":
                        showExecuteResultTypeTv.setText("新发现异常:");
                        break;
                    case "2":
                        showExecuteResultTypeTv.setText("异常已处理");
                        break;
                    case "3":
                        showExecuteResultTypeTv.setText("异常状态延续:");

                        break;
                    default:
                        break;
                }
            }


        }
        //正常
        normalBtn.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(item.getExecuteResultDescription())) {
                showDialog(item,"0");
                return;
            }
            clickNormal(item,"0");
        });
        //异常
        btAbnormal.setOnClickListener(v -> {
            if (onGetCurrentClickListener != null) {
                onGetCurrentClickListener.onCilck(item);
            }
            item.setIsCommitLocal("0");
            item.setCompleteStatus("0");
            item.updateAll("itemId=?",item.getItemId());

            refresh(item);
            goToTroubleRecordView(item.getItemId(),"1");

        });
        //已正常
        hasNormalBtn.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(item.getExecuteResultDescription())) {
                showDialog(item,"2");
                return;
            }
            clickNormal(item,"2");

        });
        //仍异常
        stillAbBtn.setOnClickListener(v -> {
            if (onGetCurrentClickListener != null) {
                onGetCurrentClickListener.onCilck(item);
            }
            item.setIsCommitLocal("0");
            item.setCompleteStatus("0");
            item.updateAll("itemId=?",item.getItemId());

            refresh(item);

            goToTroubleRecordView(item.getItemId(),"3");
        });
        //确认正常
        querenNormalBtn.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(item.getExecuteResultDescription())) {
                showDialog(item,"0");
                return;
            }
            clickNormal(item,"0");
        });

        //发现异常
        faxianyichangBtn.setOnClickListener(v -> {
            if (onGetCurrentClickListener != null) {
                onGetCurrentClickListener.onCilck(item);
            }

            item.setCompleteStatus("0");
            item.setIsCommitLocal("0");
            item.updateAll("itemId=?",item.getItemId());
            refresh(item);

            goToTroubleRecordView(item.getItemId(),"1");
        });

        editBtn.setOnClickListener(v -> {

            boolean isNormal = "".equals(current) || "0".equals(current) || "2".equals(current);
            if (isNormal) {
                new AlertDialog.Builder(mContext)
                        .setMessage("正在将该巡查项的巡查结果改为异常,是否继续执行?")
                        .setPositiveButton("确定", (dialog, which) -> {
                            if (onGetCurrentClickListener != null) {
                                onGetCurrentClickListener.onCilck(item);
                            }

                            item.setCompleteStatus("0");
                            item.setIsCommitLocal("0");
                            item.updateAll("itemId=?",item.getItemId());
                            refresh(item);
                            goToTroubleRecordView(item.getItemId(),"1");
                        })
                        .setNegativeButton("取消", (dialog, which) -> {

                        }).show();
            }else{
                if (!TextUtils.isEmpty(item.getExecuteResultDescription())) {
                    showDialog(item,"2");
                    return;
                }
                clickNormal(item,"2");
            }
        });




    }

    private void goTrouble(View view, TaskItemBean taskItemBean){
        view.setEnabled(true);
        view.setOnClickListener(v -> {
            goToTroubleRecordView(taskItemBean.getItemId(),taskItemBean.getExecuteResultType());
        });
    }

    private void goToTroubleRecordView(String id,String executeResultType){
        Intent intent = new Intent(mContext, TroubleRecordActivity.class);
        intent.putExtra("item",id);
        intent.putExtra("executeResultType",executeResultType);
        mContext.startActivity(intent);
    }


    private void showDialog(TaskItemBean item,String executeResultType){
        new AlertDialog.Builder(mactivity)
                .setMessage("目前有异常描述信息，是否清空异常并将巡查项结果修改为正常?")
                .setPositiveButton("确定", (dialog, which) -> clickNormal(item,executeResultType))
                .setNegativeButton("取消", (dialog, which) -> {

                }).show();

    }

    /**
     * 恢复正常
     * @param item
     * @param executeResultType
     */
    private void clickNormal(TaskItemBean item,String executeResultType){
        if (onGetCurrentClickListener != null) {
            onGetCurrentClickListener.onCilck(item);
        }
        item.setExcuteDate(TimeFormatUtils.getStringDate());

        item.setIsCommitLocal("0");
        item.setExecuteResultDescription("");
        item.setBeforelist("");
//        item.setPositionName("");
        DataSupport.deleteAll(CheckTaskPicturesBean.class,"itemId=?",item.getItemId());

        item.setCompleteStatus("1");
        item.setExecuteResultType(executeResultType);
        item.updateAll("itemId=?",item.getItemId());
        refresh(item);
    }

    public OnGetCurrentClickListener onGetCurrentClickListener;

    public OnGetCurrentClickListener getOnGetCurrentClickListener() {
        return onGetCurrentClickListener;
    }

    public void setOnGetCurrentClickListener(OnGetCurrentClickListener onGetCurrentClickListener) {
        this.onGetCurrentClickListener = onGetCurrentClickListener;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public interface OnGetCurrentClickListener {
        void onCilck(TaskItemBean taskItemBean);
    }

    private void refresh(TaskItemBean taskItemBean){
        List<TaskItemBean> taskItemBeans = DataSupport.where("positionid=? and fatherId=? and userCode=? and reservoirId=? and workorderid=?",
                taskItemBean.getPositionId(),taskItemBean.getFatherId(),taskItemBean.getUserCode(),taskItemBean.getReservoirId(),taskItemBean.getWorkOrderId()).find(TaskItemBean.class);
        setNewData(taskItemBeans);
        mactivity.refreshTitle();
    }

    /**
     * 有多少离线数据没有提交
     *
     * @return
     */
    public List<TaskItemBean> getLocalData(String workorderid) {

        List<TaskItemBean> localData = new ArrayList<>();
        localData = DataSupport.where("workorderid=? and iscommitlocal=?",workorderid,"0").find(TaskItemBean.class);


        return localData;
    }

    /**
     * 有异常的项
     *
     * @return
     */
    public List<TaskItemBean> getAbnormalityList(String workorderid) {
        List<TaskItemBean> abnormality = new ArrayList<>();

        List<TaskItemBean> abnormality1 = DataSupport.where("workorderid=? and iscommitlocal=? and executeResultType=? ",workorderid,"0","1").find(TaskItemBean.class);
        List<TaskItemBean> abnormality2 = DataSupport.where("workorderid=? and iscommitlocal=? and executeResultType=?",workorderid,"0","3").find(TaskItemBean.class);
        abnormality.addAll(abnormality1);
        abnormality.addAll(abnormality2);
        return abnormality;
    }
}
