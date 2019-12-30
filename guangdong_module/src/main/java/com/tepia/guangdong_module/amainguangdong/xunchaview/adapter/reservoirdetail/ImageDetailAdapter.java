package com.tepia.guangdong_module.amainguangdong.xunchaview.adapter.reservoirdetail;

import android.support.annotation.Nullable;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.guangdong_module.R;
import com.tepia.base.ConfigConsts;
import com.tepia.base.utils.Utils;
import com.tepia.guangdong_module.amainguangdong.common.RoundImageView;
import com.tepia.guangdong_module.amainguangdong.model.reservoirdetail.PicDetailBean;
import com.tepia.guangdong_module.amainguangdong.xunchaview.fragment.MainFragment;
import com.tepia.photo_picker.PhotoPreview;

import java.util.List;

/**
  * Created by      Android studio
  *
  * @author :wwj (from Center Of Wuhan)
  * Date    :2019/5/29
  * Version :1.0
  * 功能描述 :  图像详情
 **/
public class ImageDetailAdapter extends BaseQuickAdapter<PicDetailBean.DataBean, BaseViewHolder> {
    public ImageDetailAdapter(int layoutResId, @Nullable List<PicDetailBean.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PicDetailBean.DataBean item) {
        RoundImageView imageView = helper.getView(R.id.img_bg);
        String picpath = item.getPicpath();
        if ( picpath != null &&  (picpath.contains("http://") || picpath.contains("https://")) ) {
            final RequestOptions options = new RequestOptions();
            options.error(R.mipmap.icon_empty);
            Glide.with(Utils.getContext())
                    .load(picpath)
                    .apply(ConfigConsts.options)
                    .apply(options)
                    .into(imageView);
        }else{
            imageView.setImageResource(R.mipmap.icon_empty);
        }
        helper.setText(R.id.tv_pic_time,item.getTm()==null?"":item.getTm());
    }
}
