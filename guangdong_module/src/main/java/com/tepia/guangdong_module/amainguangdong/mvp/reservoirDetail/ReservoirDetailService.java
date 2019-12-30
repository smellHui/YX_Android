package com.tepia.guangdong_module.amainguangdong.mvp.reservoirDetail;

import com.tepia.guangdong_module.amainguangdong.model.reservoirdetail.PicDetailBean;
import com.tepia.guangdong_module.amainguangdong.model.reservoirdetail.RainDetailBean;
import com.tepia.guangdong_module.amainguangdong.model.reservoirdetail.WaterRegimeDetailBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.ReservoirOfflineResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
  * Created by      Android studio
  *
  * @author :wwj (from Center Of Wuhan)
  * Date    :2019/5/29
  * Version :1.0
  * 功能描述 :
 **/
public interface ReservoirDetailService {

    /**
     *  查询水库雨情（小时、日雨量）
     * @param token
     * @param reservoirId  6942292cad144bds97fa6c31f96ee696
     * @param startDate    2018-08-09 00:00:00
     * @param endDate       2018-08-16 00:00:00
     * @param selectType  time 小时雨量 day 日雨量
     * @return
     */
    @GET("appThree/StPptnR/listReservoirStPptnRBySelectType")
    Observable<RainDetailBean> getRainDetailList(@Header("Authorization") String token,
                                                 @Query("reservoirId") String reservoirId,
                                                 @Query("startDate") String startDate,
                                                 @Query("endDate") String endDate,
                                                 @Query("selectType") String selectType);

    /**
     * 查询水库水情列表
     * @param token
     * @param reservoirId
     * @param startDate
     * @param endDate
     * @return
     */
    @GET("app/appReservoirBase/listReservoirWater")
    Observable<WaterRegimeDetailBean> getReservoirWaterList(@Header("Authorization") String token,
                                                            @Query("reservoirId") String reservoirId,
                                                            @Query("startDate") String startDate,
                                                            @Query("endDate") String endDate);

    /**
     * 水库图像列表
     * @param token
     * @param reservoirId
     * @param startDate
     * @param endDate
     * @return
     */
    @GET("app/appPictureR/listPictureRByReservoir")
    Observable<PicDetailBean> getPictureDetailList(@Header("Authorization") String token,
                                                   @Query("reservoirId") String reservoirId,
                                                   @Query("startDate") String startDate,
                                                   @Query("endDate") String endDate);
}
