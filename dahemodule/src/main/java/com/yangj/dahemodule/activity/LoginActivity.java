package com.yangj.dahemodule.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.tepia.base.AppRoutePath;
import com.tepia.base.mvp.BaseActivity;
import com.yangj.dahemodule.R;
import com.yangj.dahemodule.viewmodel.LoginViewModel;

/**
 * Author:xch
 * Date:2019/8/29
 * Description:
 */
@Route(path = AppRoutePath.app_dahe_login)
public class LoginActivity extends BaseActivity {
    private EditText phoneEt;
    private EditText passwordEt;
    private Button loginBtn;
    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initData() {
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        loginViewModel.sysUser.observe(this, user -> {
            ARouter.getInstance().build(AppRoutePath.app_dahe_main).navigation();
        });
    }

    @Override
    public void initView() {
        phoneEt = findViewById(R.id.et_phone);
        passwordEt = findViewById(R.id.et_password);
        loginBtn = findViewById(R.id.btn_login);

        //技术责任人 13276223255  123456
        //巡查责任人  13961507642  123456
        /**
         * 管理员  ：admin
         * 水源地   巡查：sydxc   技术：sydjs
         * 安全      巡查：aqxc    技术：aqjs
         */
        phoneEt.setText("sydxc");
        passwordEt.setText("123456");
        loginBtn.setOnClickListener(v -> {
            String phone = phoneEt.getText().toString().trim();
            String password = passwordEt.getText().toString().trim();
            loginViewModel.login(phone, password);
        });
    }


    @Override
    protected void initListener() {


    }

    @Override
    protected void initRequestData() {

    }
}
