package com.tepia.guangdong_module.amainguangdong.mvp.typhoonroute;

import com.arialyy.frame.util.show.T;
import com.tepia.base.mvp.BasePresenter;
import com.tepia.base.mvp.BaseView;
import com.tepia.guangdong_module.amainguangdong.model.typhoonroute.TyphoonDetailResponse;
import com.tepia.guangdong_module.amainguangdong.model.typhoonroute.TyphoonListResponse;
import com.tepia.guangdong_module.amainguangdong.mvp.taskdetail.TaskDetailContract;

/**
 * Created by Android Studio
 *
 * @author : Arthur
 * Date:    2019/5/31
 * Time:    10:47
 * Describe:
 */
public class TyphoonRouteContract {

    public interface View extends BaseView {
        /**
         *  展示台风列表
         *
         * @param data 台风列表返回数据
         */
        void showTyphoonList(TyphoonListResponse data);

        /**
         * 绘制台风路径
         * @param data
         */
        void showTyphoonRoute(TyphoonDetailResponse data);

        /**
         * 网络失败提示
         * @param msg
         */
        void onFail(String msg);
    }

    public interface Presenter extends BasePresenter<View> {

        /**
         * 网络访问获取台风列表
         * @param year 年份
         * @param isShow 是否展示进度条
         * @param msg 网络进度条文字
         */
        void getTyphoonList(String year, boolean isShow, String msg);

        /**
         * 获取台风路径详情
         * @param typhoonId 台风id
         * @param isShow 是否展示进度条
         * @param msg 网络进度条文字
         */
        void getTyphoonDetails(String typhoonId, boolean isShow, String msg);
    }
}
