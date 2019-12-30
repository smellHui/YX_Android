package com.yangj.dahemodule.activity.patrol;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.bilibili.boxing_impl.view.SpacesItemDecoration;
import com.tepia.base.ConfigConsts;
import com.tepia.base.http.BaseResponse;
import com.tepia.base.http.LoadingSubject;
import com.tepia.base.mvp.BaseActivity;
import com.tepia.base.utils.LogUtil;
import com.tepia.base.utils.ToastUtils;
import com.tepia.base.utils.UUIDUtil;
import com.tepia.base.view.floatview.CollectionsUtil;
import com.tepia.guangdong_module.amainguangdong.common.UserManager;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.ReservoirBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.RouteListBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.RoutePosition;
import com.tepia.guangdong_module.amainguangdong.route.TaskBean;
import com.tepia.guangdong_module.amainguangdong.route.TaskItemBean;
import com.tepia.guangdong_module.amainguangdong.utils.SqlManager;
import com.tepia.guangdong_module.amainguangdong.xunchaview.fragment.MainFragment;
import com.tepia.photo_picker.entity.CheckTaskPicturesBean;
import com.yangj.dahemodule.R;
import com.yangj.dahemodule.adapter.PatrolRecordAdapter;
import com.yangj.dahemodule.common.HttpManager;
import com.yangj.dahemodule.model.main.ReservoirStructure;
import com.yangj.dahemodule.model.patrol.PatrolFlowBean;
import com.yangj.dahemodule.model.patrol.PatrolRecordBean;
import com.yangj.dahemodule.model.patrol.PatrolRecordDataBean;
import com.yangj.dahemodule.model.xuncha.ProtalBean;
import com.yangj.dahemodule.model.xuncha.ProtalDataBean;
import com.yangj.dahemodule.model.xuncha.ProtalItemBean;
import com.yangj.dahemodule.util.UiHelper;
import com.yangj.dahemodule.view.WrapLayoutManager;
import com.yangj.dahemodule.view.patrol.PatrolInfoView;
import com.yangj.dahemodule.wrap.SubmitTaskWrap;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author:xch
 * Date:2019/12/23
 * Description:巡查记录
 */
public class PatrolRecordActivity extends BaseActivity implements PatrolRecordAdapter.ReviewPatrolListener {

    private RecyclerView rv;
    private PatrolInfoView patrolInfoView;
    private PatrolRecordAdapter patrolRecordAdapter;
    private String omRecordCode;

    @Override
    public int getLayoutId() {
        return R.layout.activity_patrol_record;
    }

    @Override
    public void initData() {
        EventBus.getDefault().register(this);
        Intent intent = getIntent();
        if (intent != null) {
            omRecordCode = intent.getStringExtra("omRecordCode");
        }
    }

    @Override
    public void initView() {
        setLeftTitle("巡查记录");
        showBack();
        patrolInfoView = findViewById(R.id.view_patrol_info);
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new WrapLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rv.addItemDecoration(new SpacesItemDecoration(2));
        patrolRecordAdapter = new PatrolRecordAdapter(this);
        patrolInfoView.setOnClickListener(v -> {
            SqlManager.getInstance().deleteTask(omRecordCode);
            loadPatrolDetail(omRecordCode);
        });

        rv.setAdapter(patrolRecordAdapter);

    }

    @Override
    protected void initListener() {
        loadData();
    }

    @Override
    protected void initRequestData() {

    }

    /**
     * 审核处理
     *
     * @param resultType
     * @param resultDes
     */
    @Override
    public void reviewPatrolClick(String resultType, String resultDes) {
        HttpManager.getInstance().examinePatrol("omRecordCode", omRecordCode, "resultDes", resultDes, "resultType", resultType)
                .subscribe(new LoadingSubject<BaseResponse>(true, "提交中...") {

                    @Override
                    protected void _onNext(BaseResponse baseResponse) {
                        loadData();
                    }

                    @Override
                    protected void _onError(String message) {

                    }
                });
    }

