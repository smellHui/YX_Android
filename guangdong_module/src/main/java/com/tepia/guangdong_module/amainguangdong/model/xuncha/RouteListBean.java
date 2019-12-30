package com.tepia.guangdong_module.amainguangdong.model.xuncha;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.tepia.base.CacheConsts;
import com.tepia.base.utils.LogUtil;
import com.tepia.base.utils.ToastUtils;
import com.tepia.base.utils.UUIDUtil;
import com.tepia.base.view.floatview.CollectionsUtil;
import com.tepia.guangdong_module.amainguangdong.route.TaskItemBean;
import com.tepia.photo_picker.utils.SPUtils;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

/**
 * Created by      android studio
 *
 * @author :       ly
 * Date            :       2019-05-07
 * Time            :       下午7:39
 * Version         :       1.0
 * location        :       武汉研发中心
 * 功能描述         :
 **/
public class RouteListBean extends DataSupport implements Serializable {

    @Override
    public synchronized boolean saveOrUpdate(String... conditions) {
        for (TaskItemBean taskItemBean : itemList) {

            TaskItemBean taskItemBean1 =    DataSupport.where("superviseItemCode=? and positionId=? and usercode=? and reservoirId=? and workorderid is null",
                    taskItemBean.getSuperviseItemCode(),taskItemBean.getPositionId(),userCode,reservoirId).findFirst(TaskItemBean.class);
            if (taskItemBean1 != null) {
                taskItemBean.updateAll("superviseItemCode=? and positionId=? and usercode=? and reservoirId=? and workorderid is null",
                        taskItemBean.getSuperviseItemCode(),taskItemBean.getPositionId(),userCode,reservoirId);
            }else{
                String userCode = SPUtils.getInstance().getString(CacheConsts.userCode, "");
                String reservoirId = SPUtils.getInstance().getString(CacheConsts.reservoirId, "");
                taskItemBean.setUserCode(userCode);
                taskItemBean.setReservoirId(reservoirId);
                taskItemBean.setFatherId(onlyId);
                taskItemBean.setIsCommitLocal("0");
                taskItemBean.setItemId(UUIDUtil.getUUID32());
                boolean save = taskItemBean.save();
            }


           /* boolean save = taskItemBean.saveOrUpdate("superviseItemCode=? and positionId=? and usercode=? and reservoirId=?",
                    taskItemBean.getSuperviseItemCode(),taskItemBean.getPositionId(),userCode,reservoirId);*/
        }


        for (RoutePosition routePosition : routePositions) {


            RoutePosition routePosition1 = DataSupport.where("positionId=? and usercode=? and reservoirId=? and workorderid is null",routePosition.getPositionId(),userCode,reservoirId).findFirst(RoutePosition.class);
            if (routePosition1 != null) {
                routePosition.updateAll("positionId=? and usercode=? and reservoirId=? and workorderid is null",routePosition.getPositionId(),userCode,reservoirId);
            }else{
                String userCode = SPUtils.getInstance().getString(CacheConsts.userCode, "");
                String reservoirId = SPUtils.getInstance().getString(CacheConsts.reservoirId, "");
                routePosition.setUserCode(userCode);
                routePosition.setReservoirId(reservoirId);
                routePosition.setFatherId(onlyId);
                boolean save = routePosition.save();
            }

//            boolean save = routePosition.saveOrUpdate("positionId=? and usercode=? and reservoirId=?",routePosition.getPositionId(),userCode,reservoirId);

        }

        return super.saveOrUpdate(conditions);
    }




