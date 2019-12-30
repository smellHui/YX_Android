package com.tepia.guangdong_module.amainguangdong.utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.tepia.base.view.floatview.CollectionsUtil;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.RouteListBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.RoutePosition;
import com.tepia.guangdong_module.amainguangdong.route.TaskBean;
import com.tepia.guangdong_module.amainguangdong.route.TaskItemBean;

import org.litepal.crud.DataSupport;
import org.litepal.crud.callback.UpdateOrDeleteCallback;

import java.util.List;

/**
 * Author:xch
 * Date:2019/11/21
 * Description:
 */
public class SqlManager {

    private volatile static SqlManager mSingleton = null;

    private SqlManager() {
    }

    public static SqlManager getInstance() {
        if (mSingleton == null) {
            synchronized (SqlManager.class) {
                if (mSingleton == null) {
                    mSingleton = new SqlManager();
                }
            }
        }
        return mSingleton;
    }

    public TaskBean queryTaskSqlByWorkOrderId(String workOrderId) {
        return DataSupport.where("workOrderId=?", workOrderId).findFirst(TaskBean.class);
    }

    public RouteListBean queryRouteSqlByWorkOrderId(String workOrderId) {
        return DataSupport.where("workorderid=?", workOrderId).findFirst(RouteListBean.class);
    }

    public List<TaskItemBean> queryTaskItem(String workOrderId, String positionId) {
        return DataSupport.where("workorderid=? and positionid=?", workOrderId, positionId).find(TaskItemBean.class);
    }

    public TaskItemBean queryTaskItemByItemId(String workOrderId, String itemId) {
        return DataSupport.where("workorderid=? and itemId=?", workOrderId, itemId).findFirst(TaskItemBean.class);
    }

    public List<TaskItemBean> queryTaskItem(String workOrderId, String positionId, String completeStatus) {
        return DataSupport.where("workorderid=? and positionid=? and completeStatus=?", workOrderId, positionId, completeStatus).find(TaskItemBean.class);
    }

    public List<TaskItemBean> queryTaskItem(String workOrderId) {
        return DataSupport.where("workorderid=?", workOrderId).find(TaskItemBean.class);
    }

    public List<TaskItemBean> queryTaskItemByStatus(String workOrderId, String completeStatus) {
        return DataSupport.where("workorderid=? and completeStatus=?", workOrderId, completeStatus).find(TaskItemBean.class);
    }

    public int getTotalTaskByStatus(String workOrderId, String completeStatus) {
        List<TaskItemBean> taskItemBeans = queryTaskItemByStatus(workOrderId, completeStatus);
        return CollectionsUtil.isEmpty(taskItemBeans) ? 0 : taskItemBeans.size();
    }

    public int getTotalTaskById(String workOrderId) {
        List<TaskItemBean> taskItemBeans = queryTaskItem(workOrderId);
        return CollectionsUtil.isEmpty(taskItemBeans) ? 0 : taskItemBeans.size();
    }

    public int getCompletedTaskById(String workorderId, String positionId) {
        List<TaskItemBean> taskItemBeans = queryTaskItem(workorderId, positionId, "1");
        return CollectionsUtil.isEmpty(taskItemBeans) ? 0 : taskItemBeans.size();
    }

    public int getNotCompletedTaskById(String workorderId, String positionId) {
        List<TaskItemBean> taskItemBeans = queryTaskItem(workorderId, positionId, "0");
        return CollectionsUtil.isEmpty(taskItemBeans) ? 0 : taskItemBeans.size();
    }

    /**
     * 获取正常项数量，异常项数量
     *
     * @param workorderId
     * @param positionId
     * @param isNormal
     * @return
     */
    public int getNormalTaskById(String workorderId, String positionId, boolean isNormal) {
        List<TaskItemBean> taskItemBeans = null;
        if (TextUtils.isEmpty(positionId)) {
            taskItemBeans = queryTaskItem(workorderId);
        } else {
            taskItemBeans = queryTaskItem(workorderId, positionId);
        }
        int sum = 0;
        if (CollectionsUtil.isEmpty(taskItemBeans)) return sum;
        for (TaskItemBean taskItemBean : taskItemBeans) {
            if (taskItemBean == null) continue;
            String executeResultType = taskItemBean.getExecuteResultType();
            if (executeResultType == null) continue;
            if (isNormal) {
                if (executeResultType.equals("") || executeResultType.equals("0") || executeResultType.equals("2"))
                    sum++;
            } else {
                if (executeResultType.equals("1") || executeResultType.equals("3"))
                    sum++;
            }
        }
        return sum;
    }

    public int getNormalTaskById(String workorderId, boolean isNormal) {
        return getNormalTaskById(workorderId, null, isNormal);
    }

    public List<RoutePosition> queryRoutePositionsByWorkid(String workOrderId) {
        return DataSupport.where("workorderid=?", workOrderId).find(RoutePosition.class);
    }

    public void updateTaskItemAsyn(TaskItemBean taskItemBean, UpdateOrDeleteCallback updateOrDeleteCallback) {
        taskItemBean.updateAllAsync("itemId=?", taskItemBean.getItemId()).listen(updateOrDeleteCallback);
    }

    public void updateTaskAsyn(TaskBean taskBean, UpdateOrDeleteCallback updateOrDeleteCallback) {
        taskBean.updateAllAsync("workorderid=?", taskBean.getWorkOrderId()).listen(updateOrDeleteCallback);
    }

    /**
     * 有多少离线数据没有提交
     *
     * @return
     */
    public List<TaskItemBean> queryLocalData(String workorderid) {
        return DataSupport.where("workorderid=? and iscommitlocal=?", workorderid, "0").find(TaskItemBean.class);
    }

    /**
     *
     * @param workOrderId
     */
    public void deleteTask(@NonNull String workOrderId){
        DataSupport.deleteAll(TaskBean.class, "workOrderId=?", workOrderId);
        DataSupport.deleteAll(TaskItemBean.class, "workOrderId=?", workOrderId);
        DataSupport.deleteAll(RouteListBean.class, "workOrderId=?", workOrderId);
        DataSupport.deleteAll(RoutePosition.class, "workOrderId=?", workOrderId);
    }

}
