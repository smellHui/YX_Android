package com.tepia.arcgismap.layer.observer;

import com.amap.api.location.AMapLocation;

/**
 * Author:xch
 * Date:2019/12/19
 * Description:定位返回
 */
public interface IMapLocationChangedObserver extends IMapObserver {
    void onLocationChanged(AMapLocation aMapLocation);
}
