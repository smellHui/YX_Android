package com.yangj.dahemodule.adapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tepia.guangdong_module.amainguangdong.common.RoundImageView;
import com.yangj.dahemodule.R;

/**
 * Author:xch
 * Date:2019/9/19
 * Description:
 */
public class PictureAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public PictureAdapter() {
        super(R.layout.item_image_detail, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        RoundImageView imageView = helper.getView(R.id.img_bg);
        Glide.with(mContext).
                load(item).
                thumbnail(0.5f)
                .apply(new RequestOptions()
                        .centerCrop()
                        .placeholder(com.example.guangdong_module.R.drawable.icon_empty)
                        .error(com.example.guangdong_module.R.drawable.icon_empty)
                        .priority(Priority.HIGH)
                        .diskCacheStrategy(DiskCacheStrategy.NONE))
                .into(imageView);
    }
}
