package com.tepia.arcgismap.layer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.tepia.arcgismap.layer.core.ICallout;
import com.tepia.arcgismap.layer.core.IMap;

/**
 * Author:xch
 * Date:2019/7/26
 * Description:弹层管理类
 */
public class CallOutManager implements ICallout {

    private Callout mCallout;
    private Context mContext;

    CallOutManager(@NonNull IMap map) {
        this.mContext = map.getContext();
        this.mCallout = map.getCallout();
    }

    @Override
    public void show(@NonNull Point location, @NonNull View contentView) {
        dismiss();
        mCallout.setLocation(location);
        mCallout.setContent(contentView);
        mCallout.show();
    }
    @Override
    public void show(@NonNull Point location, @NonNull String content) {
        dismiss();
        TextView textView = new TextView(mContext);
        textView.setText(content);
        mCallout.setLocation(location);
        mCallout.setContent(textView);
        mCallout.show();
    }
    @Override
    public void dismiss() {
        if (mCallout.isShowing())
            mCallout.dismiss();
    }
}
