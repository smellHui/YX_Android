package com.tepia.base.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.tepia.base.R;

/**
 * Author:xch
 * Date:2019/10/24
 * Description:
 */
public class ImageLoaderUtil {
    static ImageLoaderUtil instance;

    public ImageLoaderUtil() {
    }

    public static ImageLoaderUtil getInstance() {
        if (null == instance) {
            synchronized (ImageLoaderUtil.class) {
                if (null == instance) {
                    instance = new ImageLoaderUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 普通加载
     * @param context
     * @param url
     * @param imageView
     */
    public  void loadImage(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .format(DecodeFormat.PREFER_RGB_565)
                .centerCrop()
                .placeholder(R.mipmap.icon_empty)
                .skipMemoryCache(true)
                .error(R.mipmap.icon_empty)
                // 取消动画，防止第一次加载不出来
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.DATA);
        Glide.with(context)
                //放弃图片质量提高加载速度
                .asBitmap()
                //加载缩略图
                .thumbnail(0.3f)
                .load(url)
                .apply(options)
                .into(imageView);
    }

    /**
     * 加载圆角图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public  void loadRoundImage(Context context, String url, ImageView imageView) {
        RequestOptions options = RequestOptions
                .bitmapTransform(new RoundedCorners(20))
                .placeholder(R.mipmap.icon_empty)
                .error(R.mipmap.icon_empty);

        Glide.with(context).load(url).apply(options).into(imageView);
    }
}
