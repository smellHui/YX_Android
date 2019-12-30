package com.yangj.dahemodule.model;

import com.tepia.base.http.BaseResponse;

/**
 * Author:xch
 * Date:2019/9/3
 * Description:
 */
public class BaseBean<T> extends BaseResponse {

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseBean{" +
                "data=" + data +
                '}';
    }
}
