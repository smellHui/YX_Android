package com.tepia.guangdong_module.amainguangdong.xunchaview.activity;

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
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.guangdong_module.R;
import com.example.guangdong_module.databinding.ActivityReservoirRegimenBinding;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.tepia.base.http.LoadingSubject;
import com.tepia.base.mvp.MVPBaseActivity;
import com.tepia.base.utils.TimePickerDialogUtil;
import com.tepia.base.utils.ToastUtils;
import com.tepia.base.utils.Utils;
import com.tepia.base.view.dialog.basedailog.ActionSheetDialog;
import com.tepia.base.view.dialog.basedailog.OnOpenItemClick;
import com.tepia.guangdong_module.amainguangdong.model.reservoirregimen.ReservoirRegimenItemBean;
import com.tepia.guangdong_module.amainguangdong.model.reservoirregimen.ReservoirRegimenResponse;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.UserReservoirCount;
import com.tepia.guangdong_module.amainguangdong.mvp.reserviorlistmvp.ReserviorManager;
import com.tepia.guangdong_module.amainguangdong.mvp.reservoirregimen.ReservoirRegimenContract;
import com.tepia.guangdong_module.amainguangdong.mvp.reservoirregimen.ReservoirRegimenPresenter;
import com.tepia.guangdong_module.amainguangdong.utils.EmptyLayoutUtil;
import com.tepia.guangdong_module.amainguangdong.xunchaview.adapter.AdapterReservoirRegimenList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.tepia.guangdong_module.amainguangdong.utils.TimeUtils.getLastHourTime;
import static com.tepia.guangdong_module.amainguangdong.utils.TimeUtils.isDateOneBigger;

/**
 * Created by Android Studio
 *
 * @author: Arthur
 * Date:    2019/5/29
 * Time:    17:27
 * Describe: 水库水情界面
 */
