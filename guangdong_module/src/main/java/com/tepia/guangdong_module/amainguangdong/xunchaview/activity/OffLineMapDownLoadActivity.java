package com.tepia.guangdong_module.amainguangdong.xunchaview.activity;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.arialyy.frame.util.FileUtil;
import com.example.guangdong_module.R;
import com.google.gson.Gson;
import com.ixuea.android.downloader.DownloadService;
import com.ixuea.android.downloader.callback.DownloadListener;
import com.ixuea.android.downloader.callback.DownloadManager;
import com.ixuea.android.downloader.domain.DownloadInfo;
import com.ixuea.android.downloader.exception.DownloadException;
import com.tepia.base.mvp.BaseActivity;
import com.tepia.base.utils.LogUtil;
import com.tepia.base.utils.NetUtil;
import com.tepia.base.utils.SPUtils;
import com.tepia.base.utils.ToastUtils;
import com.tepia.base.view.HorizontalProgressBar.HorizontalProgressBarWithNumber;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.ReservoirBean;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Android Studio
 *
 * @author : Arthur
 * Date :    2019/6/18
 * Time :    10:34
 * Describe : 离线地图下载
 */

public class OffLineMapDownLoadActivity extends BaseActivity implements View.OnClickListener {
    public static final int resultCode = 1002;
    public static String isKeyBack = "isKeyBack";
    private String downloadSpName = "downloadMap";
    /**
     * 地图包存储地址
     */
    private String parentPath = Environment.getExternalStorageDirectory() + "/SKXCDownloads/tpk/";
    /**
     * 地图包名
     */
    private String fileName = "";
    /**
     * 下载路径
     */
    private String filePath = "";
    /**
     * 下载进度
     */
    private TextView tvSpeed;
    /**
     * 下载进度条
     */
    private HorizontalProgressBarWithNumber progressBar;
    /**
     * 继续下载按钮
     */
    private TextView tvDownloadPreview;
    /**
     * 是否已完成下载
     */
    private boolean isLoadComplete;
    private Map<String, Boolean> downloadMap;
    /**
     * 水库对象
     */
    private ReservoirBean defaultReservoir;

    /**
     * 下载信息参数
     */
    private DownloadInfo downloadInfo;
    /**
     * 下载管理器
     */
    private DownloadManager downloadManager;

    /**
     * 处理网络检查线程
     */
    private Handler handler;

    /**
     * 检查网络线程
     */
    private Thread thread;

    @Override
    public int getLayoutId() {
        return R.layout.activity_download;
    }

