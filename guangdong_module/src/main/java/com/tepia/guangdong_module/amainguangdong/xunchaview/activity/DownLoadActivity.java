package com.tepia.guangdong_module.amainguangdong.xunchaview.activity;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.utils.TextUtils;
import com.arialyy.annotations.Download;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.download.DownloadTarget;
import com.arialyy.aria.core.download.DownloadTask;
import com.arialyy.aria.util.CommonUtil;
import com.arialyy.frame.permission.OnPermissionCallback;
import com.arialyy.frame.permission.PermissionManager;
import com.arialyy.frame.util.show.T;
import com.example.guangdong_module.R;
import com.google.gson.Gson;
import com.tepia.base.mvp.BaseActivity;
import com.tepia.base.utils.LogUtil;
import com.tepia.base.utils.NetUtil;
import com.tepia.base.utils.SPUtils;
import com.tepia.base.utils.ToastUtils;
import com.tepia.base.view.HorizontalProgressBar.HorizontalProgressBarWithNumber;
import com.tepia.guangdong_module.amainguangdong.common.UserManager;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.ReservoirBean;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by      Android studio
 *
 * @author :wwj (from Center Of Wuhan)
 * Date    :2018/10/9
 * Version :1.0
 * 功能描述 :下载
 **/

public class DownLoadActivity extends BaseActivity {
    public static final int resultCode = 1002;
    public static String isKeyBack = "isKeyBack";
    private String downloadSpName = "downloadMap";
    private static String DOWNLOAD_URL = "http://rs.0.gaoshouyou.com/d/04/1e/400423a7551e1f3f0eb1812afa1f9b44.apk";
    String parentPath = Environment.getExternalStorageDirectory() + "/SKXCDownloads/tpk/";
    private String fileName="";
    private String filePath="";
    private TextView tvFileName;
    private TextView tvSpeed;
    private HorizontalProgressBarWithNumber progressBar;
    private ImageView ivStop;
    private TextView tvDownloadPreview;
    private DownloadTarget target;
    private boolean isLoadComplete;
    private SharedPreferences sharedPreferences;
    private Map<String, Boolean> downloadMap;
    private String downloadMapStr;
    private int downloadStatus;
    private ReservoirBean defaultReservoir;
    private String iFileName;
    private String iFilePath;
    private String reservoirCode;

    public static Intent setIntent(Intent intent, String fileName, String filePath) {
        intent.putExtra("fileName", fileName);
        intent.putExtra("filePath", filePath);
        return intent;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_download;
    }

