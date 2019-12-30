package com.yangj.dahemodule.view;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.yangj.dahemodule.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:xch
 * Date:2019/12/18
 * Description:
 */
public class FeedbackSelectPhotoView extends ViewBase {

    private TextView photoNumTv;
    private TextView tv_no_photo;
    private SelectPhotoRecycleView selectPhotoRecycleView;

    public FeedbackSelectPhotoView(Context context) {
        super(context);
    }

    public FeedbackSelectPhotoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FeedbackSelectPhotoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getViewId() {
        return R.layout.view_feed_back_select_photo;
    }

    @Override
    public void initData() {
        photoNumTv = findViewById(R.id.tv_photo_num);
        tv_no_photo = findViewById(R.id.tv_no_photo);
        selectPhotoRecycleView = findViewById(R.id.view_select_photo);
        setPhotoNumTv(0);
    }

    public void setPhotoPaths(List<String> photoPaths) {
        if (photoPaths == null) return;
        setPhotoNumTv(photoPaths.size());
        selectPhotoRecycleView.setPhotoPaths((ArrayList<String>) photoPaths);
    }

    public void setPhotoPaths(List<String> photoPaths, boolean noCanAdd) {
        if (photoPaths == null) return;
        setPhotoNumTv(photoPaths.size());
        selectPhotoRecycleView.setPhotoPaths((ArrayList<String>) photoPaths, noCanAdd);
    }

    public void setNoCanSelectPhoto() {
        tv_no_photo.setVisibility(View.VISIBLE);
        selectPhotoRecycleView.setVisibility(GONE);
    }

    public List<String> getPhotoPaths() {
        return selectPhotoRecycleView.getPhotoPaths();
    }

    private void setPhotoNumTv(@IntRange(from = 0) int num) {
        String str = "<font color='#000000'>现场照片    </font><font color='#FFB61C'>" + num + "/5 </font>";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            photoNumTv.setText(Html.fromHtml(str, Html.FROM_HTML_MODE_LEGACY));
        } else {
            photoNumTv.setText(Html.fromHtml(str));
        }
    }
}
