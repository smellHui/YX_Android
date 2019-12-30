package com.tepia.guangdong_module.amainguangdong.xunchaview.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.guangdong_module.R;
import com.tepia.base.AppRoutePath;
import com.tepia.base.http.LoadingSubject;
import com.tepia.base.mvp.BaseCommonFragment;
import com.tepia.base.utils.AppManager;
import com.tepia.base.utils.LogUtil;
import com.tepia.base.utils.NetUtil;
import com.tepia.base.utils.ToastUtils;
import com.tepia.guangdong_module.amainguangdong.LoginOfGDActivity;
import com.tepia.guangdong_module.amainguangdong.common.MySettingView;
import com.tepia.guangdong_module.amainguangdong.common.UserManager;
import com.tepia.guangdong_module.amainguangdong.model.UserInfoBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.ReservoirBean;
import com.tepia.guangdong_module.amainguangdong.xunchaview.activity.OfflineMapListActivity;
import com.tepia.guangdong_module.amainguangdong.xunchaview.activity.VersionActivity;

/**
 * Created by      Android studio
 *
 * @author :ly (from Center Of Wuhan)
 * 创建时间 :2019-4-28
 * 更新时间 :
 * Version :1.0
 * 功能描述 :
 **/
public class SettingFragment extends BaseCommonFragment implements View.OnClickListener {

    MySettingView loginOutMv;
    MySettingView setMv;
    MySettingView downMv;
    private MySettingView offlineMap;
    private ReservoirBean defaultReservoir;

    public SettingFragment() {
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void initData() {
        defaultReservoir = UserManager.getInstance().getDefaultReservoir();
    }

    @Override
    protected void initView(View view) {
        setCenterTitle("我的");
        userTv = findView(R.id.userTv);
        zhizeTv = findView(R.id.zhizeTv);

        loginOutMv = findView(R.id.loginOutMv);
        setMv = findView(R.id.settingMv);
        downMv = findView(R.id.downMv);
        //离线地图包测试
        offlineMap = findView(R.id.offlineMap);
        loginOutMv.setOnClickListener(this);
        setMv.setOnClickListener(this);
        downMv.setOnClickListener(this);
        offlineMap.setOnClickListener(this);
        setItem();
    }

    @Override
    protected void initRequestData() {

    }

    private void setItem() {
        /*personinfoMv.setTitle(getString(R.string.personinfostr));
        personinfoMv.setIvLeft(R.drawable.s_personinfo);
        personinfoMv.setVisibility(View.GONE);
        worknotificationMv.setTitle("工作通知");
        worknotificationMv.setIvLeft(R.drawable.s_tongzhi);
        peixunMv.setTitle(getString(R.string.peixunstr));
        peixunMv.setIvLeft(R.drawable.s_peixun);
        zhizeMvMv.setTitle(getString(R.string.zhizestr));
        zhizeMvMv.setIvLeft(R.drawable.s_zhize);


        msgMv.setTitle(getString(R.string.phonestr));
        msgMv.setIvLeft(R.drawable.s_msg);
        setMv.setTitle(getString(R.string.setstr));
        setMv.setIvLeft(R.drawable.s_set);
        mvVoiceAssistant.setTitle(getString(R.string.text_voice_assistant));
        mvVoiceAssistant.setIvLeft(R.drawable.s_yuyin);
        mvVoiceAssistant.setVisibility(View.GONE);*/
        setMv.setTitle("设置");
        setMv.setIvLeft(R.drawable.s_set);

        downMv.setTitle("离线数据包管理");
        downMv.setIvLeft(R.drawable.s_personinfo);

        loginOutMv.setTitle("退出登录");
        loginOutMv.setIvLeft(R.drawable.s_loginout);

        offlineMap.setTitle("离线地图包管理");
        offlineMap.setIvLeft(R.drawable.s_zhize);

        /*roleSwitchMv.setTitle("切换登录身份");
        roleSwitchMv.setIvLeft(R.drawable.p_switchrole);*/


    }

    /**
     * 获取用户信息并保存
     */
    private TextView userTv;
    private TextView zhizeTv;

    private void saveUserInfoBean() {
        UserManager.getInstance_ADMIN().getLoginUser().subscribe(new LoadingSubject<UserInfoBean>(false, "正在提交...") {
            @Override
            protected void _onNext(UserInfoBean userInfoBean) {
                if (userInfoBean != null) {
                    if (userInfoBean.getCode() == 0) {
                        LogUtil.e("getLoginUser", "getLoginUser:成功获取用户信息------");
                        UserManager.getInstance().setUserBean(userInfoBean);
                        String roleName = "";

                        if (TextUtils.isEmpty(roleName)) {
                            zhizeTv.setText(userInfoBean.getData().getOfficeName());

                        } else {
                            zhizeTv.setText(roleName);

                        }
                        userTv.setText(userInfoBean.getData().getUserName());

                    } else {
                        ToastUtils.longToast(userInfoBean.getMsg());

                    }
                }
            }

            @Override
            protected void _onError(String message) {
                UserInfoBean userInfoBean = UserManager.getInstance().getUserBean();
                if (userInfoBean != null) {
                    userTv.setText(userInfoBean.getData().getUserName());
                }
                LogUtil.e("getLoginUser:获取用户信息失败-----");


            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (NetUtil.isNetworkConnected(getBaseActivity())) {
            saveUserInfoBean();
        }
//        updateData();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        if (v.getId() == R.id.loginOutMv) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getBaseActivity());
            builder.setMessage(R.string.exit_message);
            builder.setCancelable(true);
            builder.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                    DataSupport.deleteAll(TaskBean.class);

                    UserManager.getInstance().clearCacheAndStopPush();
                    AppManager.getInstance().finishAll();
                    TabMainFragmentFactory.getInstance().clearFragment();
                    Intent intent = new Intent(getActivity(), LoginOfGDActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.create().show();

        } else if (v.getId() == R.id.settingMv) {
            intent = new Intent(getBaseActivity(), VersionActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.downMv) {
            ARouter.getInstance().build(AppRoutePath.app_select_reservor_downoffline)
                    .withString("offlineFlag", "10")
                    .navigation();

        } else if (v.getId() == R.id.offlineMap) {
            if (!NetUtil.isWifiConnected(getBaseActivity())) {
                new AlertDialog.Builder(getBaseActivity())
                        .setMessage("当前不再wifi环境下，将消耗移动流量，是否继续下载?")
                        .setPositiveButton("继续", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                download();
                                return;

                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                return;
            }
            download();

        }
    }

    private void download() {
        //离线地图包测试
       /* String reservoirCode = "";
        defaultReservoir = UserManager.getInstance().getDefaultReservoir();
        if (defaultReservoir!=null){
            reservoirCode = defaultReservoir.getReservoirCode();
        }
        Intent intent1 = new Intent(getActivity(), DownLoadActivity.class);
        String fileName = reservoirCode+".tpk";
        DownLoadActivity.setIntent(intent1, fileName, APPCostant.BASE_DOWN_MAP_URL + fileName);
        intent1.putExtra("code",reservoirCode);
        startActivity(intent1);*/
        startActivity(new Intent(getActivity(), OfflineMapListActivity.class));
    }
}
