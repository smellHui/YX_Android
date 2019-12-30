package com.tepia.guangdong_module.amainguangdong.common;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.tepia.base.utils.LogUtil;
import com.tepia.base.utils.ToastUtils;
import com.tepia.base.utils.Utils;
import com.tepia.guangdong_module.amainguangdong.SplashOfGDActivity;


/**
 * @author  Liying
 * on 2017-3-8.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    public static final String TAG = "CrashHandler";
    private static CrashHandler INSTANCE = new CrashHandler();
    private Context mContext;

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    public void init(Context context) {
        //避免
        mContext = context;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        //throwEglException

        LogUtil.e(TAG, "some uncaughtException happend"+ex.getMessage());
        String mes = ex.getMessage();
        if (mes != null && mes.contains( "EGL_BAD_CONFIG")) {
            LogUtil.e(TAG, "该机型不支持Arcgis地图");

            ToastUtils.shortToast("该机型不支持Arcgis地图");
            new Thread() {
                @SuppressWarnings("WrongConstant")
                @Override
                public void run() {
                    ((Activity)mContext).finish();
                   /* Intent intent = new Intent(mContext, SplashOfGDActivity.class);
                    PendingIntent restartIntent = PendingIntent.getActivity(mContext, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
                    AlarmManager mgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                    mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, restartIntent);
                    android.os.Process.killProcess(android.os.Process.myPid());*/
                }
            }.start();
        }else if(mes != null && mes.contains("EGL_SUCCESS")){
            ToastUtils.shortToast("EGL_SUCCESS报错");
            new Thread(){
                @Override
                public void run() {
                    ((Activity)mContext).finish();

                }
            }.start();
        }

    }
}
