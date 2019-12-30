package com.tepia.base.utils;

import java.util.UUID;

/**
 * Created by      android studio
 *
 * @author :       ly
 * Date            :       2019-04-09
 * Time            :       上午11:30
 * Version         :       1.0
 * location        :       武汉研发中心
 * 功能描述         :
 **/
public class UUIDUtil {
    public static String getUUID32(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}
