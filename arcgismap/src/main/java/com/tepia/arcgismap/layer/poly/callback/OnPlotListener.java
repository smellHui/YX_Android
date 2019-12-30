package com.tepia.arcgismap.layer.poly.callback;

import com.esri.arcgisruntime.geometry.Point;

/**
 * Author:xch
 * Date:2019/8/1
 * Description:
 */
public interface OnPlotListener {

    void onTouchMapListener(Point point);

    void onViewpointChangedListener(Point point);
}
