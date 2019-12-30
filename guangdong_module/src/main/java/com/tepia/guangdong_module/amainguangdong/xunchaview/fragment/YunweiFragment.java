package com.tepia.guangdong_module.amainguangdong.xunchaview.fragment;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.guangdong_module.R;
import com.example.guangdong_module.databinding.FragmentYunweiBinding;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.tepia.base.ConfigConsts;
import com.tepia.base.mvp.MVPBaseFragment;
import com.tepia.base.utils.DoubleClickUtil;
import com.tepia.base.utils.LogUtil;
import com.tepia.base.utils.TimeFormatUtils;
import com.tepia.base.utils.TimePickerDialogUtil;
import com.tepia.base.utils.ToastUtils;
import com.tepia.base.utils.Utils;
import com.tepia.base.view.dialog.loading.SimpleLoadDialog;
import com.tepia.guangdong_module.amainguangdong.common.UserManager;
import com.tepia.guangdong_module.amainguangdong.model.UserInfoBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.ReservoirBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.RouteListBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.RoutePosition;
import com.tepia.guangdong_module.amainguangdong.mvp.taskdetail.YunWeiListContract;
import com.tepia.guangdong_module.amainguangdong.mvp.taskdetail.YunWeiListPresenter;
import com.tepia.guangdong_module.amainguangdong.route.TaskBean;
import com.tepia.guangdong_module.amainguangdong.route.TaskBeanFromNet;
import com.tepia.guangdong_module.amainguangdong.route.TaskItemBean;
import com.tepia.guangdong_module.amainguangdong.route.TaskItemBeanFromNet;
import com.tepia.guangdong_module.amainguangdong.utils.EmptyLayoutUtil;
import com.tepia.guangdong_module.amainguangdong.xunchaview.activity.StartInspectionActivity;
import com.tepia.guangdong_module.amainguangdong.xunchaview.adapter.AdapterPatrolWorkOrderList;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by      Android studio
 *
 * @author :ly (from Center Of Wuhan)
 * 创建时间 :2019-4-28
 * 更新时间 :
 * Version :1.0
 * 功能描述 :
 **/
public class YunweiFragment extends MVPBaseFragment<YunWeiListContract.View, YunWeiListPresenter> implements YunWeiListContract.View<Object>, OnDateSetListener {
    FragmentYunweiBinding mBinding;

    private ReservoirBean selectedResrvoir;

    /**
     * 本地数据库
     */
    private AdapterPatrolWorkOrderList adapterPatrolWorkOrderList;
    List<TaskBean> taskBeanList = new ArrayList<>();
    private int pageSize = 10;
    private int currentPage = 1;
    private int mCurrentCounter = 0;
    private boolean first;
    private boolean isloadmore;
    private int totalSize;

    /**
     * 服务器数据
     */
    private AdapterPatrolWorkOrderList netOfadapterPatrolWorkOrderList;
    List<TaskBean> netOftaskBeanList = new ArrayList<>();

    private int netOfpageSize = 10;
    private int netOfcurrentPage = 1;
    private int netOfmCurrentCounter = 0;
    private boolean netOffirst;
    private boolean netOfisloadmore;

    /**
     * 时间选择器
     */
    private String startTimeOfnet = "" ;
    private String endTimeOfnet = "";
    /**时间类型
     *
     * 查询类型（years 逐年，month 逐月，day 逐日，time 逐时）
     *
     */
    private String timeType = "";
    private String stcdstr = "";

    private TextView mstarttimeTv;
    private TextView mendtimeTv;



    String userCode;
    String reservoirId = "";
    int positionItem;
    public static final int resultCode = 1101;



    private String startTimeOrCreattime;
    private String routeName;
    private String routeType;

    private int currentStatus = 1;

    public YunweiFragment() {
    }


    // 起始时间选择器
    private boolean startflag = false;
    // 终止时间选择器
    private boolean endflag = false;
    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private long last_millseconds_start = 0;
    private long last_millseconds_end = 0;

