package com.tepia.guangdong_module.amainguangdong.mvp.taskdetail;

import com.example.guangdong_module.R;
import com.tepia.base.http.BaseResponse;
import com.tepia.base.http.LoadingSubject;
import com.tepia.base.mvp.BasePresenterImpl;
import com.tepia.base.utils.ResUtils;
import com.tepia.guangdong_module.amainguangdong.route.TaskBean;
import com.tepia.guangdong_module.amainguangdong.route.TaskBeanFromNet;
import com.tepia.guangdong_module.amainguangdong.route.TaskItemBeanFromNet;

/**
 * Created by      android studio
 *
 * @author :       ly
 * Date            :       2019-05-07
 * Time            :       下午5:06
 * Version         :       1.0
 * location        :       武汉研发中心
 * 功能描述         :
 **/
public class YunWeiListPresenter extends BasePresenterImpl<YunWeiListContract.View> implements YunWeiListContract.Presenter{

    /**
     *
     * @param reservoirId
     * @param startDate
     * @param endDate
     * @param currentPage
     * @param pageSize
     * @param isshowing
     * @return
     */
    @Override
    public void getTaskBeanList(String reservoirId, String startDate, String endDate, int currentPage, int pageSize, boolean isshowing) {
        TaskManager.getInstance().listReservoirWorkOrder(reservoirId,startDate,endDate,currentPage,pageSize).subscribe(new LoadingSubject<TaskBeanFromNet>(isshowing, ResUtils.getString(R.string.data_fromnet)) {
            @Override
            protected void _onNext(TaskBeanFromNet taskBeanFromNet) {
                if (taskBeanFromNet != null) {
                    if (taskBeanFromNet.getCode() == 0) {
                        if (mView != null) {
                            mView.success(taskBeanFromNet);
                        }
                    }
                }
            }

            @Override
            protected void _onError(String message) {
                if (mView != null) {
                    mView.failure(message);
                }
            }
        });

    }

    /**
     * 查询工单巡查项详情
     * @param workOrderId
     */
    @Override
    public void getByworkOrderId(String workOrderId) {
        TaskManager.getInstance().getWorkOrderDetailInfo(workOrderId).subscribe(new LoadingSubject<TaskItemBeanFromNet>(true, ResUtils.getString(R.string.data_fromnet)) {
            @Override
            protected void _onNext(TaskItemBeanFromNet taskItemBeanFromNet) {


                if (taskItemBeanFromNet != null) {
                    if (taskItemBeanFromNet.getCode() == 0) {
                        if (mView != null) {
                            mView.success(taskItemBeanFromNet);

                        }
                    }
                }

            }

            @Override
            protected void _onError(String message) {
                if (mView != null) {
                    mView.failure(message);
                }
            }
        });
    }


}
