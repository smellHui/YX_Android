package com.tepia.guangdong_module;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.amap.api.location.AMapLocationClientOption;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.ashokvarma.bottomnavigation.TextBadgeItem;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.example.gaodelibrary.GPSUtil;
import com.example.gaodelibrary.GaodeEntity;
import com.example.guangdong_module.R;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.tepia.base.AppRoutePath;
import com.tepia.base.CacheConsts;
import com.tepia.base.common.CommonFragmentPagerAdapter;
import com.tepia.base.common.HorizontalViewPager;
import com.tepia.base.http.BaseResponse;
import com.tepia.base.http.LoadingSubject;
import com.tepia.base.mvp.BaseActivity;
import com.tepia.base.utils.AppManager;
import com.tepia.base.utils.LogUtil;
import com.tepia.base.utils.ToastUtils;
import com.tepia.base.utils.Utils;
import com.tepia.base.view.floatview.CollectionsUtil;
import com.tepia.guangdong_module.amainguangdong.common.UserManager;
import com.tepia.guangdong_module.amainguangdong.model.UserInfoBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.ReservoirBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.ReservoirListResponse;
import com.tepia.guangdong_module.amainguangdong.xunchaview.activity.ChangeReservoirActivity;
import com.tepia.guangdong_module.amainguangdong.xunchaview.activity.StartInspectionActivity;
import com.tepia.guangdong_module.amainguangdong.xunchaview.fragment.TabMainFragmentFactory;
import com.tepia.guangdong_module.amainguangdong.xunchaview.fragment.YunweiFragment;
import com.tepia.photo_picker.utils.SPUtils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;

/**
 * Created by      Android studio
 *
 * @author :ly (from Center Of Wuhan)
 * 创建时间 :
 * 更新时间 :
 * Version :1.0
 * 功能描述 :巡查app首页
 **/
@Route(path = AppRoutePath.appMainGuangdong)
public class MainActivity extends BaseActivity {

    BottomNavigationBar mBottomNavigation;
    HorizontalViewPager mViewPagers;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

