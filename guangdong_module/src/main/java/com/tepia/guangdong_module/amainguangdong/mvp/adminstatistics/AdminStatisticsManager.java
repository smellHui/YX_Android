package com.tepia.guangdong_module.amainguangdong.mvp.adminstatistics;

import com.tepia.base.http.RetrofitManager;
import com.tepia.guangdong_module.amainguangdong.common.UserManager;
import com.tepia.guangdong_module.amainguangdong.model.adminstatistics.InspectionStatisticsResponse;
import com.tepia.guangdong_module.amainguangdong.model.adminstatistics.RealTimeMonitorResponse;
import com.tepia.guangdong_module.amainguangdong.model.adminstatistics.SafetyIdentifyResponse;
import com.tepia.guangdong_module.amainguangdong.model.adminstatistics.ThreeKeyPointResponse;
import com.tepia.guangdong_module.amainguangdong.model.adminstatistics.ThreePersonsResponse;
import com.tepia.guangdong_module.APPCostant;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Android Studio
 *
 * @author : Arthur
 * Date :    2019/6/3
 * Time :    9:32
 * Describe :
 */
public class AdminStatisticsManager {

    private static final AdminStatisticsManager ourInstance = new AdminStatisticsManager();
    private AdminStatisticsHttpService mRetrofitService;
    private AdminStatisticsHttpService mRetrofitServiceUser;

    public static AdminStatisticsManager getInstance() {
        return ourInstance;
    }

    private AdminStatisticsManager() {
        mRetrofitService =
                RetrofitManager.getRetrofit(APPCostant.API_SERVER_URL + APPCostant.API_SERVER_MONITOR_AREA)
                        .create(AdminStatisticsHttpService.class);
        mRetrofitServiceUser =   RetrofitManager.getRetrofit(APPCostant.API_SERVER_URL + APPCostant.API_SERVER_USER_ADMIN)
                .create(AdminStatisticsHttpService.class);;
    }


    /**
     *
     * 查询行政统计首页-实时监测数据
     *
     * @return
     */
    public Observable<RealTimeMonitorResponse> getRealTimeMonitor() {
        String token = UserManager.getInstance().getToken();
        return mRetrofitService.getRealTimeMonitor(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     *
     * 查询行政统计首页-实时监测数据
     *
     * @return
     */
    public Observable<InspectionStatisticsResponse> getInspectionStatistics() {
        String token = UserManager.getInstance().getToken();
        return mRetrofitService.getInspectionStatistics(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     *
     * 查询行政统计首页-三个责任人数据
     *
     * @return
     */
    public Observable<ThreePersonsResponse> getThreePersons() {
        String token = UserManager.getInstance().getToken();
        return mRetrofitService.getThreePersons(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     *
     * 查询行政统计首页-三个重点g环节数据
     *
     * @return
     */
    public Observable<ThreeKeyPointResponse> getThreeKeyPoints() {
        String token = UserManager.getInstance().getToken();
        return mRetrofitService.getThreeKeyPoints(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     *
     * 查询行政统计首页-安全鉴定数据
     *
     * @return
     */
    public Observable<SafetyIdentifyResponse> getSafetyIndentify() {
        String token = UserManager.getInstance().getToken();
        return mRetrofitService.getSafetyIndentify(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
