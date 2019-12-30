package com.yangj.dahemodule.fragment;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.tepia.base.AppRoutePath;
import com.tepia.base.mvp.BaseCommonFragment;
import com.tepia.base.utils.AppManager;
import com.tepia.guangdong_module.amainguangdong.common.UserManager;
import com.yangj.dahemodule.R;
import com.yangj.dahemodule.activity.DangerReportListActivity;
import com.yangj.dahemodule.activity.MyDealActivity;
import com.yangj.dahemodule.activity.OperatesActivity;
import com.yangj.dahemodule.activity.OperatesForJsActivity;
import com.yangj.dahemodule.activity.VersionActivity;
import com.yangj.dahemodule.common.HttpManager;
import com.yangj.dahemodule.model.UserBean;
import com.yangj.dahemodule.model.user.RolesBean;
import com.yangj.dahemodule.model.user.SysUserBean;

/**
 * Author:xch
 * Date:2019/8/27
 * Description:我的
 */
public class MineFragment extends BaseCommonFragment {

    private TextView nameTv;
    private TextView dateTv;
    private TextView tv_deal;
    private String loginTime;

    private RolesBean role;
    private boolean isXC;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initData() {
        RolesBean role = HttpManager.getInstance().getRolesBean();
        isXC = role != null && role.isXC();

        UserBean userBean = HttpManager.getInstance().getUser();
        if (userBean != null) {
            loginTime = userBean.getLoginTime();
        }
    }

    @Override
    protected void initView(View view) {
        nameTv = findView(R.id.tv_name);
        dateTv = findView(R.id.tv_date);
        tv_deal = findView(R.id.tv_deal);
        tv_deal.setText(isXC ? "险情上报记录" : "险情处置记录");
        findView(R.id.ll_one).setOnClickListener(v -> {
            if (isXC) {
                startActivity(new Intent(getContext(), OperatesActivity.class));
            }else {
                startActivity(new Intent(getContext(), OperatesForJsActivity.class));
            }
        });
        findView(R.id.ll_two).setOnClickListener(v -> {
            if (isXC) {
                startActivity(new Intent(getContext(), DangerReportListActivity.class));
            } else {
                startActivity(new Intent(getContext(), MyDealActivity.class));
            }
        });
        findView(R.id.ll_three).setOnClickListener(v -> {
            startActivity(new Intent(getContext(), VersionActivity.class));
//            startActivity(new Intent(getContext(), PatrolMapControlActivity.class));
        });
        findView(R.id.ll_four).setOnClickListener(v -> {
            loginOutClick();
        });

        SysUserBean sysUser = HttpManager.getInstance().getSysUser();
        nameTv.setText(sysUser.getUsername());
        if (!TextUtils.isEmpty(loginTime)) {
            dateTv.setText(String.format("登录时间：%s", loginTime));
        }
    }

    @Override
    protected void initRequestData() {

    }

    private void loginOutClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getBaseActivity());
        builder.setMessage(com.example.guangdong_module.R.string.exit_message);
        builder.setCancelable(true);
        builder.setPositiveButton(com.example.guangdong_module.R.string.sure, (dialog, which) -> {
            UserManager.getInstance().clearCacheAndStopPush();
            ARouter.getInstance().build(AppRoutePath.app_dahe_login).navigation();
            AppManager.getInstance().finishAll();
        });
        builder.setNegativeButton(com.example.guangdong_module.R.string.cancel, (dialog, which) -> {

        });
        builder.create().show();
    }
}
