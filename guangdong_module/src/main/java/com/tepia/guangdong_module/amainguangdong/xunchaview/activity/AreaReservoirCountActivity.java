package com.tepia.guangdong_module.amainguangdong.xunchaview.activity;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.CompoundButton;

import com.example.guangdong_module.R;
import com.example.guangdong_module.databinding.ActivityAreaReservoirCountBinding;
import com.tepia.base.mvp.MVPBaseActivity;
import com.tepia.base.utils.NetUtil;
import com.tepia.base.utils.Utils;
import com.tepia.base.view.floatview.CollectionsUtil;
import com.tepia.guangdong_module.amainguangdong.model.adminstatistics.RealTimeMonitorBean;
import com.tepia.guangdong_module.amainguangdong.model.adminstatistics.RealTimeMonitorResponse;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.AreaReservoirCountBean;
import com.tepia.guangdong_module.amainguangdong.mvp.reserviorlistmvp.ReservoirListContract;
import com.tepia.guangdong_module.amainguangdong.mvp.reserviorlistmvp.ReservoirListPresenter;
import com.tepia.guangdong_module.amainguangdong.utils.EmptyLayoutUtil;
import com.tepia.guangdong_module.amainguangdong.xunchaview.adapter.AdapterAreaReservior;
import com.tepia.guangdong_module.amainguangdong.xunchaview.adapter.AdapterAreaReserviorLine;
import com.tepia.guangdong_module.amainguangdong.xunchaview.adapter.AreaReservoirCountAdapter;

import java.util.ArrayList;
import java.util.List;

/**
  * Created by      Android studio
  *
  * @author :ly (from Center Of Wuhan)
  * 创建时间 :2019-6-3 18:00
  * 更新时间 :
  * Version :1.0
  * 功能描述 :行政统计-实时监测统计
 **/
public class AreaReservoirCountActivity extends MVPBaseActivity<ReservoirListContract.View, ReservoirListPresenter>
        implements ReservoirListContract.View<AreaReservoirCountBean> {

    ActivityAreaReservoirCountBinding binding;
//    AdapterAreaReservior adapterAreaReservior;
    AdapterAreaReserviorLine adapterAreaReserviorLine;
    List<AreaReservoirCountBean.DataBean> data = new ArrayList<>();
    /**
     * 水库监测数据
     */
    private String contentTimeMonitor;
    private RealTimeMonitorBean realTimeMonitorBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_area_reservoir_count;
    }

    @Override
    public void initData() {

        binding = DataBindingUtil.bind(mRootView);
        contentTimeMonitor =
                "<font color=\"#373737\">水库总数：</font><font color=\"#3951ad\">%1$s</font><font color=\"#373737\">宗　</font>"
                        + "<font color=\"#373737\">监测：</font><font color=\"#3951ad\">%2$s</font><font color=\"#373737\">宗　</font>"
                        + "<font color=\"#373737\">数据上报：</font><font color=\"#36b23\">%3$s</font><font color=\"#373737\">宗</font>";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.tongjiTv.setText(
                    Html.fromHtml(String.format(contentTimeMonitor, "--", "--", "--"), Html.FROM_HTML_MODE_LEGACY));
        } else {
            binding.tongjiTv.setText(Html.fromHtml(String.format(contentTimeMonitor, "--", "--", "--")));
        }

        // String content = getIntent().getStringExtra("title");
        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        // binding.tongjiTv.setText(Html.fromHtml(content,Html.FROM_HTML_MODE_LEGACY));
        //
        // }else {
        // binding.tongjiTv.setText(Html.fromHtml(content));
        // }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCenterTitle("水库监测情况统计");
        showBack();

        binding.areaRec.setLayoutManager(new LinearLayoutManager(this));
        binding.areaLineRec.setLayoutManager(new LinearLayoutManager(this));
//        adapterAreaReservior = new AdapterAreaReservior(R.layout.item_area_layout, data);
        adapterAreaReserviorLine = new AdapterAreaReserviorLine(R.layout.area_reservoir_list_layout, data);
//        binding.areaRec.setAdapter(adapterAreaReservior);
        binding.areaLineRec.setAdapter(adapterAreaReserviorLine);

        binding.areaLy.setVisibility(View.VISIBLE);
        binding.lineLy.setVisibility(View.GONE);
        binding.rightCheckB.setText("切换表格");

        mPresenter.getRealTimeMonitor(true, "正在获取数据...");

    }

    @Override
    public void initView() {

    }

    @Override
    protected void initListener() {
        binding.rightCheckB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.areaLy.setVisibility(View.GONE);
                    binding.lineLy.setVisibility(View.VISIBLE);
                    binding.rightCheckB.setText("切换图表");

                } else {
                    binding.areaLy.setVisibility(View.VISIBLE);
                    binding.lineLy.setVisibility(View.GONE);
                    binding.rightCheckB.setText("切换表格");

                }
            }
        });
    }

    @Override
    protected void initRequestData() {

    }

    @Override
    public void success(AreaReservoirCountBean areaReservoirCountBean) {
        AreaReservoirCountAdapter adapter = new AreaReservoirCountAdapter(this, areaReservoirCountBean.getData(), TextUtils.isEmpty(realTimeMonitorBean.getAllCount())?0:Integer.valueOf(realTimeMonitorBean.getAllCount()));
        binding.areaRec.setAdapter(adapter);

        List<AreaReservoirCountBean.DataBean> dataBeanList = areaReservoirCountBean.getData();
        data.clear();
        data.addAll(dataBeanList);
        int maxNum = 0;
        for (AreaReservoirCountBean.DataBean dataBean : dataBeanList) {
            int all = Integer.valueOf(dataBean.getAllCount());
            int monitor = dataBean.getMonitorCount();
            int report = dataBean.getReportCount();
            int max = Math.max(Math.max(monitor, report), all);
            if (max > maxNum) {
                maxNum = max;
            }
        }
        if (maxNum > 0) {
//            adapterAreaReservior.setUnitDp(maxNum);
        }
        if (CollectionsUtil.isEmpty(data)) {
//            adapterAreaReservior.setEmptyView(EmptyLayoutUtil.showNew("暂无数据"));
            adapterAreaReserviorLine.setEmptyView(EmptyLayoutUtil.showNew("暂无数据"));
        } else {
//            adapterAreaReservior.notifyDataSetChanged();
            adapterAreaReserviorLine.notifyDataSetChanged();
        }

    }

    @Override
    public void failure(String mes) {
//        adapterAreaReservior.setEmptyView(EmptyLayoutUtil.showNew(mes));
        adapterAreaReserviorLine.setEmptyView(EmptyLayoutUtil.showNew(mes));

    }

    @Override
    public void showRealTimeMonitor(RealTimeMonitorResponse data) {
        RealTimeMonitorBean bean = data.getData();
        if (bean != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                realTimeMonitorBean = bean;
                binding.tongjiTv.setText(Html.fromHtml(String.format(contentTimeMonitor, bean.getAllCount(),
                                bean.getMonitorCount(), bean.getReportCount()), Html.FROM_HTML_MODE_LEGACY));
            } else {
                binding.tongjiTv.setText(Html.fromHtml(String.format(contentTimeMonitor,
                        bean.getAllCount(), bean.getMonitorCount(), bean.getReportCount())));
            }
            // 实时监测统计数据
            mPresenter.getAreaReservoirCount();
        }
    }
}
