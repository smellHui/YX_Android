package com.tepia.guangdong_module.amainguangdong.common;


import android.text.TextUtils;

import com.google.gson.Gson;
import com.tepia.base.common.DictMapEntity;
import com.tepia.base.common.DictMapResponse;
import com.tepia.base.http.LoadingSubject;
import com.tepia.base.http.RetrofitManager;
import com.tepia.base.utils.LogUtil;
import com.tepia.photo_picker.utils.SPUtils;
import com.tepia.guangdong_module.APPCostant;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 数据字典
 * Created by Joeshould on 2018/7/31.
 */

public class DictMapManager {
    private static final DictMapManager ourInstance = new DictMapManager();
    private DictMapService mRetrofitService;
    private DictMapEntity mDictMap;

    public static DictMapManager getInstance() {
        return ourInstance;
    }

    private DictMapManager() {
        this.mRetrofitService = RetrofitManager.getRetrofit(APPCostant.API_SERVER_URL).create(DictMapService.class);
//        getDictMapEntity();
    }

    /**
     * 获取数据字典
     *
     * @return
     */
    public Observable<DictMapResponse> getDictMap() {
        String token = UserManager.getInstance().getToken();
        return mRetrofitService.getDictMap(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public void getDictMapEntity() {
        getDictMap().safeSubscribe(new LoadingSubject<DictMapResponse>(false, "") {
            @Override
            protected void _onNext(DictMapResponse dictMapResponse) {
                if (dictMapResponse.getCode() == 0) {
                    LogUtil.e("获取数据字典getDictMapEntity---:成功");
                    mDictMap = dictMapResponse.getData();
                    setmDictMap(mDictMap);
                } else {
                    _onError(dictMapResponse.getMsg());
                }
            }

            @Override
            protected void _onError(String message) {
                LogUtil.e("getDictMapEntity---:"+message);
            }
        });
    }

    public DictMapEntity getmDictMap() {
        if (mDictMap == null){
            String temp = SPUtils.getInstance().getString("dictMap","");
            if (!TextUtils.isEmpty(temp)){
                mDictMap = new Gson().fromJson(temp,DictMapEntity.class);
            }
        }
        return mDictMap;
    }

    public void setmDictMap(DictMapEntity mDictMap) {
        this.mDictMap = mDictMap;
        SPUtils.getInstance().putString("dictMap",new Gson().toJson(mDictMap));
    }
}
