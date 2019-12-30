package com.tepia.guangdong_module.amainguangdong.xunchaview.activity;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;

import com.example.guangdong_module.R;
import com.example.guangdong_module.databinding.ActivitySeepageAnomalousBinding;
import com.tepia.base.mvp.MVPBaseActivity;
import com.tepia.guangdong_module.amainguangdong.mvp.seepageanomalous.SeepageAnomalousContract;
import com.tepia.guangdong_module.amainguangdong.mvp.seepageanomalous.SeepageAnomalousPresenter;
import com.tepia.guangdong_module.amainguangdong.xunchaview.adapter.AdapterRainfallStatistics;
import com.tepia.guangdong_module.amainguangdong.xunchaview.adapter.AdapterSeepageAnomalous;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android Studio
 *
 * @author : Arthur
 * Date :    2019/6/5
 * Time :    15:37
 * Describe : 渗流异常界面
 */
public class SeepageAnomalousActivity extends MVPBaseActivity<SeepageAnomalousContract.View, SeepageAnomalousPresenter> implements SeepageAnomalousContract.View {

    ActivitySeepageAnomalousBinding mBinding;

    @Override
    public int getLayoutId() {
        return R.layout.activity_seepage_anomalous;
    }

    @Override
    public void initData() {
        mBinding = DataBindingUtil.bind(mRootView);
        mBinding = DataBindingUtil.bind(mRootView);
        mBinding.rvReservoirList.setLayoutManager(new LinearLayoutManager(this));
        List<String> list = new ArrayList<>();
        for (int i=0;i<15;i++){
            list.add("");
        }
        mBinding.rvReservoirList.setAdapter(new AdapterSeepageAnomalous(R.layout.lv_item_seepage_anomalous, list));
    }

    @Override
    public void initView() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initRequestData() {

    }
}
