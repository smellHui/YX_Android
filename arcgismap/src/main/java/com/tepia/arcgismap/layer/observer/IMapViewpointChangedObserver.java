package com.tepia.arcgismap.layer.observer;

import com.esri.arcgisruntime.mapping.view.ViewpointChangedEvent;

/**
 * Author:xch
 * Date:2019/8/1
 * Description:
 */
public interface IMapViewpointChangedObserver extends IMapObserver {

    void viewpointChanged(ViewpointChangedEvent var1);
}
