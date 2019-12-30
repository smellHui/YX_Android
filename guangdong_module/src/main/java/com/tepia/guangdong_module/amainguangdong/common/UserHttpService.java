package com.tepia.guangdong_module.amainguangdong.common;


import com.tepia.base.http.BaseResponse;
import com.tepia.guangdong_module.amainguangdong.model.JsonBean;
import com.tepia.guangdong_module.amainguangdong.model.NewNoticeBean;
import com.tepia.guangdong_module.amainguangdong.model.UserInfoBean;
import com.tepia.guangdong_module.amainguangdong.model.WeatherWarnBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.AreaBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.ReservoirListResponse;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.ReservoirOfflineResponse;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by      android studio
 *
 * @author :       ly
 * Date            :       2019-04-16
 * Time            :       上午12:32
 * Version         :       1.0
 * location        :       武汉研发中心
 * 功能描述         :
 **/
interface UserHttpService {

    @FormUrlEncoded
    @POST("jwt/app/getToken")
    Observable<UserLoginResponse> login(@Field("logincode") String name,
                                        @Field("password") String password,
                                        @Field("registId") String deviceId);

    ;


    /**
     * 获取用户登录信息
     * <p>
     * 2019-5-29 登录接口路径变更
     *
     * @param token
     * @return
     */
    @GET("app/appSysUser/getLoginUserInfo")
    Observable<UserInfoBean> getLoginUser(@Header("Authorization") String token);

    /**
     * @param token
     * @return
     */
    @GET("app/patrolAppQueryData/listReservoir")
    Observable<ReservoirListResponse> getReservoirList(@Header("Authorization") String token);

    /**
     * APP查询某个水库全部信息
     *
     * @param token
     * @param reservoirId
     * @return
     */
    @GET("app/patrolAppQueryData/listReservoirRoute")
    Observable<ReservoirOfflineResponse> getAllReservoirData(@Header("Authorization") String token,
                                                             @Query("reservoirId") String reservoirId);

    /**
     * 获取水库最新通知
     *
     * @param token
     * @param reservoirId
     * @return
     */
    @GET("app/workNotice/getNewNotice")
    Observable<NewNoticeBean> getNewNotice(@Header("Authorization") String token,
                                           @Query("reservoirId") String reservoirId);

    @FormUrlEncoded
    @POST("app/workNotice/updateNotice")
    Observable<BaseResponse> updateNotice(@Header("Authorization") String token,
                                          @Field("id") String id,
                                          @Field("workOrderId") String workOrderId
    );

    /**
     * 天气预警
     *
     * @param token
     * @return
     */
    @GET("app/appWarnInfo/getWeatherWarn")
    Observable<WeatherWarnBean> getWeatherWarn(@Header("Authorization") String token);

    @GET("app/appSysUser/getAreaSelect")
    Observable<AreaBean> getAreaSelect(@Header("Authorization") String token);

    /**
     * 人员位置上报接口
     * @param token
     * @param reservoirId
     * @param longitude
     * @param latitude
     * @return
     */
    @FormUrlEncoded
    @POST("app/bizCheckmanLocation/uploadCheckManLocation")
    Observable<BaseResponse> uploadCheckManLocation(@Header("Authorization") String token,
                                                    @Field("reservoirId") String reservoirId,
                                                    @Field("longitude") String longitude,
                                                    @Field("latitude") String latitude
    );

    @GET("vrs/login_message.json")
    Observable<JsonBean> getJson();
}
