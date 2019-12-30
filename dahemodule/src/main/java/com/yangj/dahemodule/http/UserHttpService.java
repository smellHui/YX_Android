package com.yangj.dahemodule.http;

import com.tepia.base.http.BaseResponse;
import com.tepia.guangdong_module.amainguangdong.model.UserInfoBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.AreaBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.ReservoirListResponse;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.ReservoirOfflineResponse;
import com.yangj.dahemodule.model.JsonBean;
import com.yangj.dahemodule.model.NewNoticeBean;
import com.yangj.dahemodule.model.UserDataBean;
import com.yangj.dahemodule.model.WeatherWarnBean;
import com.yangj.dahemodule.model.danger.DangerDataBean;
import com.yangj.dahemodule.model.main.MainDataBean;
import com.yangj.dahemodule.model.patrol.PatrolRecordDataBean;
import com.yangj.dahemodule.model.report.ReportDataBean;
import com.yangj.dahemodule.model.report.ReportDetailDataBean;
import com.yangj.dahemodule.model.user.SysUserDataBean;
import com.yangj.dahemodule.model.xuncha.ProtalDataBean;
import com.yangj.dahemodule.model.xuncha.RecordDataBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Author:xch
 * Date:2019/9/2
 * Description:
 */
public interface UserHttpService {

    @POST("oauth/login")
    @Headers({
            "Content-Type:application/x-www-form-urlencoded",
            "Authorization:Basic c2stYXBwOnNrLWFwcA=="
    })
    Observable<UserDataBean> login(@Body RequestBody requestBody);

    /**
     * 【查询】巡检记录列表
     *
     * @param token
     * @param requestBody
     * @return
     */
    @GET("app/patrol/listAll")
    Observable<RecordDataBean> getRecordList(@Header("Authorization") String token, @QueryMap Map<String, String> requestBody);

    /**
     * 【查询】我的巡检列表
     *
     * @param token
     * @param requestBody
     * @return
     */
    @GET("app/patrol/list4Me")
    Observable<RecordDataBean> getRecordListByMe(@Header("Authorization") String token, @QueryMap Map<String, String> requestBody);

    /**
     * 【查询】待处理险情列表
     *
     * @param token
     * @param requestBody
     * @return
     */
    @GET("app/problem/listPendingHandle")
    Observable<DangerDataBean> getPendingHandleList(@Header("Authorization") String token, @QueryMap Map<String, String> requestBody);

    /**
     * 【查询】我处置过的险情
     *
     * @param token
     * @param requestBody
     * @return
     */
    @GET("app/problem/listHandle4Me")
    Observable<DangerDataBean> getHandleList(@Header("Authorization") String token, @QueryMap Map<String, String> requestBody);


    /**
     * 【查询】我上报的险情
     *
     * @param token
     * @param requestBody
     * @return
     */
    @GET("app/problem/list4Me")
    Observable<ReportDataBean> getReportList(@Header("Authorization") String token, @QueryMap Map<String, String> requestBody);

    /**
     * 【查询】巡检详情
     *
     * @param token
     * @param omRecordCode
     * @return
     */
    @GET("/app/patrol/detail")
    Observable<ProtalDataBean> getPatrolDetail(@Header("Authorization") String token, @Query("omRecordCode") String omRecordCode);

    /**
     * 【查询】险情详情
     *
     * @param token
     * @param id
     * @return
     */
    @GET("app/problem/detail")
    Observable<ReportDetailDataBean> loadReportDetail(@Header("Authorization") String token, @Query("id") String id);

    /**
     * 【查询】详情和操作流水
     *
     * @param token
     * @param omRecordCode
     * @return
     */
    @GET("app/patrol/detailAndFlow")
    Observable<PatrolRecordDataBean> loadPatrolDetailAndFlow(@Header("Authorization") String token, @Query("omRecordCode") String omRecordCode);

    /**
     * 【查询】加载数据接口
     *
     * @param token
     * @param reservoirCode
     * @return
     */
    @GET("app/patrol/loadData")
    Observable<MainDataBean> loadData(@Header("Authorization") String token, @Query("reservoirCode") String reservoirCode);

    /**
     * 【查询】用户信息
     *
     * @param token
     * @return
     */
    @GET("app/user/info")
    Observable<SysUserDataBean> getUserInfo(@Header("Authorization") String token);

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
     * 【审核】审核记录
     *
     * @param token
     * @param body
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("app/patrol/examine")
    Observable<BaseResponse> examinePatrol(@Header("Authorization") String token,
                                           @Body RequestBody body
    );


    /**
     * 【新增】险情反馈
     *
     * @param token
     * @return
     */
    @Multipart
    @POST("app/problem/feedback")
    Observable<BaseResponse> feekBackProblem(@Header("Authorization") String token,
                                             @PartMap Map<String, RequestBody> params,
                                             @Part List<MultipartBody.Part> beforePathList

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
     *
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
