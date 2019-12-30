package com.tepia.guangdong_module.amainguangdong.mvp.taskdetail;

import com.tepia.base.mvp.BasePresenter;
import com.tepia.base.mvp.BaseView;

import java.util.List;


/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class TaskDetailContract {

    public interface View extends BaseView {


        void startExecuteSucess();

        void endExecuteSucess();


        void appReservoirWorkOrderItemCommitOneByOneSuccess();
    }

    public interface  Presenter extends BasePresenter<View> {
    }
}
