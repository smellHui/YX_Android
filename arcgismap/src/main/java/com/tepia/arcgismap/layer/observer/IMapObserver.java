package com.tepia.arcgismap.layer.observer;

import com.tepia.arcgismap.layer.core.IMap;

/**
 * Author:xch
 * Date:2019/8/1
 * Description:
 */
public interface IMapObserver {

    void onAttach(IMap map);

    void onDetach();

    int getPriority();
}
