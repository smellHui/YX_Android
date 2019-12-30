package com.tepia.guangdong_module.amainguangdong.mvp.rainfallstatistics;

import com.example.guangdong_module.R;
import com.tepia.base.http.LoadingSubject;
import com.tepia.base.mvp.BasePresenterImpl;
import com.tepia.base.utils.ResUtils;
import com.tepia.guangdong_module.amainguangdong.model.rainfallstatistics.RainfallListResponse;
import com.tepia.guangdong_module.amainguangdong.model.rainfallstatistics.ReservoirNumResponse;

/**
 * Created by Android Studio
 *
 * @author : Arthur
 * Date :    2019/6/5
 * Time :    14:48
 * Describe :
 */
public class RainfallStatisticsPesenter extends BasePresenterImpl<RainfallStatisticsContract.View> implements RainfallStatisticsContract.Presenter {


    /**
     *
     * 行政统计-雨量统计-水库数量
     *
     * @param startTime
     * @param endTime
     * @param period
     * @return
     */
    @Override
    public void getReservoirNum(String startTime, String endTime, String period,boolean isshowing, String msg) {
        RainfallStatisticsManager.getInstance().getReservoirNum(startTime,endTime,period).subscribe(new LoadingSubject<ReservoirNumResponse>(isshowing, ResUtils.getString(R.string.data_loading)) {
            @Override
            protected void _onNext(ReservoirNumResponse reservoirNumResponse) {
                if (reservoirNumResponse != null) {
                    if (reservoirNumResponse.getCode() == 0) {
                        if (mView != null) {
                            mView.showReservoirNum(reservoirNumResponse);
                        }
                    }
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
     * 行政统计-雨量统计-列表
     *
     * @param currentPage
     * @param pageSize
     * @param startTime
     * @param endTime
     * @param period
     * @param level
     * @param isshowing
     * @param msg
     */
    @Override
    public void getRainfallList(int currentPage, int pageSize, String startTime, String endTime, String period, String level,boolean isshowing, String msg) {
        RainfallStatisticsManager.getInstance().getRainfallList(currentPage,pageSize, startTime,endTime,period,level).subscribe(new LoadingSubject<RainfallListResponse>(isshowing, msg) {
            @Override
            protected void _onNext(RainfallListResponse rainfallListResponse) {
                if (rainfallListResponse != null) {
                    if (rainfallListResponse.getCode() == 0) {
                        if (mView != null) {
                            mView.showRainfallList(rainfallListResponse);
                        }
                    }
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
