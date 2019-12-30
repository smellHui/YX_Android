package com.tepia.guangdong_module.amainguangdong.route;

import com.tepia.base.http.BaseResponse;

/**
 * Created by Joeshould on 2018/5/23.
 */

public class TaskDetailResponse extends BaseResponse {

    private TaskBean data;

    public TaskBean getData() {
        return data;
    }

    public void setData(TaskBean data) {
        this.data = data;
    }


}
