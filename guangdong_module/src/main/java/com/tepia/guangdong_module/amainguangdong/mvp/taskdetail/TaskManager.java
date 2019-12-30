package com.tepia.guangdong_module.amainguangdong.mvp.taskdetail;


import com.alibaba.fastjson.JSON;
import com.tepia.base.http.BaseResponse;
import com.tepia.base.http.RetrofitManager;
import com.tepia.guangdong_module.amainguangdong.common.UserManager;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.DangerBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.WaterPptnPictureBean;
import com.tepia.guangdong_module.amainguangdong.route.TaskBeanFromNet;
import com.tepia.guangdong_module.amainguangdong.route.TaskDetailResponse;
import com.tepia.guangdong_module.amainguangdong.route.TaskItemBean;
import com.tepia.guangdong_module.amainguangdong.route.TaskItemBeanFromNet;
import com.tepia.guangdong_module.APPCostant;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


/**
 * Created by Joeshould on 2018/5/23.
 */

public class TaskManager {
    private static final TaskManager ourInstance = new TaskManager();
    private TaskHttpService mRetrofitService;

    public static TaskManager getInstance() {
        return ourInstance;
    }

    private TaskManager() {
        this.mRetrofitService = RetrofitManager.getRetrofit(APPCostant.API_SERVER_URL).create(TaskHttpService.class);
    }


    /**
     * 开始执行任务
     *
     * @param workOrderId
     * @param positionStr
     * @return
     */
    public Observable<BaseResponse> startExecute(String workOrderId, String positionStr) {
        String token = makeToken();
        return mRetrofitService.startExecute(token, workOrderId, positionStr)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * APP 巡检人员工单执行完成提交 （App 端使用）
     *
     * @param workOrderId
     * @param positionStr
     * @return
     */
    public Observable<BaseResponse> endExecute(String workOrderId, String positionStr) {
        String token = makeToken();
        return mRetrofitService.endExecute(token, workOrderId, positionStr)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 离线模式提交巡检项
     *
     * @return
     */
    public Observable<BaseResponse> appReservoirWorkOrderItemCommitOne(TaskItemBean taskItemBean,
                                                                       List<String> files
    ) {
        String token = makeToken();
        Map<String, RequestBody> params = new HashMap<>();
        params.put("omRecordCode", RetrofitManager.convertToRequestBody(taskItemBean.getWorkOrderId()));
        params.put("reservoirStructureId", RetrofitManager.convertToRequestBody(taskItemBean.getPositionId()));
        params.put("omItemId", RetrofitManager.convertToRequestBody(taskItemBean.getSuperviseItemCode()));
        params.put("omRecordItemCode", RetrofitManager.convertToRequestBody(taskItemBean.getItemId()));
        params.put("superviseItemName", RetrofitManager.convertToRequestBody(taskItemBean.getSuperviseItemName()));
        params.put("content", RetrofitManager.convertToRequestBody(taskItemBean.getContent()));
        params.put("executeResultType", RetrofitManager.convertToRequestBody(taskItemBean.getExecuteResultType()));
        params.put("executeResultDescription", RetrofitManager.convertToRequestBody(taskItemBean.getExecuteResultDescription()));
        params.put("executeLongitude", RetrofitManager.convertToRequestBody(taskItemBean.getExcuteLongitude()));
        params.put("executeLatitude", RetrofitManager.convertToRequestBody(taskItemBean.getExcuteLatitude()));
        params.put("executeTime", RetrofitManager.convertToRequestBody(taskItemBean.getExcuteDate()));
        params.put("lastExecuteResultType", RetrofitManager.convertToRequestBody(taskItemBean.getLastExcuteResultType()));
        params.put("lastExecuteResultDescription", RetrofitManager.convertToRequestBody(taskItemBean.getLastExecuteResultDescription()));
        params.put("completeStatus", RetrofitManager.convertToRequestBody(taskItemBean.getCompleteStatus()));
        params.put("positionName", RetrofitManager.convertToRequestBody(taskItemBean.getPositionName()));
        //字段路径缺少

        List<File> beforefileList = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            File file = new File(files.get(i));
            beforefileList.add(file);
        }
        List<MultipartBody.Part> beforePathList = RetrofitManager.filesToMultipartBodyParts("pictures", beforefileList);


        return mRetrofitService.appReservoirWorkOrderItemCommitOne(token, params, beforePathList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 开始执行工单
     *
     * @return
     */
    public Observable<TaskDetailResponse> newStartExecute(String omRecordCode, String omRouteId, String createTime) {
        String token = makeToken();
        Map<String, String> map = new HashMap<>();
        map.put("omRecordCode", omRecordCode);
        map.put("omRouteId", omRouteId);
        map.put("createTime", createTime);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(map));
        return mRetrofitService.newStartExecute(token, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private String makeToken() {
        return UserManager.getInstance().makeToken();
    }

    /**
     * 上报险情
     *
     * @param files
     * @return
     */
    public Observable<BaseResponse> reportProblem(DangerBean dangerBean,List<String> files) {
        String token = makeToken();
        Map<String, RequestBody> params = new HashMap<>();
        if (dangerBean != null) {
            params.put("reservoirCode", RetrofitManager.convertToRequestBody(dangerBean.getReservoirId()));
            params.put("positionId", RetrofitManager.convertToRequestBody(dangerBean.getPositionId()));
            params.put("positionName", RetrofitManager.convertToRequestBody(dangerBean.getPositionName()));
            params.put("problemDescription", RetrofitManager.convertToRequestBody(dangerBean.getProblemDescription()));
        }
        List<File> beforefileList = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            File file = new File(files.get(i));
            beforefileList.add(file);
        }
        List<MultipartBody.Part> beforePathList = RetrofitManager.filesToMultipartBodyParts("pictures", beforefileList);
        return mRetrofitService.reportProblem(token, params, beforePathList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<WaterPptnPictureBean> newWaterPptnPicture(String reservoirId) {
        String token = makeToken();
        return mRetrofitService.newWaterPptnPicture(token, reservoirId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 分页查询水库工单列表
     *
     * @param reservoirId
     * @param startDate
     * @param endDate
     * @param currentPage
     * @param pageSize
     * @return
     */
    public Observable<TaskBeanFromNet> listReservoirWorkOrder(String reservoirId, String startDate, String endDate, int currentPage, int pageSize) {
        String token = makeToken();
        return mRetrofitService.listReservoirWorkOrder(token, reservoirId, startDate, endDate, currentPage, pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 根据工单id获取详情
     *
     * @param workOrderId
     * @return
     */
    public Observable<TaskItemBeanFromNet> getWorkOrderDetailInfo(String workOrderId) {

        String token = makeToken();
        return mRetrofitService.getWorkOrderDetailInfo(token, workOrderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
