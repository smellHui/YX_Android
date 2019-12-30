package com.tepia.guangdong_module.amainguangdong.xunchaview.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.guangdong_module.R;
import com.example.guangdong_module.databinding.ReservoirListLayoutBinding;
import com.tepia.base.common.DictMapEntity;
import com.tepia.base.utils.ResUtils;
import com.tepia.guangdong_module.amainguangdong.common.DictMapManager;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.PersonDutyBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.ReserviorListBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.ReserviorListBean.DataBean.ListBean;

import java.util.List;
import java.util.Map;

/**
  * Created by      Android studio
  *
  * @author :ly (from Center Of Wuhan)
  * 创建时间 :2019-5-30
  * 更新时间 :
  * Version :1.0
  * 功能描述 : 主页 - 水库检索 适配器
 **/
public class AdapterReserList extends BaseQuickAdapter<ListBean, BaseViewHolder> {
    public AdapterReserList(int layoutResId, @Nullable List<ListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ListBean item) {
        ReservoirListLayoutBinding mBinding = DataBindingUtil.bind(helper.itemView);

        int position = helper.getAdapterPosition();
        mBinding.serialTv.setText(position+1+"");
        mBinding.areaTv.setText(item.getAreaName());
        double rz = item.getRz();
        double floodSeasonWaterLevel = item.getFloodSeasonWaterLevel();
        mBinding.waterTv.setText(rz+"");
        mBinding.nameTv.setText(item.getReservoir());

        DictMapEntity dictMapEntity = DictMapManager.getInstance().getmDictMap();
        if (dictMapEntity != null && dictMapEntity.getObject() != null) {

            Map<String, String> map_Reservoirtype = dictMapEntity.getObject().getReservoir_type();
            Map<String, String> map_Damtype = dictMapEntity.getObject().getDam_type();

            mBinding.baTypeTv.setText(map_Damtype.get(item.getDamType()));
            mBinding.levelTv.setText(map_Reservoirtype.get(item.getReservoirType()));

        }

        if(position%2 == 0){
            mBinding.rootLy.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
        }else{
            mBinding.rootLy.setBackgroundColor(ContextCompat.getColor(mContext, R.color.reportitem));

        }

        if(rz > floodSeasonWaterLevel){
            mBinding.waterTv.setTextColor(ResUtils.getColor(R.color.common_red));
        }else{
            mBinding.waterTv.setTextColor(ResUtils.getColor(R.color.water_rz_color));

        }






    }

}
