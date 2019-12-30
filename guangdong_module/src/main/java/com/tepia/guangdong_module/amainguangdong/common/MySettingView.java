package com.tepia.guangdong_module.amainguangdong.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guangdong_module.R;


/**
 * 复写设置页面的item
 * Created by liying on 2018-3-6.
 */
public class MySettingView extends FrameLayout {
    private Context mContext;
    private TextView titleTv;
    private TextView secondtitleTv;
    private ImageView leftIv;
    private ImageView rightIv;
    public MySettingView(Context context) {
        this(context,null);
    }

    public MySettingView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MySettingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView(){
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.fragment_setting_item, null);
        leftIv = view.findViewById(R.id.leftIv);
        titleTv =  view.findViewById(R.id.titleTv);
        secondtitleTv =  view.findViewById(R.id.secondtitleTv);
        rightIv = view.findViewById(R.id.rightIv);

        addView(view);
    }


    public TextView getTitleTv() {
        return titleTv;
    }

    public void setTitleTv(TextView titleTv) {
        this.titleTv = titleTv;
    }

    public ImageView getLeftIv() {
        return leftIv;
    }

    public void setLeftIv(ImageView leftIv) {
        this.leftIv = leftIv;
    }

    public ImageView getRightIv() {
        return rightIv;
    }

    public void setIvRight(ImageView rightIv) {
        this.rightIv = rightIv;
    }


    public void setIvLeft(int resId){
        leftIv.setImageResource(resId);
    }

    public void setTitle(String titleStr){
        titleTv.setText(titleStr);
    }

    public TextView getSecondtitleTv() {
        return secondtitleTv;
    }

    public void setSecondtitleTv(TextView secondtitleTv) {
        this.secondtitleTv = secondtitleTv;
    }

    public void setRightIv(ImageView rightIv) {
        this.rightIv = rightIv;
    }


}
