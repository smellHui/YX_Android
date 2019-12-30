package com.tepia.guangdong_module.amainguangdong.mvp.rainfallstatistics;

import com.tepia.base.mvp.BasePresenter;
import com.tepia.base.mvp.BaseView;
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
public class RainfallStatisticsContract {

    public interface View extends BaseView {
        /**
         *  访问失败
         * @param msg
         */
        void onFail(String msg);

        /**
         * 展示水库雨情数据
         * @param data
         */
        void showReservoirNum(ReservoirNumResponse data);

        /**
         * 展示雨情列表数据
         * @param data
         */
        void showRainfallList(RainfallListResponse data);
    }

    public interface Presenter extends BasePresenter<View> {
        void getReservoirNum(String startTime, String endTime, String period, boolean isshowing, String msg);

        void getRainfallList(int currentPage, int pageSize, String startTime, String endTime, String period,
                             String level, boolean isshowing, String msg);
    }
}
