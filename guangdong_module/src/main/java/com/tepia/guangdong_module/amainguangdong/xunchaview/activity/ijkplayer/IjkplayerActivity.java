package com.tepia.guangdong_module.amainguangdong.xunchaview.activity.ijkplayer;

import android.net.Uri;
import android.view.View;

import com.example.guangdong_module.R;
import com.tepia.base.mvp.BaseActivity;
import com.tepia.base.utils.LogUtil;
import com.tepia.base.utils.ToastUtils;
import com.tepia.base.view.dialog.loading.SimpleLoadDialog;
import com.tepia.guangdong_module.amainguangdong.xunchaview.activity.ijkplayer.view.media.IRenderView;
import com.tepia.guangdong_module.amainguangdong.xunchaview.activity.ijkplayer.view.media.IjkVideoView;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
  * Created by      Android studio
  *
  * @author :wwj (from Center Of Wuhan)
  * Date    :2019/6/5
  * Version :1.0
  * 功能描述 :  直播流播放
 **/
public class IjkplayerActivity extends BaseActivity {
    private IjkVideoView mVideoView;
//    String videoPath = "rtmp://rtmp01open.ys7.com/openlive/18ac488eda6e43a39bf9a51b69fe7000";

    String videoPath = "rtmp://rtmp01open.ys7.com/openlive/2ffbb2425d9d43e4954b0d555c08c266";

    @Override
    public int getLayoutId() {
        return R.layout.activity_ijkplayer;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        setCenterTitle("视频播放");
        showBack();
        mVideoView = findViewById(R.id.video_view);
        initRtmpVideo();
    }

    private void initRtmpVideo() {
        mVideoView.setAspectRatio(IRenderView.AR_ASPECT_FIT_PARENT);

        mVideoView.setVideoURI(Uri.parse(videoPath));
        mVideoView.start();
        SimpleLoadDialog simpleLoadDialog = new SimpleLoadDialog(this,"正在加载视频...",true);
        simpleLoadDialog.show();
        mVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                LogUtil.e("onPrepared-------");
                if (simpleLoadDialog != null) {
                    simpleLoadDialog.dismiss();
                }
            }
        });

        mVideoView.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
                if (simpleLoadDialog != null) {
                    simpleLoadDialog.dismiss();
                    ToastUtils.shortToast("视频无法播放");
                }
                return false;
            }
        });

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
    }
}
