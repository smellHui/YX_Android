package com.tepia.guangdong_module.amainguangdong.mvp.reservoirregimen;

import com.arialyy.frame.util.show.T;
import com.tepia.base.mvp.BasePresenter;
import com.tepia.base.mvp.BaseView;
import com.tepia.guangdong_module.amainguangdong.model.reservoirregimen.ReservoirRegimenResponse;
import com.tepia.guangdong_module.amainguangdong.mvp.adminstatistics.AdminStatisticsContract;

import java.text.ParseException;

/**
 * Created by Android Studio
 *
 * @author: Arthur
 * Date:    2019/5/29
 * Time:    17:28
 * Describe:
 */
public class ReservoirRegimenContract {
    public interface View extends BaseView {
        /**
         * 网络失败提示
         */
        void onFail(String msg);

        /**
         * 展示水情列表
         *
         */
        void showList(ReservoirRegimenResponse data);
    }

    public interface Presenter extends BasePresenter<View> {

        /**
         * 获取水情列表
         */
       void getReservoirRegimenList(int currentPage, int pageSize, String type, String identifyType, String startTime, String endTime, boolean isShow, String msg) throws ParseException;
    }
}
