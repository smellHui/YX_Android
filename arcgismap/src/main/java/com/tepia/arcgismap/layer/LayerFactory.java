package com.tepia.arcgismap.layer;

import com.esri.arcgisruntime.layers.Layer;
import com.tepia.arcgismap.googlemap.GoogleTiteLayer;
import com.tepia.arcgismap.listener.MapLoadingListener;

/**
 * Author:xch
 * Date:2019/7/18
 * Description:
 */
public class LayerFactory {

    public static Layer createGoogleLayer(GoogleTiteLayer.Type type, MapLoadingListener listener) {
        return GoogleTiteLayer.createWebTiteLayer(type);
    }
}