    private TimePickerDialogUtil timePickerDialogUtil;
    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {

        if (startflag) {
            last_millseconds_start = millseconds;
            String text = timePickerDialogUtil.getDateToString(millseconds);
            startTimeOfnet = text;
            mstarttimeTv.setText(startTimeOfnet);
            startflag = false;

        }
        else if(endflag) {
            last_millseconds_end = millseconds;
            String text = timePickerDialogUtil.getDateToString(millseconds);
            endTimeOfnet = text;
            mendtimeTv.setText(endTimeOfnet);
            endflag = false;

        }

    }

    /**
     * init timePicker
     */
    public void firstSearch(){
        if (timePickerDialogUtil == null) {
            timePickerDialogUtil = new TimePickerDialogUtil(Utils.getContext(),sf);
            timePickerDialogUtil.initTimePicker(this, Type.ALL);
        }
        endTimeOfnet = TimeFormatUtils.getStringDate();
        last_millseconds_end = TimeFormatUtils.strToLong(endTimeOfnet);
        //往前一个月
        startTimeOfnet = TimeFormatUtils.getNextDay(endTimeOfnet,30,-1,sf);
        last_millseconds_start = TimeFormatUtils.strToLong(startTimeOfnet);
        mstarttimeTv.setText(startTimeOfnet);
        mendtimeTv.setText(endTimeOfnet);
    }

    SimpleLoadDialog simpleLoadDialog = null;
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int i = view.getId();
            if (i == R.id.mstarttimeTv) {
                if (timePickerDialogUtil.startDialog != null) {
                    timePickerDialogUtil.startDialog = null;
                }
                startflag = true;
                if (last_millseconds_start != 0) {
                    timePickerDialogUtil.builder.setCurrentMillseconds(last_millseconds_start);
                }
                timePickerDialogUtil.builder.setTitleStringId("请选择起始时间");
                timePickerDialogUtil.startDialog = timePickerDialogUtil.builder.build();
                timePickerDialogUtil.startDialog.show(getFragmentManager(), "all");


            } else if (i == R.id.mendtimeTv) {
                if (timePickerDialogUtil.startDialog != null) {
                    timePickerDialogUtil.startDialog = null;
                }
                endflag = true;
                if (last_millseconds_end != 0) {
                    timePickerDialogUtil.builder.setCurrentMillseconds(last_millseconds_end);
                }
                timePickerDialogUtil.builder.setTitleStringId("请选择终止时间");
                timePickerDialogUtil.startDialog = timePickerDialogUtil.builder.build();

                timePickerDialogUtil.startDialog.show(getFragmentManager(), "all");

            } else if(i == R.id.searchTv){
                if (DoubleClickUtil.isFastDoubleClick()) {
                    return;
                }
                if (last_millseconds_end < last_millseconds_start) {
                    ToastUtils.shortToast("起始日期不能大于终止日期");
                    return;
                }
                if (currentStatus == 1) {
                    if (simpleLoadDialog == null) {
                        simpleLoadDialog = new SimpleLoadDialog(getBaseActivity(),"正在查询...",false);

                    }
                    simpleLoadDialog.show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (simpleLoadDialog != null) {
                                simpleLoadDialog.dismiss();
                            }
                        }
                    },800);
                    refresh();
                }else{
                    netOfrefresh(true);
                }
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_yunwei;
    }

    @Override
    protected void initData() {
        mBinding = DataBindingUtil.bind(mRootView);


    }

    @Override
    protected void initView(View view) {
        setCenterTitle(getString(R.string.title_dashboard));
        initRec();
        initRecOfnet();
        mstarttimeTv = findView(R.id.mstarttimeTv);
        mendtimeTv = findView(R.id.mendtimeTv);
        TextView searchTv = findView(R.id.searchTv);
        searchTv.setOnClickListener(onClickListener);
        mstarttimeTv.setOnClickListener(onClickListener);
        mendtimeTv.setOnClickListener(onClickListener);
//        firstSearch();

        currentStatus = 1;
        mBinding.swipeRl.setVisibility(View.VISIBLE);
        mBinding.swipeRlOfNet.setVisibility(View.GONE);
        mBinding.baseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.swipeRl.setVisibility(View.VISIBLE);
                mBinding.swipeRlOfNet.setVisibility(View.GONE);
                currentStatus = 1;
                clickRunmode(currentStatus);
            }
        });

        mBinding.quanbuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.swipeRlOfNet.setVisibility(View.VISIBLE);
                mBinding.swipeRl.setVisibility(View.GONE);
                currentStatus = 2;
                clickRunmode(currentStatus);

