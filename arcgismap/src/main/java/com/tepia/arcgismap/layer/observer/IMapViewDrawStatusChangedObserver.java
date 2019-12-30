package com.tepia.arcgismap.layer.observer;

import com.esri.arcgisruntime.mapping.view.DrawStatusChangedEvent;

/**
 * Author:xch
 * Date:2019/8/1
 * Description:
 */
public interface IMapViewDrawStatusChangedObserver extends IMapObserver {

    void viewDrawStatusChanged(DrawStatusChangedEvent var1);
}
