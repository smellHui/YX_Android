package com.tepia.arcgismap.listener;

import com.esri.arcgisruntime.loadable.LoadStatus;

/**
 * Author:xch
 * Date:2019/7/18
 * Description:
 */
public interface MapLoadingListener {
    void loadingSuccess();
    void loadingFailed(LoadStatus status);
}
