package com.tepia.guangdong_module.amainguangdong.mvp.reserviorlistmvp;

import com.example.guangdong_module.R;
import com.tepia.base.http.BaseResponse;
import com.tepia.base.http.LoadingSubject;
import com.tepia.base.mvp.BasePresenterImpl;
import com.tepia.base.utils.LogUtil;
import com.tepia.base.utils.ResUtils;
import com.tepia.base.utils.ToastUtils;
import com.tepia.guangdong_module.amainguangdong.model.adminstatistics.RealTimeMonitorResponse;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.AreaReservoirCountBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.ReserviorListBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.UserReservoirCount;
import com.tepia.guangdong_module.amainguangdong.mvp.adminstatistics.AdminStatisticsManager;

/**
 * Created by      android studio
 *
 * @author :       ly
 * Date            :       2019-05-30
 * Time            :       上午11:23
 * Version         :       1.0
 * location        :       武汉研发中心
 * 功能描述         :
 **/
public class ReservoirListPresenter extends BasePresenterImpl<ReservoirListContract.View> implements ReservoirListContract.Presenter {


    @Override
    public void listReservoirInfo(String reservoir, String reservoirType, String areaCode, int currentPage, int pageSize, boolean isshowing) {

        ReserviorManager.getInstance().listReservoirInfo(reservoir,reservoirType,areaCode,currentPage,pageSize).subscribe(new LoadingSubject<ReserviorListBean>(isshowing, ResUtils.getString(R.string.data_loading)) {
            @Override
            protected void _onNext(ReserviorListBean reserviorListBean) {
                if (reserviorListBean != null) {
                    if (reserviorListBean.getCode() == 0) {
                        if (mView != null) {
                            mView.success(reserviorListBean);
                        }
                    }
                }
            }

            @Override
            protected void _onError(String message) {
                if (mView != null) {
                    mView.failure(message);
                }

            }
        });


    }

    @Override
    public void getAreaReservoirCount() {
        ReserviorManager.getInstance().getAreaReservoirCount().subscribe(new LoadingSubject<AreaReservoirCountBean>(true, ResUtils.getString(R.string.data_loading)) {
            @Override
            protected void _onNext(AreaReservoirCountBean areaReservoirCountBean) {
                if (areaReservoirCountBean != null) {
                    if (areaReservoirCountBean.getCode() == 0 && mView != null) {
                        mView.success(areaReservoirCountBean);
                    }
                }
            }

            @Override
            protected void _onError(String message) {
                if (mView != null) {
                    mView.failure(message);

                }
            }
        });
    }

    /**
     *
     * 行政统计首页-实时监测
     *
     * @param isShow
     * @param msg
     */
    @Override
    public void getRealTimeMonitor(boolean isShow, String msg) {
        AdminStatisticsManager.getInstance().getRealTimeMonitor()
                .subscribe(new LoadingSubject<RealTimeMonitorResponse>(isShow, msg) {
                    @Override
                    protected void _onNext(RealTimeMonitorResponse realTimeMonitorResponse) {
                        if (mView != null) {
                            mView.showRealTimeMonitor(realTimeMonitorResponse);
                        }
                    }

                    @Override
                    protected void _onError(String message) {
                        ToastUtils.shortToast(message);
                    }
                });
    }
}
