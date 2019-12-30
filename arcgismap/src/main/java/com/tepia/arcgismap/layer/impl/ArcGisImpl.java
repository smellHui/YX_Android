package com.tepia.arcgismap.layer.impl;

import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.LayerList;
import com.tepia.arcgismap.googlemap.GoogleTiteLayer;
import com.tepia.arcgismap.layer.core.IArcGis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Author:xch
 * Date:2019/7/18
 * Description:
 */
public class ArcGisImpl implements IArcGis {

    private ArcGISMap arcGISMap;

    public ArcGisImpl() {
        this.arcGISMap = new ArcGISMap();
        addDefaultLayers();
    }

    private void addDefaultLayers() {
        List<Layer> layers = new ArrayList<>();
        layers.add(GoogleTiteLayer.createWebTiteLayer(GoogleTiteLayer.Type.IMAGE));
        layers.add(GoogleTiteLayer.createWebTiteLayer(GoogleTiteLayer.Type.IMAGE_ANNOTATION));
        addOperationalLayers(layers);
    }

    @Override
    public ArcGISMap getArcGISMap() {
        return arcGISMap;
    }

    @Override
    public boolean addOperationalLayer(Layer layer) {
        return arcGISMap.getOperationalLayers().add(layer);
    }

    @Override
    public boolean addOperationalLayers(Collection<? extends Layer> layers) {
        return arcGISMap.getOperationalLayers().addAll(layers);
    }

    @Override
    public Basemap getBasemap() {
        return arcGISMap.getBasemap();
    }

    @Override
    public void setBasemap(Basemap basemap) {
        arcGISMap.setBasemap(basemap);
    }

    @Override
    public LayerList getBaseLayers() {
        return getBasemap().getBaseLayers();
    }


}
