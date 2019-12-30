package com.yangj.dahemodule.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.amap.api.location.AMapLocationClientOption;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.util.ListenableList;
import com.example.gaodelibrary.GPSUtil;
import com.example.gaodelibrary.GaodeEntity;
import com.tepia.base.utils.ToastUtils;
import com.yangj.dahemodule.R;

import java.util.concurrent.ExecutionException;

/**
 * Author:xch
 * Date:2019/11/26
 * Description:
 */
public class GaodeHelper {

    private Context mContext;
    private Class<?> startClass;
    private Point currectPoint;
    private GaodeEntity gaodeEntity;
    private GraphicsOverlay graphicsOverlay;
    private LocationListener locationListener;

    public GaodeHelper(Context context, Class<?> startClass,GraphicsOverlay graphicsOverlay, @NonNull LocationListener locationListener) {
        this.mContext = context;
        this.startClass = startClass;
        this.graphicsOverlay = graphicsOverlay;
        this.locationListener = locationListener;
        init();
    }

    private void init() {
        gaodeEntity = new GaodeEntity(mContext, startClass, R.mipmap.logo);
        gaodeEntity.setLocationListen(aMapLocation -> {
            if (aMapLocation.getErrorCode() == 14) {
//                    ToastUtils.shortToast("设备当前 GPS 状态差,请持设备到相对开阔的露天场所再次尝试");
                new AlertDialog.Builder(mContext)
                        .setMessage("设备当前 GPS 状态差,请持设备到相对开阔的露天场所再次尝试")
                        .setCancelable(true)
                        .setPositiveButton("好的", (dialog, which) -> {

                        })
                        .show();
                return;
            }
            if (aMapLocation.getErrorCode() != 0) {
//                    ToastUtils.shortToast(aMapLocation.getErrorInfo()+aMapLocation.getLocationDetail());
                gaodeEntity.getLocationClient().setLocationOption(getDefaultOption(5000));
                if (aMapLocation.getErrorCode() == 4) {
                    ToastUtils.shortToast("当前网络较差，已自动切换为无网模式定位");
                }
            }
            double[] temp = GPSUtil.gcj02_To_Gps84(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            double latitude = temp[0];
            double longitude = temp[1];
            if (latitude == 0 || longitude == 0) {
                return;
            }
            currectPoint = new Point(longitude, latitude, SpatialReference.create(4326));
            locationListener.locationCallback(currectPoint);
            //添加定位图标
            Point locationPoint = new Point(aMapLocation.getLongitude(), aMapLocation.getLatitude(), SpatialReference.create(4326));
            addLocationPic(graphicsOverlay, com.example.guangdong_module.R.drawable.google_location, locationPoint);
        });
    }

    public void startLocation() {
        //开始定位
        if (gaodeEntity != null) {
            //开始轨迹
            gaodeEntity.startTrace();
        }
    }

    public void stopLocation() {
        if (gaodeEntity != null) {
            gaodeEntity.stopTrace();
        }
    }

    public void closeLocation() {
        if (gaodeEntity != null) {
            gaodeEntity.closeLocation();
        }
    }

    /**
     * 添加定位图标
     *
     * @param graphicsOverlay
     * @param id
     * @param point
     */
    Bitmap bitmapLocation;

    public void addLocationPic(GraphicsOverlay graphicsOverlay, int id, Point point) {
        PictureMarkerSymbol pictureMarkerSymbol1 = null;
        try {
            if (bitmapLocation == null) {
                bitmapLocation = BitmapFactory.decodeResource(mContext.getResources(), id);
            }
            if (bitmapLocation == null) {
                return;
            }
            pictureMarkerSymbol1 = PictureMarkerSymbol.createAsync(new BitmapDrawable(mContext.getResources(), bitmapLocation)).get();
            Graphic picGraphic = new Graphic(point, pictureMarkerSymbol1);
            ListenableList<Graphic> graphics = graphicsOverlay.getGraphics();
            if (graphics.size() > 0) {
                graphics.set(0, picGraphic);
            } else {
                graphics.add(picGraphic);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
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

    public interface LocationListener {
        void locationCallback(Point point);
    }

}
