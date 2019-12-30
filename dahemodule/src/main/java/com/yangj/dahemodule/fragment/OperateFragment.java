package com.yangj.dahemodule.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.tepia.base.ConfigConsts;
import com.tepia.base.http.LoadingSubject;
import com.tepia.base.mvp.BaseCommonFragment;
import com.tepia.base.utils.LogUtil;
import com.tepia.base.utils.TimeFormatUtils;
import com.tepia.base.utils.ToastUtils;
import com.tepia.base.utils.UUIDUtil;
import com.tepia.base.view.dialog.permissiondialog.Px2dpUtils;
import com.tepia.base.view.floatview.CollectionsUtil;
import com.tepia.guangdong_module.amainguangdong.common.UserManager;
import com.tepia.guangdong_module.amainguangdong.model.DangerousPosition;
import com.tepia.guangdong_module.amainguangdong.model.UserInfo;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.ReservoirBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.RouteListBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.RoutePosition;
import com.tepia.guangdong_module.amainguangdong.route.TaskBean;
import com.tepia.guangdong_module.amainguangdong.route.TaskItemBean;
import com.tepia.guangdong_module.amainguangdong.xunchaview.fragment.MainFragment;
import com.tepia.photo_picker.entity.CheckTaskPicturesBean;
import com.yangj.dahemodule.R;
import com.yangj.dahemodule.adapter.OperateMainAdapter;
import com.yangj.dahemodule.common.HttpManager;
import com.yangj.dahemodule.model.UserBean;
import com.yangj.dahemodule.model.main.MainBean;
import com.yangj.dahemodule.model.main.MainDataBean;
import com.yangj.dahemodule.model.main.Omltem;
import com.yangj.dahemodule.model.main.ReservoirInfo;
import com.yangj.dahemodule.model.main.ReservoirStructure;
import com.yangj.dahemodule.model.main.Route;
import com.yangj.dahemodule.util.UiHelper;
import com.yangj.dahemodule.view.WrapLayoutManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:xch
 * Date:2019/8/27
 * Description:运维
 */
public class OperateFragment extends BaseCommonFragment {

