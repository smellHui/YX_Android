package com.tepia.arcgismap.layer.core;

import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;

/**
 * Author:xch
 * Date:2019/7/18
 * Description:
 */
public interface IArcGis extends IBaseMap, Ilayer {

    Basemap getBasemap();

    ArcGISMap getArcGISMap();
}
