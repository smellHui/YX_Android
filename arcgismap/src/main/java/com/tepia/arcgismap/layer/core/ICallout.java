package com.tepia.arcgismap.layer.core;

import android.support.annotation.NonNull;
import android.view.View;

import com.esri.arcgisruntime.geometry.Point;

/**
 * Author:xch
 * Date:2019/7/30
 * Description:
 */
public interface ICallout {

    void show(@NonNull Point location, @NonNull View contentView);

    void show(@NonNull Point location, @NonNull String content);

    void dismiss();
}