    private ReservoirBean reservoirBean;
    private OperateMainAdapter operateMainAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_operate;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view) {

        swipeRefreshLayout = findView(R.id.layout_swipe_refresh);
        recyclerView = findView(R.id.rv);
        operateMainAdapter = new OperateMainAdapter();
        recyclerView.setLayoutManager(new WrapLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        int zeroPx = Px2dpUtils.dip2px(getContext(), 0);
        recyclerView.setPadding(zeroPx, zeroPx, zeroPx, zeroPx);
        recyclerView.setAdapter(operateMainAdapter);

        swipeRefreshLayout.setOnRefreshListener(this::loadData);

        operateMainAdapter.setOnItemClickListener((adapter, v, position) -> {
            Route route = (Route) adapter.getItem(position);
            if (route == null) return;
            List<ReservoirStructure> reservoirStructures = route.getReservoirStructureList();
            if (CollectionsUtil.isEmpty(reservoirStructures)) {
                String workOrderId = UUIDUtil.getUUID32();
                TaskBean taskBean = new TaskBean();
                taskBean.setWorkOrderId(workOrderId);
                taskBean.setRouteId(route.getId());
                taskBean.setRouteName(route.getName());
                taskBean.setRouteType(route.getType());
                taskBean.setExecuteStatus("2");
                taskBean.setStartTime(TimeFormatUtils.getStringDate());
                taskBean.saveAsync().listen(success -> {
                    UiHelper.goToNoPathMapControlView(getActivity(), workOrderId);
                });

            } else {
                createWorkOrder(route);
            }
        });
    }

    @Override
    protected void initRequestData() {
        swipeRefreshLayout.setRefreshing(true);
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
                        operateMainAdapter.setNewData(routeList);
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
                            reservoirBean.setManageDepId(reservoirInfo.getReservoirOverview());
                            UserManager.getInstance().saveDefaultReservoir(reservoirBean);
                        }
                    }

                    @Override
                    protected void _onError(String message) {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    private void createWorkOrder(Route route) {
        String workOrderId = UUIDUtil.getUUID32();
        reservoirBean = UserManager.getInstance().getDefaultReservoir();
        if (reservoirBean == null) return;
        String userCode = UserManager.getInstance().getUserCode();
        if (route == null) {
            ToastUtils.shortToast("暂无巡查路线");
            return;
        }
        List<RoutePosition> routePositions = new ArrayList<>();
        List<ReservoirStructure> reservoirStructures = route.getReservoirStructureList();
        if (!CollectionsUtil.isEmpty(reservoirStructures)) {
            for (ReservoirStructure reservoirStructure : reservoirStructures) {
                RoutePosition routePosition = new RoutePosition();
                routePosition.setWorkOrderId(workOrderId);
                routePosition.setUserCode(userCode);
                routePosition.setReservoirId(reservoirBean.getReservoirId());
                routePosition.setStructureName(reservoirStructure.getStructureName());
                routePosition.setStructurePath(reservoirStructure.getStructurePath());
                routePosition.setPositionId(reservoirStructure.getId());
                routePosition.setPositionLgtd(reservoirStructure.getPositionLongitude());
                routePosition.setPositionLttd(reservoirStructure.getPositionLatitude());
                routePositions.add(routePosition);
            }
        }
        List<TaskItemBean> itemList = new ArrayList<>();
        List<Omltem> omltems = route.getOmItemList();
        if (!CollectionsUtil.isEmpty(omltems)) {
            for (Omltem omltem : omltems) {
                TaskItemBean taskItemBean = new TaskItemBean();
                taskItemBean.setWorkOrderId(workOrderId);
                taskItemBean.setUserCode(userCode);
                taskItemBean.setItemId(UUIDUtil.getUUID32());
                taskItemBean.setPositionId(omltem.getReservoirStructureId());
                taskItemBean.setPositionTreeNames(omltem.getPositionName());
                taskItemBean.setSuperviseItemContent(omltem.getOmItemContent());
                taskItemBean.setSuperviseItemCode(omltem.getOmItemId());
                taskItemBean.setOperationLevel(omltem.getOmItemLevel());
                taskItemBean.setReservoirId(reservoirBean.getReservoirId());
                taskItemBean.setSuperviseItemName(omltem.getOmItemName());
                itemList.add(taskItemBean);
            }
        }
        String startTime = TimeFormatUtils.getStringDate();
        RouteListBean routeListBeanNew = new RouteListBean();
        routeListBeanNew.setWorkOrderId(workOrderId);
        routeListBeanNew.setId(route.getId());
        routeListBeanNew.setRouteContent(route.getPath());
//                routeListBeanNew.setUserCode(userCode);
        routeListBeanNew.setReservoirId(reservoirBean.getReservoirId());
        routeListBeanNew.setItemList(itemList);
        routeListBeanNew.setRouteName(route.getName());
        routeListBeanNew.setRoutePositions(routePositions);
        routeListBeanNew.setRouteType(route.getType());
        boolean save = routeListBeanNew.save();

        TaskBean taskBean = new TaskBean();
        taskBean.setWorkOrderId(workOrderId);
        taskBean.setRouteId(routeListBeanNew.getId());
        taskBean.setReservoirId(reservoirBean.getReservoirId());
        taskBean.setReservoir(reservoirBean.getReservoir());
        taskBean.setUserCode(userCode);
        UserBean userBean = HttpManager.getInstance().getUser();
//            taskBean.setRoleName(userInfoBean.getData().getOfficeName());
        if (userBean != null) {
            taskBean.setExecutorName(userBean.getAccess_token());
        }
        taskBean.setStartTime(startTime);
        taskBean.setExecuteStatus("2");
        taskBean.setRouteName(routeListBeanNew.getRouteName());
        taskBean.setRouteType(route.getType());
        RouteListBean routeListBean_a = taskBean.getRouteListBeanByWorkId(workOrderId);
        if (save && routeListBean_a != null) {
            saveOther(workOrderId, routeListBeanNew, "0", userCode);
            boolean issave = taskBean.save();
            if (issave) {
//                UiHelper.goToStartInspectionView(getBaseActivity(), workOrderId);
                UiHelper.goToPatrolMapControlView(getBaseActivity(), workOrderId);
            }
        }
    }

    /**
     * 保存
     *
     * @param workOrderId
     * @param routeListBean
     */
    public static void saveOther(String workOrderId, RouteListBean routeListBean, String completeStatus, String userCode) {
        LogUtil.i(MainFragment.class.getName(), "--------------工单workOrderId:" + workOrderId);
        int num = 0;
        int numOFTaskItemBean = 0;
        for (TaskItemBean taskItemBean1 : routeListBean.getItemList()) {
            String reservoirId = taskItemBean1.getReservoirId();
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
            taskItemBean.setItemId(taskItemBean1.getItemId());
            taskItemBean.setCompleteStatus(completeStatus);

            if ("1".equals(completeStatus)) {
                taskItemBean.setExecuteResultType(taskItemBean1.getExecuteResultType());
                taskItemBean.setExecuteResultDescription(taskItemBean1.getExecuteResultDescription());
                taskItemBean.setLastExcuteResultType(taskItemBean1.getLastExcuteResultType());
                taskItemBean.setLastExecuteResultDescription(taskItemBean1.getLastExecuteResultDescription());
                taskItemBean.setLastExcuteDate(taskItemBean1.getLastExcuteDate());
                List<TaskItemBean.ISysFileUploadsBean> iSysFileUploadsBeanList = taskItemBean1.getiSysFileUploads();
                if (!CollectionsUtil.isEmpty(iSysFileUploadsBeanList)) {
                    for (TaskItemBean.ISysFileUploadsBean iSysFileUploadsBean : iSysFileUploadsBeanList) {
                        CheckTaskPicturesBean checkTaskPicturesBean = new CheckTaskPicturesBean();
                        checkTaskPicturesBean.setFilePath(iSysFileUploadsBean.getFilePath());
                        LogUtil.e(MainFragment.class.getName(), "---------图片路径：" + iSysFileUploadsBean.getFilePath());
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
            routePosition.setStructurePath(routePosition1.getStructurePath());
            routePosition.setStructureName(routePosition1.getStructureName());

//            String userCode = SPUtils.getInstance().getString(CacheConsts.userCode, "");
//            String reservoirId = SPUtils.getInstance().getString(CacheConsts.reservoirId, "");
            routePosition.setUserCode(userCode);
            routePosition.setReservoirId(routePosition1.getReservoirId());
            routePosition.setFatherId(routeListBean.getId());
            routePosition.setWorkOrderId(workOrderId);
            routePosition.setUuid(UUIDUtil.getUUID32());
            boolean save = routePosition.save();
            num++;
            LogUtil.i("save", "----------------RoutePosition保存：" + save + num);

        }
    }

}
