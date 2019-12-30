package com.tepia.arcgismap.layer.observer;

import com.esri.arcgisruntime.mapping.view.MapRotationChangedEvent;

/**
 * Author:xch
 * Date:2019/8/1
 * Description:
 */
public interface IMapMapRotationChangedObserver extends IMapObserver {

    void mapRotationChanged(MapRotationChangedEvent var1);
}
