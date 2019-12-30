package com.tepia.arcgismap.layer.observer;

import android.view.MotionEvent;

/**
 * Author:xch
 * Date:2019/8/1
 * Description:
 */
public interface IMapSingleTapConfirmedObserver extends IMapObserver{

    boolean onSingleTapConfirmed(MotionEvent e);
}
