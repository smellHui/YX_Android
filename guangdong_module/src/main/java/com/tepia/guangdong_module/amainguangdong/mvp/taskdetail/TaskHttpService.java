package com.tepia.guangdong_module.amainguangdong.mvp.taskdetail;

import com.tepia.base.http.BaseResponse;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.WaterPptnPictureBean;
import com.tepia.guangdong_module.amainguangdong.route.TaskBean;
import com.tepia.guangdong_module.amainguangdong.route.TaskBeanFromNet;
import com.tepia.guangdong_module.amainguangdong.route.TaskDetailResponse;
import com.tepia.guangdong_module.amainguangdong.route.TaskItemBeanFromNet;

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
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

/**
 * Created by Joeshould on 2018/5/23.
 */

interface TaskHttpService {



    /**
     * 修改工单状态为执行中
     *
     * @param token
     * @param workOrderId 工单id
     * @return
     */
    @FormUrlEncoded
    @POST("app/appWorkOrder/startExecute")
    Observable<BaseResponse> startExecute(@Header("Authorization") String token,
                                          @Field("workOrderId") String workOrderId,
                                          @Field("positionStr") String positionStr);



    /**
     * APP 提交单个配置运维结果
     *
     * @param params
     * @param beforePathList
     * @return
     */
    @Multipart
    @POST("app/patrol/addItem")
    Observable<BaseResponse> appReservoirWorkOrderItemCommitOne(@Header("Authorization") String token,
                                                                @PartMap Map<String, RequestBody> params,
                                                                @Part List<MultipartBody.Part> beforePathList
    );


    /**
     * APP 巡检人员工单执行完成提交 （App 端使用）
     *
     * @param token
     * @param omRecordCode    工单id
     * @param omPath 流线信息
     * @return
     */
    @FormUrlEncoded
    @POST("app/patrol/complete")
    Observable<BaseResponse> endExecute(@Header("Authorization") String token,
                                        @Field("omRecordCode") String omRecordCode,
                                        @Field("omPath") String omPath);

    @FormUrlEncoded
    @POST("app/patrolAppWorkOrder/endExcute")
    Observable<BaseResponse> endExecuteNew(@Header("Authorization") String token,
                                           @Field("workOrderId") String workOrderId
    );


    @POST("app/patrol/add")
    Observable<TaskDetailResponse> newStartExecute(@Header("Authorization") String token,
                                                   @Body RequestBody info
    );

    @Multipart
    @POST("app/problem/report")
    Observable<BaseResponse> reportProblem(@Header("Authorization") String token,
                                           @PartMap Map<String, RequestBody> params,
                                           @Part List<MultipartBody.Part> beforePathList

    );


    /**
     * 水雨情
     * @param token
     * @param reservoirId
     * @return
     */
    @GET("app/patrolAppQueryData/newWaterPptnPicture")
    Observable<WaterPptnPictureBean> newWaterPptnPicture(@Header("Authorization") String token,
                                                         @Query("reservoirId") String reservoirId);


    /**
     * 分页查询水库工单列表
     * @param token
     * @param reservoirId
     * @param startDate
     * @param endDate
     * @param currentPage
     * @param pageSize
     * @return
     */
    @GET("app/patrolAppQueryData/listReservoirWorkOrder")
    Observable<TaskBeanFromNet> listReservoirWorkOrder(@Header("Authorization") String token,
                                                       @Query("reservoirId") String reservoirId,
                                                       @Query("startDate") String startDate,
                                                       @Query("endDate") String endDate,
                                                       @Query("currentPage") int currentPage,
                                                       @Query("pageSize") int pageSize
    );
    @GET("app/patrolAppQueryData/getWorkOrderDetailInfo")
    Observable<TaskItemBeanFromNet> getWorkOrderDetailInfo(@Header("Authorization") String token,
                                                           @Query("workOrderId") String workOrderId);
}
