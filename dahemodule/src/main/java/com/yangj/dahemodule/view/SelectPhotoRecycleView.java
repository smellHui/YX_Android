package com.yangj.dahemodule.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

import com.tepia.guangdong_module.amainguangdong.common.PhotoSelectAdapter;
import com.tepia.guangdong_module.amainguangdong.common.pickview.PhotoRecycleViewAdapter;
import com.tepia.photo_picker.PhotoPicker;
import com.tepia.photo_picker.PhotoPreview;
import com.yangj.dahemodule.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:xch
 * Date:2019/12/19
 * Description:
 */
public class SelectPhotoRecycleView extends ViewBase {

    private RecyclerView rv;
    private PhotoSelectAdapter photoSelectAdapter;
    private boolean canDelete = true;

    public SelectPhotoRecycleView(Context context) {
        super(context);
    }

    public SelectPhotoRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SelectPhotoRecycleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getViewId() {
        return R.layout.view_select_photo_recycle;
    }

    @Override
    public void initData() {
        rv = findViewById(R.id.rv);
        photoSelectAdapter = new PhotoSelectAdapter(getContext(), true);
        rv.setLayoutManager(new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL));
        rv.setAdapter(photoSelectAdapter);
        photoSelectAdapter.setOnItemClickListener((view, position) -> {
            ArrayList<String> photoPaths = photoSelectAdapter.getPhotoPaths();
            if (photoSelectAdapter.getItemViewType(position) == PhotoRecycleViewAdapter.TYPE_ADD) {
                PhotoPicker.builder()
                        .setPhotoCount(5)
                        .setShowCamera(true)
                        .setPreviewEnabled(true)
                        .setSelected(photoPaths)
                        .start((Activity) mContext, 100);
            } else {
                PhotoPreview.builder()
                        .setPhotos(photoPaths)
                        .setCurrentItem(position)
                        .setShowDeleteButton(canDelete)
                        .start((Activity) mContext, 100);
            }
        });
    }

    public void setPhotoPaths(@NonNull ArrayList<String> imgPaths) {
        photoSelectAdapter.setLocalData(imgPaths);
    }

    public void setPhotoPaths(@NonNull ArrayList<String> imgPaths, boolean noCanAdd) {
        canDelete = !noCanAdd;
        photoSelectAdapter.setLocalData(imgPaths, noCanAdd);
    }

    public List<String> getPhotoPaths() {
        return photoSelectAdapter.getPhotoPaths();
    }
}
