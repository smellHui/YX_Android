package com.tepia.arcgismap.layer.core;

import com.esri.arcgisruntime.layers.Layer;

import java.util.Collection;

/**
 * Author:xch
 * Date:2019/7/18
 * Description:
 */
public interface Ilayer {

    boolean addOperationalLayer(Layer layer);

    boolean addOperationalLayers(Collection<? extends Layer> layers);
}
