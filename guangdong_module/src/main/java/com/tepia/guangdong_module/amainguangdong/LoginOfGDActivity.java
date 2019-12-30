package com.tepia.guangdong_module.amainguangdong;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.guangdong_module.R;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.tepia.base.AppRoutePath;
import com.tepia.base.mvp.MVPBaseActivity;
import com.tepia.base.utils.DoubleClickUtil;
import com.tepia.guangdong_module.amainguangdong.model.JsonBean;
import com.tepia.photo_picker.utils.SPUtils;
import com.tepia.base.utils.ToastUtils;
import com.tepia.base.utils.Utils;
import com.tepia.base.CacheConsts;
import com.tepia.guangdong_module.amainguangdong.mvp.GDLoginContract;
import com.tepia.guangdong_module.amainguangdong.mvp.GDLoginPresenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.pgyersdk.update.UpdateManagerListener.startDownloadTask;


/**
 * 登录页
 * 邮箱 784787081@qq.com
 */
@Route(path = AppRoutePath.applogin_guangdong)
public class LoginOfGDActivity extends MVPBaseActivity<GDLoginContract.View, GDLoginPresenter> implements GDLoginContract.View ,View.OnClickListener{
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 200;
    private AppBean appBean;

    private EditText usernameEt;
    private EditText psEt;
    private TextView bt_login,tv_Content;

    private final int REQUEST_READPHONE_STATE_CODE = 100; // 请求权限码



    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login_guangdong;
    }

    @Override
    public void initView() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initLayout();
        TextView textView = findViewById(R.id.versionTv);
        textView.setText("当前版本号:"+getVersion());
    }

    @Override
    public void initData() {
//        UserManager.getInstance().clearCacheAndStopPush();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        PgyUpdateManager.unregister();
        super.onDestroy();
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initRequestData() {
        mPresenter.getJson();
    }

    @Override
    public void loginSuccess() {
        finish();
        ARouter.getInstance().build(AppRoutePath.appMainGuangdong).navigation();
    }

    @Override
    public void jsonSuccess(JsonBean jsonBean) {
        String str = jsonBean.getMessage();
            tv_Content.setText(Html.fromHtml(str));
    }

    private void initLayout(){
        usernameEt = findViewById(R.id.usernameEt);
        psEt = findViewById(R.id.psEt);
        bt_login = findViewById(R.id.bt_login);
        String userName = SPUtils.getInstance().getString(CacheConsts.USERNAME_LOGIN,"");
        usernameEt.setText(userName);
        tv_Content = findViewById(R.id.tv_Content);



        bt_login.setOnClickListener(view -> {
            if (DoubleClickUtil.isFastDoubleClick()) {
                return ;
            }
            loginOperation();

        });

    }


    private void loginOperation(){
        if (TextUtils.isEmpty(usernameEt.getText())) {
            ToastUtils.shortToast(R.string.hintstr);
            return;
        }

        if(TextUtils.isEmpty(psEt.getText())){
            ToastUtils.shortToast(R.string.hintps);
            return ;
        }
//            String registId = JPushInterface.getRegistrationID(Utils.getContext());
//            LogUtil.e("LoginActivity","获取极光推送注册："+registId);
        String userNameStr = usernameEt.getText().toString();
        userNameStr = userNameStr.replaceAll(" ","");

        String oldUserName = SPUtils.getInstance().getString(CacheConsts.USERNAME_LOGIN,"");

           /* if (!userNameStr.equals(oldUserName) && !TextUtils.isEmpty(oldUserName)) {
                new AlertDialog.Builder(getContext())
                        .setMessage("当前登录用户与上次登录用户名不一致")
                return;
            }*/
        SPUtils.getInstance().putString(CacheConsts.USERNAME_LOGIN,userNameStr);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] { Manifest.permission.READ_PHONE_STATE }, REQUEST_READPHONE_STATE_CODE);
            } else {
                // 获取系统imei号
                TelephonyManager tm = (TelephonyManager) getSystemService(Activity.TELEPHONY_SERVICE);
                String deviceId = null;
                if (tm != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        deviceId = tm.getImei();
                    } else {
                        deviceId = tm.getDeviceId();
                    }
//                    mPresenter.login(this, userNameStr, psEt.getText().toString(), "", "866642032607732"); // 866642032607733 866642032607732
                }
               /* if (!TextUtils.isEmpty(deviceId)){
                    mPresenter.login(this, userNameStr, psEt.getText().toString(), "", deviceId);
                }else {
                    ToastUtils.shortToast("手机设备号获取失败，无法登录");
                }*/
                mPresenter.login(this, userNameStr, psEt.getText().toString(), "", deviceId);
            }
        }else {
            TelephonyManager tm = (TelephonyManager) getSystemService(Activity.TELEPHONY_SERVICE);
            String deviceId = null;
            if (tm != null) {
                deviceId = tm.getDeviceId();
            }
            if (!TextUtils.isEmpty(deviceId)){
                mPresenter.login(this, userNameStr, psEt.getText().toString(), "", deviceId);
            }else {
                ToastUtils.shortToast("手机设备号获取失败，无法登录");
            }
        }


    }


    /**
     * 动态权限回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length >= 1) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startDownloadTask(LoginOfGDActivity.this, appBean.getDownloadURL());
                } else {
                    new AlertDialog.Builder(LoginOfGDActivity.this)
                            .setMessage("读写内存权限被拒绝,将导致定位功能无法正常使用，需要到设置页面手动授权")
                            .setPositiveButton("去授权", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //引导用户至设置页手动授权
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", Utils.getContext().getPackageName(), null);
                                    intent.setData(uri);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //引导用户手动授权，权限请求失败
                                    ToastUtils.longToast("拒绝了读写内存权限，将不能使用自动升级！！！");

                                }
                            }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            //引导用户手动授权，权限请求失败
                            ToastUtils.longToast("拒绝了读写内存权限，将不能使用自动升级！！！");

                        }
                    }).show();
                    // Permission Denied
                }
            }
            return;
        }else if (requestCode == REQUEST_READPHONE_STATE_CODE){ // 手机状态获取权限
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loginOperation();
            }else {
                new AlertDialog.Builder(LoginOfGDActivity.this)
                        .setMessage("获取手机状态权限被拒绝,将导致登录功能无法正常使用，需要到设置页面手动授权")
                        .setPositiveButton("去授权", (dialog, which) -> {
                            //引导用户至设置页手动授权
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", Utils.getContext().getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        })
                        .setNegativeButton("取消", (dialog, which) -> {
                            //引导用户手动授权，权限请求失败
                            ToastUtils.longToast("拒绝了获取手机状态权限，将不能登录！！！");

                        }).setOnCancelListener(dialog -> {
                            //引导用户手动授权，权限请求失败
                            ToastUtils.longToast("拒绝了获取手机状态权限，将不能使用自动升级！！！");

                        }).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onClick(View v) {

    }
}
