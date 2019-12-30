package com.tepia.guangdong_module.amainguangdong.route;

import java.util.Comparator;

/**
 * Created by      android studio
 *
 * @author :       ly
 * Date            :       2019-04-09
 * Time            :       下午5:41
 * Version         :       1.0
 * location        :       武汉研发中心
 * 功能描述         :
 **/
public class SortClass implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        TaskBean taskBean1 = (TaskBean) o1;
        TaskBean taskBean2 = (TaskBean) o2;

        return taskBean2.getStartTime().compareTo(taskBean1.getStartTime());
    }
}
