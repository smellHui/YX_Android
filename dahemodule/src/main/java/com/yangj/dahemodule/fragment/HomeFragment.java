package com.yangj.dahemodule.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSON;
import com.tepia.base.AppRoutePath;
import com.tepia.base.http.LoadingSubject;
import com.tepia.base.mvp.BaseCommonFragment;
import com.tepia.base.view.floatview.CollectionsUtil;
import com.tepia.guangdong_module.amainguangdong.common.UserManager;
import com.tepia.guangdong_module.amainguangdong.model.DangerousPosition;
import com.tepia.guangdong_module.amainguangdong.model.UserInfo;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.ReservoirBean;
import com.tepia.guangdong_module.amainguangdong.xunchaview.activity.DangerReportActivity;
import com.yangj.dahemodule.R;
import com.yangj.dahemodule.common.HttpManager;
import com.yangj.dahemodule.model.main.MainBean;
import com.yangj.dahemodule.model.main.MainDataBean;
import com.yangj.dahemodule.model.main.ReservoirInfo;
import com.yangj.dahemodule.model.main.Route;
import com.yangj.dahemodule.util.UiHelper;
import com.yangj.dahemodule.view.BasicInfoView;

import java.util.List;

/**
 * Author:xch
 * Date:2019/8/27
 * Description:首页
 */
public class HomeFragment extends BaseCommonFragment {

    private TextView btn_report;
    private BasicInfoView basicInfoView;

    private boolean isXC;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initData() {
        isXC = HttpManager.getInstance().isXC();
    }

    @Override
    protected void initView(View view) {
        basicInfoView = findView(R.id.view_basic_info);

        btn_report = findView(R.id.btn_report);
        btn_report.setVisibility(isXC ? View.VISIBLE : View.GONE);
        btn_report.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), DangerReportActivity.class));
        });

        findView(R.id.tv_weather).setOnClickListener(v -> {
//            ARouter.getInstance().build(AppRoutePath.app_weather_forecast).navigation();
            UiHelper.goToScanView(getBaseActivity());
        });
    }

    @Override
    protected void initRequestData() {
        loadData();
    }

    private void loadData() {
        HttpManager.getInstance().loadData("")
                .subscribe(new LoadingSubject<MainDataBean>() {

                    @Override
                    protected void _onNext(MainDataBean mainDataBean) {
                        MainBean mainBean = mainDataBean.getData();
                        if (mainBean == null) return;
                        List<Route> routeList = mainBean.getRouteList();
                        if (!CollectionsUtil.isEmpty(routeList)) {
                            HttpManager.getInstance().saveRoutes(JSON.toJSONString(routeList));
                        }
                        List<UserInfo> userInfos = mainBean.getUserList();
                        if (!CollectionsUtil.isEmpty(userInfos)) {
                            UserManager.getInstance().saveUserInfos(JSON.toJSONString(userInfos));
                        }
                        List<DangerousPosition> dangerousPositions = mainBean.getDangerousPositionList();
                        if (!CollectionsUtil.isEmpty(dangerousPositions)) {
                            UserManager.getInstance().saveDangerousPositions(JSON.toJSONString(dangerousPositions));
                        }
                        ReservoirInfo reservoirInfo = mainBean.getReservoirInfo();
                        if (reservoirInfo != null) {
                            ReservoirBean reservoirBean = new ReservoirBean();
                            reservoirBean.setReservoirId(reservoirInfo.getId());
                            reservoirBean.setReservoirCode(reservoirInfo.getCode());
                            reservoirBean.setReservoir(reservoirInfo.getName());
                            reservoirBean.setReservoirType(reservoirInfo.getDamType());
                            reservoirBean.setReservoirLongitude(reservoirInfo.getLongitude());
                            reservoirBean.setReservoirLatitude(reservoirInfo.getLatitude());
                            UserManager.getInstance().saveDefaultReservoir(reservoirBean);
                            basicInfoView.setData(reservoirInfo);
                        }
                    }

                    @Override
                    protected void _onError(String message) {

                    }
                });
    }
}
