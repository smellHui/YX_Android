package com.tepia.guangdong_module.amainguangdong.xunchaview.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.guangdong_module.R;
import com.example.guangdong_module.databinding.ActivityRainfallStatisticsBinding;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.tepia.base.mvp.MVPBaseActivity;
import com.tepia.base.utils.TimePickerDialogUtil;
import com.tepia.base.utils.ToastUtils;
import com.tepia.base.utils.Utils;
import com.tepia.base.view.dialog.basedailog.ActionSheetDialog;
import com.tepia.base.view.dialog.basedailog.OnOpenItemClick;
import com.tepia.guangdong_module.amainguangdong.model.rainfallstatistics.RainfallItemBean;
import com.tepia.guangdong_module.amainguangdong.model.rainfallstatistics.RainfallListResponse;
import com.tepia.guangdong_module.amainguangdong.model.rainfallstatistics.ReservoirNumBean;
import com.tepia.guangdong_module.amainguangdong.model.rainfallstatistics.ReservoirNumResponse;
import com.tepia.guangdong_module.amainguangdong.mvp.rainfallstatistics.RainfallStatisticsContract;
import com.tepia.guangdong_module.amainguangdong.mvp.rainfallstatistics.RainfallStatisticsPesenter;
import com.tepia.guangdong_module.amainguangdong.utils.EmptyLayoutUtil;
import com.tepia.guangdong_module.amainguangdong.xunchaview.adapter.AdapterRainfallStatistics;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import static com.tepia.guangdong_module.amainguangdong.utils.TimeUtils.getCurrentHourTime;
import static com.tepia.guangdong_module.amainguangdong.utils.TimeUtils.getLastHourTime;
import static com.tepia.guangdong_module.amainguangdong.utils.TimeUtils.isDateOneBigger;

/**
 * Created by Android Studio
 *
 * @author : Arthur
 * Date :    2019/6/5
 * Time :    14:47
 * Describe : 雨量统计界面
 */
