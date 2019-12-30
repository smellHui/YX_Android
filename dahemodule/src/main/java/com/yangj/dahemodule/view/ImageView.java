package com.yangj.dahemodule.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.bumptech.glide.Glide;
import com.tepia.base.utils.ImageLoaderUtil;
import com.yangj.dahemodule.R;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:xch
 * Date:2019/8/27
 * Description:
 */
public class ImageView extends ViewBase {

    private Banner banner;

    public ImageView(Context context) {
        super(context);
    }

    public ImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getViewId() {
        return R.layout.view_image;
    }

    @Override
    public void initData() {
        banner = findViewById(R.id.banner);
        List<String> urls = new ArrayList<>();
        urls.add("https://yxhs-skgj-test.oss-cn-hangzhou.aliyuncs.com/reservoir/under_three.jpg");
        urls.add("https://yxhs-skgj-test.oss-cn-hangzhou.aliyuncs.com/reservoir/under_four.jpg");
        urls.add("https://yxhs-skgj-test.oss-cn-hangzhou.aliyuncs.com/reservoir/under_two.jpg");
        urls.add("https://yxhs-skgj-test.oss-cn-hangzhou.aliyuncs.com/reservoir/in_one.jpg");
        banner.setImages(urls)
                .setImageLoader(new ImageLoader() {
                    @Override
                    public void displayImage(Context context, Object path, android.widget.ImageView imageView) {
                        ImageLoaderUtil.getInstance().loadImage(mContext,(String) path,imageView);
                    }
                })
                .setDelayTime(3000)
                .isAutoPlay(true)
                .start();

    }
}
