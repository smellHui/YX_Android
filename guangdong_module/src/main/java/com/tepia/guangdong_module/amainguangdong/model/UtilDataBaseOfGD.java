package com.tepia.guangdong_module.amainguangdong.model;

import com.tepia.base.CacheConsts;
import com.tepia.base.view.floatview.CollectionsUtil;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.RouteListBean;
import com.tepia.photo_picker.entity.CheckTaskPicturesBean;
import com.tepia.photo_picker.utils.SPUtils;

import org.litepal.crud.DataSupport;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by      android studio
 *
 * @author :       ly
 * Date            :       2019-04-17
 * Time            :       上午10:23
 * Version         :       1.0
 * location        :       武汉研发中心
 * 功能描述         :       数据库操作类
 **/
public class UtilDataBaseOfGD {
    private static final UtilDataBaseOfGD ourInstance = new UtilDataBaseOfGD();

    public static UtilDataBaseOfGD getInstance() {
        return ourInstance;
    }


    public void deletePic(String bizType, String filepath, String userCode, String reservoirId){
        DataSupport.deleteAll(CheckTaskPicturesBean.class,"bizType=? and filePath=? and userCode=? and reservoirId=?",
                bizType,filepath,userCode,reservoirId);
    }

    /**
     * 获取险情照片
     *
     * @param bizType
     * @return
     */
    public List<CheckTaskPicturesBean> getCheckTaskPicturesBeanOfDanger(String bizType, String userCode, String reservoirId) {

        List<CheckTaskPicturesBean> checkTaskPicturesBeanList = DataSupport.where("bizType=? and userCode=? and reservoirId=?", bizType,userCode,reservoirId).find(CheckTaskPicturesBean.class);

        return checkTaskPicturesBeanList;
    }

    /**
     * 隐患图片
     * @param bizType
     * @param userCode
     * @param reservoirId
     * @param itemId
     * @return
     */
    public List<CheckTaskPicturesBean> getCheckTaskPicturesBeanOfTrouble(String bizType,String userCode,String reservoirId,String itemId) {

        List<CheckTaskPicturesBean> checkTaskPicturesBeanList = DataSupport.where("bizType=? and userCode=? and itemId=?", bizType,userCode,itemId).find(CheckTaskPicturesBean.class);

        return checkTaskPicturesBeanList;
    }

    public List<CheckTaskPicturesBean> getCheckTaskPicturesBeanOfTrouble(String bizType,String itemId) {

        List<CheckTaskPicturesBean> checkTaskPicturesBeanList = DataSupport.where("bizType=? and itemId=?", bizType,itemId).find(CheckTaskPicturesBean.class);

        return checkTaskPicturesBeanList;
    }

    public CheckTaskPicturesBean getCheckTaskPicturesBean(String bizKey, String bizType,String path){
        CheckTaskPicturesBean checkTaskPicturesBeanList = DataSupport.where("bizKey=? and biztype=? and filePath=?", bizKey, bizType,path).findFirst(CheckTaskPicturesBean.class);
        return checkTaskPicturesBeanList;
    }




    /**
     * 排序 升序
     *
     * @param bizCheckTaskSonWorksBeanList
     * @param standType
     */
   /* public void listBizCheckTaskSonWorksBean(List<BizCheckTaskSonWorksBean> bizCheckTaskSonWorksBeanList, String standType) {
        if (CollectionsUtil.isEmpty(bizCheckTaskSonWorksBeanList)) {
            return;
        }
        for (BizCheckTaskSonWorksBean bizCheckTaskSonWorksBean : bizCheckTaskSonWorksBeanList) {
            bizCheckTaskSonWorksBean.saveOrUpdate("onlyId = ?", bizCheckTaskSonWorksBean.getOnlyId());

        }

        *//**
         * 排序 升序
         *//*
        Collections.sort(bizCheckTaskSonWorksBeanList, new Comparator<BizCheckTaskSonWorksBean>() {
            @Override
            public int compare(BizCheckTaskSonWorksBean o1, BizCheckTaskSonWorksBean o2) {
                if (o1.getSort() < o2.getSort()) {
                    return -1;
                } else if (o1.getSort() > o2.getSort()) {
                    return 1;
                }
                return 0;
            }
        });
    }
*/


}
