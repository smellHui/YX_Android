package com.tepia.guangdong_module.amainguangdong.xunchaview.activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.guangdong_module.R;
import com.tepia.base.mvp.BaseActivity;
import com.tepia.base.utils.ToastUtils;
import com.tepia.guangdong_module.amainguangdong.common.UserManager;
import com.tepia.guangdong_module.amainguangdong.model.reservoirdetail.PicDetailBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.ReservoirBean;
import com.tepia.guangdong_module.amainguangdong.mvp.reservoirDetail.ReservoirDetailContract;
import com.tepia.guangdong_module.amainguangdong.mvp.reservoirDetail.ReservoirDetailPresenter;
import com.tepia.guangdong_module.amainguangdong.utils.EmptyLayoutUtil;
import com.tepia.guangdong_module.amainguangdong.xunchaview.adapter.reservoirdetail.ImageDetailAdapter;
import com.tepia.guangdong_module.amainguangdong.xunchaview.adapter.layoutmanager.PickerLayoutManager;
import com.tepia.guangdong_module.amainguangdong.xunchaview.fragment.MainFragment;
import com.tepia.photo_picker.PhotoPreview;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
  * Created by      Android studio
  *
  * @author :wwj (from Center Of Wuhan)
  * Date    :2019/5/28
  * Version :1.0
  * 功能描述 :  图像详情
 **/
public class ImageDetailActivity extends BaseActivity {
    private String[] searchDateType = {"今日", "昨日"};
    private ReservoirBean defaultReservoir;
    private Spinner spinnerHour;
    private int hourPos = 0;
    private TextView tvHourDate;
    private TextView tvHourDateEnd;
    private RecyclerView rvImg;
    private RecyclerView rvTime;
    private List<PicDetailBean.DataBean> picList;
    private ArrayList<String> picStrList;
    private ReservoirDetailPresenter mPresenter;
    private ImageDetailAdapter imageDetailAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_image_detail;
    }

    @Override
    public void initData() {
        defaultReservoir = UserManager.getInstance().getDefaultReservoir();
        mPresenter = new ReservoirDetailPresenter();
        mPresenter.attachView(new ReservoirDetailContract.View<PicDetailBean>() {
            @Override
            public void success(PicDetailBean dataBean) {
                List<PicDetailBean.DataBean> dataList = dataBean.getData();
                picList.clear();
                picStrList.clear();
                if (dataList!=null&&dataList.size()>0){
                    picList.addAll(dataList);
                    for (int i = 0; i < dataList.size(); i++) {
                        String picpath = dataList.get(i).getPicpath();
                        picStrList.add(picpath);
                    }
                    imageDetailAdapter.notifyDataSetChanged();
                }else {
                    imageDetailAdapter.setEmptyView(EmptyLayoutUtil.show("暂无数据"));
                    imageDetailAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void failure(String msg) {
                picList.clear();
                picStrList.clear();
                imageDetailAdapter.setEmptyView(EmptyLayoutUtil.show(msg));
                imageDetailAdapter.notifyDataSetChanged();
                ToastUtils.shortToast(msg);
            }

            @Override
            public Context getContext() {
                return null;
            }
        });
    }

    @Override
    public void initView() {
        picList = new ArrayList<>();
        picStrList = new ArrayList<>();
        if (defaultReservoir!=null){
            String reservoir = defaultReservoir.getReservoir();
            setCenterTitle(reservoir+"图像监控");
        }else {
            setCenterTitle("图像监控");
        }
        showBack();
        spinnerHour = findViewById(R.id.spinner_hour);
        tvHourDate = findViewById(R.id.tv_hour_date);
        tvHourDateEnd = findViewById(R.id.tv_hour_date_end);
        rvImg = findViewById(R.id.rv_img);
        rvTime = findViewById(R.id.rv_time);
        initSpinner();
        initHourDate();
        initRecyclerView();
        requestPicResponse();
    }

    private void requestPicResponse() {
        String reservoirId = defaultReservoir.getReservoirId();
        Calendar cd = Calendar.getInstance();
        int year = cd.get(Calendar.YEAR);
        String strHour = tvHourDate.getText().toString();
        String strHourEnd = tvHourDateEnd.getText().toString();
        String startDate = year+"-"+strHour+":00";
        String endDate = year+"-"+strHourEnd+":59";
        mPresenter.getPictureDetailList(reservoirId, startDate, endDate, true);
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvImg.setLayoutManager(linearLayoutManager);
//        SnapHelper，他的作用是让RecyclerView滑动视图后使停止位置正好停在某页的正中间
//        new LinearSnapHelper().attachToRecyclerView(rvImg);
        imageDetailAdapter = new ImageDetailAdapter(R.layout.item_image_detail, picList);
        imageDetailAdapter.addFooterView(EmptyLayoutUtil.getFootView());
        rvImg.setAdapter(imageDetailAdapter);
        imageDetailAdapter.setOnItemClickListener((adapter, view, position) -> {
            PhotoPreview.builder()
                    .setPhotos(picStrList)
                    .setShowDeleteButton(false)
                    .setCurrentItem(position)
                    .start(this,0);
        });
//        //设置日期列表
//        ImageDetailAdapter imageDetailTimeAdapter = new ImageDetailAdapter(R.layout.item_image_detail_time, imgs);
//        PickerLayoutManager  mPickerLayoutManager1 = new PickerLayoutManager(getContext(),rvTime, PickerLayoutManager.VERTICAL, false,1,0.8f,true);
//        rvTime.setLayoutManager(mPickerLayoutManager1);
//        rvTime.setAdapter(imageDetailTimeAdapter);
    }

    private void initSpinner() {
        ArrayAdapter spinnerAdapter = new ArrayAdapter<String>(this, R.layout.spinner_rain_detail, searchDateType);
        //设置下拉列表的风格
        spinnerAdapter.setDropDownViewResource(R.layout.dropdown_stytle);
        //将adapter2 添加到spinner中
        spinnerHour.setAdapter(spinnerAdapter);
        //添加事件Spinner事件监听
        spinnerHour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                hourPos = pos;
                initHourDate();
                requestPicResponse();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }

    /**
     * 设置小时降雨量时间段时间段
     */
    private void initHourDate() {
        //得到一个Calendar的实例
        Calendar ca = Calendar.getInstance();
        //设置时间为当前时间
        ca.setTime(new Date());
        //昨天减1
        if (hourPos == 0) {
            //今天
            ca.add(Calendar.DATE, 0);
        } else {
            //昨天
            ca.add(Calendar.DATE, -1);
        }
        Date startDate = ca.getTime();
        String hourStr = dateToStrLong(startDate);
        tvHourDate.setText(hourStr + " 00:00");
        tvHourDateEnd.setText(hourStr + " 23:59");
    }

    public static String dateToStrLong(Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initRequestData() {

    }
}
