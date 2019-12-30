package com.tepia.arcgismap.layer.observer;

import android.view.MotionEvent;

/**
 * Author:xch
 * Date:2019/8/1
 * Description:
 */
public interface IMapUpObserver extends IMapObserver{

    boolean onUp(MotionEvent e);
}
