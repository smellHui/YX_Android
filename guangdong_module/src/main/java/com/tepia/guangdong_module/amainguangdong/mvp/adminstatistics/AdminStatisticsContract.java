package com.tepia.guangdong_module.amainguangdong.mvp.adminstatistics;

import com.tepia.base.mvp.BasePresenter;
import com.tepia.base.mvp.BaseView;
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
 * Time:    9:20
 * Describe:行政统计首页
 */
public class AdminStatisticsContract {

    public interface View extends BaseView {
        /**
         *  展示行政统计首页-实时监测数据
         *
         * @param data 行政统计首页-实时监测返回数据
         */
        void showRealTimeMonitor(RealTimeMonitorResponse data);

        /**
         * 展示行政统计首页-巡查统计数据
         * @param data 行政统计首页-巡查统计返回数据
         */
        void showInspectionStatistics(InspectionStatisticsResponse data);

        /**
         * 展示行政统计首页-三个责任人数据
         * @param data 行政统计首页-三个责任人返回数据
         */
        void showThreePersons(ThreePersonsResponse data);

        /**
         * 展示行政统计首页-三个重点数据
         * @param data 行政统计首页-三个重点返回数据
         */
        void showThreeKeyPoints(ThreeKeyPointResponse data);

        /**
         * 展示行政统计首页-安全鉴定数据
         * @param data 行行政统计首页-安全鉴定返回数据
         */
        void showSafetyIndentify(SafetyIdentifyResponse data);

        /**
         * 网络失败提示
         * @param msg
         */
        void onFail(String msg);
    }

    public interface Presenter extends BasePresenter<View> {

        /**
         * 获取行政统计首页-实时监测数据
         *
         */
        void getRealTimeMonitor(boolean isShow, String msg);

        /**
         * 获取行政统计首页-巡查统计数据
         *
         */
        void getInspectionStatistics(boolean isShow, String msg);

        /**
         * 获取行政统计首页-三个责任人数据
         */
        void getThreePersons(boolean isShow, String msg);

        /**
         * 获取行政统计首页-三个重点数据
         *
         */
        void getThreeKeyPoints(boolean isShow, String msg);

        /**
         * 行政统计首页-安全鉴定
         *
         */
        void getSafetyIndentify(boolean isShow, String msg);
    }
}
