package com.yangj.dahemodule.common;


import com.tepia.base.http.BaseResponse;
import com.tepia.base.http.RetrofitManager;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.WaterPptnPictureBean;
import com.tepia.guangdong_module.amainguangdong.route.TaskBeanFromNet;
import com.tepia.guangdong_module.amainguangdong.route.TaskDetailResponse;
import com.tepia.guangdong_module.amainguangdong.route.TaskItemBean;
import com.tepia.guangdong_module.amainguangdong.route.TaskItemBeanFromNet;
import com.tepia.guangdong_module.APPCostant;
import com.yangj.dahemodule.http.TaskHttpService;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
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
        this.mRetrofitService = RetrofitManager.getRetrofit(APPCostant.API_SERVER_URL + APPCostant.API_SERVER_TASK_AREA).create(TaskHttpService.class);
    }


    /**
     * 开始执行任务
     *
     * @param workOrderId
     * @param positionStr
     * @return
     */
    public Observable<BaseResponse> startExecute(String workOrderId, String positionStr) {
        String token = HttpManager.getInstance().getToken();
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
        String token = HttpManager.getInstance().getToken();
        return mRetrofitService.endExecute(token, workOrderId, positionStr)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }




    /**
     * 离线模式提交巡检项
     * @return
     */
    public Observable<BaseResponse> appReservoirWorkOrderItemCommitOne(TaskItemBean taskItemBean,
                                                                       List<String> files
                                                                       ) {
        String token = HttpManager.getInstance().getToken();
        Map<String, RequestBody> params = new HashMap<>();
        params.put("workOrderId", RetrofitManager.convertToRequestBody(taskItemBean.getWorkOrderId()));
        params.put("itemId", RetrofitManager.convertToRequestBody(taskItemBean.getItemId()));
        params.put("reservoirId", RetrofitManager.convertToRequestBody(taskItemBean.getReservoirId()));
        params.put("positionId", RetrofitManager.convertToRequestBody(taskItemBean.getPositionId()));
        params.put("positionTreeNames", RetrofitManager.convertToRequestBody(taskItemBean.getPositionTreeNames()));
        params.put("positionLongitude", RetrofitManager.convertToRequestBody(taskItemBean.getPositionLongitude()));
        params.put("positionLatitude", RetrofitManager.convertToRequestBody(taskItemBean.getPositionLatitude()));
        params.put("superviseItemCode", RetrofitManager.convertToRequestBody(taskItemBean.getSuperviseItemCode()));
        params.put("superviseItemName", RetrofitManager.convertToRequestBody(taskItemBean.getSuperviseItemName()));
        params.put("content", RetrofitManager.convertToRequestBody(taskItemBean.getContent()));
        params.put("executeResultType", RetrofitManager.convertToRequestBody(taskItemBean.getExecuteResultType()));
        params.put("executeResultDescription", RetrofitManager.convertToRequestBody(taskItemBean.getExecuteResultDescription()));
        params.put("excuteLongitude", RetrofitManager.convertToRequestBody(taskItemBean.getExcuteLongitude()));
        params.put("excuteLatitude", RetrofitManager.convertToRequestBody(taskItemBean.getExcuteLatitude()));
        params.put("excuteDate", RetrofitManager.convertToRequestBody(taskItemBean.getExcuteDate()));
        params.put("lastExcuteResultType", RetrofitManager.convertToRequestBody(taskItemBean.getLastExcuteResultType()));
        params.put("lastExecuteResultDescription", RetrofitManager.convertToRequestBody(taskItemBean.getLastExecuteResultDescription()));
        params.put("completeStatus", RetrofitManager.convertToRequestBody(taskItemBean.getCompleteStatus()));
        params.put("positionName", RetrofitManager.convertToRequestBody(taskItemBean.getPositionName()));
        //字段路径缺少

        List<File> beforefileList = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            File file = new File(files.get(i));
            beforefileList.add(file);
        }
        List<MultipartBody.Part> beforePathList = RetrofitManager.filesToMultipartBodyParts("files", beforefileList);


        return mRetrofitService.appReservoirWorkOrderItemCommitOne(token, params, beforePathList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 开始执行工单
     *
     * @param reservoirId
     * @return
     */
    public Observable<TaskDetailResponse> newStartExecute(String workOrderId, String routeId, String reservoirId, String reservoir, String startTime) {
        String token = HttpManager.getInstance().getToken();
        return mRetrofitService.newStartExecute(token, workOrderId, routeId, reservoirId, reservoir, startTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<WaterPptnPictureBean> newWaterPptnPicture(String reservoirId) {
        String token = HttpManager.getInstance().getToken();
        return mRetrofitService.newWaterPptnPicture(token, reservoirId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 分页查询水库工单列表
     * @param reservoirId
     * @param startDate
     * @param endDate
     * @param currentPage
     * @param pageSize
     * @return
     */
    public Observable<TaskBeanFromNet> listReservoirWorkOrder(String reservoirId, String startDate, String endDate, int currentPage, int pageSize) {
        String token = HttpManager.getInstance().getToken();
        return mRetrofitService.listReservoirWorkOrder(token, reservoirId,startDate,endDate,currentPage,pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 根据工单id获取详情
     * @param workOrderId
     * @return
     */
    public Observable<TaskItemBeanFromNet> getWorkOrderDetailInfo(String workOrderId){

        String token = HttpManager.getInstance().getToken();
        return mRetrofitService.getWorkOrderDetailInfo(token, workOrderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
