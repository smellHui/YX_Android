package com.tepia.guangdong_module.amainguangdong.mvp.rainfallstatistics;

import com.tepia.base.http.RetrofitManager;
import com.tepia.guangdong_module.APPCostant;
import com.tepia.guangdong_module.amainguangdong.common.UserManager;
import com.tepia.guangdong_module.amainguangdong.model.rainfallstatistics.RainfallListResponse;
import com.tepia.guangdong_module.amainguangdong.model.rainfallstatistics.ReservoirNumResponse;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Android Studio
 *
 * @author : Arthur
 * Date :    2019/6/5
 * Time :    16:23
 * Describe :
 */
public class RainfallStatisticsManager {
    private static final RainfallStatisticsManager ourInstance = new RainfallStatisticsManager();
    private RainfallStatisticsHttpService mRetrofitService;

    public static RainfallStatisticsManager getInstance() {
        return ourInstance;
    }

    private RainfallStatisticsManager() {
        this.mRetrofitService =
                RetrofitManager.getRetrofit(APPCostant.API_SERVER_URL + APPCostant.API_SERVER_MONITOR_AREA)
                        .create(RainfallStatisticsHttpService.class);
    }

    /**
     * 行政统计-雨量统计-水库数量
     *
     * @param startTime
     * @param endTime
     * @param period
     * @return
     */
    public Observable<ReservoirNumResponse> getReservoirNum(String startTime, String endTime, String period) {
        String token = UserManager.getInstance().getToken();
        return mRetrofitService.getReservoirNum(token, startTime, endTime, period).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 行政统计-雨量统计-列表
     *
     * @param pageSize
     * @param startTime
     * @param endTime
     * @param period
     * @param level
     * @return
     */
    public Observable<RainfallListResponse> getRainfallList(int currentPage, int pageSize, String startTime,
                                                            String endTime, String period, String level) {
        String token = UserManager.getInstance().getToken();
        return mRetrofitService.getRainfallList(token, currentPage, pageSize, startTime, endTime, period, level)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}
