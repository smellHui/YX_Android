package com.tepia.guangdong_module.amainguangdong.mvp.reservoirregimen;

import com.tepia.base.http.LoadingSubject;
import com.tepia.base.mvp.BasePresenterImpl;
import com.tepia.guangdong_module.amainguangdong.model.reservoirregimen.ReservoirRegimenResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Android Studio
 *
 * @author: Arthur
 * Date:    2019/5/29
 * Time:    17:28
 * Describe:
 */
public class ReservoirRegimenPresenter extends BasePresenterImpl<ReservoirRegimenContract.View>
        implements ReservoirRegimenContract.Presenter {

    /**
     *
     * 获取水情列表
     *
     * @param isShow
     * @param msg
     */
    @Override
    public void getReservoirRegimenList(int currentPage, int pageSize, String type, String identifyType,
            String startTime, String endTime, boolean isShow, String msg) {

        Calendar cd = Calendar.getInstance();
        int year = cd.get(Calendar.YEAR);
        startTime = year + "-" + startTime + ":00";
        endTime = year + "-" + endTime + ":00";
        ReservoirRegimenManager.getInstance()
                .getReservoirRegimenList(currentPage, pageSize, type, identifyType, startTime, endTime)
                .subscribe(new LoadingSubject<ReservoirRegimenResponse>(isShow, msg) {
                    @Override
                    protected void _onNext(ReservoirRegimenResponse realTimeMonitorResponse) {
                        if (mView != null) {
                            mView.showList(realTimeMonitorResponse);
                        }
                    }

                    @Override
                    protected void _onError(String message) {
                        if (mView != null) {
                            mView.onFail(message);
                        }
                    }
                });
    }

}
