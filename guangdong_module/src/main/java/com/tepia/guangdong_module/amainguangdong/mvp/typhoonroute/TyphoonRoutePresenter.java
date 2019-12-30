package com.tepia.guangdong_module.amainguangdong.mvp.typhoonroute;

import com.tepia.base.http.LoadingSubject;
import com.tepia.base.mvp.BasePresenterImpl;
import com.tepia.guangdong_module.amainguangdong.model.typhoonroute.TyphoonDetailResponse;
import com.tepia.guangdong_module.amainguangdong.model.typhoonroute.TyphoonListResponse;

import static com.tepia.guangdong_module.amainguangdong.mvp.typhoonroute.TyphoonRouteContract.*;

/**
 * Created by Android Studio
 *
 * @author : Arthur
 * Date:    2019/5/31
 * Time:    10:47
 * Describe:
 */
public class TyphoonRoutePresenter extends BasePresenterImpl<View>
        implements Presenter {

    /**
     *
     * 获取台风列表数据
     *
     * @param year 年限
     * @param isShow
     * @param msg
     */
    @Override
    public void getTyphoonList(String year, boolean isShow, String msg) {
        TyphoonRouteManager.getInstance().getTyphoonList(year)
                .subscribe(new LoadingSubject<TyphoonListResponse>(isShow, msg) {
                    @Override
                    protected void _onNext(TyphoonListResponse typhoonListResponse) {
                        if (mView != null) {
                            mView.showTyphoonList(typhoonListResponse);
                        }
                    }

                    @Override
                    protected void _onError(String message) {
                        if (mView != null) {
                            mView.onFail(message);
                        }
                    }
                });
    }

    /**
     *
     * 获取台风详情数据
     *
     * @param typhoonId 台风id
     * @param isShow
     * @param msg
     */
    @Override
    public void getTyphoonDetails(String typhoonId, boolean isShow, String msg) {
        TyphoonRouteManager.getInstance().getTyphoonDetails(typhoonId)
                .subscribe(new LoadingSubject<TyphoonDetailResponse>(isShow, msg) {
                    @Override
                    protected void _onNext(TyphoonDetailResponse typhoonListResponse) {
                        if (mView != null) {
                            mView.showTyphoonRoute(typhoonListResponse);
                        }
                    }

                    @Override
                    protected void _onError(String message) {
                        if (mView != null) {
                            mView.onFail(message);
                        }
                    }
                });
    }
}
