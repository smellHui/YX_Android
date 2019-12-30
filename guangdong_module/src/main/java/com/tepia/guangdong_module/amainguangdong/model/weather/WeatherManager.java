package com.tepia.guangdong_module.amainguangdong.model.weather;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.tepia.base.http.RetrofitManager;
import com.tepia.base.utils.TimeFormatUtils;
import com.tepia.base.utils.Utils;
import com.tepia.guangdong_module.amainguangdong.common.UserManager;
import com.tepia.photo_picker.utils.SPUtils;
import com.tepia.guangdong_module.APPCostant;

import java.util.Date;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 数据字典
 * Created by Joeshould on 2018/7/31.
 */

public class WeatherManager {
    private static final WeatherManager ourInstance = new WeatherManager();
    private WeatherService mRetrofitService;
    private WeahterBean weatherBean;


    public static WeatherManager getInstance() {
        return ourInstance;
    }

    private WeatherManager() {
        this.mRetrofitService = RetrofitManager.getRetrofit(APPCostant.API_SERVER_URL_WEATHER).create(WeatherService.class);

    }

    private void getWeatherEntity() {
        getWeather("武汉").safeSubscribe(new Observer<WeatherResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(WeatherResponse weatherResponse) {
                if (weatherResponse.getStatus().equals("0")) {
                    weatherBean = weatherResponse.getResult();
                    SPUtils.getInstance(Utils.getContext()).putString("WEATHERBEAN", new Gson().toJson(weatherBean));
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * 获取数据字典
     *
     * @param city
     * @return
     */
    public Observable<WeatherResponse> getWeather(String city) {
        String token = UserManager.getInstance().getToken();
        String appcode = APPCostant.API_SERVER_URL_WEATHER_APP_CODE;
        return mRetrofitService.getWeather(appcode,city)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public WeahterBean getWeatherBean() {
        weatherBean = null;
        String temp = SPUtils.getInstance(Utils.getContext()).getString("WEATHERBEAN", "");
        if (!TextUtils.isEmpty(temp)) {
            WeahterBean bean = new Gson().fromJson(temp, WeahterBean.class);
            if (bean == null) {
                getWeatherEntity();
            } else {
                Date lastUpdateTime = TimeFormatUtils.strToDateLong(bean.getUpdatetime());
                Date nowDate = new Date(System.currentTimeMillis());

                if (lastUpdateTime.getHours() != nowDate.getTime()) {
//                    getWeatherEntity();
                    weatherBean = bean;
                } else {
                    weatherBean = bean;
                }
            }

        } else {
            getWeatherEntity();
        }
        return weatherBean;
    }

    public Observable<Weather2Response> getWeatherbyArea(String area) {
        String token = UserManager.getInstance().getToken();
        String appcode = "APPCODE ba471f686c564c98854da1c88443d07e";
        String url = "http://102b631ae1094e33a77c45312226184e-cn-qingdao.alicloudapi.com/area-to-weather";
        String needMoreDay = "1";
        return mRetrofitService.getWeatherbyArea(url,appcode,area,needMoreDay)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<WeatherHour24Response> getWeatherbyHour24(String area) {
        String appcode = "APPCODE ba471f686c564c98854da1c88443d07e";
        String url = "http://102b631ae1094e33a77c45312226184e-cn-qingdao.alicloudapi.com/hour24";
        return mRetrofitService.getWeatherbyHour24(url,appcode,area)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
