package com.tepia.guangdong_module.amainguangdong.model.typhoonroute;

import java.util.List;

/**
 * Created by Android Studio
 *
 * @author : Arthur
 * Date :    2019/6/3
 * Time :    10:19
 * Describe :
 */
public class TyphoonForcastRouteBean {

    private List<TyphoonForcastPointsBean> forecastpoints;

    private String tm;

    public List<TyphoonForcastPointsBean> getForecastpoints() {
        return forecastpoints;
    }

    public void setForecastpoints(List<TyphoonForcastPointsBean> forecastpoints) {
        this.forecastpoints = forecastpoints;
    }

    public String getTm() {
        return tm;
    }

    public void setTm(String tm) {
        this.tm = tm;
    }
}
