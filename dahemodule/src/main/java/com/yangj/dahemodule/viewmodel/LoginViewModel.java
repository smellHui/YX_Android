package com.yangj.dahemodule.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSON;
import com.tepia.base.AppRoutePath;
import com.tepia.base.http.LoadingSubject;
import com.tepia.base.utils.LogUtil;
import com.tepia.base.utils.ToastUtils;
import com.tepia.base.view.floatview.CollectionsUtil;
import com.tepia.guangdong_module.amainguangdong.common.UserManager;
import com.yangj.dahemodule.common.HttpManager;
import com.yangj.dahemodule.model.UserBean;
import com.yangj.dahemodule.model.UserDataBean;
import com.yangj.dahemodule.model.user.RolesBean;
import com.yangj.dahemodule.model.user.SysUserBean;
import com.yangj.dahemodule.model.user.SysUserDataBean;
import com.yangj.dahemodule.util.AesUtils;

import java.util.List;

/**
 * Author:xch
 * Date:2019/12/26
 * Description:
 */
public class LoginViewModel extends ViewModel {

    public MutableLiveData<SysUserDataBean> sysUser = new MutableLiveData<>();

    public void login(String username, String password){
        HttpManager.getInstance().login(username, AesUtils.encrypt(password)) .subscribe(new LoadingSubject<UserDataBean>(true, "正在登录...") {
            @Override
            protected void _onNext(UserDataBean userLoginResponse) {
                if (userLoginResponse == null) return;
                UserBean userBean = userLoginResponse.getData();
                if (userBean == null) return;
                HttpManager.getInstance().saveUser(JSON.toJSONString(userBean));
                UserManager.getInstance().saveToken(userBean.getAccess_token());
                UserManager.getInstance().saveUserCode(username);
                loadUserInfo();
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.shortToast(message);
                LogUtil.e("userLoginResponse--->" + message);
            }

        });
    }

    private void loadUserInfo(){
        HttpManager.getInstance().getUserInfo()
                .subscribe(new LoadingSubject<SysUserDataBean>() {

                    @Override
                    protected void _onNext(SysUserDataBean sysUserDataBean) {
                        SysUserBean sysUserBean = sysUserDataBean.getData();
                        if (sysUserBean == null) return;
                        HttpManager.getInstance().saveSysUser(JSON.toJSONString(sysUserBean));

                        List<RolesBean> roles = sysUserBean.getRoleList();
                        if (!CollectionsUtil.isEmpty(roles)) {
                            HttpManager.getInstance().saveRolesBean(JSON.toJSONString(roles.get(0)));
                        }
                        sysUser.setValue(sysUserDataBean);
                    }

                    @Override
                    protected void _onError(String message) {
                        ToastUtils.shortToast(message);
                    }
                });
    }
}
