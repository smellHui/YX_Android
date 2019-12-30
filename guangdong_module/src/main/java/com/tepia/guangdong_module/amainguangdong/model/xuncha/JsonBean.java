package com.tepia.guangdong_module.amainguangdong.model.xuncha;

import com.contrarywind.interfaces.IPickerViewData;

import java.util.List;

/**
 * Created by      android studio
 *
 * @author :       ly
 * Date            :       2019-05-30
 * Time            :       下午8:59
 * Version         :       1.0
 * location        :       武汉研发中心
 * 功能描述         :
 **/
public class JsonBean implements IPickerViewData {

    private String name;
    private List<CityBean> city;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CityBean> getCityList() {
        return city;
    }

    public void setCityList(List<CityBean> city) {
        this.city = city;
    }

    // 实现 IPickerViewData 接口，
    // 这个用来显示在PickerView上面的字符串，
    // PickerView会通过IPickerViewData获取getPickerViewText方法显示出来。
    @Override
    public String getPickerViewText() {
        return this.name;
    }


    public static class CityBean {

        private String name;
        private List<String> area;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getArea() {
            return area;
        }

        public void setArea(List<String> area) {
            this.area = area;
        }
    }
}