    /**
     * 加载详情数据
     */
    private void loadData() {
        HttpManager.getInstance().loadPatrolDetailAndFlow(omRecordCode)
                .subscribe(new LoadingSubject<PatrolRecordDataBean>() {

                    @Override
                    protected void _onNext(PatrolRecordDataBean recordDataBean) {
                        if (recordDataBean == null) return;
                        PatrolRecordBean patrolRecordBean = recordDataBean.getData();
                        if (patrolRecordBean == null) return;
                        patrolInfoView.setDate(patrolRecordBean.getRecordName(), patrolRecordBean.getCreateTime(), "宜兴水库");
                        List<PatrolFlowBean> flowList = patrolRecordBean.getFlowList();
                        patrolRecordAdapter.setNewData(flowList);
                    }

                    @Override
                    protected void _onError(String message) {

                    }
                });
    }

    private void loadPatrolDetail(String omRecordCode) {
        HttpManager.getInstance().getPatrolDetail(omRecordCode)
                .subscribe(new LoadingSubject<ProtalDataBean>() {

                    @Override
                    protected void _onNext(ProtalDataBean reportDataBean) {
                        if (reportDataBean == null) return;
                        LogUtil.i(reportDataBean.toString());
                        doPatrolDetail(reportDataBean.getData());
                    }

                    @Override
                    protected void _onError(String message) {
                        ToastUtils.shortToast(message);
                    }
                });
    }

