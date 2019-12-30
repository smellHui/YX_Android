package com.tepia.guangdong_module.amainguangdong.mvp.reservoirDetail;

import com.tepia.base.http.RetrofitManager;
import com.tepia.base.utils.Utils;
import com.tepia.guangdong_module.amainguangdong.model.reservoirdetail.PicDetailBean;
import com.tepia.guangdong_module.amainguangdong.model.reservoirdetail.RainDetailBean;
import com.tepia.guangdong_module.amainguangdong.model.reservoirdetail.WaterRegimeDetailBean;
import com.tepia.photo_picker.utils.SPUtils;
import com.tepia.guangdong_module.APPCostant;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by      Android studio
 *
 * @author :wwj (from Center Of Wuhan)
 * Date    :2019/5/29
 * Version :1.0
 * 功能描述 : 水库详情
 **/
public class ReservoirDetailManager {
    private ReservoirDetailService mRetrofitService;
    private static final ReservoirDetailManager ourInstance = new ReservoirDetailManager();
    private static final ReservoirDetailManager ourInstance_admin = new ReservoirDetailManager(APPCostant.API_SERVER_USER_ADMIN);
    private static final ReservoirDetailManager ourInstance_works = new ReservoirDetailManager(APPCostant.API_SERVER_TASK_AREA);
    private static final ReservoirDetailManager ourInstance_monitor = new ReservoirDetailManager(APPCostant.API_SERVER_MONITOR_AREA);
    public static final String USERINFO = "USERINFO";
    private static final String TOKEN = "TOKEN";
    private static final String DEFAULT_RES = "DEFAULT_RES";

    /**
     * 获取用户token
     *
     * @return
     */
    public String getToken() {
        String temp = SPUtils.getInstance(Utils.getContext()).getString("TOKEN", "");
        return temp;
    }

    public static ReservoirDetailManager getInstance() {
        return ourInstance;
    }

    public static ReservoirDetailManager getInstance_ADMIN() {
        return ourInstance_admin;
    }

    public static ReservoirDetailManager getInstance_Works() {
        return ourInstance_works;
    }

    public static ReservoirDetailManager getInstance_Monitor() {
        return ourInstance_monitor;
    }

    private ReservoirDetailManager() {
        this.mRetrofitService = RetrofitManager.getRetrofit(APPCostant.API_SERVER_URL + APPCostant.API_SERVER_USER_AREA).create(ReservoirDetailService.class);
    }

    private ReservoirDetailManager(String adminstr) {
        this.mRetrofitService = RetrofitManager.getRetrofit(APPCostant.API_SERVER_URL + adminstr).create(ReservoirDetailService.class);
    }

    /**
     * 查询水库雨情（小时、日雨量）
     *
     * @param reservoirId
     * @param startDate
     * @param endDate
     * @param selectType
     * @return
     */
    public Observable<RainDetailBean> getRainDetailList(String reservoirId, String startDate, String endDate, String selectType) {
        String token = getToken();
        return mRetrofitService.getRainDetailList(token, reservoirId, startDate, endDate, selectType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 查询水库水情列表
     *
     * @param reservoirId
     * @param startDate
     * @param endDate
     * @return
     */
    public Observable<WaterRegimeDetailBean> getReservoirWaterList(String reservoirId, String startDate, String endDate) {
        String token = getToken();
        return mRetrofitService.getReservoirWaterList(token, reservoirId, startDate, endDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 查询水库图像列表
     *
     * @param reservoirId
     * @param startDate
     * @param endDate
     * @return
     */
    public Observable<PicDetailBean> getPictureDetailList(String reservoirId, String startDate, String endDate) {
        String token = getToken();
        return mRetrofitService.getPictureDetailList(token, reservoirId, startDate, endDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
