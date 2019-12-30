package com.tepia.guangdong_module.amainguangdong.xunchaview.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.guangdong_module.R;
import com.google.gson.Gson;
import com.tepia.base.CacheConsts;
import com.tepia.base.http.LoadingSubject;
import com.tepia.base.utils.LogUtil;
import com.tepia.base.utils.NetState;
import com.tepia.base.utils.TimeFormatUtils;
import com.tepia.base.utils.ToastUtils;
import com.tepia.base.utils.Utils;
import com.tepia.base.view.floatview.CollectionsUtil;
import com.tepia.guangdong_module.amainguangdong.common.UserManager;
import com.tepia.guangdong_module.amainguangdong.model.UserInfoBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.DataBeanOflistReservoirRoute;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.ReservoirBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.ReservoirOfflineResponse;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.RouteListBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.RoutePosition;
import com.tepia.guangdong_module.amainguangdong.route.TaskBean;
import com.tepia.guangdong_module.amainguangdong.route.TaskItemBean;
import com.tepia.photo_picker.utils.SPUtils;

import org.litepal.crud.DataSupport;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by      Android studio
 *
 * @author :wwj (from Center Of Wuhan)
 * Date    :2018/10/16
 * Version :1.0
 * 功能描述 :选择水库适配器
 **/

public class MySelectReservoirAdapter extends BaseQuickAdapter<ReservoirBean, BaseViewHolder> {
    private String currentTime;
//    private E
    private ScheduledExecutorService executorService;
    private String offlineFlag;


    public MySelectReservoirAdapter(int layoutResId, @Nullable List<ReservoirBean> data, String currentTime, String offlineFlag) {
        super(layoutResId, data);
        this.currentTime = currentTime;
        executorService = new ScheduledThreadPoolExecutor(10);
        this.offlineFlag = offlineFlag;

    }

