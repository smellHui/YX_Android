package com.tepia.guangdong_module.amainguangdong.xunchaview.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.guangdong_module.R;
import com.example.guangdong_module.databinding.LvItemReservoirRegimenBinding;
import com.tepia.guangdong_module.amainguangdong.model.reservoirregimen.ReservoirRegimenItemBean;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Android Studio
 *
 * @author: Arthur
 * Date:    2019/5/30
 * Time:    10:22
 * Describe:
 */
public class AdapterReservoirRegimenList extends BaseQuickAdapter<ReservoirRegimenItemBean, BaseViewHolder> {

    LvItemReservoirRegimenBinding mBinding;

    public AdapterReservoirRegimenList(int layoutResId, @Nullable List<ReservoirRegimenItemBean> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, ReservoirRegimenItemBean item) {
        mBinding = DataBindingUtil.bind(helper.itemView);
        if (helper.getLayoutPosition() % 2 == 0) {
            helper.setBackgroundColor( R.id.rootLy, ContextCompat.getColor(mContext, R.color.white));
        } else {
            helper.setBackgroundColor(R.id.rootLy, ContextCompat.getColor(mContext, R.color.reportitem));
        }

        mBinding.tvName.setText(item.getReservoir());
        mBinding.tvTime.setText(item.getTm());

        BigDecimal bg_sw = new BigDecimal(item.getRz());
        BigDecimal bg_xx = new BigDecimal(item.getFloodSeasonWaterLevel());
        BigDecimal bg_cz = bg_sw.subtract(bg_xx);
        mBinding.tvHeightDifference.setText(bg_cz.toString());

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM-dd HH:mm");
        try {
            Date time = sdf1.parse(item.getTm());

            mBinding.tvTime.setText(sdf2.format(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mBinding.tvWaterHeight.setText(item.getRz());
        mBinding.tvWaterHeightLimit.setText(item.getFloodSeasonWaterLevel());

    }
}
