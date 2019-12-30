package com.tepia.guangdong_module.amainguangdong.mvp.reservoirregimen;

import com.tepia.base.http.RetrofitManager;
import com.tepia.guangdong_module.amainguangdong.common.UserManager;
import com.tepia.guangdong_module.amainguangdong.model.reservoirregimen.ReservoirRegimenResponse;
import com.tepia.guangdong_module.APPCostant;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Android Studio
 *
 * @author : Arthur
 * Date :    2019/6/5
 * Time :    16:19
 * Describe :
 */
public class ReservoirRegimenManager {
    private static final ReservoirRegimenManager ourInstance = new ReservoirRegimenManager();
    private ReservoirRegimenHttpService mRetrofitService;

    public static ReservoirRegimenManager getInstance() {
        return ourInstance;
    }

    private ReservoirRegimenManager() {
        mRetrofitService =
                RetrofitManager.getRetrofit(APPCostant.API_SERVER_URL + APPCostant.API_SERVER_MONITOR_AREA)
                        .create(ReservoirRegimenHttpService.class);
    }


    /**
     * 获取水库水情列表
     *
     * @return
     */
    public Observable<ReservoirRegimenResponse> getReservoirRegimenList(int currentPage, int pageSize, String type, String identifyType, String startTime, String endTime) {
        String token = UserManager.getInstance().getToken();
        return mRetrofitService.getReservoirRegimenList(token, currentPage, pageSize, type, identifyType, startTime, endTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
