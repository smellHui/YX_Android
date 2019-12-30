package com.tepia.arcgismap.layer;

import android.content.Context;

import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.example.gaodelibrary.GaodeEntity;
import com.example.gaodelibrary.OnGaodeLibraryListen;
import com.tepia.arcgismap.R;

/**
 * Author:xch
 * Date:2019/7/25
 * Description:
 */
public class LocationManager {

    private GaodeEntity gaodeEntity;

    public LocationManager(Context context, Class<?> startClass, int resIdIcon) {
        gaodeEntity = new GaodeEntity(context, startClass, resIdIcon);
    }

    public void setLocationListen(OnGaodeLibraryListen.LocationListen locationListen) {
        gaodeEntity.setLocationListen(locationListen);
    }

    public void startLocation() {
        //开始定位
        if (gaodeEntity != null) {
            //开始轨迹
            gaodeEntity.startTrace();
        }
    }

    public void stopLocation() {
        if (gaodeEntity != null) {
            gaodeEntity.stopTrace();
        }
    }

    public void closeLocation() {
        if (gaodeEntity != null) {
            gaodeEntity.closeLocation();
        }
    }


}
