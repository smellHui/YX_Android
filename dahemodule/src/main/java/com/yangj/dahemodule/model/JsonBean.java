package com.yangj.dahemodule.model;

import com.tepia.base.http.BaseResponse;

/**
 * Created by Android Studio
 *
 * @author : Arthur
 * Date :    2019/8/12
 * Time :    18:35
 * Describe :
 */
public class JsonBean extends BaseResponse {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