    /**
     * id : 386d477c8c584608822325a74c664903
     * routeType : 1
     * routeName : 日常路线
     * routeContent : [ { 113.38352861700002, 23.222737473000052 }, { 113.38339129400003, 23.222868117000075 }, { 113.38238360800005, 23.223524867000037 }, { 113.38238873300008, 23.223330643000054 }, { 113.38322724400007, 23.222791114000074 }, { 113.3830617860001, 23.222705705000067 }, { 113.38244641600011, 23.223090255000045 }, { 113.38247484500005, 23.222909814000047 }, { 113.38259542500009, 23.222757218000027 }, { 113.3826033680001, 23.222748371000023 }, { 113.38271602600003, 23.222670282000024 }, { 113.38285892100009, 23.222616490000064 }, { 113.38292834600009, 23.222546836000049 }, { 113.38332241300009, 23.222828711000034 }, { 113.3834633030001, 23.222726127000044 }, { 113.38371048500005, 23.222516144000053 }, { 113.38385631600011, 23.222297807000075 }, {113.38396571300007, 23.22214273000003 }, { 113.3839955840001, 23.222107117000064 }, { 113.38415307500009, 23.222251321000044 }, { 113.38405939200004, 23.22204808500004 } ]
     * itemList : [{"superviseItemCode":"99447e2fa7c44edfafe3eab5c09dab8f","positionTreeNames":"坝脚区","positionName":"坝脚区","superviseItemName":"（4）岸坡危岩崩坍","superviseItemContent":"（4）岸坡危岩崩坍"},{"superviseItemCode":"99447e2fa7c44edfafe3eab5c09dab8f","positionTreeNames":"溢洪道","positionName":"溢洪道","superviseItemName":"（4）岸坡危岩崩坍","superviseItemContent":"（4）岸坡危岩崩坍"},{"superviseItemCode":"8d5b6b5aaf7546c8aeb5dfe455b924bf","positionTreeNames":"坝脚区","positionName":"坝脚区","superviseItemName":"（2）闸门无法正常启闭","superviseItemContent":"（2）闸门无法正常启闭"},{"superviseItemCode":"8d5b6b5aaf7546c8aeb5dfe455b924bf","positionTreeNames":"溢洪道","positionName":"溢洪道","superviseItemName":"（2）闸门无法正常启闭","superviseItemContent":"（2）闸门无法正常启闭"},{"superviseItemCode":"d7925e82eaf1449690eafa7df1296364","positionTreeNames":"坝脚区","positionName":"坝脚区","superviseItemName":"（3）堆石反滤体异常变形","superviseItemContent":"（3）堆石反滤体异常变形"},{"superviseItemCode":"d7925e82eaf1449690eafa7df1296364","positionTreeNames":"溢洪道","positionName":"溢洪道","superviseItemName":"（3）堆石反滤体异常变形","superviseItemContent":"（3）堆石反滤体异常变形"},{"superviseItemCode":"1fd02d27a5dc411c9f4eaaf845077815","positionTreeNames":"坝脚区","positionName":"坝脚区","superviseItemName":"（3）杂草杂物侵占泄洪通道","superviseItemContent":"（3）杂草杂物侵占泄洪通道"},{"superviseItemCode":"1fd02d27a5dc411c9f4eaaf845077815","positionTreeNames":"溢洪道","positionName":"溢洪道","superviseItemName":"（3）杂草杂物侵占泄洪通道","superviseItemContent":"（3）杂草杂物侵占泄洪通道"},{"superviseItemCode":"5d2e77bbfa834b919f985ed15784d6c6","positionTreeNames":"坝脚区","positionName":"坝脚区","superviseItemName":"（2）堆石反滤体完整性","superviseItemContent":"（2）堆石反滤体完整性"},{"superviseItemCode":"5d2e77bbfa834b919f985ed15784d6c6","positionTreeNames":"溢洪道","positionName":"溢洪道","superviseItemName":"（2）堆石反滤体完整性","superviseItemContent":"（2）堆石反滤体完整性"},{"superviseItemCode":"d65f8e8932804e20bd5233b15f36ac93","positionTreeNames":"坝脚区","positionName":"坝脚区","superviseItemName":"（1）异常渗漏 ","superviseItemContent":"（1）异常渗漏（喷水、浊水、管涌等）"},{"superviseItemCode":"d65f8e8932804e20bd5233b15f36ac93","positionTreeNames":"溢洪道","positionName":"溢洪道","superviseItemName":"（1）异常渗漏 ","superviseItemContent":"（1）异常渗漏（喷水、浊水、管涌等）"},{"superviseItemCode":"4b01a79bea4646f4ac35aee06461aa62","positionTreeNames":"坝脚区","positionName":"坝脚区","superviseItemName":"（5）边墙异常变形","superviseItemContent":"（5）边墙异常变形"},{"superviseItemCode":"4b01a79bea4646f4ac35aee06461aa62","positionTreeNames":"溢洪道","positionName":"溢洪道","superviseItemName":"（5）边墙异常变形","superviseItemContent":"（5）边墙异常变形"},{"superviseItemCode":"a8a4e93425ce408da2bd762deda18cc0","positionTreeNames":"坝脚区","positionName":"坝脚区","superviseItemName":"（4）排水沟堵塞","superviseItemContent":"（4）排水沟堵塞"},{"superviseItemCode":"a8a4e93425ce408da2bd762deda18cc0","positionTreeNames":"溢洪道","positionName":"溢洪道","superviseItemName":"（4）排水沟堵塞","superviseItemContent":"（4）排水沟堵塞"},{"superviseItemCode":"b1a23b3d93584bfe8d76a4a7b369f23a","positionTreeNames":"坝脚区","positionName":"坝脚区","superviseItemName":"（1）进口障碍物","superviseItemContent":"（1）进口障碍物(人为加高)"},{"superviseItemCode":"b1a23b3d93584bfe8d76a4a7b369f23a","positionTreeNames":"溢洪道","positionName":"溢洪道","superviseItemName":"（1）进口障碍物","superviseItemContent":"（1）进口障碍物(人为加高)"},{"superviseItemCode":"f52be6e131bd4505a6be8bcf616be876","positionTreeNames":"坝脚区","positionName":"坝脚区","superviseItemName":"（6）溢流面砼面板","superviseItemContent":"（6）溢流面砼面板异常变形或严重破损"},{"superviseItemCode":"f52be6e131bd4505a6be8bcf616be876","positionTreeNames":"溢洪道","positionName":"溢洪道","superviseItemName":"（6）溢流面砼面板","superviseItemContent":"（6）溢流面砼面板异常变形或严重破损"}]
     */


