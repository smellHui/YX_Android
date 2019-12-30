package com.tepia.guangdong_module.amainguangdong.mvp;

import android.content.Context;
import android.util.Log;

import com.tepia.base.common.DictMapResponse;
import com.tepia.base.http.LoadingSubject;
import com.tepia.base.mvp.BasePresenterImpl;
import com.tepia.base.utils.LogUtil;
import com.tepia.base.utils.ToastUtils;
import com.tepia.guangdong_module.amainguangdong.common.DictMapManager;
import com.tepia.guangdong_module.amainguangdong.common.UserLoginResponse;
import com.tepia.guangdong_module.amainguangdong.common.UserManager;
import com.tepia.guangdong_module.amainguangdong.model.JsonBean;
import com.tepia.guangdong_module.amainguangdong.model.UserInfoBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.ReservoirListResponse;
import com.tepia.guangdong_module.amainguangdong.xunchaview.fragment.TabMainFragmentFactory;

/**
 * Created by      android studio
 *
 * @author :       ly
 * Date            :       2019-04-15
 * Time            :       下午6:22
 * Version         :       1.0
 * location        :       武汉研发中心
 * 功能描述         :
 **/
public class GDLoginPresenter extends BasePresenterImpl<GDLoginContract.View> implements GDLoginContract.Presenter{
    @Override
    public void login(Context mContext, String username, String password, String registId, String deviceId) {

        UserManager.getInstance().login(username,password,registId,deviceId).subscribe(new LoadingSubject<UserLoginResponse>(true,"正在登录..."){
            @Override
            protected void _onNext(UserLoginResponse userLoginResponse) {
                if (userLoginResponse != null && mView != null) {
                    if (userLoginResponse.getCode() == 0) {
                        UserManager.getInstance().saveToken(userLoginResponse);
//                        getStandardInit();
//                        DictMapManager.getInstance().getDictMapEntity();

                        DictMapManager.getInstance().getDictMap().safeSubscribe(new LoadingSubject<DictMapResponse>(true, "正在获取数据字典...") {
                            @Override
                            protected void _onNext(DictMapResponse dictMapResponse) {
                                if (dictMapResponse.getCode() == 0) {
                                    LogUtil.e("获取数据字典getDictMapEntity---:成功");
                                    DictMapManager.getInstance().setmDictMap(dictMapResponse.getData());
                                    saveUserInfoBean();
                                } else {
                                    _onError(dictMapResponse.getMsg());
                                }
                            }

                            @Override
                            protected void _onError(String message) {
                                LogUtil.e("getDictMapEntity---:"+message);
                                ToastUtils.shortToast("获取数据字典失败"+message+" ");
                            }
                        });

                    }
                }
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.shortToast(message);
            }
        });
    }


    /**
     * 获取用户信息并保存
     */
    private void saveUserInfoBean() {
        UserManager.getInstance_ADMIN().getLoginUser().subscribe(new LoadingSubject<UserInfoBean>(true, "正在获取数据...") {
            @Override
            protected void _onNext(UserInfoBean userInfoBean) {
                if (userInfoBean != null) {
                    if (userInfoBean.getCode() == 0) {
                        LogUtil.e("getLoginUser", "getLoginUser:成功获取用户信息------");
                        UserManager.getInstance().setUserBean(userInfoBean);
                        String roleType = UserManager.getInstance().getRoleType();
                        if (roleType != null) {
                            // 巡查
                            if ("4".equals(roleType)) {
                                getReservoirList(false);
                            }
                            //行政
                            else{
                                mView.loginSuccess();
                            }
                        }

                    } else {
                        ToastUtils.longToast(userInfoBean.getMsg());

                    }
                }
            }

            @Override
            protected void _onError(String message) {
                LogUtil.e("getLoginUser:获取用户信息失败-----");


            }
        });
    }

    /**
     * 获取登录界面展示json
     */
    public void getJson() {
        UserManager.getInstance_Json().getJson().subscribe(new LoadingSubject<JsonBean>(true, "正在获取数据...") {
            @Override
            protected void _onNext(JsonBean jsonBean) {
                if (jsonBean != null) {
                    mView.jsonSuccess(jsonBean);
                }
            }

            @Override
            protected void _onError(String message) {
                Log.i("sss","kkkk");
            }
        });
    }

    /**
     * 获取负责的水库列表
     *
     * @return
     */
    private void getReservoirList(boolean loginAgain) {
        UserManager.getInstance_Works().getReservoirList().safeSubscribe(new LoadingSubject<ReservoirListResponse>(true,"正在获取水库列表...") {
            @Override
            protected void _onNext(ReservoirListResponse response) {
                if (response.getCode() == 0) {

                    UserManager.getInstance().saveReservoirList(response.getData());
                    mView.loginSuccess();
                    if (response.getData() != null && response.getData().size() > 0) {
                        UserManager.getInstance().saveDefaultReservoir(response.getData().get(0));
                    }


                }
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.shortToast(message);

            }
        });

    }


}
