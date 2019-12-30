package com.tepia.guangdong_module.amainguangdong.mvp.reservoirDetail;

import com.tepia.base.http.LoadingSubject;
import com.tepia.base.mvp.BasePresenterImpl;
import com.tepia.guangdong_module.amainguangdong.model.reservoirdetail.PicDetailBean;
import com.tepia.guangdong_module.amainguangdong.model.reservoirdetail.RainDetailBean;
import com.tepia.guangdong_module.amainguangdong.model.reservoirdetail.WaterRegimeDetailBean;

import java.util.List;

/**
  * Created by      Android studio
  *
  * @author :wwj (from Center Of Wuhan)
  * Date    :2019/5/30
  * Version :1.0
  * 功能描述 :
 **/
public class ReservoirDetailPresenter extends BasePresenterImpl<ReservoirDetailContract.View> implements ReservoirDetailContract.Presenter{
    @Override
    public void getRainDetailList(String reservoirId, String startDate, String endDate, String selectType) {
        ReservoirDetailManager.getInstance_Monitor().getRainDetailList(reservoirId,startDate,endDate,selectType).subscribe(new LoadingSubject<RainDetailBean>(false,"正在加载中"){

            @Override
            protected void _onNext(RainDetailBean rainDetailBean) {
                if (rainDetailBean.getCode()==0){
                    RainDetailBean.DataBean data = rainDetailBean.getData();
                    if (mView!=null){
                        mView.success(rainDetailBean);
                    }
                }else {
                    if (mView!=null){
                        mView.failure(rainDetailBean.getMsg());
                    }
                }
            }

            @Override
            protected void _onError(String message) {
                if (mView!=null){
                    mView.failure(message);
                }
            }
        });
    }

    @Override
    public void getReservoirWaterList(String reservoirId, String startDate, String endDate, boolean isShow) {
        ReservoirDetailManager.getInstance_Monitor().getReservoirWaterList(reservoirId, startDate, endDate).subscribe(new LoadingSubject<WaterRegimeDetailBean>(isShow,"正在加载中...") {
            @Override
            protected void _onNext(WaterRegimeDetailBean waterRegimeDetailBean) {
                if (waterRegimeDetailBean!=null&&waterRegimeDetailBean.getCode()==0){
                    if (mView!=null){
                        mView.success(waterRegimeDetailBean);
                    }
                }else {
                    if (mView!=null){
                        mView.failure(waterRegimeDetailBean.getMsg());
                    }
                }
            }

            @Override
            protected void _onError(String message) {
                if (mView!=null){
                    mView.failure(message);
                }
            }
        });
    }

    @Override
    public void getPictureDetailList(String reservoirId, String startDate, String endDate, boolean isShow) {
        ReservoirDetailManager.getInstance_Monitor().getPictureDetailList(reservoirId, startDate, endDate).subscribe(new LoadingSubject<PicDetailBean>(isShow,"正在加载中...") {
            @Override
            protected void _onNext(PicDetailBean picDetailBean) {
                if (picDetailBean!=null&&picDetailBean.getCode()==0){
                    if (mView!=null){
                        mView.success(picDetailBean);
                    }
                }else {
                    if (mView!=null){
                        mView.failure(picDetailBean.getMsg());
                    }
                }
            }

            @Override
            protected void _onError(String message) {
                if (mView!=null){
                    mView.failure(message);
                }
            }
        });
    }
}
