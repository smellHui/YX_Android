package com.tepia.guangdong_module.amainguangdong.xunchaview.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.guangdong_module.R;
import com.example.guangdong_module.databinding.FragmentMainNewBinding;
import com.google.gson.Gson;
import com.tepia.base.AppRoutePath;
import com.tepia.base.CacheConsts;
import com.tepia.base.ConfigConsts;
import com.tepia.base.common.DictMapEntity;
import com.tepia.base.http.BaseResponse;
import com.tepia.base.http.LoadingSubject;
import com.tepia.base.mvp.BaseCommonFragment;
import com.tepia.base.utils.DoubleClickUtil;
import com.tepia.base.utils.LogUtil;
import com.tepia.base.utils.ResUtils;
import com.tepia.base.utils.TimeFormatUtils;
import com.tepia.base.utils.ToastUtils;
import com.tepia.base.utils.UUIDUtil;
import com.tepia.base.utils.Utils;
import com.tepia.base.view.dialog.loading.SimpleLoadDialog;
import com.tepia.base.view.floatview.CollectionsUtil;
import com.tepia.guangdong_module.amainguangdong.common.DictMapManager;
import com.tepia.guangdong_module.amainguangdong.common.UserManager;
import com.tepia.guangdong_module.amainguangdong.common.WeatherDialogFragment;
import com.tepia.guangdong_module.amainguangdong.model.NewNoticeBean;
import com.tepia.guangdong_module.amainguangdong.model.UserInfoBean;
import com.tepia.guangdong_module.amainguangdong.model.UtilDataBaseOfGD;
import com.tepia.guangdong_module.amainguangdong.model.WeatherWarnBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.DangerBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.DataBeanOflistReservoirRoute;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.ReservoirBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.ReservoirOfflineResponse;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.RouteListBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.RoutePosition;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.SpecialNoticeBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.WaterPptnPictureBean;
import com.tepia.guangdong_module.amainguangdong.mvp.taskdetail.TaskManager;
import com.tepia.guangdong_module.amainguangdong.route.TaskBean;
import com.tepia.guangdong_module.amainguangdong.route.TaskItemBean;
import com.tepia.guangdong_module.amainguangdong.xunchaview.activity.ChangeReservoirActivity;
import com.tepia.guangdong_module.amainguangdong.xunchaview.activity.ChoiceReservoirActivity;
import com.tepia.guangdong_module.amainguangdong.xunchaview.activity.DangerReportActivity;
import com.tepia.guangdong_module.amainguangdong.xunchaview.activity.ImageDetailActivity;
import com.tepia.guangdong_module.amainguangdong.xunchaview.activity.RainDetailActivity;
import com.tepia.guangdong_module.amainguangdong.xunchaview.activity.StartInspectionActivity;
import com.tepia.guangdong_module.amainguangdong.xunchaview.activity.WaterRegimeDetailActivity;
import com.tepia.guangdong_module.amainguangdong.xunchaview.activity.ijkplayer.IjkplayerActivity;
import com.tepia.guangdong_module.amainguangdong.xunchaview.adapter.ImageShowAdapter;
import com.tepia.guangdong_module.amainguangdong.xunchaview.adapter.MySelectReservoirAdapter;
import com.tepia.photo_picker.PhotoPreview;
import com.tepia.photo_picker.entity.CheckTaskPicturesBean;
import com.tepia.photo_picker.utils.SPUtils;

import org.litepal.crud.DataSupport;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Created by      Android studio
 *
 * @author :ly (from Center Of Wuhan)
 * 创建时间 :2019-4-28
 * 更新时间 :
 * Version :1.0
 * 功能描述 :
 **/
public class MainFragment extends BaseCommonFragment {


    FragmentMainNewBinding binding;
    private ImageShowAdapter imageShowAdapter;
    private ArrayList<String> images = new ArrayList<>();
    private ReservoirBean selectedResrvoir;
    private String userCode;

    DataBeanOflistReservoirRoute offlineDataBean;
    String contentOfWeather;


    public MainFragment() {
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_new;
    }

    @Override
    protected void initData() {
        binding = DataBindingUtil.bind(mRootView);
        selectedResrvoir = UserManager.getInstance().getDefaultReservoir();
        String reservoirId = selectedResrvoir.getReservoirId();
        SPUtils.getInstance().putString(CacheConsts.reservoirId, reservoirId);
        //默认开启
//        SPUtils.getInstance().putBoolean(CacheConsts.OFFLINE, true);
        UserInfoBean userInfoBean = UserManager.getInstance().getUserBean();
        userCode = userInfoBean.getData().getUserCode();
        //获取水库最新通知
//        UserManager.getInstance_ADMIN().getNewNotice(reservoirId).subscribe(new LoadingSubject<NewNoticeBean>());

    }

