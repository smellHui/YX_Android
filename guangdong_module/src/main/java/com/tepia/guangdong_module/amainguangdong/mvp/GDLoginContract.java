package com.tepia.guangdong_module.amainguangdong.mvp;

import android.content.Context;

import com.tepia.base.mvp.BasePresenter;
import com.tepia.base.mvp.BaseView;
import com.tepia.guangdong_module.amainguangdong.model.JsonBean;

/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class GDLoginContract {
    public interface View extends BaseView {
        void loginSuccess();
        void jsonSuccess(JsonBean bean);
    }

    public interface  Presenter extends BasePresenter<View> {
        void login(Context mContext, String username, String password, String registId, String deviceId);
    }
}
