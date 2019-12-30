package com.yangj.dahemodule.intefaces;

import com.tepia.base.mvp.BaseView;

/**
 * Author:xch
 * Date:2019/4/4
 * Do:
 */
public interface NetListListener<T> extends BaseView {

    void success(T t);

    void error();
}