    @Override
    protected void initView(View view) {
        setCenterTitle(getString(R.string.title_home));
        getRightTianqi().setVisibility(View.VISIBLE);

        binding.loHeader.tvReservoirName.setText(selectedResrvoir.getReservoir());
        DictMapEntity dictMapEntity = DictMapManager.getInstance().getmDictMap();
        if (dictMapEntity != null) {

            Map<String, String> map_Reservoirtype = dictMapEntity.getObject().getReservoir_type();
            Map<String, String> map_Damtype = dictMapEntity.getObject().getDam_type();
            //identifyType 坝等级    drainMethod  泄水方式

            Map<String, String> map_identifyType = dictMapEntity.getObject().getIdentifyType();
            Map<String, String> map_drainMethod = dictMapEntity.getObject().getDrainMethod();
            String waterLeveVolume = "--";
            String damHeight = "--";


            StringBuilder stringBuilder = new StringBuilder();
            if (map_identifyType != null && map_drainMethod != null) {
                stringBuilder.append(map_Reservoirtype.get(selectedResrvoir.getReservoirType()) + "  " +
                        map_Damtype.get(selectedResrvoir.getDamType()) + "  " +
                        map_identifyType.get(selectedResrvoir.getIdentifyType()) + "  " +
                        map_drainMethod.get(selectedResrvoir.getDrainMethod()));
                waterLeveVolume = selectedResrvoir.getWaterLevelVolume();
                if (!TextUtils.isEmpty(waterLeveVolume) && !"0".equals(waterLeveVolume)) {
                    LogUtil.e("库容："+waterLeveVolume);
                    stringBuilder.append("\n库容："+waterLeveVolume +"万m³"+"  ");
                }

                damHeight = selectedResrvoir.getDamHeight();
                if (!TextUtils.isEmpty(damHeight) && !"0".equals(damHeight)) {
                    LogUtil.e("坝高："+damHeight);
                    stringBuilder.append("\n坝高：" + damHeight +"m");

                }

                binding.loHeader.reserviorTypeTv.setText(
                        stringBuilder.toString());
            }else{
                DictMapManager.getInstance().getDictMapEntity();
                stringBuilder.append(map_Reservoirtype.get(selectedResrvoir.getReservoirType()) + "  " +
                        map_Damtype.get(selectedResrvoir.getDamType()));

                waterLeveVolume = selectedResrvoir.getWaterLevelVolume();
                if (!TextUtils.isEmpty(waterLeveVolume) && !"0".equals(waterLeveVolume)) {
                    LogUtil.e("库容："+waterLeveVolume);
                    stringBuilder.append("\n库容："+waterLeveVolume +"万m³"+"  ");
                }

                damHeight = selectedResrvoir.getDamHeight();
                if (!TextUtils.isEmpty(damHeight) && !"0".equals(damHeight)) {
                    LogUtil.e("坝高："+damHeight);
                    stringBuilder.append("\n坝高：" + damHeight +"m");

                }

                binding.loHeader.reserviorTypeTv.setText(stringBuilder.toString());
            }
        }


//        binding.layoutImg.imgRootLy.setVisibility(View.GONE);

        initRouterRecycle();
        initListen();
//        specialNotice();

        getReservoirBean(selectedResrvoir,true);
        binding.tianqiLy.setVisibility(View.GONE);
        binding.wuTv.setVisibility(View.VISIBLE);


    }

    /**
     * 获取天气预警
     */
    private void getWeatherWarn() {
        UserManager.getInstance_Monitor().getWeatherWarn().subscribe(new LoadingSubject<WeatherWarnBean>() {
            @Override
            protected void _onNext(WeatherWarnBean weatherWarnBean) {
                //2019/5/10日晚测试没有预警
                if (weatherWarnBean.getCode() == 0) {
                    if (weatherWarnBean != null && weatherWarnBean.getData() != null) {
                        binding.tianqiLy.setVisibility(View.VISIBLE);
                        binding.wuTv.setVisibility(View.GONE);

                        WeatherWarnBean.DataBean dataBean = weatherWarnBean.getData();
                        contentOfWeather = dataBean.getCategoryCnname()+dataBean.getDepartmentlevelCnname();
                        binding.tianqiTv.setText(dataBean.getContent());
                        binding.tianqiTimeTv.setText("发布时间："+ dataBean.getPublishdate());
                        if (!TextUtils.isEmpty(dataBean.getIcon())) {
                            int identifier = getResources().getIdentifier("a"+dataBean.getIcon(), "mipmap", "com.tepia.reservoirxuncha");
                            if (identifier != 0) {
                                binding.tianqiOfpicIv.setImageResource(identifier);
                            }else{
                                binding.tianqiOfpicIv.setImageResource(R.mipmap.a11);
                            }
                        }else{
                            binding.tianqiOfpicIv.setImageResource(R.mipmap.a11);

                        }
                        if (dataBean != null) {
                            setClickOftianqi(dataBean);
                        }


                    }else {
                        binding.tianqiLy.setVisibility(View.GONE);
                        binding.wuTv.setVisibility(View.VISIBLE);


                    }
                }else{
                    binding.tianqiLy.setVisibility(View.GONE);
                    binding.wuTv.setVisibility(View.VISIBLE);

                }

            }

            @Override
            protected void _onError(String message) {
                binding.tianqiLy.setVisibility(View.GONE);
                binding.wuTv.setVisibility(View.VISIBLE);
                LogUtil.e(MainFragment.class.getName(),message+" 天气");
                contentOfWeather = "";
            }
        });
    }

