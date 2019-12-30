package com.tepia.guangdong_module.amainguangdong.mvp.reservoirDetail;

import com.tepia.base.mvp.BasePresenter;
import com.tepia.base.mvp.BaseView;

/**
 * Created by      Intellij IDEA
 *
 * @author :       wwj
 * Date    :       2019-05-29
 * Time    :       11:21
 * Version :       1.0
 * Company :       北京太比雅科技(武汉研发中心)
 **/
public class ReservoirDetailContract {
    public interface View<T> extends BaseView {
        void success(T data);
        void failure(String msg);
    }

    interface  Presenter extends BasePresenter<View> {
        void getRainDetailList(String reservoirId, String startDate, String endDate, String selectType);
        void getReservoirWaterList(String reservoirId, String startDate, String endDate, boolean isShow);
        void getPictureDetailList(String reservoirId, String startDate, String endDate, boolean isShow);
    }
}
