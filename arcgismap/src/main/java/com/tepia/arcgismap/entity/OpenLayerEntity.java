package com.tepia.arcgismap.entity;

import com.esri.arcgisruntime.layers.Layer;
import com.tepia.arcgismap.common.Variable;

/**
 * Author:xch
 * Date:2019/7/18
 * Description:
 */
public class OpenLayerEntity {

    private String layerKey;
    private String url;
    private Variable.LayerType type;
    private Layer layer;


    public String getLayerKey() {
        return layerKey;
    }

    public void setLayerKey(String layerKey) {
        this.layerKey = layerKey;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Variable.LayerType getType() {
        return type;
    }

    public void setType(Variable.LayerType type) {
        this.type = type;
    }

    public Layer getLayer() {
        return layer;
    }

    public void setLayer(Layer layer) {
        this.layer = layer;
    }
}
