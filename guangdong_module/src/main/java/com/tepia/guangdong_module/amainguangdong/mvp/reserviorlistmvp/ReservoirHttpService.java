package com.tepia.guangdong_module.amainguangdong.mvp.reserviorlistmvp;

import com.tepia.guangdong_module.amainguangdong.model.xuncha.AreaReservoirCountBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.ReserviorListBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.UserReservoirCount;
import com.tepia.guangdong_module.amainguangdong.route.TaskBeanFromNet;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Created by      android studio
 *
 * @author :       ly
 * Date            :       2019-05-30
 * Time            :       下午7:02
 * Version         :       1.0
 * location        :       武汉研发中心
 * 功能描述         :
 **/
interface ReservoirHttpService {

    /**
     * 行政统计-水库列表
     * @param token
     * @param reservoir
     * @param reservoirType
     * @param areaCode
     * @param currentPage
     * @param pageSize
     * @return
     */
    @GET("app/appReservoirBase/listReservoirInfo")
    Observable<ReserviorListBean> listReservoirInfo(@Header("Authorization") String token,
                                                    @Query("reservoir") String reservoir,
                                                    @Query("reservoirType") String reservoirType,
                                                    @Query("areaCode") String areaCode,
                                                    @Query("currentPage") int currentPage,
                                                    @Query("pageSize") int pageSize);

    /**
     * 行政统计-水库数量统计
     * @param token
     * @return
     */
    @GET("app/appReservoirBase/getUserReservoirCount")
    Observable<UserReservoirCount> getUserReservoirCount(@Header("Authorization") String token
    );

    /**
     * 行政统计-实时监测统计
     * @param token
     * @return
     */
    @GET("app/appReservoirBase/getAreaReservoirCount")
    Observable<AreaReservoirCountBean> getAreaReservoirCount(@Header("Authorization") String token
    );
}