//                netOfrefresh(true);


            }
        });
    }

    /**
     * 点击效果
     * @param runmode
     */
    private void clickRunmode(int runmode) {
       /* Drawable wanddrawable = ContextCompat.getDrawable(getContext(),
                R.drawable.switch_button_left_checked);
        Drawable baobaodrawable = ContextCompat.getDrawable(getContext(), R.drawable.switch_button_right);*/
        int whiteInt = ContextCompat.getColor(getContext(), R.color.white);
        int leftColor = ContextCompat.getColor(getContext(), R.color.net_color_solidleft);
        if (runmode == 2) {
            mBinding.baseBtn.setBackgroundResource(R.drawable.switch_button_left);

            mBinding.baseBtn.setTextColor(leftColor);
            mBinding.quanbuBtn.setBackgroundResource(R.drawable.switch_button_right_checked);
            mBinding.quanbuBtn.setTextColor(whiteInt);
        } else if (runmode == 1) {

            mBinding.baseBtn.setBackgroundResource(R.drawable.switch_button_left_checked);
            mBinding.baseBtn.setTextColor(whiteInt);

            mBinding.quanbuBtn.setBackgroundResource(R.drawable.switch_button_right);
            mBinding.quanbuBtn.setTextColor(leftColor);
        }
    }

    private void initRec(){
        mBinding.rvWorkOrderList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterPatrolWorkOrderList = new AdapterPatrolWorkOrderList(R.layout.lv_patrol_workorder_list, taskBeanList);
        mBinding.rvWorkOrderList.setAdapter(adapterPatrolWorkOrderList);
        mBinding.swipeRl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                selectedResrvoir = null;
                refresh();
                mBinding.swipeRl.setRefreshing(false);
            }
        });

        adapterPatrolWorkOrderList.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                positionItem = position;
                List<TaskBean> taskBeans = adapter.getData();
                TaskBean taskBean = taskBeans.get(position);
                if (taskBean != null) {
                    LogUtil.e(YunweiFragment.class.getName(), "工单id-------" + taskBean.getWorkOrderId());
                    goStart(taskBean.getWorkOrderId());
                }

            }
        });

        adapterPatrolWorkOrderList.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {

                LogUtil.e("开始加载更多----------" + currentPage);
                mBinding.rvWorkOrderList.postDelayed(() -> {
                    currentPage += pageSize;
                    first = false;
                    isloadmore = true;

                    List<TaskBean> taskBeanList = getTaskBeanList(userCode, reservoirId, "");
                    showData(taskBeanList);


                }, 1000);


            }
        }, mBinding.rvWorkOrderList);
    }

    /**
     * 初始化服务器rec
     */
    private void initRecOfnet(){
        mBinding.rvWorkOrderListOfnet.setLayoutManager(new LinearLayoutManager(getContext()));
        netOfadapterPatrolWorkOrderList = new AdapterPatrolWorkOrderList(R.layout.lv_patrol_workorder_list, netOftaskBeanList);
        mBinding.rvWorkOrderListOfnet.setAdapter(netOfadapterPatrolWorkOrderList);
        mBinding.swipeRlOfNet.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                selectedResrvoir = null;
                netOfrefresh(false);
                mBinding.swipeRlOfNet.setRefreshing(false);
            }
        });

        netOfadapterPatrolWorkOrderList.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                TaskBean taskBean = netOftaskBeanList.get(position);
                if (taskBean != null) {
                    LogUtil.e(YunweiFragment.class.getName(), "工单id-------" + taskBean.getWorkOrderId());

                    TaskBean taskBean1 = DataSupport.where("workOrderId=?",taskBean.getWorkOrderId()).findFirst(TaskBean.class);
                    if (taskBean1 != null && "3".equals(taskBean1.getExecuteStatus())) {
                        //如果本地已有此工单则走本地
                        goStart(taskBean.getWorkOrderId());

                    } else {
                        //没有，则走网络
                        startTimeOrCreattime = taskBean.getCreateDate();
                        routeName = taskBean.getRouteName();
                        routeType = taskBean.getRouteType();
                        getByworkOrderId(taskBean);
                    }

                }

            }
        });

        netOfadapterPatrolWorkOrderList.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {

                LogUtil.e("开始加载更多----------" + netOfcurrentPage);
                mBinding.rvWorkOrderListOfnet.postDelayed(() -> {
                    netOfcurrentPage++;
                    netOffirst = false;
                    netOfisloadmore = true;
                    mPresenter.getTaskBeanList(selectedResrvoir.getReservoirId(),startTimeOfnet,endTimeOfnet,netOfcurrentPage,netOfpageSize,false);


                }, 1000);


            }
        }, mBinding.rvWorkOrderListOfnet);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == resultCode) {
            adapterPatrolWorkOrderList.notifyItemChanged(positionItem);
        }
    }

    /**
     * 刷新方法
     */
    public void refresh() {


        ReservoirBean defaultReservoir = UserManager.getInstance().getDefaultReservoir();
        if (selectedResrvoir == null || (selectedResrvoir != null && defaultReservoir != null && !selectedResrvoir.getReservoirId().equals(defaultReservoir.getReservoirId()))) {
            selectedResrvoir = defaultReservoir;
            reservoirId = selectedResrvoir.getReservoirId();
        }
        UserInfoBean.DataBean dataBean = UserManager.getInstance().getUserBean().getData();
        userCode = dataBean.getUserCode();

        if (last_millseconds_end < last_millseconds_start) {
            ToastUtils.shortToast("起始日期不能大于终止日期");
            return;
        }

        List<TaskBean> taskBeanListToal = getAllTaskBeanList(userCode,reservoirId,startTimeOfnet,endTimeOfnet);
        if (taskBeanListToal != null) {
            totalSize = taskBeanListToal.size();
        }

        LogUtil.e(YunweiFragment.class.getName(), "------------当前水库" + selectedResrvoir.getReservoir());
        if (adapterPatrolWorkOrderList != null) {
            LogUtil.e(YunweiFragment.class.getName(), "------------当前水库" + selectedResrvoir.getReservoir());
            adapterPatrolWorkOrderList.setEnableLoadMore(false);
            adapterPatrolWorkOrderList.notifyDataSetChanged();
            currentPage = 0;
            first = true;
            isloadmore = false;


            mBinding.loHeader.tvReservoirName.setText(selectedResrvoir.getReservoir());
            if (taskBeanList != null) {
                taskBeanList.clear();
            }
            List<TaskBean> taskBeanList = getTaskBeanList(userCode, reservoirId, "");
            showData(taskBeanList);

        }


    }

    private void search(boolean isshowloadiing) {
        if (netOfadapterPatrolWorkOrderList != null) {

            netOfadapterPatrolWorkOrderList.setEnableLoadMore(false);
            netOftaskBeanList.clear();
            netOfadapterPatrolWorkOrderList.notifyDataSetChanged();
            netOfcurrentPage = 1;
            netOffirst = true;
            netOfisloadmore = false;
            if (mPresenter != null) {
                mPresenter.getTaskBeanList(selectedResrvoir.getReservoirId(),startTimeOfnet,endTimeOfnet,netOfcurrentPage,netOfpageSize,isshowloadiing);

            }
        }

    }

    /**
     * 服务器数据刷新
     * @param isshowloading
     */
    public void netOfrefresh(boolean isshowloading){
        ReservoirBean defaultReservoir = UserManager.getInstance().getDefaultReservoir();
        if (selectedResrvoir == null || (selectedResrvoir != null && defaultReservoir != null && !selectedResrvoir.getReservoirId().equals(defaultReservoir.getReservoirId()))) {
            selectedResrvoir = defaultReservoir;
            reservoirId = selectedResrvoir.getReservoirId();
        }
        UserInfoBean.DataBean dataBean = UserManager.getInstance().getUserBean().getData();
        userCode = dataBean.getUserCode();

        if (last_millseconds_end < last_millseconds_start) {
            ToastUtils.shortToast("起始日期不能大于终止日期");
            return;
        }

        search(isshowloading);

    }

    /**
     * 分页查询本地数据库
     *
     * @param userCode
     * @param reservoirId
     * @param executeStatus
     * @return
     */
    public List<TaskBean> getTaskBeanList(String userCode, String reservoirId, String executeStatus) {
        List<TaskBean> templist = new ArrayList<>();

        templist = DataSupport.where("userCode=? and reservoirId=? and workorderid is not null and starttime > ? and starttime < ?", userCode, reservoirId,startTimeOfnet,endTimeOfnet)
                .order("starttime desc")
                .limit(pageSize)
                .offset(currentPage)
                .find(TaskBean.class);
        return templist;
    }

    /**
     * 查询全部
     * @param userCode
     * @param reservoirId
     * @param starttime
     * @param endtime
     * @return
     */
    private  List<TaskBean> getAllTaskBeanList(String userCode, String reservoirId, String starttime, String endtime){
        List<TaskBean> taskBeanListToal = DataSupport.where("userCode=? and reservoirId=? and workorderid is not null and starttime > ? and starttime < ?", userCode, reservoirId,starttime,endtime)
                .find(TaskBean.class);
        return taskBeanListToal;
    }

    /**
     * 展示数据
     *
     * @param data
     */
    private void showData(List<TaskBean> data) {


        if (first) {
            //首次加载
            if (data == null || data.size() == 0) {
//                        showEmptyView();
                View view = LayoutInflater.from(Utils.getContext()).inflate(R.layout.view_empty_list_view, null);
                adapterPatrolWorkOrderList.setEmptyView(view);
            } else {
                taskBeanList.clear();
                //首次加载
                taskBeanList.addAll(data);
                adapterPatrolWorkOrderList.notifyDataSetChanged();
            }
            adapterPatrolWorkOrderList.setEnableLoadMore(true);
        } else if (isloadmore) {

            adapterPatrolWorkOrderList.addData(data);
            mCurrentCounter = adapterPatrolWorkOrderList.getData().size();
            if (mCurrentCounter >= totalSize) {
                //数据全部加载完毕
                adapterPatrolWorkOrderList.loadMoreEnd();
                return;
            }
            adapterPatrolWorkOrderList.loadMoreComplete();

        }

    }

    @Override
    protected void initRequestData() {
        LogUtil.e("mess");
    }

    @Override
    public void onResume() {
        super.onResume();
//        refresh();

    }

    /**
     * 获取工单详情
     * @param taskBean
     */
    private void getByworkOrderId(TaskBean taskBean) {
        mPresenter.getByworkOrderId(taskBean.getWorkOrderId());
    }

    /**
     * 服务器数据
     * @param taskBeanFromNet
     */
    private void netOfshowData( TaskBeanFromNet taskBeanFromNet ){
        List<TaskBean> data = taskBeanFromNet.getData().getList();
        int totalSize = taskBeanFromNet.getData().getTotal();
        if (netOffirst) {
            //首次加载
            if (data == null || data.size() == 0) {
//                        showEmptyView();
                netOfadapterPatrolWorkOrderList.setEmptyView(EmptyLayoutUtil.showNew("暂无数据"));
            } else {
                netOftaskBeanList.clear();
                //首次加载
                netOftaskBeanList.addAll(data);
                netOfadapterPatrolWorkOrderList.notifyDataSetChanged();
              
            }
            netOfadapterPatrolWorkOrderList.setEnableLoadMore(true);
        } else if (netOfisloadmore) {

            netOfadapterPatrolWorkOrderList.addData(data);
            netOfmCurrentCounter = netOfadapterPatrolWorkOrderList.getData().size();
            if (netOfmCurrentCounter >= totalSize) {
                //数据全部加载完毕
                netOfadapterPatrolWorkOrderList.loadMoreEnd();
                return;
            }
            netOfadapterPatrolWorkOrderList.loadMoreComplete();

        }
    }

    @Override
    public void success(Object data) {
        if (data instanceof TaskBeanFromNet) {
           LogUtil.e("-------------网络获取工单成功");
            TaskBeanFromNet taskBeanFromNet = (TaskBeanFromNet) data;
            netOfshowData(taskBeanFromNet);

        } else if (data instanceof TaskItemBeanFromNet) {
            LogUtil.e("-----------------网络获取工单详情成功");
            //本地有相同workorderid的taskitembean则更新，否则保存，此操作成功后跳转下一页

            TaskItemBeanFromNet taskItemBeanFromNet = (TaskItemBeanFromNet) data;
            if (taskItemBeanFromNet != null) {
                TaskItemBeanFromNet.DataBean dataBean = taskItemBeanFromNet.getData();
                if (dataBean != null) {
                    String userCodeOfnet = dataBean.getUserCode();
                    String workOrderId = dataBean.getWorkOrderId();
                    String id = dataBean.getId();
                    String executorName = dataBean.getExecutorName();
                    List<TaskItemBean> taskItemBeanList = dataBean.getItemList();
                    String roleName = dataBean.getRoleName();
                    String routeContent = dataBean.getRouteContent();
                    List<RoutePosition> routePositionList = dataBean.getRoutePositions();
                    String workOrderName = dataBean.getWorkOrderName();
                    String workorderroute = dataBean.getWorkOrderRoute();
//                    String routeType = dataBean.getRouteType();
//                    String routeName = dataBean.getRouteName();

                    TaskItemBean taskItemBean = DataSupport.where("workorderid=?", workOrderId).findFirst(TaskItemBean.class);
                    if (taskItemBean == null) {
                        //本地不存在工单详情数据，则保存
                        TaskBean taskBean = new TaskBean();
                        taskBean.setWorkOrderId(workOrderId);
                        taskBean.setRouteId(id);
                        taskBean.setReservoirId(selectedResrvoir.getReservoirId());
                        taskBean.setReservoir(selectedResrvoir.getReservoir());
                        taskBean.setUserCode(userCodeOfnet);
                        taskBean.setRoleName(roleName);
                        taskBean.setExecutorName(executorName);
                        taskBean.setStartTime(startTimeOrCreattime);
                        taskBean.setExecuteStatus("3");
                        //工单名字
                        taskBean.setRouteName(routeName);
                        taskBean.setWorkOrderRoute(workorderroute);
                        taskBean.save();

                        RouteListBean routeListBeanNew = new RouteListBean();
                        routeListBeanNew.setWorkOrderId(workOrderId);
                        routeListBeanNew.setId(id);
                        routeListBeanNew.setRouteContent(routeContent);
                        routeListBeanNew.setUserCode(userCodeOfnet);
                        routeListBeanNew.setReservoirId(selectedResrvoir.getReservoirId());
                        routeListBeanNew.setItemList(taskItemBeanList);
                        routeListBeanNew.setRouteName(routeName);
                        routeListBeanNew.setRoutePositions(routePositionList);
                        routeListBeanNew.setRouteType(routeType);
                        boolean save = routeListBeanNew.save();

                        RouteListBean routeListBean_a = taskBean.getRouteListBeanByWorkId(workOrderId);


                        if(save && routeListBean_a != null) {
                            MainFragment.saveOther(workOrderId, routeListBeanNew,"1",userCodeOfnet);
                            goStart(workOrderId);
                        }

                    } /*else {
                        TaskBean taskBean = DataSupport.where("workorderid=?", workOrderId).findFirst(TaskBean.class);
                        taskBean.setRoleName(roleName);
                        taskBean.updateAll("workorderid=?", workOrderId);
                        goStart(workOrderId);
                    }*/
                }
            }
        }

    }

    private void goStart(String workorderid) {
        Intent intent = new Intent();
        intent.setClass(getBaseActivity(), StartInspectionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ConfigConsts.Workorderid, workorderid);
        intent.putExtras(bundle);
        startActivityForResult(intent, resultCode);
    }

    @Override
    public void failure(String mes) {
        ToastUtils.shortToast(mes+" ");
    }


}
