package com.tepia.guangdong_module.amainguangdong.mvp.reserviorlistmvp;

import com.tepia.base.http.RetrofitManager;
import com.tepia.guangdong_module.amainguangdong.common.UserManager;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.AreaReservoirCountBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.ReserviorListBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.UserReservoirCount;
import com.tepia.guangdong_module.APPCostant;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by      android studio
 *
 * @author :       ly
 * Date            :       2019-05-30
 * Time            :       下午7:01
 * Version         :       1.0
 * location        :       武汉研发中心
 * 功能描述         :
 **/
public class ReserviorManager {
    private static final ReserviorManager ourInstance = new ReserviorManager();
    private ReservoirHttpService mRetrofitService;

    public static ReserviorManager getInstance() {
        return ourInstance;
    }

    private ReserviorManager() {
        this.mRetrofitService = RetrofitManager.getRetrofit(APPCostant.API_SERVER_URL + APPCostant.API_SERVER_MONITOR_AREA).create(ReservoirHttpService.class);
    }


    public Observable<ReserviorListBean> listReservoirInfo(String reservoir, String reservoirType, String areaCode, int currentPage, int pageSize) {
        String token = UserManager.getInstance().getToken();
        return mRetrofitService.listReservoirInfo(token, reservoir, reservoirType, areaCode, currentPage, pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<UserReservoirCount> getUserReservoirCount() {
        String token = UserManager.getInstance().getToken();
        return mRetrofitService.getUserReservoirCount(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<AreaReservoirCountBean> getAreaReservoirCount() {
        String token = UserManager.getInstance().getToken();

        return mRetrofitService.getAreaReservoirCount(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
