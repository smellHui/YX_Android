package com.tepia.guangdong_module.amainguangdong.mvp.taskdetail;

import com.tepia.base.mvp.BasePresenter;
import com.tepia.base.mvp.BaseView;
import com.tepia.guangdong_module.amainguangdong.route.TaskBean;
import com.tepia.guangdong_module.amainguangdong.route.TaskListResponse;

import java.util.List;

/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class YunWeiListContract {
    public interface View<T> extends BaseView {

        void success(T data);


        void failure(String mes);
    }

    interface  Presenter extends BasePresenter<View> {
        void getTaskBeanList(String reservoirId, String startDate, String endDate, int currentPage, int pageSize, boolean isshowing);
        void getByworkOrderId(String workOrderId);
    }
}