    @Override
    public void initView() {
        TextView tvLeftText = findViewById(R.id.tv_left_text);
        tvLeftText.setVisibility(View.VISIBLE);
        tvLeftText.setOnClickListener(v -> {
            String spDownloadMapStr = SPUtils.getInstance().getString("downloadMap", "");
            if (null == spDownloadMapStr || "".equals(spDownloadMapStr)) {
                //没有下载数据  保存数据
                downloadMap.put(fileName, isLoadComplete);
            } else {
                //拿到保存的下载完成数据
                downloadMap = new Gson().fromJson(spDownloadMapStr, downloadMap.getClass());
                if (downloadMap != null && downloadMap.size() > 0) {
                    downloadMap.put(fileName, isLoadComplete);
                }
            }
            SPUtils.getInstance().putString(downloadSpName, new Gson().toJson(downloadMap));
            Intent intent = new Intent();
            intent.putExtra(isKeyBack, true);
            DownLoadActivity.this.setResult(resultCode, intent);
            finish();
        });
        if (defaultReservoir!=null){
            String reservoir = defaultReservoir.getReservoir();
            if (reservoir!=null&&reservoir.length()>0){
                setCenterTitle(reservoir+"离线地图包下载");
            }else {
                setCenterTitle("地图包下载");
            }
        }else {
            setCenterTitle("地图包下载");
        }
        showBack();
        Intent intent = getIntent();
        iFileName = intent.getStringExtra("fileName");
        iFilePath = intent.getStringExtra("filePath");
        reservoirCode = intent.getStringExtra("code");
        ImageView ivTitle = findViewById(R.id.iv_title);
//        this.fileName = "ggsg8.apk";
        tvFileName = findViewById(R.id.tv_file_name);
        tvSpeed = findViewById(R.id.tv_speed);
        progressBar = findViewById(R.id.progressBar);
        ivStop = findViewById(R.id.iv_stop);
        tvDownloadPreview = findViewById(R.id.tv_download_preview);
        ivTitle.setOnClickListener(v -> {
            initPermission();
        });
//        String tvSpeedString = "下载中...("+0+"/"+target.getConvertFileSize()+")";
//        tvSpeed.setText(tvSpeedString);
        ivStop.setOnClickListener(v -> {
            tvDownloadPreview.setVisibility(View.VISIBLE);
            //暂停
            Aria.download(DownLoadActivity.this).load(filePath).stop();
        });
        tvDownloadPreview.setOnClickListener(v -> {
            if (isLoadComplete) {
            } else {
                if (NetUtil.isNetworkConnected(getContext())){
                    tvDownloadPreview.setVisibility(View.GONE);
                    initPermission();
                }else {
                    ToastUtils.shortToast("网络未连接");
                }
            }
        });
        if (iFileName != null && iFilePath != null) {
            fileName = iFileName;
            filePath = iFilePath;
            tvFileName.setText(fileName);
            target = Aria.download(this).load(filePath);
            long fileSize = target.getFileSize();
//            // 删除原来的文件
            File file = new File(parentPath + "/" + iFileName);
            if (file.isFile()){
                file.delete();
            }

            initPermission();
            //如果文件不存在并且不是目录文件
          /*  if (!file.exists() && !file.isDirectory()) {
                //如果大小小于2b
//                LogUtil.i("filePath:" + filePath);
//                LogUtil.i("fileSize:" + fileSize);
                //                下载文件
                isLoadComplete = false;
                initPermission();
            } else {
                //文件存在
                String spDownloadStr = SPUtils.getInstance().getString(downloadSpName, "");
                if (null == spDownloadStr || "".equals(spDownloadStr)) {
                    //文件没有下载完全  开始下载
                    isLoadComplete = false;
                    initPermission();
                } else {
                    //是否下载完成
                    boolean isloadComplete = false;
                    downloadMap = new Gson().fromJson(spDownloadStr, downloadMap.getClass());
                    if (downloadMap != null && downloadMap.size() > 0) {
                        for (Map.Entry<String, Boolean> entry : downloadMap.entrySet()) {
                            String key = entry.getKey();
                            if (key.equals(fileName)) {
                                Boolean isLoadComplete = entry.getValue();
                                isloadComplete = isLoadComplete;
                            }
                        }
                    }
                    if (isloadComplete) {
                        //如果为ture 表示之前已经下载完成过
                        //文件已经下载
                        isLoadComplete = true;
                        tvDownloadPreview.setText("下载完成");
                        tvDownloadPreview.setVisibility(View.VISIBLE);
                        progressBar.setProgress(100);
                    } else {
                        //文件没有下载完全  开始下载
                        isLoadComplete = false;
                        initPermission();
                    }
                }
            }*/
        } else {
            isLoadComplete = false;
            ToastUtils.longToast("水库信息不完整，无法下载离线地图数据");
        }
    }

