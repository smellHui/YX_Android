package com.tepia.guangdong_module.amainguangdong.mvp.typhoonroute;

import com.tepia.base.http.RetrofitManager;
import com.tepia.guangdong_module.amainguangdong.common.UserManager;
import com.tepia.guangdong_module.amainguangdong.model.typhoonroute.TyphoonDetailResponse;
import com.tepia.guangdong_module.amainguangdong.model.typhoonroute.TyphoonListResponse;
import com.tepia.guangdong_module.APPCostant;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Android Studio
 *
 * @author : Arthur
 * Date:    2019/5/31
 * Time:    10:47
 * Describe:
 */
public class TyphoonRouteManager {
    private static final TyphoonRouteManager ourInstance = new TyphoonRouteManager();
    private TyphoonRouteHttpService mRetrofitService;

    public static TyphoonRouteManager getInstance() {
        return ourInstance;
    }

    private TyphoonRouteManager() {
        mRetrofitService =
                RetrofitManager.getRetrofit(APPCostant.API_SERVER_URL + APPCostant.API_SERVER_USER_ADMIN)
                        .create(TyphoonRouteHttpService.class);
    }

    /**
     * 查询台风列表
     *
     * @param year 年限
     * @return
     */
    public Observable<TyphoonListResponse> getTyphoonList(String year) {
        String token = UserManager.getInstance().getToken();
        return mRetrofitService.getTyphoonList(token, year)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取台风详情数据
     *
     * @param typhoonId 台风id
     * @return
     */
    public Observable<TyphoonDetailResponse> getTyphoonDetails(String typhoonId) {
        String token = UserManager.getInstance().getToken();
        return mRetrofitService.getTyphoonDetail(token, typhoonId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