    private String userCode;
    private String reservoirId;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getReservoirId() {
        return reservoirId;
    }

    public void setReservoirId(String reservoirId) {
        this.reservoirId = reservoirId;
    }

    @SerializedName("id")
    private String onlyId;

    private String routeType;
    private String filePath;
    private String routeName;
    private String routeContent;
    private String workOrderId;
    private List<TaskItemBean> itemList;
    private List<RoutePosition> routePositions;

    public List<RoutePosition> getRoutePositionsByworkid(String workOrderId) {
        List<RoutePosition> routePositionsNew = DataSupport.where("workorderid=?",workOrderId).find(RoutePosition.class);
        return routePositionsNew;
    }

    public List<RoutePosition> getRoutePositions() {
       /* String userCode = SPUtils.getInstance().getString(CacheConsts.userCode, "");
        String reservoirId = SPUtils.getInstance().getString(CacheConsts.reservoirId, "");
        List<RoutePosition> routePositionsNew = DataSupport.where("usercode=? and reservoirid=?", userCode, reservoirId).find(RoutePosition.class);*/

        return routePositions;
    }

    public void setRoutePositions(List<RoutePosition> routePositions) {
        this.routePositions = routePositions;
    }

    public String getId() {
        return onlyId;
    }

    public void setId(String id) {
        this.onlyId = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getRouteType() {
        return routeType;
    }

    public void setRouteType(String routeType) {
        this.routeType = routeType;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getRouteContent() {
        return routeContent;
    }

    public void setRouteContent(String routeContent) {
        this.routeContent = routeContent;
    }

    public  List<TaskItemBean> getItemList(){
       /* String userCode = SPUtils.getInstance().getString(CacheConsts.userCode, "");
        String reservoirId = SPUtils.getInstance().getString(CacheConsts.reservoirId, "");
        itemList = DataSupport.where("userCode=? and reservoirId=?", userCode,reservoirId).find(TaskItemBean.class);*/
        return itemList;
    }

    public List<TaskItemBean> getItemListByworkid(String workOrderId) {

        List<TaskItemBean> itemListNew = DataSupport.where("workorderid=?", workOrderId).find(TaskItemBean.class);

        return itemListNew;
    }

    public int getItemListHasNotCheck(String workOrderId,String completestatus) {
        int num = 0;
       /* List<RoutePosition> routePositionList = getRoutePositionsByworkid(workOrderId);
        for (RoutePosition routePosition : routePositionList) {


            String userCode = SPUtils.getInstance().getString(CacheConsts.userCode, "");
            String reservoirId = SPUtils.getInstance().getString(CacheConsts.reservoirId, "");
            List<TaskItemBean> itemList = DataSupport.where("fatherid=? and usercode=? and reservoirid=? and workorderid=? and positionid=?",
                    onlyId, userCode, reservoirId, workOrderId, routePosition.getPositionId()).find(TaskItemBean.class);


            for (TaskItemBean taskItemBean : itemList) {
                if ("0".equals(taskItemBean.getCompleteStatus())) {
                    num++;
                    break;
                }
            }
        }*/

        List<TaskItemBean> itemList = DataSupport.where("workorderid=? and completestatus=?",
                 workOrderId,completestatus).find(TaskItemBean.class);


        if (!CollectionsUtil.isEmpty(itemList)) {
            num = itemList.size();
        }

        return num;
    }

    public void setItemList(List<TaskItemBean> itemList) {
        this.itemList = itemList;
    }

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }


}
