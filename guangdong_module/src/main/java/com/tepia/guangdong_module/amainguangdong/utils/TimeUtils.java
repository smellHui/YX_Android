package com.tepia.guangdong_module.amainguangdong.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Android Studio
 *
 * @author : Arthur
 * Date :    2019/6/17
 * Time :    14:17
 * Describe :
 */
public class TimeUtils {


    /**
     * 比较两个日期大小
     * @param str1
     * @param str2
     * @return
     */
    public static boolean isDateOneBigger(String str1, String str2) {
        boolean isBigger = false;
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        Date dt1 = null;
        Date dt2 = null;
        try {
            dt1 = sdf.parse(str1);
            dt2 = sdf.parse(str2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dt1.getTime() > dt2.getTime()) {
            isBigger = true;
        } else if (dt1.getTime() < dt2.getTime()) {
            isBigger = false;
        }
        return isBigger;
    }

    /**获取上n个小时整点小时时间
     * @return
     */
    public static String getLastHourTime(int n) {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.MINUTE, 0);
        ca.set(Calendar.SECOND, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        ca.set(Calendar.HOUR_OF_DAY, ca.get(Calendar.HOUR_OF_DAY) - n);
        Date date = ca.getTime();
        return sdf.format(date);
    }

    /**
     * 获取当前时间
     * @return
     */
    public static String getCurrentHourTime(){
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.MINUTE, 0);
        ca.set(Calendar.SECOND, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        ca.set(Calendar.HOUR_OF_DAY, ca.get(Calendar.HOUR_OF_DAY));
        Date date = ca.getTime();
        return sdf.format(date);
    }
}
