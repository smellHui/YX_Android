package com.tepia.guangdong_module.amainguangdong.xunchaview.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.guangdong_module.R;
import com.example.guangdong_module.databinding.ItemTyphoonRouteBinding;
import com.tepia.guangdong_module.amainguangdong.model.typhoonroute.TyphoonListItemBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TyphoonRouteAdapter extends BaseQuickAdapter<TyphoonListItemBean, BaseViewHolder> {

    ItemTyphoonRouteBinding itemTyphoonRouteBinding;
    private ClickListener mlistener;
    private Context mcontext;

    public TyphoonRouteAdapter(int layoutResId, @Nullable List<TyphoonListItemBean> data, ClickListener listener, Context context) {
        super(layoutResId, data);
        mlistener = listener;
        mcontext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, TyphoonListItemBean item) {
        itemTyphoonRouteBinding = DataBindingUtil.bind(helper.itemView);

        itemTyphoonRouteBinding.tvYear.setText(item.getTfId());
        itemTyphoonRouteBinding.tvNameTyphoon.setText(item.getName());
        itemTyphoonRouteBinding.tvDate.setText(changeTime(item.getStartTime()));

        itemTyphoonRouteBinding.tvViewRoute.setOnClickListener(v -> mlistener.onClick(item));

        String strong = item.getTyphoonPointInfo().getStrong();
        itemTyphoonRouteBinding.tvTyphoonType.setText(strong);
        // 热带低压
        if ("热带低压".equals(strong)) {
            itemTyphoonRouteBinding.ivTyphoonType.setImageDrawable( ContextCompat.getDrawable(mcontext, R.mipmap.wind_level1));
        } // 热带风暴
        else if ("热带风暴".equals(strong)) {
            itemTyphoonRouteBinding.ivTyphoonType.setImageDrawable( ContextCompat.getDrawable(mcontext, R.mipmap.wind_level2));
        }
        // 强热带风暴
        else if ("强热带风暴".equals(strong)) {
            itemTyphoonRouteBinding.ivTyphoonType.setImageDrawable( ContextCompat.getDrawable(mcontext, R.mipmap.wind_level3));
        }
        // 台风
        else if ("台风".equals(strong)) {
            itemTyphoonRouteBinding.ivTyphoonType.setImageDrawable( ContextCompat.getDrawable(mcontext, R.mipmap.wind_level4));
        }
        // 强台风
        else if ("强台风".equals(strong)) {
            itemTyphoonRouteBinding.ivTyphoonType.setImageDrawable( ContextCompat.getDrawable(mcontext, R.mipmap.wind_level5));
        }
        // 超强台风
        else {
            itemTyphoonRouteBinding.ivTyphoonType.setImageDrawable( ContextCompat.getDrawable(mcontext, R.mipmap.wind_level6));
        }


    }

    private String changeTime(String time) {
        String newtime = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date date;
        try {
            date = format.parse(time);
            SimpleDateFormat format1 = new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA);
            newtime = format1.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newtime;
    }

    public interface ClickListener{
        void onClick(TyphoonListItemBean bean);
    }

}
