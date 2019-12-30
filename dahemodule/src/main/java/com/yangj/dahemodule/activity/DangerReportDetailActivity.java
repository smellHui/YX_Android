package com.yangj.dahemodule.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.tepia.base.http.BaseResponse;
import com.tepia.base.http.LoadingSubject;
import com.tepia.base.mvp.BaseActivity;
import com.tepia.base.utils.ToastUtils;
import com.tepia.photo_picker.PhotoPicker;
import com.yangj.dahemodule.R;
import com.yangj.dahemodule.common.HttpManager;
import com.yangj.dahemodule.model.report.ReportBean;
import com.yangj.dahemodule.model.report.ReportDetailDataBean;
import com.yangj.dahemodule.model.user.RolesBean;
import com.yangj.dahemodule.view.danger.CompleteView;
import com.yangj.dahemodule.view.danger.DetailView;
import com.yangj.dahemodule.view.danger.ReportedView;
import com.yangj.dahemodule.view.danger.ResponseView;
import com.yangj.dahemodule.wrap.DealFeedWrap;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author:xch
 * Date:2019/9/19
 * Description:
 */
public class DangerReportDetailActivity extends BaseActivity implements ResponseView.FeekBackListener {

    private DetailView detailView;
    private ReportedView reportedView;
    private ReportedView responseView;
    private ResponseView feedView;
    private CompleteView completeView;

    private String reportId;
    private boolean isXC;

    @Override
    public int getLayoutId() {
        return R.layout.activity_danger_report_detail;
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            reportId = intent.getStringExtra("reportId");
        }
        isXC = HttpManager.getInstance().isXC();
    }

    @Override
    public void initView() {
        setLeftTitle("险情反馈");
        showBack();
        detailView = findViewById(R.id.view_detail);
        reportedView = findViewById(R.id.view_report);
        responseView = findViewById(R.id.view_response);
        feedView = findViewById(R.id.view_feed);
        completeView = findViewById(R.id.view_complete);
        feedView.setFeekBackListener(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initRequestData() {
    }

    private void loadData() {
        HttpManager.getInstance().loadReportDetail(reportId)
                .subscribe(new LoadingSubject<ReportDetailDataBean>() {

                    @Override
                    protected void _onNext(ReportDetailDataBean reportDetailDataBean) {
                        if (reportDetailDataBean == null) return;
                        ReportBean reportBean = reportDetailDataBean.getData();
                        if (reportBean == null) return;
                        detailView.setDate(reportBean.getTitle(), reportBean.getCreateTime(), reportBean.getDescription());
                        List<String> reportImgs = convertImageData(reportBean.getPictures());
                        reportedView.setDate("已上报", reportBean.getCreatorName(), reportBean.getCreateTime(), "", reportImgs);
                        //已完结
                        if (reportBean.isComplete()) {
                            List<String> responseImgs = convertImageData(reportBean.getFeedbackPictures());
                            responseView.setDate("已反馈", reportBean.getUpdaterName(), reportBean.getUpdateTime(), reportBean.getFeedback(), responseImgs);
                            completeView.setData("已完结");
                            feedView.setVisibility(View.GONE);
                            responseView.setVisibility(View.VISIBLE);
                            completeView.setVisibility(View.VISIBLE);
                        } else {
                            if (isXC) {
                                completeView.setData("待反馈");
                                feedView.setVisibility(View.GONE);
                                responseView.setVisibility(View.GONE);
                                completeView.setVisibility(View.VISIBLE);
                            } else {
                                feedView.setVisibility(View.VISIBLE);
                                completeView.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    protected void _onError(String message) {

                    }
                });
    }

    private List<String> convertImageData(String pictures) {
        List<String> imgs = null;
        if (!TextUtils.isEmpty(pictures)) {
            String[] urls = pictures.split(",");
            imgs = Arrays.asList(urls);
        }
        return imgs;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (data != null) {
                ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                feedView.setData(photos);
            }
        }
    }

    @Override
    public void feekBackClick(String content, List<String> imgs) {
        HttpManager.getInstance().feekBackProblem(reportId, content, imgs)
                .subscribe(new LoadingSubject<BaseResponse>(true,"反馈中...") {

                    @Override
                    protected void _onNext(BaseResponse baseResponse) {
                        ToastUtils.shortToast("反馈成功");
                        EventBus.getDefault().post(new DealFeedWrap());
                        loadData();
                    }

                    @Override
                    protected void _onError(String message) {
                    }
                });
    }
}
