package com.tepia.guangdong_module.amainguangdong.mvp.typhoonroute;

import com.tepia.guangdong_module.amainguangdong.model.typhoonroute.TyphoonDetailResponse;
import com.tepia.guangdong_module.amainguangdong.model.typhoonroute.TyphoonListResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Created by Android Studio
 *
 * @author : Arthur
 * Date :    2019/5/31
 * Time :    10:34
 * Describe :
 */
public interface TyphoonRouteHttpService {

    /**
     * 获取台风列表
     *
     * @param token
     * @param year 年限
     * @return
     */
    @GET("app/appTyphoon/listTyphoonInfo")
    Observable<TyphoonListResponse> getTyphoonList(@Header("Authorization") String token,
                                                   @Query("year") String year);


    /**
     * 获取台风路径详情
     *
     * @param token
     * @param typhoonId 台风id
     * @return
     */
    @GET("app/appTyphoon/getTyphoonRouteInfo")
    Observable<TyphoonDetailResponse> getTyphoonDetail(@Header("Authorization") String token,
                                                       @Query("tfId") String typhoonId);
}
