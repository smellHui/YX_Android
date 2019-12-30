package com.yangj.dahemodule.model;

import com.tepia.base.http.BaseResponse;

import java.io.Serializable;

/**
  * Created by      Android studio
  *
  * @author :wwj (from Center Of Wuhan)
  * Date    :2019/5/10
  * Version :1.0
  * 功能描述 : 天气预警
 **/
public class WeatherWarnBean extends BaseResponse implements Serializable {

    /**
     * data : {"categoryCnname":"暴雨","content":"广州市气象台11日20时26分发布暴雨橙色预警信号:过去1小时，荔湾区已出现50毫米以上降水，预计未来两小时强降水仍将持续，范围有所扩大，累积雨量40-60毫米。从11日20时30分起，广州市天河区、越秀区、荔湾区暴雨黄色预警信号升级为橙色。目前全市雷雨大风黄色，南沙区暴雨橙色，番禺区、海珠区、黄埔区、增城区暴雨黄色，番禺区、南沙区冰雹橙色预警信号已经生效，请注意做好防御工作。广州预警发布中心04月11日20时26分发布","departmentlevelCnname":"橙色","indentifier":"020006201904112037064835","publishdate":"2019-05-10 10:26:00","status":"1"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * categoryCnname : 暴雨
         * content : 广州市气象台11日20时26分发布暴雨橙色预警信号:过去1小时，荔湾区已出现50毫米以上降水，预计未来两小时强降水仍将持续，范围有所扩大，累积雨量40-60毫米。从11日20时30分起，广州市天河区、越秀区、荔湾区暴雨黄色预警信号升级为橙色。目前全市雷雨大风黄色，南沙区暴雨橙色，番禺区、海珠区、黄埔区、增城区暴雨黄色，番禺区、南沙区冰雹橙色预警信号已经生效，请注意做好防御工作。广州预警发布中心04月11日20时26分发布
         * departmentlevelCnname : 橙色
         * indentifier : 020006201904112037064835
         * publishdate : 2019-05-10 10:26:00
         * status : 1
         */

        private String categoryCnname;//天气
        private String content;//预警内容
        private String departmentlevelCnname;//预警等级
        private String indentifier;//发布时间
        private String publishdate;
        private String status;
        private String icon;

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getCategoryCnname() {
            return categoryCnname;
        }

        public void setCategoryCnname(String categoryCnname) {
            this.categoryCnname = categoryCnname;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getDepartmentlevelCnname() {
            return departmentlevelCnname;
        }

        public void setDepartmentlevelCnname(String departmentlevelCnname) {
            this.departmentlevelCnname = departmentlevelCnname;
        }

        public String getIndentifier() {
            return indentifier;
        }

        public void setIndentifier(String indentifier) {
            this.indentifier = indentifier;
        }

        public String getPublishdate() {
            return publishdate;
        }

        public void setPublishdate(String publishdate) {
            this.publishdate = publishdate;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
