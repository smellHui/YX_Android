package com.yangj.dahemodule.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yangj.dahemodule.R;
import com.yangj.dahemodule.model.main.Route;

/**
 * Author:xch
 * Date:2019/9/18
 * Description:
 */
public class OperateMainAdapter extends BaseQuickAdapter<Route, BaseViewHolder> {

    public OperateMainAdapter() {
        super(R.layout.item_operate_main);
    }

    @Override
    protected void convert(BaseViewHolder helper, Route item) {
        View bg = helper.getView(R.id.ll_one);
        ImageView cateImg = helper.getView(R.id.img_cate);
        String type = item.getType();
        if (!TextUtils.isEmpty(type)) {
            switch (type) {
                case "1":
                    bg.setBackgroundResource(R.mipmap.btn_1);
                    cateImg.setImageResource(R.mipmap.btn_cir_1);
                    helper.setTextColor(R.id.tv_btn, mContext.getResources().getColor(R.color.color_9370E6));
                    helper.setText(R.id.tv_eng, String.format("%s INSPECTION", "ROUTINE"));
                    break;
                case "2":
                    bg.setBackgroundResource(R.mipmap.btn_2);
                    cateImg.setImageResource(R.mipmap.btn_cir_2);
                    helper.setTextColor(R.id.tv_btn, mContext.getResources().getColor(R.color.color_1785F8));
                    helper.setText(R.id.tv_eng, String.format("%s INSPECTION", "REGULAR"));
                    break;
                case "3":
                    bg.setBackgroundResource(R.mipmap.btn_3);
                    cateImg.setImageResource(R.mipmap.btn_cir_3);
                    helper.setTextColor(R.id.tv_btn, mContext.getResources().getColor(R.color.color_1DB87B));
                    helper.setText(R.id.tv_eng, String.format("%s INSPECTION", "SPECIAL"));
                    break;
                default:
                    bg.setBackgroundResource(R.mipmap.btn_4);
                    cateImg.setImageResource(R.mipmap.btn_cir_4);
                    helper.setTextColor(R.id.tv_btn, mContext.getResources().getColor(R.color.color_667EDD));
                    helper.setText(R.id.tv_eng, String.format("%s INSPECTION", "DAILY"));
                    break;
            }
        }
        helper.setText(R.id.tv_name, item.getName());
    }
}
