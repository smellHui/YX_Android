package com.tepia.guangdong_module.amainguangdong.common;

import com.tepia.base.common.DictMapResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by Joeshould on 2018/5/25.
 */

public interface DictMapService {
    /**
     * 获取数据字典
     * @param token
     * @return
     */
    @GET("api/admin/sysDict/getDictMap")
    Observable<DictMapResponse>  getDictMap(@Header("Authorization") String token);






}