    private void initPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            downLoad();
        } else {  //6.0处理
            boolean hasPermission = PermissionManager.getInstance()
                    .checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasPermission) {
                downLoad();
            } else {
                PermissionManager.getInstance().requestPermission(this, new OnPermissionCallback() {
                    @Override
                    public void onSuccess(String... permissions) {
                        downLoad();
                    }

                    @Override
                    public void onFail(String... permissions) {
                        T.showShort(DownLoadActivity.this, "没有文件读写权限");
                    }
                }, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        }
    }

    private void downLoad() {
        new Thread(() -> checkDownloadUri(filePath)).start();
        File file = new File(Environment.getExternalStorageDirectory() + "SKXCDownloads/tpk");
        if (!file.exists() && !file.isDirectory()) {
            boolean mkdir = file.mkdir();
        }
        String path = Environment.getExternalStorageDirectory() + "/SKXCDownloads/tpk/" + fileName;
        Aria.download(DownLoadActivity.this)
                .load(filePath)
                .setFilePath(path)
                .start();
    }

    /**
     * 判断下载资源是否存在
     * @param filePath
     */
    private void checkDownloadUri(String filePath) {
        try {
            URL url = new URL(filePath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10*1000);
            int code = conn.getResponseCode();
            downloadStatus = code;
//            Log.i("code","code:"+code);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initData() {
        Aria.download(this).register();
        downloadMap = new HashMap();
        downloadMapStr = new Gson().toJson(downloadMap);
        Intent intent = getIntent();
        defaultReservoir = (ReservoirBean) intent.getSerializableExtra("bean");
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initRequestData() {

    }

    @Download.onWait
    void onWait(DownloadTask task) {
        if (task.getKey().equals(filePath)) {
//            Log.d(TAG, "wait ==> " + task.getDownloadEntity().getFileName());
            LogUtil.i("wait ==> " + task.getDownloadEntity().getFileName());
        }
    }

    @Download.onPre
    protected void onPre(DownloadTask task) {
        if (task.getKey().equals(filePath)) {
//            Log.d(TAG, "onPre ==> " + task.getDownloadEntity().getFileName());
            LogUtil.i("onPre ==> " + task.getDownloadEntity().getFileName());
            LogUtil.i("downloadStatus:"+downloadStatus);
            if (downloadStatus==404){
//                Aria.download(this).load(filePath).cancel(true);
                task.cancel();
                ToastUtils.longToast("离线地图数据制作中。请稍后重试。！");
            }
        }
    }

    @Download.onTaskStart
    void taskStart(DownloadTask task) {
        if (task.getKey().equals(filePath)) {
//            Log.d(TAG, "taskStart ==> " + task.getDownloadEntity().getFileName()+"文件大小:"+task.getDownloadEntity().getFileSize());
            LogUtil.i("taskStart ==> " + task.getDownloadEntity().getFileName() + "文件大小:" + task.getDownloadEntity().getFileSize());
            String tvSpeedString = "下载中...(" + 0 + "/" + target.getConvertFileSize() + ")";
            tvSpeed.setText(tvSpeedString);
        }
    }

    @Download.onTaskRunning
    protected void running(DownloadTask task) {
//        ALog.d(TAG, String.format("%s_running_%s", getClass().getName(), hashCode()));
        LogUtil.i(String.format("%s_running_%s", getClass().getName(), hashCode()));
        if (task.getKey().equals(filePath)) {
            //Log.d(TAG, task.getKey());
            long len = task.getFileSize();
            int percent = task.getPercent();
            long size = task.getFileSize() * percent / 100;
            String s = CommonUtil.formatFileSize(size);
//            Log.i(TAG,"已下载:"+s);
            LogUtil.i("已下载:" + s);
            if (len == 0) {
                progressBar.setProgress(0);
                String tvSpeedString = "下载中...(" + 0 + "/" + target.getConvertFileSize() + ")";
                tvSpeed.setText(tvSpeedString);
            } else {
                progressBar.setProgress(task.getPercent());
                String tvSpeedString = "下载中...(" + s + "/" + target.getConvertFileSize() + ")";
                tvSpeed.setText(tvSpeedString);
            }
        }
    }

    @Download.onTaskResume
    void taskResume(DownloadTask task) {
        if (task.getKey().equals(filePath)) {
//            ALog.d(TAG, String.format("暂停", getClass().getName(), hashCode()));
            LogUtil.i(String.format("暂停", getClass().getName(), hashCode()));
        }
    }

    @Download.onTaskStop
    void taskStop(DownloadTask task) {
        if (task.getKey().equals(filePath)) {
//            Log.d(TAG, "taskStop ==> " + task.getDownloadEntity().getFileName());
            LogUtil.i("taskStop ==> " + task.getDownloadEntity().getFileName());

        }
    }

    @Download.onTaskCancel
    void taskCancel(DownloadTask task) {
        if (task.getKey().equals(filePath)) {
//            Toast.makeText(DownLoadActivity.this, "取消下载", Toast.LENGTH_SHORT).show();
            LogUtil.i("cancel");
            progressBar.setProgress(0);
        }
    }

    @Download.onTaskFail
    void taskFail(DownloadTask task) {
        if (task.getKey().equals(filePath)) {
//            Log.d(TAG, "下载失败");
            LogUtil.i("下载失败");
//            Toast.makeText(DownLoadActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
            ToastUtils.shortToast("下载失败");
            tvDownloadPreview.setVisibility(View.VISIBLE);
            isLoadComplete= false;
        }
    }

    @Download.onTaskComplete
    void taskComplete(DownloadTask task) {
        if (task.getKey().equals(filePath)) {
            LogUtil.i("下载完成");
//            btOpenFile.setVisibility(View.VISIBLE);
            progressBar.setProgress(100);
//            Toast.makeText(DownLoadActivity.this, "下载完成"+"路径("+parentPath+")", Toast.LENGTH_SHORT).show();
            ToastUtils.longToast("下载完成"+"路径("+parentPath+")");
            tvDownloadPreview.setText("下载完成");
            tvDownloadPreview.setVisibility(View.VISIBLE);
            isLoadComplete = true;
            //拿到首选项中的map
            String spDownloadMapStr = SPUtils.getInstance().getString("downloadMap", "");
            if (null == spDownloadMapStr || "".equals(spDownloadMapStr)) {
                //没有下载数据  保存数据
                downloadMap.put(fileName, true);
            } else {
                //拿到保存的下载完成数据
                downloadMap = new Gson().fromJson(spDownloadMapStr, downloadMap.getClass());
                if (downloadMap != null && downloadMap.size() > 0) {
                    downloadMap.put(fileName, true);
                }
            }
            SPUtils.getInstance().putString(downloadSpName, new Gson().toJson(downloadMap));
            //保存数据库
            defaultReservoir.save();
//            L.d(TAG, "path ==> " + task.getDownloadEntity().getDownloadPath());
            LogUtil.i(task.getDownloadEntity().getDownloadPath());
//            L.d(TAG, "md5Code ==> " + CommonUtil.getFileMD5(new File(task.getDownloadPath())));
            LogUtil.i("md5Code ==> " + CommonUtil.getFileMD5(new File(task.getDownloadPath())));
//            L.d(TAG, "data ==> " + Aria.download(this).getDownloadEntity(DOWNLOAD_URL));
            LogUtil.i("data ==> " + Aria.download(this).getDownloadEntity(filePath));
            //Intent install = new Intent(Intent.ACTION_VIEW);
            //install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //File apkFile = new File(task.getDownloadPath());
            //install.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            //startActivity(install);
        }
    }

    @Download.onNoSupportBreakPoint
    public void onNoSupportBreakPoint(DownloadTask task) {
        if (task.getKey().equals(filePath)) {
//            Log.d(TAG, "该下载链接不支持断点");
            LogUtil.i("该下载链接不支持断点");
            T.showShort(DownLoadActivity.this, "该下载链接不支持断点");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Aria.download(this).unRegister();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        Aria.download(this).unRegister();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            String spDownloadMapStr = SPUtils.getInstance().getString("downloadMap", "");
            if (null == spDownloadMapStr || "".equals(spDownloadMapStr)) {
                //没有下载数据  保存数据
                downloadMap.put(fileName, isLoadComplete);
            } else {
                //拿到保存的下载完成数据
                downloadMap = new Gson().fromJson(spDownloadMapStr, downloadMap.getClass());
                if (downloadMap != null && downloadMap.size() > 0) {
                    downloadMap.put(fileName, isLoadComplete);
                }
            }
            SPUtils.getInstance().putString(downloadSpName, new Gson().toJson(downloadMap));
            Intent intent = new Intent();
            DownLoadActivity.this.setResult(resultCode, intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

