package com.tepia.guangdong_module.amainguangdong.mvp.rainfallstatistics;

import com.tepia.guangdong_module.amainguangdong.model.rainfallstatistics.RainfallListResponse;
import com.tepia.guangdong_module.amainguangdong.model.rainfallstatistics.ReservoirNumResponse;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.ReserviorListBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Created by Android Studio
 *
 * @author : Arthur
 * Date :    2019/6/5
 * Time :    16:24
 * Describe :
 */
public interface RainfallStatisticsHttpService {

    /**
     *
     * 行政统计-雨量统计-水库数量
     *
     * @param token
     * @param startTime
     * @param endTime
     * @param period
     * @return
     */
    @GET("app/appStPptnR/reservoirPPthLevelCount")
    Observable<ReservoirNumResponse> getReservoirNum(@Header("Authorization") String token,
                                                     @Query("startTime") String startTime,
                                                     @Query("endTime") String endTime,
                                                     @Query("period") String period);


    /**
     *
     * 行政统计-雨量统计-列表
     *
     * @param token
     * @param pageSize
     * @param startTime
     * @param endTime
     * @param period
     * @param level
     * @return
     */
    @GET("app/appStPptnR/listReservoirPPth")
    Observable<RainfallListResponse> getRainfallList(@Header("Authorization") String token,
                                                     @Query("currentPage") int currentPage,
                                                     @Query("pageSize") int pageSize,
                                                     @Query("startTime") String startTime,
                                                     @Query("endTime") String endTime,
                                                     @Query("period") String period,
                                                     @Query("level") String level);
}