    @Override
    protected void convert(BaseViewHolder helper, ReservoirBean item) {
        helper.setText(R.id.tv_reservoir_name, item.getReservoir());
//        TextView downTv = helper.getView(R.id.downTv);
//        downTv.setVisibility(View.GONE);


        TextView downTv = helper.getView(R.id.downTv);
        if (item == null || TextUtils.isEmpty(item.getReservoirId())) {
            return;
        }
        UserInfoBean userInfoBean = UserManager.getInstance().getUserBean();

        List<ReservoirBean> reservoirBeanList = DataSupport.where("reservoirId = ? and userCode=?", item.getReservoirId(),userInfoBean.getData().getUserCode()).find(ReservoirBean.class);
        if (!CollectionsUtil.isEmpty(reservoirBeanList)) {
            ReservoirBean reservoirBean = reservoirBeanList.get(0);
            String jsonAboutInfo = reservoirBean.getJsonAboutInfo();
            if (TextUtils.isEmpty(jsonAboutInfo)) {
                downTv.setTextColor(Color.parseColor("#333333"));
                downTv.setText("下载");
                downTv.setEnabled(true);
                downTv.setClickable(true);
                downTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        ToastUtils.shortToast("开始下载");
                        downTv.setText("正在下载");

                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                getAllReservoirData(item.getReservoirId(), item,downTv);

                            }
                        };
                        executorService.schedule(runnable, 500, TimeUnit.MILLISECONDS);

                    }
                });

            } else {
                if ("10".equals(offlineFlag)) {
                    helper.setText(R.id.tv_reservoir_name, reservoirBean.getReservoir()+ "("+reservoirBean.getUpdateTimeOfthisData()+")");
                    downTv.setTextColor(Color.parseColor("#333333"));
                    downTv.setText("更新");
                    downTv.setEnabled(true);
                    downTv.setClickable(true);


                    String updateTimeOfthisData = reservoirBean.getUpdateTimeOfthisData();
                    String days = TimeFormatUtils.getTwoDay(updateTimeOfthisData, currentTime);
                    LogUtil.e("----------updateTimeOfthisData"+updateTimeOfthisData+"       currentTime"+currentTime+"    days"+days);
                    if (!TextUtils.isEmpty(days)) {
                        //默认30天更新一次离线包信息
                        if (Integer.valueOf(days) >= 30) {
                            int net_state = NetState.getNetWorkStatus(Utils.getContext());
                            boolean strong_network = (net_state == NetState.NETWORK_CLASS_4_G || net_state == NetState.NETWORK_WIFI);
                            if (!strong_network) {
                                return;
                            }
                            //更新数据库
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    getAllReservoirData(item.getReservoirId(), item, downTv);

                                }
                            };
                            executorService.schedule(runnable, 500, TimeUnit.MILLISECONDS);
                        }
                    }

                    downTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            downTv.setText("正在下载");
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    getAllReservoirData(item.getReservoirId(), item,downTv);

                                }
                            };
                            executorService.schedule(runnable, 500, TimeUnit.MILLISECONDS);

                        }
                    });

                }else {
                    downTv.setTextColor(Color.parseColor("#C8C8C8"));
                    downTv.setText("已下载");
                    downTv.setEnabled(false);
                    downTv.setClickable(false);
                    String updateTimeOfthisData = reservoirBean.getUpdateTimeOfthisData();
                    String days = TimeFormatUtils.getTwoDay(updateTimeOfthisData, currentTime);
                    if (!TextUtils.isEmpty(days)) {
                        //默认30天更新一次离线包信息
                        if (Integer.valueOf(days) >= 30) {
                            int net_state = NetState.getNetWorkStatus(Utils.getContext());
                            boolean strong_network = (net_state == NetState.NETWORK_CLASS_4_G || net_state == NetState.NETWORK_WIFI);
                            if (!strong_network) {
                                return;
                            }
                            //更新数据库
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    getAllReservoirData(item.getReservoirId(), item, downTv);

                                }
                            };
                            executorService.schedule(runnable, 500, TimeUnit.MILLISECONDS);
                        }
                    }
                }

            }

        }



    }

    /**
     * 获取指定水库全部信息
     *
     * @param reservoirId
     * @param reservoirBean
     */
    public void getAllReservoirData(String reservoirId, ReservoirBean reservoirBean,TextView downTv) {


        UserManager.getInstance_Works().getAllReservoirData(reservoirId).safeSubscribe(new LoadingSubject<ReservoirOfflineResponse>(false, "正在获取水库列表...") {
            @Override
            protected void _onNext(ReservoirOfflineResponse reservoirOfflineResponse) {
                if (reservoirOfflineResponse.getCode() == 0) {

                    DataBeanOflistReservoirRoute dataBeanOflistReservoirRoute = reservoirOfflineResponse.getData();

                    dataBeanOflistReservoirRoute.saveOrUpdate();

                    reservoirBean.setJsonAboutInfo(new Gson().toJson(reservoirOfflineResponse));
                    reservoirBean.setUpdateTimeOfthisData(TimeFormatUtils.getUserDate("yyyy-MM-dd"));
                    UserInfoBean userInfoBean = UserManager.getInstance().getUserBean();
                    String usercode = userInfoBean.getData().getUserCode();
                    reservoirBean.setUserCode(usercode);
                    ReservoirBean reservoirBeanNew = DataSupport.where("reservoirId = ? and userCode=?", reservoirBean.getReservoirId(),usercode).findFirst(ReservoirBean.class);

                    if (reservoirBeanNew == null) {
                        reservoirBean.save();
                    }else{
                        reservoirBean.updateAll("reservoirid=? and usercode=?", reservoirBean.getReservoirId(),usercode);

                    }
                    downTv.setTextColor(Color.parseColor("#C8C8C8"));
                    downTv.setText("已下载");
                    downTv.setEnabled(false);
                    downTv.setClickable(false);

                    notifyDataSetChanged();

                }
            }

            @Override
            protected void _onError(String message) {
                if (reservoirBean != null) {
                    downTv.setTextColor(Color.parseColor("#333333"));
                    downTv.setText("重试");
                    downTv.setEnabled(true);
                    downTv.setClickable(true);
                    ToastUtils.shortToast(reservoirBean.getReservoir() + message);
                }
                LogUtil.e(MySelectReservoirAdapter.class.getName(),message + " ");

            }
        });
    }


}