public class ReservoirRegimenActivity extends MVPBaseActivity<ReservoirRegimenContract.View, ReservoirRegimenPresenter>
        implements ReservoirRegimenContract.View, View.OnClickListener {

    private ActivityReservoirRegimenBinding mBinding;

    /**
     * 时间选择器
     */
    private TimePickerDialogUtil timePickerDialogUtil;

    /**
     * 起始时间
     */
    private String timeStart = "";

    /**
     * 终止时间
     */
    private String timeEnd = "";

    private TextView tv_center_text;

     private TextView tv_left_text;

   /**
     * 判断当前是选择起始时间还是终止时间
     */
    private boolean isChooseStart = false;

    private int netOfpageSize = 10;
    private int netOfcurrentPage = 1;
    private int netOfmCurrentCounter = 0;
    private boolean netOffirst;
    private boolean netOfisloadmore;
    private String mType = "new";
    private AdapterReservoirRegimenList adapterReservoirRegimenList;
    private List<ReservoirRegimenItemBean> dataList;
    private String strTop =
            "<font color=\"#454545\">监测水库</font><font color=\"#000000\">%1$d</font><font color=\"#454545\">宗,</font>"
                    + "<font color=\"#454545\">共有</font><font color=\"#000000\">%2$d</font><font color=\"#454545\">宗水库目前超汛限运行，其中大坝安全鉴定为三类坝的水库共有</font>"
                    + "<font color=\"#B90A0A\">%3$d</font><font color=\"#454545\">座</font><font color=\"#B90A0A\">(查看)</font>";

    @Override
    public int getLayoutId() {
        return R.layout.activity_reservoir_regimen;
    }

    @Override
    public void initData() {
        tv_center_text = findViewById(R.id.tv_center_text);
        tv_left_text = findViewById(R.id.tv_left_text);
        tv_center_text.setText("水库水情");
        mBinding = DataBindingUtil.bind(mRootView);
        mBinding.rvReservoirList.setLayoutManager(new LinearLayoutManager(this));
        dataList = new ArrayList<>();
        // for (int i=0;i<10;i++){
        // dataList.add(new ReservoirRegimenItemBean());
        // }
        adapterReservoirRegimenList = new AdapterReservoirRegimenList(R.layout.lv_item_reservoir_regimen, dataList);
        mBinding.rvReservoirList.setAdapter(adapterReservoirRegimenList);
    }

    @Override
    public void initView() {
        mBinding.srflContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                netOfcurrentPage = 1;
                netOffirst = true;
                netOfisloadmore = false;
                mPresenter.getReservoirRegimenList(netOfcurrentPage, netOfpageSize, mType, "", timeStart, timeEnd,
                        true, "正在加载");
            }
        });
        adapterReservoirRegimenList.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {

            @Override
            public void onLoadMoreRequested() {
                netOfcurrentPage++;
                netOffirst = false;
                netOfisloadmore = true;
                mPresenter.getReservoirRegimenList(netOfcurrentPage, netOfpageSize, mType, "", timeStart, timeEnd, true,
                        "正在加载");
            }
        });
        initTimePicker();
    }

    @Override
    protected void initListener() {
        mBinding.rlChooseType.setOnClickListener(this);
        mBinding.etReservoirEndtime.setOnClickListener(this);
        mBinding.etReservoirStarttime.setOnClickListener(this);
        tv_left_text.setOnClickListener(this);
    }

    @Override
    protected void initRequestData() {
        netOffirst = true;
        netOfisloadmore = false;
        netOfcurrentPage = 1;
        mPresenter.getReservoirRegimenList(netOfcurrentPage, netOfpageSize, mType, "", timeStart, timeEnd, true,
                "正在加载");

        ReserviorManager.getInstance().getUserReservoirCount()
                .subscribe(new LoadingSubject<UserReservoirCount>(false, "") {
                    @Override
                    protected void _onNext(UserReservoirCount userReservoirCount) {
                        if (userReservoirCount != null) {
                            if (userReservoirCount.getCode() == 0) {
                                UserReservoirCount.DataBean dataBean = userReservoirCount.getData();


                                Spanned test;
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                    test = Html.fromHtml(String.format(strTop, dataBean.getAllCount(), dataBean.getOverLevelConut(), dataBean.getThreeDamCount()),
                                            Html.FROM_HTML_MODE_LEGACY);
                                } else {
                                    test = Html.fromHtml(String.format(strTop,dataBean.getAllCount(), dataBean.getOverLevelConut(), dataBean.getThreeDamCount()));
                                }
                                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                                spannableStringBuilder.append(test);
                                ClickableSpan colorSpan = new ClickableSpan() {
                                    @Override
                                    public void onClick(@NonNull View widget) {
                                        netOfcurrentPage = 1;
                                        netOffirst = true;
                                        netOfisloadmore = false;
                                        mPresenter.getReservoirRegimenList(netOfcurrentPage, netOfpageSize, mType, "3", timeStart, timeEnd,
                                                true, "正在加载");
                                    }

                                    @Override
                                    public void updateDrawState(TextPaint ds) {
                                        ds.setColor(ContextCompat.getColor(ReservoirRegimenActivity.this, R.color.spannalbe_red));
                                        ds.setUnderlineText(false);
                                    }
                                };
                                spannableStringBuilder.setSpan(colorSpan, test.length()-4, test.length(),
                                        Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                                mBinding.tvTotalDescribe.setText(spannableStringBuilder);
                                mBinding.tvTotalDescribe.setMovementMethod(LinkMovementMethod.getInstance());
                            }
                        }
                    }

                    @Override
                    protected void _onError(String message) {

                    }
                });
    }

    @Override
    public void onClick(View v) {
        // 水位类型选择
        if (v.getId() == R.id.rl_choose_type) {
            showDialog();
        }
        // 起始查询时间选择
        else if (v.getId() == R.id.et_reservoir_starttime) {
            isChooseStart = true;
            if (timePickerDialogUtil.startDialog != null) {
                timePickerDialogUtil.startDialog = null;
            }
            timePickerDialogUtil.startDialog = timePickerDialogUtil.builder.build();
            timePickerDialogUtil.startDialog.show(getSupportFragmentManager(), "all");
        }
        // 终止查询时间选择
        else if (v.getId() == R.id.et_reservoir_endtime) {
            isChooseStart = false;
            if (timePickerDialogUtil.startDialog != null) {
                timePickerDialogUtil.startDialog = null;
            }
            timePickerDialogUtil.startDialog = timePickerDialogUtil.builder.build();
            timePickerDialogUtil.startDialog.show(getSupportFragmentManager(), "all");
        }
        else if (v.getId() == R.id.tv_left_text){
            finish();
        }
    }

    /**
     * 水位级别选择弹出窗
     */
    private void showDialog() {
        String[] stringItems = new String[2];
        stringItems[0] = "最新";
        stringItems[1] = "最高";
        final ActionSheetDialog dialog = new ActionSheetDialog(this, stringItems, null);
        dialog.title("请选择级别").titleTextSize_SP(14.5f).widthScale(0.8f).show();
        dialog.setOnOpenItemClickL(new OnOpenItemClick() {
            @Override
            public void onOpenItemClick(AdapterView<?> parent, View view, int position, long id) {
                mBinding.tvLevel.setText(stringItems[position]);
                switch (position) {
                case 0:
                    mType = "new";
                    break;
                case 1:
                    mType = "height";
                    break;
                default:
                    break;
                }
                netOffirst = true;
                netOfisloadmore = false;
                netOfcurrentPage = 1;
                mPresenter.getReservoirRegimenList(netOfcurrentPage, netOfpageSize, mType, "", timeStart, timeEnd,
                        true, "正在加载");
                dialog.dismiss();

            }
        });
    }

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
                    if (isDateOneBigger(temp, timeEnd)) {
                        ToastUtils.shortToast("起始时间不能大于终止时间");
                        return;
                    }
                    timeStart = timePickerDialogUtil.getDateToString(millseconds);
                    mBinding.etReservoirStarttime.setText(timeStart);
                }
                // 当前选择的是终止时间
                else {
                    String temp = timePickerDialogUtil.getDateToString(millseconds);
                    if (isDateOneBigger(timeStart, temp)) {
                        ToastUtils.shortToast("终止时间不能大于起始时间");
                        return;
                    }
                    timeEnd = timePickerDialogUtil.getDateToString(millseconds);
                    mBinding.etReservoirEndtime.setText(timeEnd);
                }

                netOffirst = true;
                netOfisloadmore = false;
                netOfcurrentPage = 1;
                mPresenter.getReservoirRegimenList(netOfcurrentPage, netOfpageSize, mType, "", timeStart, timeEnd,
                        true, "正在加载");
            }
        }, Type.MONTH_DAY_HOUR_MIN, start, end);

        timeStart = getLastHourTime(23);
        mBinding.etReservoirStarttime.setText(timeStart);
        timeEnd = getLastHourTime(-1);
        mBinding.etReservoirEndtime.setText(timeEnd);
    }



    @Override
    public void onFail(String msg) {
        ToastUtils.shortToast(msg);
    }

    @Override
    public void showList(ReservoirRegimenResponse data) {
        showData(data);
    }

    private void showData(ReservoirRegimenResponse data) {
        List<ReservoirRegimenItemBean> list = data.getData().getList();
        int totalSize = data.getData().getTotal();
        if (netOffirst) {
            mBinding.srflContainer.setRefreshing(false);
            // 首次加载
            if (list == null || list.size() == 0) {
                // showEmptyView();
                adapterReservoirRegimenList.setEmptyView(EmptyLayoutUtil.showNew("暂无数据"));
            } else {
                dataList.clear();
                // 首次加载
                dataList.addAll(list);
                // mBinding.rvReservoirList.setAdapter(adapterReservoirRegimenList = new
                // AdapterReservoirRegimenList(R.layout.lv_item_reservoir_regimen, dataList));
                adapterReservoirRegimenList.notifyDataSetChanged();

            }
            adapterReservoirRegimenList.setEnableLoadMore(true);
        } else if (netOfisloadmore) {

            adapterReservoirRegimenList.addData(list);
            netOfmCurrentCounter = adapterReservoirRegimenList.getData().size();
            if (netOfmCurrentCounter >= totalSize) {
                // 数据全部加载完毕
                adapterReservoirRegimenList.loadMoreEnd();
                return;
            }
            adapterReservoirRegimenList.loadMoreComplete();

        }
    }
}
