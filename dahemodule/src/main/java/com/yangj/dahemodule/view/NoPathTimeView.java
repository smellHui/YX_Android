package com.yangj.dahemodule.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yangj.dahemodule.R;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Author:xch
 * Date:2019/12/17
 * Description:无线路巡查计时view
 */
public class NoPathTimeView extends ViewBase {

    private TextView tv_time;
    private TextView tv_abnormal_num;
    private Button btn_end;
    private Button btn_upload;
    private LinearLayout ll_btn;

    private int secondNum;
    private DecimalFormat decimalFormat;
    private Timer timer;
    private QueryTimerTask queryTimerTask;

    private NoPathTimeListener noPathTimeListener;

    public void setNoPathTimeListener(NoPathTimeListener noPathTimeListener) {
        this.noPathTimeListener = noPathTimeListener;
    }

    public NoPathTimeView(Context context) {
        super(context);
    }

    public NoPathTimeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NoPathTimeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getViewId() {
        return R.layout.view_no_path_time;
    }

    @Override
    public void initData() {
        tv_time = findViewById(R.id.tv_time);
        tv_abnormal_num = findViewById(R.id.tv_abnormal_num);
        ll_btn = findViewById(R.id.ll_btn);
        btn_end = findViewById(R.id.btn_end);
        btn_upload = findViewById(R.id.btn_upload);
        btn_end.setOnClickListener(v -> {
            if (noPathTimeListener != null)
                noPathTimeListener.endAbnormalClick();
        });
        btn_upload.setOnClickListener(v -> {
            if (noPathTimeListener != null)
                noPathTimeListener.uploadAbnormalClick();
        });
        setAbNormalNum(0);
        setTimeTv(secondNum);
        timer = new Timer();
    }

    public void setBtnGone() {
        ll_btn.setVisibility(GONE);
    }

    public void setAbNormalNum(int num) {
        tv_abnormal_num.setTextColor(Color.parseColor(num == 0 ? "#000000" : "#E99E00"));
        tv_abnormal_num.setText(String.format("%d项", num));
    }

    public void setTimeTv(int time) {
        tv_time.setText(formatTime(time));
    }

    public void setEndBtnEnabled(boolean enabled) {
        btn_end.setEnabled(enabled);
    }

    public void startTime() {
        if (queryTimerTask != null) {
            closeTime();
        }
        queryTimerTask = new QueryTimerTask();
        timer.schedule(queryTimerTask, 1000, 1000);
    }

    public void closeTime() {
        timer.cancel();
        queryTimerTask = null;
    }

    public long getSecondNum() {
        return secondNum;
    }

    class QueryTimerTask extends TimerTask {

        @Override
        public void run() {
            tv_time.post(() -> setTimeTv(secondNum++));
        }
    }

    public interface NoPathTimeListener {
        void endAbnormalClick();

        void uploadAbnormalClick();
    }

    /**
     * 将秒转化为 HH:mm:ss 的格式
     *
     * @param time 秒
     * @return
     */
    private String formatTime(int time) {
        if(decimalFormat == null){
            decimalFormat = new DecimalFormat("00");
        }
        String hh = decimalFormat.format(time / 3600);
        String mm = decimalFormat.format(time % 3600 / 60);
        String ss = decimalFormat.format(time % 60);
        return hh + ":" + mm + ":" + ss;
    }

}
