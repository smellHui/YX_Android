package com.tepia.guangdong_module.amainguangdong.xunchaview.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.guangdong_module.R;
import com.tepia.base.ConfigConsts;
import com.tepia.base.utils.Utils;
import com.tepia.guangdong_module.amainguangdong.common.RoundImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by      Intellij IDEA
 *
 * @author :       ly
 * Date    :       2019-05-5
 * Time    :
 * Version :       1.0
 * Company :       北京太比雅科技(武汉研发中心)
 **/
public class ImageShowAdapter extends BaseQuickAdapter<String,BaseViewHolder> {
    Map<Integer,Boolean> map;
    List<String> mdata;
    public ImageShowAdapter(Context context, int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
        this.mContext = context;
        this.mdata = data;
        map = new HashMap<>();

    }

    @Override
    protected void convert(BaseViewHolder view, String photopathstr) {
        RoundImageView imageView = view.getView(R.id.item_img);
        View view1 = view.getView(R.id.selectView);
        ImageView typeIv = view.getView(R.id.typeIv);
        int position = view.getAdapterPosition();
        String filePath = "";
        String routeType = "";
        if (!TextUtils.isEmpty(photopathstr)) {
             int index = photopathstr.indexOf(",");
             routeType = photopathstr.substring(0,index);
             filePath = photopathstr.substring(index+1,photopathstr.length());
        }
        if ( filePath != null &&  (filePath.contains("http://") || filePath.contains("https://")) ) {
            imageView.setVisibility(View.VISIBLE);
            Glide.with(Utils.getContext())
                    .load(filePath)
                    .apply(ConfigConsts.options)
                    .into(imageView);


        }else{
            imageView.setImageResource(R.mipmap.icon_empty);
        }

        if (map != null && map.containsKey(position) && map.get(position)) {
            view1.setVisibility(View.VISIBLE);
        }else{
            view1.setVisibility(View.INVISIBLE);

        }
        if ("1".equals(routeType)) {
            typeIv.setImageResource(R.drawable.home_label_rcxc);
        }else if ("2".equals(routeType)){
            typeIv.setImageResource(R.drawable.home_label_dqxc);

        }else if ("3".equals(routeType)){
            typeIv.setImageResource(R.drawable.home_label_special);
        }else if("4".equals(routeType)){
            typeIv.setImageResource(R.drawable.home_label_auto);

        }


    }

    /**
     * 选中
     * @param position
     */
    public void show(int position){
        map.clear();
        for (int i = 0; i < mdata.size(); i++) {
            map.put(i,false);
        }
        map.put(position,true);
    }
}
