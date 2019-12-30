package com.tepia.guangdong_module.amainguangdong.xunchaview.activity;

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.guangdong_module.R;
import com.tepia.base.mvp.BaseActivity;
import com.tepia.base.utils.NetUtil;
import com.tepia.base.utils.ToastUtils;
import com.tepia.base.utils.Utils;
/**
  * Created by      Android studio
  *
  * @author :ly (from Center Of Wuhan)
  * 创建时间 :2019-6-5
  * 更新时间 :
  * Version :1.0
  * 功能描述 :
 **/
public class VedioHtmlActivity extends BaseActivity {
    private ProgressBar progressBar1;
    private WebView wView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_vedio_html;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        setCenterTitle("水库视频");
        showBack();
        wView = findViewById(R.id.wv_real_time_water_level_storage_capacity);
        progressBar1 = findViewById(R.id.progressBar1);
        setView();
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initRequestData() {

    }

    private void setView(){
        if(!NetUtil.isNetworkConnected(Utils.getContext())){
            ToastUtils.shortToast(R.string.no_network);
        }
        String host = "http://10.44.46.235:7000/#/appReservoirVideo";

        WebSettings wSet = wView.getSettings();
        wSet.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wSet.setJavaScriptEnabled(true);
        // 关闭缓存
        wSet.setAppCacheEnabled(false);
        //设置 缓存模式 LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
        wSet.setCacheMode(WebSettings.LOAD_NO_CACHE);
        wSet.setLoadWithOverviewMode(true);
        wSet.setUseWideViewPort(true);
        //设定支持缩放
        wSet.setSupportZoom(true);
        // 设置出现缩放工具
        wSet.setBuiltInZoomControls(true);
        wSet.setDisplayZoomControls(false);

        //这行很关键
        wView.setWebChromeClient(new WebChromeClient());
        wView.setWebViewClient(new WebViewClient());

//        wView.loadUrl(APPCostant.WEB_COMPANY);
        wView.loadUrl(host);
        wView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress==100){
                    //加载完网页进度条消失
                    progressBar1.setVisibility(View.GONE);
                }
                else{
                    //开始加载网页时显示进度条
                    progressBar1.setVisibility(View.VISIBLE);
                    //设置进度值
                    progressBar1.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
    }

    private void a(){
        /*String host = "http://127.0.0.1:7000/#/appReservoirVideo?id=";

        ChromeClientCallbackManager.ReceivedTitleCallback mCallback = null;

        AgentWeb.with(this)
                .setAgentWebParent(mBinding.wvRealTimeWaterLevelStorageCapacity, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                .setIndicatorColorWithHeight(-1, 2)
                .setReceivedTitleCallback(mCallback) //设置 Web 页面的 title 回调
                .setSecurityType(AgentWeb.SecurityType.strict)
                .setAgentWebWebSettings(new AgentWebSettings() {
                    @Override
                    public AgentWebSettings toSetting(WebView web) {

                        //支持javascript
                        web.getSettings().setJavaScriptEnabled(true);
                        // 设置可以支持缩放
                        web.getSettings().setSupportZoom(true);
                        // 设置出现缩放工具
                        web.getSettings().setBuiltInZoomControls(true);
                        //扩大比例的缩放
                        web.getSettings().setUseWideViewPort(true);
                        //自适应屏幕
                        web.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                        web.getSettings().setLoadWithOverviewMode(true);
                        web.getSettings().setDisplayZoomControls(false);
                        return null;
                    }

                    @Override
                    public WebSettings getWebSettings() {
                        return null;
                    }
                })
                .createAgentWeb()
                .ready()
                .go(url);*/
    }
}
