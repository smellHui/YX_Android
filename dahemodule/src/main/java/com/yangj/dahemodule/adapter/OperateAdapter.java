package com.yangj.dahemodule.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.common.base.Strings;
import com.lxj.xpopup.XPopup;
import com.tepia.guangdong_module.amainguangdong.utils.SqlManager;
import com.yangj.dahemodule.R;
import com.yangj.dahemodule.model.xuncha.RecordBean;

import static com.yangj.dahemodule.fragment.operate.OperatesFragment.ALL_OPERATE;

/**
 * Author:xch
 * Date:2019/8/28
 * Description:
 */
public class OperateAdapter extends BaseQuickAdapter<RecordBean, BaseViewHolder> {

    private int pageType;

    public OperateAdapter(int pageType) {
        super(R.layout.item_operate);
        this.pageType = pageType;
    }

    @Override
    protected void convert(BaseViewHolder helper, RecordBean item) {

        helper.setText(R.id.tv_name, item.getName());
        helper.setText(R.id.tv_createTime, item.getCreateTime());

        int abnormalColor = mContext.getResources().getColor(item.isZeroAbnormal() ? R.color.color_1785F8 : R.color.color_FFA200);
        helper.setTextColor(R.id.tv_executeStatus, abnormalColor);
        helper.setTextColor(R.id.tv_abnormalNum, abnormalColor);
        helper.setText(R.id.tv_abnormalNum, item.getAbnormalNum());
        helper.setVisible(R.id.tv_creatorName, pageType == ALL_OPERATE);
        helper.setText(R.id.tv_creatorName, item.getCreatorName());

        String executeStatus = item.getExecuteStatus();
        if (Strings.isNullOrEmpty(executeStatus) || executeStatus.equals("1")) {
            helper.setBackgroundRes(R.id.tv_status, R.mipmap.blue);
            helper.setText(R.id.tv_status, "巡查中");
        } else if (executeStatus.equals("2")) {
            helper.setBackgroundRes(R.id.tv_status, R.mipmap.green);
            helper.setText(R.id.tv_status, "待审核");
        } else {
            helper.setBackgroundRes(R.id.tv_status, R.mipmap.grey);
            helper.setText(R.id.tv_status, "已审核");
        }
        //长按删除本地数据
        if (item.isTemporary()) {
            //必须要在事件发生之前就watch
            final XPopup.Builder builder = new XPopup.Builder(mContext).watchView(helper.itemView);
            helper.itemView.setOnLongClickListener(v -> {
                builder.asAttachList(new String[]{"删除"}, null, 0, 10, (position, text) -> {
                    SqlManager.getInstance().deleteTask(item.getCode());
                    getData().remove(helper.getAdapterPosition());
                    notifyItemRemoved(helper.getAdapterPosition());
                }).show();
                return true;
            });
        }
    }
}
