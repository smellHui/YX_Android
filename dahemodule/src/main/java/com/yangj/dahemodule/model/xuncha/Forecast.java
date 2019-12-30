package com.yangj.dahemodule.model.xuncha;

/**
 * Author:xch
 * Date:2019/11/19
 * Description:
 */
public class Forecast {

    private String name;
    private Weather weather;

    public Forecast(String name, Weather weather) {
        this.weather = weather;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Weather getWeather() {
        return weather;
    }
}
