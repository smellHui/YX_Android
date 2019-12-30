package com.tepia.guangdong_module.amainguangdong.mvp.reserviorlistmvp;

import com.tepia.base.mvp.BasePresenter;
import com.tepia.base.mvp.BaseView;
import com.tepia.guangdong_module.amainguangdong.model.adminstatistics.RealTimeMonitorResponse;
import com.tepia.guangdong_module.amainguangdong.mvp.taskdetail.YunWeiListContract;

/**
 * Created by      android studio
 *
 * @author :       ly
 * Date            :       2019-05-30
 * Time            :       上午11:25
 * Version         :       1.0
 * location        :       武汉研发中心
 * 功能描述         :
 **/
public class ReservoirListContract {
    public interface View<T> extends BaseView {

        void success(T data);


        void failure(String mes);

        void showRealTimeMonitor(RealTimeMonitorResponse data);
    }

    interface  Presenter extends BasePresenter<View> {
        /**
         * 行政统计-水库列表
         * @param reservoir
         * @param reservoirType
         * @param areaCode
         * @param currentPage
         * @param pageSize
         * @param isshowing
         */
        void listReservoirInfo(String reservoir, String reservoirType, String areaCode, int currentPage, int pageSize, boolean isshowing);

        /**
         * 行政统计-实时监测统计
         */
        void getAreaReservoirCount();

        /**
         * 获取行政统计首页-实时监测数据
         *
         */
        void getRealTimeMonitor(boolean isShow, String msg);
    }
}