public class RainfallStatisticsActivity
        extends MVPBaseActivity<RainfallStatisticsContract.View, RainfallStatisticsPesenter>
        implements RainfallStatisticsContract.View, View.OnClickListener {

    private ActivityRainfallStatisticsBinding mBinding;

    private TextView tv_left_text, tv_center_text;

    /**
     * 起始时间
     */
    private String timeStart = "";

    /**
     * 终止时间
     */
    private String timeEnd = "";

    /**
     * 时间选择器
     */
    private TimePickerDialogUtil timePickerDialogUtil;

    /**
     * 判断当前是选择起始时间还是终止时间
     */
    private boolean isChooseStart = false;

    private int netOfpageSize = 10;
    private int pageNo = 1;
    private int currentPage = 0;
    private boolean isFristLoad;
    private String mType = "3";
    private String mLevel = "";

    private AdapterRainfallStatistics mAdapter;
    private List<RainfallItemBean> dataList;

    /**
     * 顶部雨情描述文字
     */
    private String str1 = "<font color=\"#454545\">监测水库%1$s宗,统计时间段共有%2$s宗上报降雨数据，其中特大暴雨</font>";
    private String str2 = "<font color=\"#B90A0A\">%1$s</font>";
    private String str3 = "<font color=\"#454545\">宗，大暴雨</font>";
    private String str4 = "<font color=\"#B90A0A\">%1$s</font>";
    private String str5 = "<font color=\"#454545\">宗，暴雨</font>";
    private String str6 = "<font color=\"#B90A0A\">%1$s</font>";
    private String str7 = "<font color=\"#454545\">宗，大雨</font>";
    private String str8 = "<font color=\"#B90A0A\">%1$s</font>";
    private String str9 = "<font color=\"#454545\">宗，中雨</font>";
    private String str10 = "<font color=\"#B90A0A\">%1$s</font>";
    private String str11 = "<font color=\"#454545\">宗，小雨</font>";
    private String str12 = "<font color=\"#B90A0A\">%1$s</font>";
    private String str13 = "<font color=\"#454545\">宗</font>";

    @Override
    public int getLayoutId() {
        return R.layout.activity_rainfall_statistics;
    }

    @Override
    public void initData() {
        tv_left_text = findViewById(R.id.tv_left_text);
        tv_center_text = findViewById(R.id.tv_center_text);
        tv_center_text.setText("雨量统计");
        mBinding = DataBindingUtil.bind(mRootView);
        mBinding.rvReservoirList.setLayoutManager(new LinearLayoutManager(this));
        initTimePicker();
        dataList = new ArrayList<>();
        mAdapter = new AdapterRainfallStatistics(R.layout.lv_item_rainfall_statistics, dataList);
        mBinding.rvReservoirList.setAdapter(mAdapter);

        mBinding.srflContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                isFristLoad = true;
                mLevel = "";
                refreshData();
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {

            @Override
            public void onLoadMoreRequested() {
                isFristLoad = false;
                loadMoreData();
            }
        });

        Spanned spanned1 = Html.fromHtml(String.format(str1, "--", "--"));
        Spanned spanned2 = Html.fromHtml(String.format(str2, "--"));
        Spanned spanned3 = Html.fromHtml(str3);
        Spanned spanned4 = Html.fromHtml(String.format(str4, "--"));
        Spanned spanned5 = Html.fromHtml(str5);
        Spanned spanned6 = Html.fromHtml(String.format(str6, "--"));
        Spanned spanned7 = Html.fromHtml(str7);
        Spanned spanned8 = Html.fromHtml(String.format(str8, "--"));
        Spanned spanned9 = Html.fromHtml(str9);
        Spanned spanned10 = Html.fromHtml(String.format(str10, "--"));
        Spanned spanned11 = Html.fromHtml(str11);
        Spanned spanned12 = Html.fromHtml(String.format(str12, "--"));
        Spanned spanned13 = Html.fromHtml(str13);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        spannableStringBuilder.append(spanned1).append(spanned2).append(spanned3).append(spanned4).append(spanned5)
                .append(spanned6).append(spanned7).append(spanned8).append(spanned9).append(spanned10).append(spanned11)
                .append(spanned12).append(spanned13);
        mBinding.tvTotalDescribe.setText(spannableStringBuilder);
    }

    @Override
    public void initView() {

    }

    @Override
    protected void initListener() {
        tv_left_text.setOnClickListener(this);
    }

    @Override
    protected void initRequestData() {
        mBinding.tvLevel.setOnClickListener(this);
        mBinding.etReservoirStarttime.setOnClickListener(this);
        mBinding.etReservoirEndtime.setOnClickListener(this);
        isFristLoad = true;
        mLevel = "";
        refreshData();
    }

    @Override
    public void onFail(String msg) {
        ToastUtils.shortToast(msg);
    }

    @Override
    public void showReservoirNum(ReservoirNumResponse data) {
        ReservoirNumBean bean = data.getData();

        Spanned spanned1 = Html.fromHtml(String.format(str1, bean.getAllCount(), bean.getStCount()));
        int length1 = spanned1.length();
        Spanned spanned2 = Html.fromHtml(String.format(str2, bean.getLevel6Count()));
        int length2 = spanned2.length();
        Spanned spanned3 = Html.fromHtml(str3);
        int length3 = spanned3.length();
        Spanned spanned4 = Html.fromHtml(String.format(str4, bean.getLevel5Count()));
        int length4 = spanned4.length();
        Spanned spanned5 = Html.fromHtml(str5);
        int length5 = spanned5.length();
        Spanned spanned6 = Html.fromHtml(String.format(str6, bean.getLevel4Count()));
        int length6 = spanned6.length();
        Spanned spanned7 = Html.fromHtml(str7);
        int length7 = spanned7.length();
        Spanned spanned8 = Html.fromHtml(String.format(str8, bean.getLevel3Count()));
        int length8 = spanned8.length();
        Spanned spanned9 = Html.fromHtml(str9);
        int length9 = spanned9.length();
        Spanned spanned10 = Html.fromHtml(String.format(str10, bean.getLevel2Count()));
        int length10 = spanned10.length();
        Spanned spanned11 = Html.fromHtml(str11);
        int length11 = spanned11.length();
        Spanned spanned12 = Html.fromHtml(String.format(str12, bean.getLevel1Count()));
        int length12 = spanned12.length();
        Spanned spanned13 = Html.fromHtml(str13);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        spannableStringBuilder.append(spanned1).append(spanned2).append(spanned3).append(spanned4).append(spanned5)
                .append(spanned6).append(spanned7).append(spanned8).append(spanned9).append(spanned10).append(spanned11)
                .append(spanned12).append(spanned13);

        // 特大暴雨点击事件
        spannableStringBuilder.setSpan(new ClickableLinkSpan("1"), length1, length1 + length2,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        // 大暴雨点击事件
        spannableStringBuilder.setSpan(new ClickableLinkSpan("2"), length1 + length2 + length3,
                length1 + length2 + length3 + length4, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        // 暴雨点击事件
        spannableStringBuilder.setSpan(new ClickableLinkSpan("3"), length1 + length2 + length3 + length4 + length5,
                length1 + length2 + length3 + length4 + length5 + length6, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        // 大雨点击事件
        spannableStringBuilder.setSpan(new ClickableLinkSpan("4"),
                length1 + length2 + length3 + length4 + length5 + length6 + length7,
                length1 + length2 + length3 + length4 + length5 + length6 + length7 + length8,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        // 中雨点击事件
        spannableStringBuilder.setSpan(new ClickableLinkSpan("5"),
                length1 + length2 + length3 + length4 + length5 + length6 + length7 + length8 + length9,
                length1 + length2 + length3 + length4 + length5 + length6 + length7 + length8 + length9 + length10,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        // 小雨点击事件
        spannableStringBuilder.setSpan(new ClickableLinkSpan("6"),
                length1 + length2 + length3 + length4 + length5 + length6 + length7 + length8 + length9 + length10
                        + length11,
                length1 + length2 + length3 + length4 + length5 + length6 + length7 + length8 + length9 + length10
                        + length11 + length12,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        mBinding.tvTotalDescribe.setText(spannableStringBuilder);
        mBinding.tvTotalDescribe.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void showRainfallList(RainfallListResponse data) {

        List<RainfallItemBean> list = data.getData().getList();
        int totalSize = data.getData().getTotal();
        if (isFristLoad) {
            mBinding.srflContainer.setRefreshing(false);
            // 无数据
            if (list == null || list.size() == 0) {
                // showEmptyView();
                mAdapter.setEmptyView(EmptyLayoutUtil.showNew("暂无数据"));
            } else {
                dataList.clear();
                mAdapter.addData(list);
                // // 首次加载
                // dataList.addAll(list);
                // // mBinding.rvReservoirList.setAdapter(adapterReservoirRegimenList = new
                // // AdapterReservoirRegimenList(R.layout.lv_item_reservoir_regimen, dataList));
                // mAdapter.notifyDataSetChanged();

            }
            mAdapter.setEnableLoadMore(true);
        } else {
            mAdapter.addData(list);
            currentPage = mAdapter.getData().size();
            if (currentPage >= totalSize) {
                // 数据全部加载完毕
                mAdapter.loadMoreEnd();
                return;
            }
            mAdapter.loadMoreComplete();

        }
    }

    @Override
    public void onClick(View v) {
        // 返回
        if (v.getId() == R.id.tv_left_text) {
            finish();
        }
        // 时间段选择
        else if (v.getId() == R.id.tv_level) {
            showDialog();
        }
        // 起始时间选择
        else if (v.getId() == R.id.et_reservoir_starttime) {
            // 非自定义时间段不可手选时间
            if (!"5".equals(mType)) {
                return;
            }
            isChooseStart = true;
            if (timePickerDialogUtil.startDialog != null) {
                timePickerDialogUtil.startDialog = null;
            }
            timePickerDialogUtil.startDialog = timePickerDialogUtil.builder.build();
            timePickerDialogUtil.startDialog.show(getSupportFragmentManager(), "all");
        }
        // 终止时间选择
        else if (v.getId() == R.id.et_reservoir_endtime) {
            // 非自定义时间段不可手选时间
            if (!"5".equals(mType)) {
                return;
            }
            isChooseStart = false;
            if (timePickerDialogUtil.startDialog != null) {
                timePickerDialogUtil.startDialog = null;
            }
            timePickerDialogUtil.startDialog = timePickerDialogUtil.builder.build();
            timePickerDialogUtil.startDialog.show(getSupportFragmentManager(), "all");
        }
    }

    /**
     * 时间选择器初始化
     */
    private void initTimePicker() {
        Date currentTime = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
        // 起始时间设定为12个月前
        long start = currentTime.getTime() - 365 * 1000 * 60 * 60 * 24L;
        // 终止时间设定为当前时间的下一个小时
        long end = currentTime.getTime() + 1000 * 60 * 60L;

        timePickerDialogUtil = new TimePickerDialogUtil(Utils.getContext(), sf);
        timePickerDialogUtil.initTimePickerSetStartAndEnd(new OnDateSetListener() {
            @Override
            public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                // 当前选择的是起始时间
                if (isChooseStart) {
                    String temp = timePickerDialogUtil.getDateToString(millseconds);
                    if (!TextUtils.isEmpty(timeEnd) && isDateOneBigger(temp, timeEnd)) {
                        ToastUtils.shortToast("起始时间不能大于终止时间");
                        return;
                    }
                    timeStart = timePickerDialogUtil.getDateToString(millseconds);
                    mBinding.etReservoirStarttime.setText(timeStart);
                }
                // 当前选择的是终止时间
                else {
                    String temp = timePickerDialogUtil.getDateToString(millseconds);
                    if (!TextUtils.isEmpty(timeStart) && isDateOneBigger(timeStart, temp)) {
                        ToastUtils.shortToast("终止时间不能大于起始时间");
                        return;
                    }
                    timeEnd = timePickerDialogUtil.getDateToString(millseconds);
                    mBinding.etReservoirEndtime.setText(timeEnd);
                }

                if (!TextUtils.isEmpty(timeStart) && !TextUtils.isEmpty(timeEnd)) {
                    isFristLoad = true;
                    mLevel = "";
                    refreshData();
                }
            }
        }, Type.MONTH_DAY_HOUR_MIN, start, end);

        timeStart = getLastHourTime(+12);
        mBinding.etReservoirStarttime.setText(timeStart);
        timeEnd = getCurrentHourTime();
        mBinding.etReservoirEndtime.setText(timeEnd);
    }

    /**
     * 时间段选择弹出窗
     */
    private void showDialog() {
        String[] stringItems = new String[6];
        stringItems[0] = "1小时";
        stringItems[1] = "3小时";
        stringItems[2] = "6小时";
        stringItems[3] = "12小时";
        stringItems[4] = "24小时";
        stringItems[5] = "自定义";
        final ActionSheetDialog dialog = new ActionSheetDialog(this, stringItems, null);
        dialog.title("请选择时间段").titleTextSize_SP(14.5f).widthScale(0.8f).show();
        dialog.setOnOpenItemClickL(new OnOpenItemClick() {
            @Override
            public void onOpenItemClick(AdapterView<?> parent, View view, int position, long id) {
                mBinding.tvLevel.setText(stringItems[position]);
                switch (position) {
                // 1小时
                case 0:
                    timeStart = getLastHourTime(1);
                    mBinding.etReservoirStarttime.setText(timeStart);
                    timeEnd = getCurrentHourTime();
                    mBinding.etReservoirEndtime.setText(timeEnd);
                    mType = "0";
                    break;
                // 3小时
                case 1:
                    timeStart = getLastHourTime(3);
                    mBinding.etReservoirStarttime.setText(timeStart);
                    timeEnd = getCurrentHourTime();
                    mBinding.etReservoirEndtime.setText(timeEnd);
                    mType = "1";
                    break;
                // 6小时
                case 2:
                    timeStart = getLastHourTime(6);
                    mBinding.etReservoirStarttime.setText(timeStart);
                    timeEnd = getCurrentHourTime();
                    mBinding.etReservoirEndtime.setText(timeEnd);
                    mType = "2";
                    break;
                // 12小时
                case 3:
                    timeStart = getLastHourTime(12);
                    mBinding.etReservoirStarttime.setText(timeStart);
                    timeEnd = getCurrentHourTime();
                    mBinding.etReservoirEndtime.setText(timeEnd);
                    mType = "3";
                    break;
                case 4:
                    timeStart = getLastHourTime(24);
                    mBinding.etReservoirStarttime.setText(timeStart);
                    timeEnd = getCurrentHourTime();
                    mBinding.etReservoirEndtime.setText(timeEnd);
                    mType = "4";
                    break;
                case 5:
                    timeStart = "";
                    mBinding.etReservoirStarttime.setText("请选择");
                    timeEnd = "";
                    mBinding.etReservoirEndtime.setText("请选择");
                    mType = "5";
                    break;
                default:
                    break;
                }
                if (!"5".equals(mType)) {
                    isFristLoad = true;
                    mLevel = "";
                    refreshData();
                }
                dialog.dismiss();

            }
        });
    }

    /**
     * 刷新数据
     */
    private void refreshData() {
        pageNo = 1;
        String period = "";
        if ("0".equals(mType) || "1".equals(mType) || "2".equals(mType) || "5".equals(mType)) {
            period = "";
        } else if ("3".equals(mType)) {
            period = "12";
        } else {
            period = "24";
        }
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);
        String startTime = year + "-" + timeStart + ":00";
        String endTime = year + "-" + timeEnd + ":00";
        mPresenter.getReservoirNum(startTime, endTime, period, false, "");
        mPresenter.getRainfallList(pageNo, netOfpageSize, startTime, endTime, period, mLevel, false, "");
    }

    /**
     * 加载更多数据
     */
    private void loadMoreData() {
        pageNo++;
        String period = "";
        if ("0".equals(mType) || "1".equals(mType) || "2".equals(mType) || "5".equals(mType)) {
            period = "";
        } else if ("3".equals(mType)) {
            period = "12";
        } else {
            period = "24";
        }
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);
        String startTime = year + "-" + timeStart + ":00";
        String endTime = year + "-" + timeEnd + ":00";
        mPresenter.getRainfallList(pageNo, netOfpageSize, startTime, endTime, period, mLevel, false, "");
    }

    private class ClickableLinkSpan extends ClickableSpan {

        private String mId;

        public ClickableLinkSpan(String id) {
            mId = id;
        }

        @Override
        public void onClick(@NonNull View widget) {
            pageNo = 1;
            isFristLoad = true;
            mLevel = mId;
            refreshData();
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(ContextCompat.getColor(RainfallStatisticsActivity.this, R.color.spannalbe_red));
            ds.setUnderlineText(false);
        }
    }

}
