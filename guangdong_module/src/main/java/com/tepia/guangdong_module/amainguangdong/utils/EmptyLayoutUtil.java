package com.tepia.guangdong_module.amainguangdong.utils;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.guangdong_module.R;
import com.tepia.base.utils.Utils;

/**
 *
 * @author liying
 * @date 2018/7/26
 */

public class EmptyLayoutUtil {
    public static View show(String msg){
        View view = LayoutInflater.from(Utils.getContext()).inflate(R.layout.view_empty_list_view,null);
        TextView tv_empty_view_text = view.findViewById(R.id.tv_empty_view_text);
        ImageView imageIv = view.findViewById(R.id.imageIv);
        imageIv.setVisibility(View.GONE);
        tv_empty_view_text.setGravity(Gravity.CENTER);
        tv_empty_view_text.setTextSize(18);
        tv_empty_view_text.setText(msg);

        return view;
    }

    public static View showNew(String msg){
        View view = LayoutInflater.from(Utils.getContext()).inflate(R.layout.view_empty_list_view,null);
        TextView tv_empty_view_text = view.findViewById(R.id.tv_empty_view_text);
        tv_empty_view_text.setGravity(Gravity.CENTER);
        tv_empty_view_text.setText(msg);
        return view;
    }

    public static View getFootView(){
        View view = LayoutInflater.from(Utils.getContext()).inflate(R.layout.view_foot_view,null);
        return view;
    }

    public static View showTop(String msg){
        LinearLayout view = (LinearLayout) LayoutInflater.from(Utils.getContext()).inflate(R.layout.view_empty_list_view,null);
        view.setGravity(Gravity.CENTER_HORIZONTAL);
        TextView tv_empty_view_text = view.findViewById(R.id.tv_empty_view_text);
        ImageView imageIv = view.findViewById(R.id.imageIv);
        imageIv.setVisibility(View.GONE);
        tv_empty_view_text.setGravity(Gravity.CENTER);
        tv_empty_view_text.setTextSize(18);
        tv_empty_view_text.setText(msg);

        return view;
    }
}
