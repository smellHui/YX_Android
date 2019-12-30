package com.yangj.dahemodule;

import com.example.gaodelibrary.UtilsContextOfGaode;
import com.tepia.base.BaseApplication;

/**
 * Author:xch
 * Date:2019/8/27
 * Description:
 */
public class Application extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        UtilsContextOfGaode.init(this);
    }
}