    private void doPatrolDetail(ProtalBean protalBean) {
        if (protalBean == null) return;
        String workOrderId = protalBean.getCode();
        List<ProtalItemBean> itemList = protalBean.getItemList();
        List<ReservoirStructure> reservoirStructureList = protalBean.getReservoirStructureList();
        ReservoirBean selectedResrvoir = UserManager.getInstance().getDefaultReservoir();
        if (selectedResrvoir == null) {
            ToastUtils.shortToast("暂无水库信息");
            return;
        }

        String userCode = protalBean.getCreatorName();
        //本地不存在工单详情数据，则保存
        TaskBean taskBean = new TaskBean();
        taskBean.setWorkOrderId(workOrderId);
        taskBean.setRouteId(protalBean.getOmRouteId());
        taskBean.setReservoirId(selectedResrvoir.getReservoirId());
        taskBean.setReservoir(selectedResrvoir.getReservoir());
        taskBean.setUserCode(userCode);
        taskBean.setHasCreated(true);
        taskBean.setRoleName(protalBean.getCreatorName());
        taskBean.setExecutorName(protalBean.getCreatorName());
        taskBean.setStartTime(protalBean.getCreateTime());
        taskBean.setExecuteStatus(protalBean.isExecuting() ? "2" : "3");
        //工单名字
        taskBean.setRouteName(protalBean.getName());
        taskBean.setWorkOrderRoute(protalBean.getOmPath());
        taskBean.saveOrUpdate("workOrderId=?", workOrderId);

        List<TaskItemBean> taskItemBeanList = new ArrayList<>();
        if (!CollectionsUtil.isEmpty(itemList)) {
            for (ProtalItemBean protalItemBean : itemList) {
                TaskItemBean taskItem = new TaskItemBean();
                taskItem.setItemId(protalItemBean.getCode());
                taskItem.setWorkOrderId(workOrderId);
                taskItem.setUserCode(userCode);
                taskItem.setPositionTreeNames(protalItemBean.getPositionName());
                taskItem.setPositionName(protalItemBean.getPositionName());
                taskItem.setSuperviseItemName(protalItemBean.getOmItemName());
                taskItem.setSuperviseItemContent(protalItemBean.getOmItemContent());
                taskItem.setPositionLatitude(protalItemBean.getPositionLatitude());
                taskItem.setPositionLongitude(protalItemBean.getPositionLongitude());
                taskItem.setReservoirId(selectedResrvoir.getReservoirId());
                taskItem.setPositionId(protalItemBean.getReservoirStructureId());
                taskItem.setContent(protalItemBean.getOmItemContent());
                taskItem.setResultType(protalItemBean.getOmItemResultType());
                taskItem.setExecuteResultType(protalItemBean.getExecuteResultType());
                taskItem.setExcuteDate(protalItemBean.getExecuteTime());
                taskItem.setCompleteStatus("1");
                taskItem.setExcuteLatitude(protalItemBean.getExecuteLatitude());
                taskItem.setExcuteLongitude(protalItemBean.getExecuteLongitude());
                taskItem.setExecuteResultDescription(protalItemBean.getExecuteResultDescription());
                taskItem.setLastExcuteResultType(protalItemBean.getLastExecuteResultType());
                String pictures = protalItemBean.getPictures();
                if (!TextUtils.isEmpty(pictures)) {
                    String[] paths = pictures.split(",");
                    List<TaskItemBean.ISysFileUploadsBean> iSysFileUploadsBeans = new ArrayList<>();
                    List<CheckTaskPicturesBean> checkTaskPicturesBeans = new ArrayList<>();
                    for (String path : paths) {
                        TaskItemBean.ISysFileUploadsBean fileUploadsBean = new TaskItemBean.ISysFileUploadsBean();
                        fileUploadsBean.setFilePath(path);
                        iSysFileUploadsBeans.add(fileUploadsBean);

                        CheckTaskPicturesBean checkTaskPicturesBean = new CheckTaskPicturesBean();
                        checkTaskPicturesBean.setFilePath(path);
                        LogUtil.e(MainFragment.class.getName(), "---------图片路径：" + path);
                        checkTaskPicturesBean.setHasCheck("1");
                        checkTaskPicturesBean.setBizType(ConfigConsts.picType_trouble);
                        checkTaskPicturesBean.setItemId(taskItem.getItemId());
                        checkTaskPicturesBean.setFileId(UUIDUtil.getUUID32());
                        checkTaskPicturesBeans.add(checkTaskPicturesBean);
                    }
                    DataSupport.saveAll(checkTaskPicturesBeans);
                    List<String> photoPaths = Arrays.asList(paths);
                    taskItem.setPhotoPaths(photoPaths);
                    taskItem.setiSysFileUploads(iSysFileUploadsBeans);
                }
                taskItemBeanList.add(taskItem);
            }
        }

        List<RoutePosition> routePositions = new ArrayList<>();
        if (!CollectionsUtil.isEmpty(reservoirStructureList)) {
            for (ReservoirStructure reservoirStructure : reservoirStructureList) {
                RoutePosition routePosition = new RoutePosition();
                routePosition.setWorkOrderId(workOrderId);
                routePosition.setUserCode(userCode);
                routePosition.setReservoirId(selectedResrvoir.getReservoirId());
                routePosition.setPositionId(reservoirStructure.getId());
                routePosition.setStructureName(reservoirStructure.getStructureName());
                routePosition.setStructurePath(reservoirStructure.getStructurePath());
                routePosition.setPositionLgtd(reservoirStructure.getPositionLongitude());
                routePosition.setPositionLttd(reservoirStructure.getPositionLatitude());
                routePositions.add(routePosition);
            }
        }
        RouteListBean routeListBeanNew = new RouteListBean();
        routeListBeanNew.setWorkOrderId(workOrderId);
        routeListBeanNew.setId(protalBean.getOmRouteId());
        routeListBeanNew.setRouteContent(protalBean.getRoutePath());
        routeListBeanNew.setUserCode(userCode);
        routeListBeanNew.setReservoirId(selectedResrvoir.getReservoirId());
        routeListBeanNew.setItemList(taskItemBeanList);
        routeListBeanNew.setRouteName(protalBean.getName());
        routeListBeanNew.setRoutePositions(routePositions);
//            routeListBeanNew.setRouteType();
        DataSupport.saveAll(taskItemBeanList);
        DataSupport.saveAll(routePositions);
        boolean save = routeListBeanNew.save();

        RouteListBean routeListBean_a = taskBean.getRouteListBeanByWorkId(workOrderId);

        if (save && routeListBean_a != null) {
            if (CollectionsUtil.isEmpty(reservoirStructureList)) {
                UiHelper.goToNoPathMapControlView(this, workOrderId);
            } else {
                UiHelper.goToPatrolMapControlView(this, workOrderId);
            }
        }
    }

    //提交工单成功
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void submitCallBack(SubmitTaskWrap submitTaskWrap) {
        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
