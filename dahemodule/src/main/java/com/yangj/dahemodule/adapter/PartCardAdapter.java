package com.yangj.dahemodule.adapter;

import android.content.res.Resources;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.RoutePosition;
import com.tepia.guangdong_module.amainguangdong.utils.SqlManager;
import com.yangj.dahemodule.R;

/**
 * Author:xch
 * Date:2019/11/12
 * Description:
 */
public class PartCardAdapter extends BaseQuickAdapter<RoutePosition, BaseViewHolder> {

    private Resources resources;

    public PartCardAdapter() {
        super(R.layout.item_part_card);
    }

    @Override
    protected void convert(BaseViewHolder helper, RoutePosition item) {
        resources = mContext.getResources();
        int completedNum = SqlManager.getInstance().getCompletedTaskById(item.getWorkOrderId(), item.getPositionId());
        int notCompletedNum = SqlManager.getInstance().getNotCompletedTaskById(item.getWorkOrderId(), item.getPositionId());
        int normalNum = SqlManager.getInstance().getNormalTaskById(item.getWorkOrderId(), item.getPositionId(), true);
        int abnormalNum = SqlManager.getInstance().getNormalTaskById(item.getWorkOrderId(), item.getPositionId(), false);
        helper.setText(R.id.tv_has_patrol_num, completedNum + "");
        helper.setText(R.id.tv_not_patrol_num, notCompletedNum + "");
        helper.setText(R.id.tv_normal_num, normalNum + "");
        helper.setTextColor(R.id.tv_normal_num, resources.getColor(normalNum == 0 ? R.color.color_BDBDBD : R.color.color_2BA5FF));
        helper.setText(R.id.tv_abnormal_num, abnormalNum + "");
        helper.setTextColor(R.id.tv_abnormal_num, resources.getColor(abnormalNum == 0 ? R.color.color_BDBDBD : R.color.color_FFB61C));
        helper.setText(R.id.tv_route_name, item.getStructureName());
        View cardView = helper.getView(R.id.ll_card);
        boolean isNear = item.getIsNear() == 1;
        if (isNear) {
            helper.setText(R.id.tv_route_type, "临近巡查点");
            cardView.setBackground(mContext.getResources().getDrawable(R.mipmap.patrol_yellow));
        } else {
            if (notCompletedNum != 0) {
                helper.setText(R.id.tv_route_type, "待巡查点");
                cardView.setBackground(mContext.getResources().getDrawable(R.mipmap.patrol_green));
            } else {
                helper.setText(R.id.tv_route_type, "已巡查点");
                cardView.setBackground(mContext.getResources().getDrawable(R.mipmap.patrol_blue));
            }
        }

    }
}