    /**
     * 设置天气点击事件
     * @param dataBean
     */
    private void setClickOftianqi(WeatherWarnBean.DataBean dataBean){
        binding.tianqiLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DoubleClickUtil.isFastDoubleClick()) {
                    return;
                }
                WeatherDialogFragment weatherDialogFragment = new WeatherDialogFragment();
                weatherDialogFragment.dataBean = dataBean;
                weatherDialogFragment.setListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (DoubleClickUtil.isFastDoubleClick()) {
                            return;
                        }
                        weatherDialogFragment.dismiss();



                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (DoubleClickUtil.isFastDoubleClick()) {
                            return;
                        }
                        weatherDialogFragment.dismiss();
                    }
                });
                weatherDialogFragment.show(getChildFragmentManager(), "cb");
            }
        });
    }


    private int selectPostion;
    private void initRouterRecycle() {
      /*  for (int i = 0; i < 10; i++) {
            images.add("http://tepia-skgj.oss-cn-beijing.aliyuncs.com/PC/reservoirDevice/2018-11/23/IMG_20181121_120228.jpg");
        }*/
        //创建LinearLayoutManager
        LinearLayoutManager manager = new LinearLayoutManager(getBaseActivity());
        //设置为横向滑动
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        //设置
        binding.layoutRoute.routeRec.setLayoutManager(manager);
        imageShowAdapter = new ImageShowAdapter(getBaseActivity(), R.layout.activity_pei_xun_item, images);
        imageShowAdapter.show(0);
        selectPostion = 0;
        binding.layoutRoute.routeRec.setAdapter(imageShowAdapter);
        imageShowAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                selectPostion = position;
                imageShowAdapter.show(position);
                imageShowAdapter.notifyDataSetChanged();
            }
        });
        imageShowAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

                ArrayList<String> imalist = new ArrayList<>();

                for(String photopathstr:images){
                    if (!TextUtils.isEmpty(photopathstr)) {
                        int index = photopathstr.indexOf(",");
                        String filePath = photopathstr.substring(index+1,photopathstr.length());
                        imalist.add(filePath);
                    }
                }

                PhotoPreview.builder()
                        .setPhotos(imalist)
                        .setCurrentItem(position)
                        .setShowDeleteButton(false)
                        .start(getBaseActivity(),MainFragment.this,0);
                return false;
            }
        });

        binding.layoutImg.vedioTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getBaseActivity(), IjkplayerActivity.class);
                intent.putExtra("videoPath", "rtmp://rtmp01open.ys7.com/openlive/18ac488eda6e43a39bf9a51b69fe7000");
                startActivity(intent);
            }
        });


    }


    @Override
    protected void initRequestData() {


    }

    /**
     * 获取水雨情
     */
    private void getWater() {
        TaskManager.getInstance().newWaterPptnPicture(selectedResrvoir.getReservoirId())
                .subscribe(new LoadingSubject<WaterPptnPictureBean>(false, "") {
                    @Override
                    protected void _onNext(WaterPptnPictureBean waterPptnPictureBean) {
                        if (waterPptnPictureBean != null) {
                            if (waterPptnPictureBean.getCode() == 0) {
                                WaterPptnPictureBean.DataBean dataBean = waterPptnPictureBean.getData();
                                setWaterAndRain(dataBean);
                                binding.currentTimeTv.setVisibility(View.VISIBLE);
                                binding.currentTimeTv.setText("以下数据时间:" + TimeFormatUtils.getStringDateShort() + "");
                            }
                        }
                    }

                    @Override
                    protected void _onError(String message) {
                        LogUtil.e("获取水雨情："+message);
                        clearView();
                    }
                });
    }

    private void clearView() {
        binding.currentTimeTv.setVisibility(View.GONE);
        binding.layoutWaterlevel.currentTv.setText("--");
        binding.layoutWaterlevel.xunxianTv.setText("--");
        binding.layoutWaterlevel.juxunxianTv.setText("--");
        binding.layoutWaterlevel.baDingGaoChengTv.setText("--");
        binding.layoutRain.oneTv.setText("--");
        binding.layoutRain.sixTv.setText("--");
//        binding.layoutImg.imgRootLy.setVisibility(View.GONE);

    }

    private void setWaterAndRain(WaterPptnPictureBean.DataBean dataBean) {

        SPUtils.getInstance().remove(CacheConsts.tianqiAndwaterlevel);
        clearView();
        if (dataBean != null) {
            StringBuilder stringBuilder = new StringBuilder();

            String rz = dataBean.getRz();
            String limitWaterLevel = dataBean.getLimitWaterLevel();
            if (TextUtils.isEmpty(rz)) {
                binding.layoutWaterlevel.currentIv.setVisibility(View.GONE);
                binding.layoutWaterlevel.currentTv.setText("--");
            } else {
                binding.layoutWaterlevel.currentTv.setText(rz + "m");
            }
            stringBuilder.append("当前水库水位"+binding.layoutWaterlevel.currentTv.getText().toString()+",");


            if (TextUtils.isEmpty(limitWaterLevel)) {
                binding.layoutWaterlevel.xunxianTv.setText("--");
            } else {
                binding.layoutWaterlevel.xunxianTv.setText(limitWaterLevel + "m");

            }


            if (!TextUtils.isEmpty(rz) && !TextUtils.isEmpty(limitWaterLevel)) {
                binding.layoutWaterlevel.currentIv.setVisibility(View.VISIBLE);
                try {

                if (Double.valueOf(rz) < Double.valueOf(limitWaterLevel)) {
                    binding.layoutWaterlevel.currentTv.setTextColor(ResUtils.getColor(R.color.color_green));
                    binding.layoutWaterlevel.currentIv.setImageResource(R.drawable.home_icon_down);
                    binding.layoutWaterlevel.desXunxian.setText("距汛限水位:");
                } else {
                    binding.layoutWaterlevel.currentTv.setTextColor(ResUtils.getColor(R.color.color_red));
                    binding.layoutWaterlevel.currentIv.setImageResource(R.drawable.home_icon_up);
                    binding.layoutWaterlevel.desXunxian.setText("超汛限水位:");

                }

                String s = "--";
                BigDecimal bigDecimalOfrz = new BigDecimal(rz);
                BigDecimal bigDecimalOflimitWaterLevel = new BigDecimal(limitWaterLevel);

                if (bigDecimalOfrz != null && bigDecimalOflimitWaterLevel != null) {
                    s = bigDecimalOfrz.subtract(bigDecimalOflimitWaterLevel).setScale(2).abs().toString();
                }

                binding.layoutWaterlevel.juxunxianTv.setText(s + "m");
                }catch (Exception e){
                     LogUtil.e(MainFragment.class.getName(),e.getMessage());
                }
                stringBuilder.append(binding.layoutWaterlevel.desXunxian.getText()+binding.layoutWaterlevel.juxunxianTv.getText().toString()+",");

            } else {
                binding.layoutWaterlevel.desXunxian.setText("距汛限水位:");
                binding.layoutWaterlevel.juxunxianTv.setText("--");
            }

            String damElevation = dataBean.getDamElevation();
            if (!TextUtils.isEmpty(damElevation)) {
                binding.layoutWaterlevel.baDingGaoChengTv.setText(damElevation + "m");
            } else {
                binding.layoutWaterlevel.baDingGaoChengTv.setText("--");

            }
            stringBuilder.append("坝顶高程"+binding.layoutWaterlevel.baDingGaoChengTv.getText()+".");


            String oneDrop = dataBean.getOneHourDrp();
            String sixDrop = dataBean.getSixHourDrp();
            if (TextUtils.isEmpty(oneDrop)) {
                binding.layoutRain.oneTv.setText("--");
            } else {
                binding.layoutRain.oneTv.setText(oneDrop + "mm");
            }
            stringBuilder.append("前一小时降雨量"+ binding.layoutRain.oneTv.getText()+",");

            if (TextUtils.isEmpty(sixDrop)) {
                binding.layoutRain.sixTv.setText("--");
            } else {
                binding.layoutRain.sixTv.setText(sixDrop + "mm");
            }
            stringBuilder.append("前6小时降雨量"+ binding.layoutRain.sixTv.getText()+".");
            if (!TextUtils.isEmpty(contentOfWeather)) {
                stringBuilder.append("气象台已发布"+contentOfWeather+"预警.");

            }


            SPUtils.getInstance().putString(CacheConsts.tianqiAndwaterlevel,stringBuilder.toString());
            String picPath = dataBean.getPicPath();
            if (!TextUtils.isEmpty(picPath)) {
                binding.layoutImg.imgRootLy.setVisibility(View.VISIBLE);
                Glide.with(Utils.getContext())
                        .load(picPath)
                        .apply(ConfigConsts.options)
                        .into(binding.layoutImg.waterPicIv);

                ArrayList<String> imalist = new ArrayList<>();
                imalist.add(picPath);
                binding.layoutImg.waterPicIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PhotoPreview.builder()
                                .setPhotos(imalist)
                                .setShowDeleteButton(false)
                                .setCurrentItem(0)
                                .start(getBaseActivity(),MainFragment.this,0);
                    }
                });
            }else{
                binding.layoutImg.waterPicIv.setImageResource(R.mipmap.icon_empty);
            }

        }
    }

    SimpleLoadDialog simpleLoadDialog;
    private void initListen() {
        binding.loHeader.switchTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (DoubleClickUtil.isFastDoubleClick()) {
                    return;
                }
                startActivityForResult(new Intent(getActivity(), ChangeReservoirActivity.class), ChangeReservoirActivity.resultCode);

            }
        });

        binding.loHeader.dangerIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getBaseActivity(), DangerReportActivity.class);
                startActivity(intent);
            }
        });

        binding.layoutRoute.startCheckTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DoubleClickUtil.isFastDoubleClick()) {
                    return;
                }

              /*  List<SpecialNoticeBean> specialNoticeBeanList = DataSupport.where("completeStatus=? and reservoirId=?","0",selectedResrvoir.getReservoirId()).find(SpecialNoticeBean.class);
                if (!CollectionsUtil.isEmpty(specialNoticeBeanList)) {
                    SpecialNoticeBean specialNoticeBean = specialNoticeBeanList.get(0);
                    if (specialNoticeBeanList != null) {
                        if ("0".equals(specialNoticeBean.getCompleteStatus())) {
                            startCheck(specialNoticeBean.getNoticeType(),specialNoticeBean);
                        }
                    }
                }else {
                    startCheck("",null);
                }*/

                startCheck("",null);

            }
        });

        binding.layoutSpecial.specialStartCheckTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DoubleClickUtil.isFastDoubleClick()) {
                    return;
                }
                List<SpecialNoticeBean> specialNoticeBeanList = DataSupport.where("completeStatus=? and reservoirId=?","0",selectedResrvoir.getReservoirId()).find(SpecialNoticeBean.class);
                if (!CollectionsUtil.isEmpty(specialNoticeBeanList)) {
                    SpecialNoticeBean specialNoticeBean = specialNoticeBeanList.get(0);
                    if (specialNoticeBeanList != null) {
                        if ("0".equals(specialNoticeBean.getCompleteStatus())) {
                            startCheck(specialNoticeBean.getNoticeType(),specialNoticeBean);
                        }
                    }
                }
            }
        });
        //水位详情
        findView(R.id.layoutWaterlevel).setOnClickListener(v -> {
            if (DoubleClickUtil.isFastDoubleClick()) {
                return;
            }
            Intent intent = new Intent();
            intent.setClass(getBaseActivity(), WaterRegimeDetailActivity.class);
            startActivity(intent);
        });
        //雨量详情
        findView(R.id.layoutRain).setOnClickListener(v -> {
            if (DoubleClickUtil.isFastDoubleClick()) {
                return;
            }
            Intent intent = new Intent();
            intent.setClass(getBaseActivity(), RainDetailActivity.class);
            startActivity(intent);
        });
        //图像详情
        findView(R.id.layoutImg).setOnClickListener(v -> {
            if (DoubleClickUtil.isFastDoubleClick()) {
                return;
            }
            Intent intent = new Intent();
            intent.setClass(getBaseActivity(), ImageDetailActivity.class);
            startActivity(intent);
        });
    }

    private void startCheck(String  special,SpecialNoticeBean specialNoticeBean){
        String workOrderId = UUIDUtil.getUUID32();
        offlineDataBean = UserManager.getInstance().getOfflineReservoir(selectedResrvoir.getReservoirId(),userCode,getContext());
        if (offlineDataBean != null && !CollectionsUtil.isEmpty(offlineDataBean.getRouteList())) {
            List<RouteListBean> routeListBeanList = offlineDataBean.getRouteList();
            RouteListBean routeListBean = null;
            if(!TextUtils.isEmpty(special)){
                //特别通知巡查
                for(RouteListBean mRouteListBean:routeListBeanList){
                    if (special.equals(mRouteListBean.getRouteType())) {
                        routeListBean = mRouteListBean;
                        specialNoticeBean.setWorkOrderId(workOrderId);
                        LogUtil.e(MainFragment.class.getName(),"------------特别巡查");
                        break;
                    }
                }
            }else {
                //普通巡查
                routeListBean = routeListBeanList.get(selectPostion);
            }

            if (routeListBean == null) {
                ToastUtils.shortToast("暂无巡查路线");
                return;
            }
            RouteListBean routeListBeanNew = new RouteListBean();


            Intent intent = new Intent();
            intent.setClass(getBaseActivity(),StartInspectionActivity.class);
            Bundle bundle = new Bundle();

            /**
             * 创建工单
             */
            String startTime = TimeFormatUtils.getStringDate();
            TaskBean taskBean = new TaskBean();

            routeListBeanNew.setWorkOrderId(workOrderId);
            routeListBeanNew.setId(routeListBean.getId());
            routeListBeanNew.setRouteContent(routeListBean.getRouteContent());
            routeListBeanNew.setUserCode(userCode);
            routeListBeanNew.setReservoirId(selectedResrvoir.getReservoirId());
            routeListBeanNew.setItemList(routeListBean.getItemList());
            routeListBeanNew.setRouteName(routeListBean.getRouteName());
            routeListBeanNew.setRoutePositions(routeListBean.getRoutePositions());
            routeListBeanNew.setRouteType(routeListBean.getRouteType());
            boolean save = routeListBeanNew.save();

            taskBean.setWorkOrderId(workOrderId);
            taskBean.setRouteId(routeListBeanNew.getId());
            taskBean.setReservoirId(selectedResrvoir.getReservoirId());
            taskBean.setReservoir(selectedResrvoir.getReservoir());
            taskBean.setUserCode(userCode);
            UserInfoBean userInfoBean = UserManager.getInstance().getUserBean();
//            taskBean.setRoleName(userInfoBean.getData().getOfficeName());
            taskBean.setExecutorName(userInfoBean.getData().getUserName());
            taskBean.setStartTime(startTime);
            taskBean.setExecuteStatus("2");
            taskBean.setRouteName(routeListBeanNew.getRouteName());


            RouteListBean routeListBean_a = taskBean.getRouteListBeanByWorkId(workOrderId);


            if(save && routeListBean_a != null) {
                String userCode = SPUtils.getInstance().getString(CacheConsts.userCode, "");

                saveOther(workOrderId,routeListBeanNew,"0",userCode);
                boolean issave = taskBean.save();
                if (issave) {
                    if (specialNoticeBean != null) {
                        specialNoticeBean.setCompleteStatus("1");
                        specialNoticeBean.saveOrUpdate();
                    }
                    if (simpleLoadDialog != null) {
                        simpleLoadDialog.dismiss();
                    }
                    bundle.putString(ConfigConsts.Workorderid,taskBean.getWorkOrderId());
                    intent.putExtras(bundle);
                    startActivity(intent);

                }
            }else{
                showOffine();
            }


        }else{
            if (simpleLoadDialog != null) {
                simpleLoadDialog.dismiss();
            }
            ToastUtils.shortToast("暂无巡查路线");
            return;
        }
    }

    /**
     * 保存
     * @param workOrderId
     * @param routeListBean
     */
    public static void saveOther(String workOrderId,RouteListBean routeListBean,String completeStatus,String userCode) {
        LogUtil.i(MainFragment.class.getName(),"--------------工单workOrderId:"+workOrderId);
        int num = 0 ;
        int numOFTaskItemBean = 0;
        String reservoirId = SPUtils.getInstance().getString(CacheConsts.reservoirId, "");
        for (TaskItemBean taskItemBean1 : routeListBean.getItemList()) {
            TaskItemBean taskItemBean = new TaskItemBean();
            taskItemBean.setPositionId(taskItemBean1.getPositionId());
            taskItemBean.setSuperviseItemCode(taskItemBean1.getSuperviseItemCode());
            taskItemBean.setPositionTreeNames(taskItemBean1.getPositionTreeNames());
            taskItemBean.setPositionName(taskItemBean1.getPositionName());
            taskItemBean.setSuperviseItemName(taskItemBean1.getSuperviseItemName());
            taskItemBean.setSuperviseItemContent(taskItemBean1.getSuperviseItemContent());
            taskItemBean.setLastExcuteResultType(taskItemBean1.getLastExcuteResultType());
            taskItemBean.setExcuteLongitude(taskItemBean1.getExcuteLongitude());
            taskItemBean.setExcuteLatitude(taskItemBean1.getExcuteLatitude());
            taskItemBean.setBeforelist(taskItemBean1.getBeforelist());
            taskItemBean.setExcuteDate(taskItemBean1.getExcuteDate());
            taskItemBean.setWorkOrderId(workOrderId);
            taskItemBean.setContent(taskItemBean1.getContent());
            taskItemBean.setSuperviseItemResultType(taskItemBean1.getSuperviseItemResultType());
            taskItemBean.setResultType(taskItemBean1.getResultType());
            taskItemBean.setPositionLatitude(taskItemBean1.getPositionLatitude());
            taskItemBean.setPositionLongitude(taskItemBean1.getPositionLongitude());
            taskItemBean.setOperationLevel(taskItemBean1.getOperationLevel());




//            String userCode = SPUtils.getInstance().getString(CacheConsts.userCode, "");
            taskItemBean.setUserCode(userCode);
            taskItemBean.setReservoirId(reservoirId);
            taskItemBean.setFatherId(routeListBean.getId());
            taskItemBean.setIsCommitLocal(completeStatus);
            taskItemBean.setItemId(UUIDUtil.getUUID32());
            taskItemBean.setWorkOrderId(workOrderId);
            taskItemBean.setCompleteStatus(completeStatus);

            if ("1".equals(completeStatus)) {
                taskItemBean.setExecuteResultType(taskItemBean1.getExecuteResultType());
                taskItemBean.setExecuteResultDescription(taskItemBean1.getExecuteResultDescription());
                taskItemBean.setLastExcuteResultType(taskItemBean1.getLastExcuteResultType());
                taskItemBean.setLastExecuteResultDescription(taskItemBean1.getLastExecuteResultDescription());
                taskItemBean.setLastExcuteDate(taskItemBean1.getLastExcuteDate());
                List<TaskItemBean.ISysFileUploadsBean> iSysFileUploadsBeanList = taskItemBean1.getiSysFileUploads();
                if (!CollectionsUtil.isEmpty(iSysFileUploadsBeanList)) {
                    for(TaskItemBean.ISysFileUploadsBean iSysFileUploadsBean:iSysFileUploadsBeanList){
                        CheckTaskPicturesBean checkTaskPicturesBean = new CheckTaskPicturesBean();
                        checkTaskPicturesBean.setFilePath(iSysFileUploadsBean.getFilePath());
                        LogUtil.e(MainFragment.class.getName(),"---------图片路径："+iSysFileUploadsBean.getFilePath());
                        checkTaskPicturesBean.setHasCheck("1");
                        checkTaskPicturesBean.setReservoirId(reservoirId);
                        checkTaskPicturesBean.setUserCode(userCode);
                        checkTaskPicturesBean.setBizType(ConfigConsts.picType_trouble);
                        checkTaskPicturesBean.setItemId(taskItemBean.getItemId());
                        checkTaskPicturesBean.setFileId(UUIDUtil.getUUID32());
                        checkTaskPicturesBean.save();
                    }

                }

            }

            boolean save = taskItemBean.save();
            numOFTaskItemBean++;
            LogUtil.i("save", "----------------TaskItemBean保存：" + save + numOFTaskItemBean);
        }

        for (RoutePosition routePosition1 : routeListBean.getRoutePositions()) {
            RoutePosition routePosition = new RoutePosition();
            routePosition.setDistance(routePosition1.getDistance());
            routePosition.setPositionLgtd(routePosition1.getPositionLgtd());
            routePosition.setPositionLttd(routePosition1.getPositionLttd());
            routePosition.setPositionId(routePosition1.getPositionId());


//            String userCode = SPUtils.getInstance().getString(CacheConsts.userCode, "");
//            String reservoirId = SPUtils.getInstance().getString(CacheConsts.reservoirId, "");
            routePosition.setUserCode(userCode);
            routePosition.setReservoirId(reservoirId);
            routePosition.setFatherId(routeListBean.getId());
            routePosition.setWorkOrderId(workOrderId);
            routePosition.setUuid(UUIDUtil.getUUID32());
            boolean save = routePosition.save();
            num++;
            LogUtil.i("save", "----------------RoutePosition保存：" + save + num);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ChoiceReservoirActivity.resultCode:
                ReservoirBean defaultReservoir = UserManager.getInstance().getDefaultReservoir();
                LogUtil.e(MainFragment.class.getName(),"------------当前水库:"+defaultReservoir.getReservoir());

                if (selectedResrvoir != null && defaultReservoir != null && !selectedResrvoir.getReservoirId().equals(defaultReservoir.getReservoirId())) {
                    selectedResrvoir = defaultReservoir;
                    binding.loHeader.tvReservoirName.setText(selectedResrvoir.getReservoir());
                    String reservoirId = UserManager.getInstance().getDefaultReservoir().getReservoirId();
                    SPUtils.getInstance().putString(CacheConsts.reservoirId, reservoirId);
                    alertDialog = null;
                    getReservoirBean(selectedResrvoir,true);

                }
                break;
            default:
                break;
        }
    }


    /**
     * 提醒下载离线包
     */
    AlertDialog.Builder alertDialog;
    private void showOffine() {
        if (alertDialog == null) {
            alertDialog = new AlertDialog.Builder(getBaseActivity())
                    .setMessage("请先下载该水库的基本信息离线包")
                    .setCancelable(true)
                    .setPositiveButton("去下载", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SPUtils.getInstance().putBoolean(CacheConsts.HAS_ASK, true);
                            ARouter.getInstance().build(AppRoutePath.app_select_reservor_downoffline)
                                    .withSerializable("selectedResrvoir", selectedResrvoir)
                                    //flag为10表示 离线数据包管理 为"0" 表示选择水库
                                    .withString("offlineFlag", "10")
                                    .navigation();

                        }
                    });
        }
        alertDialog.show();


    }

    private ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(2);

    /**
     * 离线包提示下载
     *
     * @param mReservoirBean
     */
    private void getReservoirBean(ReservoirBean mReservoirBean,boolean isshow) {

        UserInfoBean userInfoBean = UserManager.getInstance().getUserBean();

        ReservoirBean reservoirBean = DataSupport.where("reservoirId = ? and userCode=?", mReservoirBean.getReservoirId(), userInfoBean.getData().getUserCode()).findFirst(ReservoirBean.class);

        if (reservoirBean!=null){
            String jsonAboutInfo = reservoirBean.getJsonAboutInfo();
            if (TextUtils.isEmpty(jsonAboutInfo)) {
                images.clear();
                imageShowAdapter.notifyDataSetChanged();
                if (isshow) {
                    showOffine();
                }

            }else{
                images.clear();
                imageShowAdapter.notifyDataSetChanged();
                offlineDataBean = getOfflineDataBean(selectedResrvoir.getReservoirId(),userCode);
                for(RouteListBean routeListBean:offlineDataBean.getRouteList()){
                    if (routeListBean != null) {
                        String routeType = routeListBean.getRouteType();
                        String filePath = routeListBean.getFilePath();
                        images.add(routeType+","+filePath);
                        imageShowAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    private DataBeanOflistReservoirRoute getOfflineDataBean(String reservoirId,String userCode){
        DataBeanOflistReservoirRoute dataBeanList = null;
        ReservoirBean reservoirBean = DataSupport.where("reservoirId = ? and userCode=?",reservoirId,userCode).findFirst(ReservoirBean.class);
        if (reservoirBean != null) {
            String jsonAboutInfo = reservoirBean.getJsonAboutInfo();
            if (TextUtils.isEmpty(jsonAboutInfo)) {

                return dataBeanList;
            }
            ReservoirOfflineResponse reservoirOfflineResponse = new Gson().fromJson(jsonAboutInfo,ReservoirOfflineResponse.class);
            if (reservoirOfflineResponse  != null) {
                dataBeanList = reservoirOfflineResponse.getData();

            }
        }
        return dataBeanList;
    }

    /**
     * 获取指定水库全部信息
     *
     * @param reservoirId
     * @param reservoirBean
     */
    public void getAllReservoirData(String reservoirId, ReservoirBean reservoirBean) {


        UserManager.getInstance_Works().getAllReservoirData(reservoirId).safeSubscribe(new LoadingSubject<ReservoirOfflineResponse>(false, "正在获取水库列表...") {
            @Override
            protected void _onNext(ReservoirOfflineResponse reservoirOfflineResponse) {
                if (reservoirOfflineResponse.getCode() == 0) {
                    reservoirBean.setJsonAboutInfo(new Gson().toJson(reservoirOfflineResponse));
                    reservoirBean.setUpdateTimeOfthisData(TimeFormatUtils.getUserDate("yyyy-MM-dd"));
                    UserInfoBean userInfoBean = UserManager.getInstance().getUserBean();

                    reservoirBean.updateAll("reservoirId = ? and userCode=?", reservoirBean.getReservoirId(), userInfoBean.getData().getUserCode());

                }
            }

            @Override
            protected void _onError(String message) {

                LogUtil.e(MySelectReservoirAdapter.class.getName(), message + " ");

            }
        });
    }

    private void startAnimation(ImageView imageView) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setInterpolator(new LinearInterpolator());
        alphaAnimation.setRepeatCount(Animation.INFINITE);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        imageView.startAnimation(alphaAnimation);
    }


    private void reportProblem() {
        UserInfoBean userInfoBean = UserManager.getInstance().getUserBean();

        String userCode = userInfoBean.getData().getUserCode();
        DangerBean dangerBean = DataSupport.where("userCode=? and reservoirId=?",
                userCode, selectedResrvoir.getReservoirId()).findFirst(DangerBean.class);
        List<String> selectPhotosBefore = new ArrayList<>();
        List<CheckTaskPicturesBean> checkTaskPicturesBeanList = UtilDataBaseOfGD.getInstance().getCheckTaskPicturesBeanOfDanger(ConfigConsts.picType_danger, userCode, selectedResrvoir.getReservoirId());
        for (CheckTaskPicturesBean checkTaskPicturesBean : checkTaskPicturesBeanList) {
            if (checkTaskPicturesBean != null) {
                selectPhotosBefore.add(checkTaskPicturesBean.getFilePath());
            }
        }

        if (dangerBean != null && "1".equals(dangerBean.getHasReport())) {

            TaskManager.getInstance().reportProblem(dangerBean, selectPhotosBefore)
                    .subscribe(new LoadingSubject<BaseResponse>(true, ResUtils.getString(R.string.data_loading)) {
                        @Override
                        protected void _onNext(BaseResponse baseResponse) {
                            if (baseResponse != null) {
                                if (baseResponse.getCode() == 0) {
                                    if (dangerBean.isSaved()) {
                                        dangerBean.delete();
                                    }
                                    LogUtil.e(MainFragment.class.getName(),"reportProblem提交成功");
                                    DataSupport.deleteAll(CheckTaskPicturesBean.class, "bizType=? and userCode=? and reservoirId=?",
                                            ConfigConsts.picType_danger, userCode, selectedResrvoir.getReservoirId());
                                }
                            }
                        }

                        @Override
                        protected void _onError(String message) {
                            LogUtil.e(message + " ");

                        }
                    });
        }

    }


    private void specialNotice(){
        //获取水库最新通知
        binding.layoutSpecial.specialRootLy.setVisibility(View.GONE);
        binding.layoutRoute.routRootLy.setVisibility(View.VISIBLE);
        UserManager.getInstance_ADMIN().getNewNotice(selectedResrvoir.getReservoirId()).subscribe(new LoadingSubject<NewNoticeBean>() {

            @Override
            protected void _onNext(NewNoticeBean newNoticeBean) {
                if (newNoticeBean != null && newNoticeBean.getCode() == 0) {
                    List<SpecialNoticeBean> newNoticeList = newNoticeBean.getData();
                    if (newNoticeList != null && newNoticeList.size() > 0) {

                        SpecialNoticeBean specialNoticeBean = newNoticeList.get(0);
                        SpecialNoticeBean specialNoticeBeanNew = DataSupport.where("onlyId=? and reservoirId=?",specialNoticeBean.getId(),selectedResrvoir.getReservoirId()).findFirst(SpecialNoticeBean.class);
                        if (specialNoticeBeanNew == null) {
                            specialNoticeBean.setCompleteStatus("0");
                            specialNoticeBean.setReservoirId(selectedResrvoir.getReservoirId());
                            boolean save = specialNoticeBean.save();
                            LogUtil.e(MainFragment.class.getName(),"SpecialNoticeBean保存成功------------");

                        }

                        binding.layoutSpecial.specialRootLy.setVisibility(View.VISIBLE);
                        binding.layoutRoute.routRootLy.setVisibility(View.GONE);
                        SpecialNoticeBean specialNoticeBeanHasCheck = DataSupport.where("onlyId=? and reservoirId=? and completeStatus=?",specialNoticeBean.getId(),selectedResrvoir.getReservoirId(),"1").findFirst(SpecialNoticeBean.class);
                        if (specialNoticeBeanHasCheck != null) {
                            String workOrderId = specialNoticeBeanHasCheck.getWorkOrderId();
                            if (!TextUtils.isEmpty(workOrderId)) {
                                updateNotice(workOrderId);
                            }
                            binding.layoutSpecial.specialRootLy.setVisibility(View.GONE);
                            binding.layoutRoute.routRootLy.setVisibility(View.VISIBLE);

                            return;

                        }

                        binding.layoutSpecial.specialTimeTv.setText(specialNoticeBean.getCreateDate());
                        binding.layoutSpecial.nameTv.setText(specialNoticeBean.getRoleName()+":"+specialNoticeBean.getUserName());
                        binding.layoutSpecial.contentTv.setText(specialNoticeBean.getNoticeContent());


                        offlineDataBean = getOfflineDataBean(selectedResrvoir.getReservoirId(),userCode);
                        if (offlineDataBean != null && !CollectionsUtil.isEmpty(offlineDataBean.getRouteList())) {
                            List<RouteListBean> routeListBeanList = offlineDataBean.getRouteList();
                            if (!TextUtils.isEmpty(specialNoticeBean.getNoticeType())) {
                                //特别通知巡查
                                for (RouteListBean mRouteListBean : routeListBeanList) {
                                    if (specialNoticeBean.getNoticeType().equals(mRouteListBean.getRouteType())) {
                                        Glide.with(Utils.getContext())
                                                .load(mRouteListBean.getFilePath())
                                                .apply(ConfigConsts.options)
                                                .into(binding.layoutSpecial.specialIv);
                                        break;

                                    }
                                }
                            }
                        }

                    }
                }
            }

            @Override
            protected void _onError(String message) {
                LogUtil.e(MainFragment.class.getName(),message+" 特别巡查通知");

            }
        });
    }

    private void updateNotice(String workOrderId){
        SpecialNoticeBean specialNoticeBeanNew = DataSupport.where("workOrderId=?",workOrderId).findFirst(SpecialNoticeBean.class);
        if (specialNoticeBeanNew != null) {
            UserManager.getInstance_ADMIN().updateNotice(specialNoticeBeanNew.getId(),workOrderId).subscribe(new LoadingSubject<BaseResponse>(true, "正在修改通知状态...") {
                @Override
                protected void _onNext(BaseResponse baseResponse) {
                    if (baseResponse.getCode() == 0) {
                        DataSupport.deleteAll(SpecialNoticeBean.class,"workOrderId=?",workOrderId);
                        LogUtil.e(StartInspectionActivity.class.getName(),"修改成功----------------"+workOrderId);
                    }
                }

                @Override
                protected void _onError(String message) {
                    LogUtil.e(StartInspectionActivity.class.getName(),message+"----------------"+workOrderId);
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        startAnimation(binding.loHeader.dangerIv);
//        getReservoirBean(selectedResrvoir);
        getReservoirBean(selectedResrvoir,false);
        getWater();
        reportProblem();
        specialNotice();
        getWeatherWarn();
    }
}
