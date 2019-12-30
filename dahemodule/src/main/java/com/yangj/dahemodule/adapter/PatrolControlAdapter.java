package com.yangj.dahemodule.adapter;

import android.content.res.Resources;
import android.text.Html;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tepia.guangdong_module.amainguangdong.route.TaskItemBean;
import com.yangj.dahemodule.R;
import com.yangj.dahemodule.weight.SwipeLayout;

/**
 * Author:xch
 * Date:2019/11/25
 * Description:
 */
public class PatrolControlAdapter extends BaseMultiItemQuickAdapter<TaskItemBean, BaseViewHolder> {

    private boolean isCompleteOfTaskBean;

    public PatrolControlAdapter(boolean isCompleteOfTaskBean) {
        super(null);
        this.isCompleteOfTaskBean = isCompleteOfTaskBean;
//        super(R.layout.item_patrol_control);
//        super(R.layout.item_right_menu);
        addItemType(0, R.layout.item_patrol_first);
        addItemType(1, R.layout.item_patrol_normal);
        addItemType(2, R.layout.item_patrol_abnormal);
    }

    @Override
    protected void convert(BaseViewHolder helper, TaskItemBean item) {
        Resources resources = mContext.getResources();
        helper.setText(R.id.tv_title, item.getSuperviseItemContent());
        boolean isNormal = item.isNormal();
        boolean isCompleted = item.isCompleted();
        helper.setGone(R.id.tv_exect_type, isCompleted);
        String exectTypeStr;
        if (isNormal) {
            exectTypeStr = "<font color='#2BA5FF'>正常√ </font>";
        } else {
            exectTypeStr = "<font color='#FFB61C'>异常！</font><font color='#979EAB'>" + item.getExecuteResultDescription() + " </font>";
        }
        helper.setText(R.id.tv_exect_type, Html.fromHtml(exectTypeStr));
        helper.setText(R.id.tv_status, isCompleted ? "已巡查" : "未巡查");
        helper.setBackgroundRes(R.id.tv_status, isCompleted ? R.mipmap.grey : R.mipmap.blue);

        SwipeLayout swipeLayout = helper.getView(R.id.swipe_layout);
        swipeLayout.setEnabledSwipe(!isCompleteOfTaskBean);
        swipeLayout.close();
        swipeLayout.setOnActionsListener(new SwipeLayout.SwipeActionsListener() {
            @Override
            public void onOpen(int direction, boolean isContinuous) {
                helper.setBackgroundColor(R.id.drag_item, resources.getColor(R.color.color_F3F5F8));
            }

            @Override
            public void onClose() {
                helper.setBackgroundColor(R.id.drag_item, resources.getColor(R.color.white));
            }
        });
        if (!isCompleteOfTaskBean) {
            helper.getView(R.id.img_right).setOnClickListener(v -> {
                if (swipeLayout.isClosed()) {
                    swipeLayout.openRight();
                } else {
                    swipeLayout.close();
                }
            });
        }

        helper.addOnClickListener(R.id.drag_item);
        switch (helper.getItemViewType()) {
            case 0:
                helper.addOnClickListener(R.id.btn_normal);
                helper.addOnClickListener(R.id.btn_abnormal);
                break;
            case 1:
                helper.addOnClickListener(R.id.btn_to_abnormal);
                break;
            case 2:
                helper.addOnClickListener(R.id.btn_to_normal);
                break;
        }

    }
}
