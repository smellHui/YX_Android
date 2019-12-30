package com.tepia.guangdong_module.amainguangdong.mvp.reservoirregimen;

import com.tepia.guangdong_module.amainguangdong.model.adminstatistics.ThreeKeyPointResponse;
import com.tepia.guangdong_module.amainguangdong.model.reservoirregimen.ReservoirRegimenResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Created by Android Studio
 *
 * @author : Arthur
 * Date :    2019/6/5
 * Time :    16:19
 * Describe : 行政统计-水库水情
 */
public interface ReservoirRegimenHttpService {

    /**
     * 获取水库水情列表
     *
     * @param token
     * @return
     */
    @GET("app/appStRsvrR/listReservoirNewStRsvr")
    Observable<ReservoirRegimenResponse> getReservoirRegimenList(@Header("Authorization") String token,
                                                                 @Query("currentPage") int currentPage,
                                                                 @Query("pageSize") int pageSize,
                                                                 @Query("type") String type,
                                                                 @Query("identifyType") String identifyType,
                                                                 @Query("startTime") String startTime,
                                                                 @Query("endTime") String endTime);
}
