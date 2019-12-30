package com.yangj.dahemodule.wrap;

/**
 * Author:xch
 * Date:2019/12/20
 * Description:巡查问题反馈后在地图上打点
 */
public class PatrolAddProblemWrap {
    private String problemId;
    private boolean toDelete;//true,删除操作

    public PatrolAddProblemWrap(String problemId) {
        this.problemId = problemId;
        this.toDelete = false;
    }

    public PatrolAddProblemWrap( boolean toDelete) {
        this.toDelete = toDelete;
    }

    public boolean isToDelete() {
        return toDelete;
    }

    public void setToDelete(boolean toDelete) {
        this.toDelete = toDelete;
    }

    public String getProblemId() {
        return problemId;
    }

    public void setProblemId(String problemId) {
        this.problemId = problemId;
    }
}