   /* @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.keyboardEnable(false).flymeOSStatusBarFontColor("#ffffff").navigationBarWithKitkatEnable(false).init();
    }*/

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        mBottomNavigation = findViewById(R.id.bottom_navigation);
        mViewPagers = findViewById(R.id.viewPagers);
        setBottomNavigation();
        initVariables();
        setViewPager();
        String roleType = UserManager.getInstance().getRoleType();
        //巡查
        if ("4".equals(roleType)) {
            updateData();
        } //行政
        else{

        }
        update();
        initGaoDeLocation();
    }

    /**
     * 初始化各个页面并和viewpager绑定
     */
    YunweiFragment yunweiFragment;
    private void initVariables() {

        String roleType = UserManager.getInstance().getRoleType();


        CommonFragmentPagerAdapter adapter = new CommonFragmentPagerAdapter(getSupportFragmentManager());
        if (roleType != null) {
            // 巡查
            if ("4".equals(roleType)) {
                //巡查
                mBottomNavigation.addItem(mItemFirst);
                adapter.addFragment(TabMainFragmentFactory.getInstance().getMainFragment());

                mBottomNavigation.addItem(mIteSecond);
                yunweiFragment = TabMainFragmentFactory.getInstance().getYunweiFragment();
                adapter.addFragment(yunweiFragment);
            }
            //行政
            else{
                mBottomNavigation.addItem(mItemFirst);
                adapter.addFragment(TabMainFragmentFactory.getInstance().getAdminStatisticsFragment());

             /*   yunweiFragment = TabMainFragmentFactory.getInstance().getYunweiFragment();
                adapter.addFragment(yunweiFragment);*/
            }
        }

        mBottomNavigation.addItem(mItemMine);
        adapter.addFragment(TabMainFragmentFactory.getInstance().getSettingFragment());

        //设置默认选择的按钮
        mBottomNavigation.setFirstSelectedPosition(0);
        mBottomNavigation.initialise();
        mViewPagers.setCurrentItem(0);
        mViewPagers.setAdapter(adapter);

    }

    /**
     * ViewPager滑动监听b
     */
    private void setViewPager() {
        mViewPagers.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                //页面滑动后，按钮改变
                mBottomNavigation.selectTab(position, false);
                if (yunweiFragment != null && position == 1) {
                    yunweiFragment.firstSearch();

                    yunweiFragment.refresh();
                    yunweiFragment.netOfrefresh(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * 初始化BottomNavigation设置
     */
    private BottomNavigationItem mItemFirst;
    private BottomNavigationItem mIteSecond;
    private BottomNavigationItem mItemMine;

    private void setBottomNavigation() {
        mBottomNavigation.setMode(BottomNavigationBar.MODE_FIXED)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
                .setBackgroundColor(ContextCompat.getColor(this, R.color.nav_bg));
        //选中颜色文字
        mBottomNavigation.setActiveColor("#0994ff")
                //默认未选择颜色
                .setInActiveColor("#a9adb4")
                //默认背景色
                .setBarBackgroundColor("#ffffff");
        mItemFirst = new BottomNavigationItem(R.mipmap.tab_icon_home_selected, "首页");
        mItemFirst.setInactiveIconResource(R.mipmap.tab_icon_home_normal);
        TextBadgeItem badgeItem = new TextBadgeItem();
        badgeItem.show(true);


        mIteSecond = new BottomNavigationItem(R.mipmap.tab_icon_safeguard_selected, getString(R.string.title_dashboard));
        mIteSecond.setInactiveIconResource(R.mipmap.tab_icon_safeguard_normal);

        mItemMine = new BottomNavigationItem(R.mipmap.tab_icon_user_selected, "我的");
        mItemMine.setInactiveIconResource(R.mipmap.tab_icon_user_normal);

        mBottomNavigation.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                mViewPagers.setCurrentItem(position);
                if (position == 1) {
//                    TabMainFragmentFactory.getInstance().getYunweiFragment().refresh();
                }
            }

            @Override
            public void onTabUnselected(int position) {
            }

            @Override
            public void onTabReselected(int position) {

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
    protected void onResume() {
        super.onResume();
//        SophixManager.getInstance().queryAndLoadNewPatch();
    }

    private AppBean appBean;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 200;

    private void update() {
        PgyUpdateManager.register(this, new UpdateManagerListener() {
            @Override
            public void onNoUpdateAvailable() {
                PgyUpdateManager.unregister();
            }

            @Override
            public void onUpdateAvailable(String result) {
                // 将新版本信息封装到AppBean中
                PgyUpdateManager.unregister();
                appBean = getAppBeanFromString(result);
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("更新")
                        .setMessage("" + appBean.getReleaseNote())
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton(
                                "确定",
                                new DialogInterface.OnClickListener() {


                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //android 6.0动态申请权限
                                        if (ContextCompat.checkSelfPermission(Utils.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                                != PackageManager.PERMISSION_GRANTED) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                                            }
                                            ActivityCompat.requestPermissions(MainActivity.this,
                                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                                        } else {
                                            startDownloadTask(MainActivity.this, appBean.getDownloadURL());

                                        }
                                    }
                                }).show();

            }
        });
    }

    /**
     * 更新水库信息
     */
    private void updateData() {
        UserManager.getInstance_Works().getReservoirList().safeSubscribe(new LoadingSubject<ReservoirListResponse>() {
            @Override
            protected void _onNext(ReservoirListResponse response) {
                if (response.getCode() == 0) {
                    UserManager.getInstance().saveReservoirList(response.getData());
                }
            }

            @Override
            protected void _onError(String message) {
                LogUtil.e("水库信息更新失败");

            }
        });

        UserInfoBean userInfoBean = UserManager.getInstance().getUserBean();
        SPUtils.getInstance().putString(CacheConsts.userCode,userInfoBean.getData().getUserCode());
        //保存到数据库
        ArrayList<ReservoirBean> reservoirBeanArrayList = UserManager.getInstance().getLocalReservoirList();
        if (!CollectionsUtil.isEmpty(reservoirBeanArrayList)) {

            int size = reservoirBeanArrayList.size();
            for (int i = 0; i < size; i++) {

                ReservoirBean reservoirBeanFromData = DataSupport.where("reservoirId = ? and userCode=?",
                        reservoirBeanArrayList.get(i).getReservoirId(),userInfoBean.getData().getUserCode()).findFirst(ReservoirBean.class);
                ReservoirBean reservoirBean = reservoirBeanArrayList.get(i);
                if (reservoirBeanFromData == null) {
                    if (userInfoBean != null) {
                        reservoirBean.setUserCode(userInfoBean.getData().getUserCode());
                    }
                    boolean save = reservoirBean.save();

                }
            }


        }
    }

    /**
     * 高德地图定位
     */
    private GaodeEntity gaodeEntity;
    private double lgtd;
    private double lttd;

    private void initGaoDeLocation() {
        if (gaodeEntity == null) {
            gaodeEntity = new GaodeEntity(this, ChangeReservoirActivity.class, R.mipmap.logo);
            gaodeEntity.initLocation();
            boolean offline = SPUtils.getInstance().getBoolean(CacheConsts.OFFLINE, false);
            if (offline) {
                //支持离线模式定位
                gaodeEntity.getLocationClient().setLocationOption(getDefaultOption(10*60*1000));

            }else {
                AMapLocationClientOption mOption = new AMapLocationClientOption();
                // 高德地图说明，来自高德android开发常见问题：
                // GPS模块无法计算出定位结果的情况多发生在卫星信号被阻隔时，在室内环境卫星信号会被墙体、玻璃窗阻隔反射，在“城市峡谷”（高楼林立的狭长街道）地段卫星信号也会损失比较多。
                //// 强依赖GPS模块的定位模式——如定位SDK的仅设备定位模式，需要在室外环境进行，多用于行车、骑行、室外运动等场景。
                // 可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
                mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
                // 可选，设置是否gps优先，只在高精度模式下有效。默认关闭
                mOption.setGpsFirst(true);
                /*
                 * mOption.setInterval(interval);//可选，设置定位间隔。默认为2秒
                 * mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
                 */
                // 可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
                mOption.setWifiScan(true);
                // 可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
                mOption.setHttpTimeOut(30000);
                // 可选，设置定位间隔。默认为2秒
                mOption.setInterval(5*60*1000);
                // 可选，设置是否返回逆地理地址信息。默认是true
                mOption.setNeedAddress(true);
                // 可选，设置是否单次定位。默认是false
                mOption.setOnceLocation(false);
                // 可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
                mOption.setOnceLocationLatest(false);
                // 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
                AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);
                // 可选，设置是否使用传感器。默认是false
                mOption.setSensorEnable(false);
                mOption.setWifiScan(true); // 可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
                mOption.setLocationCacheEnable(true); // 可选，设置是否使用缓存定位，默认为true
                gaodeEntity.getLocationClient().setLocationOption(mOption);
//                gaodeEntity.initLocation();
            }
        }
        gaodeEntity.setLocationListen(aMapLocation -> {
            if (gaodeEntity != null) {
                if (aMapLocation.getErrorCode() == 4) {
                    ToastUtils.shortToast("当前网络较差，已自动切换为无网模式定位");
                    gaodeEntity.getLocationClient().setLocationOption(getDefaultOption(10*60*1000));
                    return;
                }
                double[] temp = GPSUtil.gcj02_To_Gps84(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                double latitude = temp[0];
                double longitude = temp[1];
                LogUtil.e(StartInspectionActivity.class.getName(), "经度：" + longitude);
                if (latitude == 0 || longitude == 0) {
                    return;
                }
                lgtd = longitude;
                lttd = latitude;
                updateCheckManLocation();
                Point currentPoint = new Point(longitude, latitude, SpatialReference.create(4326));
            }
        });
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        } else {
            //开始定位
            if (gaodeEntity != null) {
                gaodeEntity.startLocation();
            }
        }
//        Handler mHandler = new Handler();
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                updateCheckManLocation();
//                //需要执行的任务在这里处理
//                mHandler.postDelayed(this, 30*60*1000);
//            }
//        }, 60*1000);
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                boolean isUploadCheckManLocation = SPUtils.getInstance().getBoolean("isUploadCheckManLocation", false);
//                if (!isUploadCheckManLocation){
//                    updateCheckManLocation();
//                }
//                //需要执行的任务在这里处理
//                handler.postDelayed(this, 10*60*1000);
//            }
//        }, 2*60*1000);
    }

    public AMapLocationClientOption getDefaultOption(int interval) {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        //高德地图说明，来自高德android开发常见问题：
        //GPS模块无法计算出定位结果的情况多发生在卫星信号被阻隔时，在室内环境卫星信号会被墙体、玻璃窗阻隔反射，在“城市峡谷”（高楼林立的狭长街道）地段卫星信号也会损失比较多。
        ////强依赖GPS模块的定位模式——如定位SDK的仅设备定位模式，需要在室外环境进行，多用于行车、骑行、室外运动等场景。
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        /*mOption.setGpsFirst(true);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setInterval(interval);//可选，设置定位间隔。默认为2秒
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差*/
        mOption.setGpsFirst(true);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(interval);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }

    /**
     * 人员位置上报接口
     */
    private void updateCheckManLocation() {
        ReservoirBean defaultReservoir = UserManager.getInstance().getDefaultReservoir();
        String reservoirId = defaultReservoir.getReservoirId();
        if (!TextUtils.isEmpty(reservoirId)){
            UserManager.getInstance_Works().uploadCheckManLocation(reservoirId,lgtd+"",lttd+"").safeSubscribe(new LoadingSubject<BaseResponse>() {
                @Override
                protected void _onNext(BaseResponse response) {
                    if (response.getCode() == 0) {
                        SPUtils.getInstance().putBoolean("isUploadCheckManLocation",true);
                    }
                }

                @Override
                protected void _onError(String message) {
                    LogUtil.e("人员位置上报错误");
                    SPUtils.getInstance().putBoolean("isUploadCheckManLocation",false);
                }
            });
        }
    }

    /**
     * @param keyCode
     * @param event
     * @return
     */
    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                ToastUtils.shortToast("再按一次退出程序");
                mExitTime = System.currentTimeMillis();
            } else {
//                closeTrace();
                AppManager.getInstance().exitApp();
                this.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length >= 1) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (gaodeEntity != null) {
                        gaodeEntity.startLocation();//开始定位
                    }
                } else {
                    new AlertDialog.Builder(this)
                            .setMessage("定位权限被拒绝,将导致定位功能无法正常使用，需要到设置页面手动授权")
                            .setPositiveButton("去授权", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //引导用户至设置页手动授权
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                                    intent.setData(uri);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("取消", (dialog, which) -> {
                                //引导用户手动授权，权限请求失败
                                ToastUtils.longToast("拒绝了定位权限，将不能使用定位功能！！！");
                            }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            //引导用户手动授权，权限请求失败
                            ToastUtils.longToast("拒绝了定位权限，将不能使用定位功能！！！");
                        }
                    }).show();
                    // Permission Denied
                }
            }
            return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gaodeEntity != null) {
            gaodeEntity.closeLocation();
        }
    }
}
