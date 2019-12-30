package com.tepia.guangdong_module.amainguangdong.mvp.adminstatistics;

import com.tepia.base.http.LoadingSubject;
import com.tepia.base.mvp.BasePresenterImpl;
import com.tepia.guangdong_module.amainguangdong.model.adminstatistics.InspectionStatisticsResponse;
import com.tepia.guangdong_module.amainguangdong.model.adminstatistics.RealTimeMonitorResponse;
import com.tepia.guangdong_module.amainguangdong.model.adminstatistics.SafetyIdentifyResponse;
import com.tepia.guangdong_module.amainguangdong.model.adminstatistics.ThreeKeyPointResponse;
import com.tepia.guangdong_module.amainguangdong.model.adminstatistics.ThreePersonsResponse;

/**
 * Created by Android Studio
 *
 * @author: Arthur
 * Date:    2019/5/28
 * Time:    9:25
 * Describe:行政统计首页
 */
public class AdminStatisticsPresenter extends BasePresenterImpl<AdminStatisticsContract.View> implements AdminStatisticsContract.Presenter {


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
                        if (mView != null) {
                            mView.onFail(message);
                        }
                    }
                });
    }

    /**
     *
     * 行政统计首页-巡查统计
     *
     * @param isShow
     * @param msg
     */
    @Override
    public void getInspectionStatistics(boolean isShow, String msg) {
        AdminStatisticsManager.getInstance().getInspectionStatistics()
                .subscribe(new LoadingSubject<InspectionStatisticsResponse>(isShow, msg) {
                    @Override
                    protected void _onNext(InspectionStatisticsResponse inspectionStatisticsResponse) {
                        if (mView != null) {
                            mView.showInspectionStatistics(inspectionStatisticsResponse);
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
     * 行政统计首页-三个责任人
     *
     * @param isShow
     * @param msg
     */
    @Override
    public void getThreePersons(boolean isShow, String msg) {
        AdminStatisticsManager.getInstance().getThreePersons()
                .subscribe(new LoadingSubject<ThreePersonsResponse>(isShow, msg) {
                    @Override
                    protected void _onNext(ThreePersonsResponse threePersonsResponse) {
                        if (mView != null) {
                            mView.showThreePersons(threePersonsResponse);
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
     * 行政统计首页-三个重点
     *
     * @param isShow
     * @param msg
     */
    @Override
    public void getThreeKeyPoints(boolean isShow, String msg) {
        AdminStatisticsManager.getInstance().getThreeKeyPoints()
                .subscribe(new LoadingSubject<ThreeKeyPointResponse>(isShow, msg) {
                    @Override
                    protected void _onNext(ThreeKeyPointResponse threeKeyPointResponse) {
                        if (mView != null) {
                            mView.showThreeKeyPoints(threeKeyPointResponse);
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
     * 行政统计首页-安全鉴定
     *
     * @param isShow
     * @param msg
     */
    @Override
    public void getSafetyIndentify(boolean isShow, String msg) {
        AdminStatisticsManager.getInstance().getSafetyIndentify()
                .subscribe(new LoadingSubject<SafetyIdentifyResponse>(isShow, msg) {
                    @Override
                    protected void _onNext(SafetyIdentifyResponse safetyIdentifyResponse) {
                        if (mView != null) {
                            mView.showSafetyIndentify(safetyIdentifyResponse);
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
