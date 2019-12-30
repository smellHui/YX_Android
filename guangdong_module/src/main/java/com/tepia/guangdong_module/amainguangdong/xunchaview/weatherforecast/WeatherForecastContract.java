package com.tepia.guangdong_module.amainguangdong.xunchaview.weatherforecast;

import com.tepia.base.mvp.BasePresenter;
import com.tepia.base.mvp.BaseView;
import com.tepia.guangdong_module.amainguangdong.model.weather.Hour24Bean;
import com.tepia.guangdong_module.amainguangdong.model.weather.WeahterBean;
import com.tepia.guangdong_module.amainguangdong.model.weather.Weather2Bean;

/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class WeatherForecastContract {
    interface View extends BaseView {

        void getWeatherSuccess(WeahterBean result);

        void getWeatherHour24Success(Hour24Bean showapi_res_body);

        void getWeather2Success(Weather2Bean showapi_res_body);

        void getWeatherfail();
    }

    interface  Presenter extends BasePresenter<View> {
        
    }
}
