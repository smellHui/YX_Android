package com.tepia.guangdong_module.amainguangdong.mvp.adminstatistics;

import com.tepia.guangdong_module.amainguangdong.model.adminstatistics.InspectionStatisticsResponse;
import com.tepia.guangdong_module.amainguangdong.model.adminstatistics.RealTimeMonitorResponse;
import com.tepia.guangdong_module.amainguangdong.model.adminstatistics.SafetyIdentifyResponse;
import com.tepia.guangdong_module.amainguangdong.model.adminstatistics.ThreeKeyPointResponse;
import com.tepia.guangdong_module.amainguangdong.model.adminstatistics.ThreePersonsResponse;
import com.tepia.guangdong_module.amainguangdong.model.typhoonroute.TyphoonListResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Created by Android Studio
 *
 * @author : Arthur
 * Date :    2019/6/3
 * Time :    9:32
 * Describe :
 */
public interface AdminStatisticsHttpService {

    /**
     * 行政统计首页-实时监测
     *
     * @param token
     * @return
     */
    @GET("app/appReservoirBase/realTimeMonitorCount")
    Observable<RealTimeMonitorResponse> getRealTimeMonitor(@Header("Authorization") String token);

    /**
     * 行政统计首页-巡查统计
     *
     * @param token
     * @return
     */
    @GET("app/appReservoirBase/inspectionStatistics")
    Observable<InspectionStatisticsResponse> getInspectionStatistics(@Header("Authorization") String token);

    /**
     * 行政统计首页-三个责任人
     *
     * @param token
     * @return
     */
    @GET("app/appReservoirBase/threePersons")
    Observable<ThreePersonsResponse> getThreePersons(@Header("Authorization") String token);

    /**
     * 行政统计首页-三个重点
     *
     * @param token
     * @return
     */
    @GET("app/appReservoirBase/threeKeyPoints")
    Observable<ThreeKeyPointResponse> getThreeKeyPoints(@Header("Authorization") String token);

    /**
     * 行政统计首页-安全鉴定
     *
     * @param token
     * @return
     */
    @GET("app/appReservoirBase/safetyIdentify")
    Observable<SafetyIdentifyResponse> getSafetyIndentify(@Header("Authorization") String token);

}
