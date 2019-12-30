package com.yangj.dahemodule.adapter;

import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yangj.dahemodule.R;
import com.yangj.dahemodule.model.patrol.PatrolFlowBean;

/**
 * Author:xch
 * Date:2019/12/23
 * Description:
 */
public class PatrolRecordAdapter extends BaseMultiItemQuickAdapter<PatrolFlowBean, BaseViewHolder> {

    private ReviewPatrolListener reviewPatrolListener;
    private String content;

    public PatrolRecordAdapter(ReviewPatrolListener reviewPatrolListener) {
        super(null);
        this.reviewPatrolListener = reviewPatrolListener;
        //提交的信息
        addItemType(1, R.layout.view_patrol_reported);
        //审核
        addItemType(2, R.layout.view_patrol_review);
        //审核详情
        addItemType(3, R.layout.view_patrol_comment);
        //完结
        addItemType(4, R.layout.view_complete);
        //执行中
        addItemType(5, R.layout.view_complete);
        //待审核
        addItemType(6, R.layout.view_complete);
    }

    @Override
    protected void convert(BaseViewHolder helper, PatrolFlowBean item) {
        TextView tv_type = helper.getView(R.id.tv_type);
        tv_type.setText(item.getFlowName());
        int itemType = helper.getItemViewType();
        switch (itemType) {
            case 1:
                helper.setText(R.id.tv_report, String.format("上报人：%s", item.getExecutorName()));
                helper.setText(R.id.tv_created_time, String.format("上报时间：%s", item.getExecuteDate()));
                break;
            case 2:
                helper.setText(R.id.tv_report, String.format("审核人：%s", item.getExecutorName()));
                helper.setText(R.id.tv_created_time, String.format("反馈时间：%s", item.getExecuteDate()));
                tv_type.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_patrol_3, 0, 0, 0);
                EditText editText = helper.getView(R.id.et_content);
                helper.getView(R.id.btn_pass).setOnClickListener(v -> {
                    content = editText.getText().toString().trim();
                    if (reviewPatrolListener != null)
                        reviewPatrolListener.reviewPatrolClick("0", content);
                });
                helper.getView(R.id.btn_back).setOnClickListener(v -> {
                    content = editText.getText().toString().trim();
                    if (reviewPatrolListener != null)
                        reviewPatrolListener.reviewPatrolClick("1", content);
                });
                break;
            case 3:
                helper.setText(R.id.tv_report, String.format("审核人：%s", item.getExecutorName()));
                helper.setText(R.id.tv_created_time, String.format("反馈时间：%s", item.getExecuteDate()));
                tv_type.setCompoundDrawablesWithIntrinsicBounds(item.isPass() ? R.mipmap.icon_patrol_3 : R.mipmap.icon_patrol_1, 0, 0, 0);
                helper.setText(R.id.tv_result_des, String.format("审核意见：%s", item.getResultDes()));
                break;
            case 6:
                tv_type.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_patrol_1, 0, 0, 0);
                break;
        }
    }

    public interface ReviewPatrolListener {
        void reviewPatrolClick(String resultType, String resultDes);
    }
}
