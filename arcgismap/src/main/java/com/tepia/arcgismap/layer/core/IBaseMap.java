package com.tepia.arcgismap.layer.core;

import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.LayerList;

/**
 * Author:xch
 * Date:2019/7/18
 * Description:
 */
public interface IBaseMap {

    LayerList getBaseLayers();

    void setBasemap(Basemap basemap);
}