    @Override
    public void initData() {
        downloadMap = new HashMap();
        fileName = getIntent().getStringExtra("fileName");
        filePath = getIntent().getStringExtra("filePath");
        defaultReservoir = (ReservoirBean) getIntent().getSerializableExtra("bean");

        if (defaultReservoir != null) {
            String reservoir = defaultReservoir.getReservoir();
            if (reservoir != null && reservoir.length() > 0) {
                setCenterTitle(reservoir + "离线地图包下载");
            } else {
                setCenterTitle("地图包下载");
            }
        } else {
            setCenterTitle("地图包下载");
        }
        // 初始化下载
        initDownLoad();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                switch (msg.what){
                    case 0:
                        int code = (int) msg.obj;
                        if (code != 200){
                            ToastUtils.shortToast("离线地图数据制作中。请稍后重试！");
                            tvDownloadPreview.setVisibility(View.VISIBLE);
                            isLoadComplete = false;
                        }else {
                            downloadManager.download(downloadInfo);
                        }
                        break;
                }
            }
        };

    }

    @Override
    public void initView() {
        // 顶部返回按钮
        TextView tvLeftText = findViewById(R.id.tv_left_text);
        tvLeftText.setVisibility(View.VISIBLE);
        tvLeftText.setOnClickListener(this);

        showBack();
        // 文件图标
        ImageView ivTitle = findViewById(R.id.iv_title);
        ivTitle.setOnClickListener(this);

        // 文件名
        TextView tvFileName = findViewById(R.id.tv_file_name);
        // 下载进度
        tvSpeed = findViewById(R.id.tv_speed);
        // 下载进度条
        progressBar = findViewById(R.id.progressBar);
        // 停止按钮
        ImageView ivStop = findViewById(R.id.iv_stop);
        ivStop.setOnClickListener(this);
        // 继续下载按钮
        tvDownloadPreview = findViewById(R.id.tv_download_preview);
        tvDownloadPreview.setOnClickListener(this);

        if (!TextUtils.isEmpty(fileName) && !TextUtils.isEmpty(filePath)) {
            tvFileName.setText(fileName);
        } else {
            isLoadComplete = false;
            ToastUtils.longToast("水库信息不完整，无法下载离线地图数据");
        }

        thread =  new Thread(() -> checkDownloadUri(filePath));
        thread.start();
    }

    /**
     * 初始化下载信息
     */
    private void initDownLoad() {
        /**
         * 初始化下载管理器
         */
        downloadManager = DownloadService.getDownloadManager(getApplicationContext());
        // 初始化下载信息
        downloadInfo = new DownloadInfo.Builder().setUrl(filePath).setPath(parentPath + "/" + fileName).build();
        // 添加下载回调接口
        downloadInfo.setDownloadListener(new DownloadListener() {

            @Override
            public void onStart() {
            }

            @Override
            public void onWaited() {
            }

            @Override
            public void onPaused() {
            }

            @Override
            public void onDownloading(long progress, long size) {
                int pg = getProgress(progress, size);
                progressBar.setProgress(pg);
                String tvSpeedString =
                        "下载中...(" + FileUtil.formatFileSize(progress) + "/" + FileUtil.formatFileSize(size) + ")";
                tvSpeed.setText(tvSpeedString);

            }

            @Override
            public void onRemoved() {
                LogUtil.i("cancel");
                downloadInfo = null;
            }

            @Override
            public void onDownloadSuccess() {
                LogUtil.i("下载完成");
                progressBar.setProgress(100);
                ToastUtils.longToast("下载完成" + "路径(" + parentPath + ")");
                tvDownloadPreview.setText("下载完成");
                tvDownloadPreview.setVisibility(View.VISIBLE);
                isLoadComplete = true;
                // 拿到首选项中的map
                String spDownloadMapStr = SPUtils.getInstance().getString("downloadMap", "");
                if (null == spDownloadMapStr || "".equals(spDownloadMapStr)) {
                    // 没有下载数据 保存数据
                    downloadMap.put(fileName, true);
                } else {
                    // 拿到保存的下载完成数据
                    downloadMap = new Gson().fromJson(spDownloadMapStr, downloadMap.getClass());
                    if (downloadMap != null && downloadMap.size() > 0) {
                        downloadMap.put(fileName, true);
                    }
                }
                SPUtils.getInstance().putString(downloadSpName, new Gson().toJson(downloadMap));
                // 保存数据库
                defaultReservoir.save();
            }

            @Override
            public void onDownloadFailed(DownloadException e) {
                e.printStackTrace();
                LogUtil.i("下载失败");
                ToastUtils.shortToast("下载失败");
                tvDownloadPreview.setVisibility(View.VISIBLE);
                isLoadComplete = false;
            }
        });
    }

    /**
     * 计算进度条百分比
     * @param size1 已下载
     * @param size2 总大小
     * @return
     */
    private int getProgress(double size1, double size2) {
        double db2 = size2 / 1024 / 1024 / 1024;
        double db1 = size1 / 1024 / 1024 / 1024;
        return (int) (db1 / db2 * 100);
    }

    /**
     * 通过网络检查下载资源是否可用
     * @param filePath
     */
    private void checkDownloadUri(String filePath) {
        try {
            URL url = new URL(filePath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10 * 1000);
            int code = conn.getResponseCode();
            Message msg = handler.obtainMessage();
            msg.what = 0;
            msg.obj = code;
            handler.sendMessage(msg);
        } catch (Exception e) {
            Message msg = handler.obtainMessage();
            msg.what = 0;
            msg.obj = 404;
            handler.sendMessage(msg);
            e.printStackTrace();
        }

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initRequestData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        downloadManager.onDestroy();
    }

    @Override
    protected void onPause(){
        super.onPause();
        downloadManager.pause(downloadInfo);
    }

    @Override
    protected void onStop() {
        super.onStop();
        downloadManager.pause(downloadInfo);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (downloadInfo.isPause()){
            downloadManager.resume(downloadInfo);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            saveAndQuit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        // 顶部返回按钮
        if (v.getId() == R.id.tv_left_text) {
            saveAndQuit();
        }
        // 文件图标
        else if (v.getId() == R.id.iv_title) {
            if (downloadInfo.isPause()) {
                downloadManager.resume(downloadInfo);
            }
        }
        // 继续下载按钮
        else if (v.getId() == R.id.tv_download_preview) {
            if (isLoadComplete) {
                ToastUtils.shortToast("下载已完成");
            } else {
                if (NetUtil.isNetworkConnected(getContext())) {
                    tvDownloadPreview.setVisibility(View.GONE);
                    if (downloadInfo.isPause()) {
                        downloadManager.resume(downloadInfo);
                    }
                } else {
                    ToastUtils.shortToast("网络未连接");
                }
            }
        }
        // 停止按钮
        else if (v.getId() == R.id.iv_stop) {
            tvDownloadPreview.setVisibility(View.VISIBLE);
            downloadManager.pause(downloadInfo);
        }
    }

    /**
     * 保存数据并退出
     */
    private void saveAndQuit() {
        String spDownloadMapStr = SPUtils.getInstance().getString("downloadMap", "");
        if (null == spDownloadMapStr || "".equals(spDownloadMapStr)) {
            // 没有下载数据 保存数据
            downloadMap.put(fileName, isLoadComplete);
        } else {
            // 拿到保存的下载完成数据
            downloadMap = new Gson().fromJson(spDownloadMapStr, downloadMap.getClass());
            if (downloadMap != null && downloadMap.size() > 0) {
                downloadMap.put(fileName, isLoadComplete);
            }
        }
        SPUtils.getInstance().putString(downloadSpName, new Gson().toJson(downloadMap));
        Intent intent = new Intent();
        intent.putExtra(isKeyBack, true);
        setResult(resultCode, intent);
        finish();
    }

}
