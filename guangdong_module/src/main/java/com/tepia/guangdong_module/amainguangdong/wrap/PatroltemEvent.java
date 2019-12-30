package com.tepia.guangdong_module.amainguangdong.wrap;

import com.tepia.guangdong_module.amainguangdong.route.TaskItemBean;

/**
 * Author:xch
 * Date:2019/11/27
 * Description:
 */
public class PatroltemEvent {

    private int position;
    private int patrolIndex;
    private TaskItemBean taskItemBean;

    public PatroltemEvent(int position,int patrolIndex,TaskItemBean taskItemBean) {
        this.position = position;
        this.patrolIndex = patrolIndex;
        this.taskItemBean = taskItemBean;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPatrolIndex() {
        return patrolIndex;
    }

    public void setPatrolIndex(int patrolIndex) {
        this.patrolIndex = patrolIndex;
    }

    public TaskItemBean getTaskItemBean() {
        return taskItemBean;
    }

    public void setTaskItemBean(TaskItemBean taskItemBean) {
        this.taskItemBean = taskItemBean;
    }
}
