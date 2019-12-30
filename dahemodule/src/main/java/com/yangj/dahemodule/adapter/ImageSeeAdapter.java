package com.yangj.dahemodule.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tepia.base.utils.ImageLoaderUtil;
import com.yangj.dahemodule.R;

import cc.shinichi.library.tool.image.ImageUtil;

/**
 * Author:xch
 * Date:2019/5/20
 * Description:
 */
public class ImageSeeAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public ImageSeeAdapter() {
        super(R.layout.item_image_see);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView img = helper.getView(R.id.img);
        ImageLoaderUtil.getInstance().loadImage(mContext,item,img);
    }
}
