package com.yangj.dahemodule.intefaces;

import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.XPopupCallback;
import com.yangj.dahemodule.view.PatrolUpControlView;

/**
 * Author:xch
 * Date:2019/12/2
 * Description:
 */
public class PatrolPickerCallback implements XPopupCallback {

    private BasePopupView basePopupView;
    private int pickerIndex;

    public PatrolPickerCallback(BasePopupView basePopupView, int pickerIndex) {
        this.basePopupView = basePopupView;
        this.pickerIndex = pickerIndex;
    }

    @Override
    public void onCreated() {

    }

    @Override
    public void beforeShow() {
        if (basePopupView instanceof PatrolUpControlView) {
            ((PatrolUpControlView) basePopupView).scrollToPosition(pickerIndex);
        }
    }

    @Override
    public void onShow() {

    }

    @Override
    public void onDismiss() {

    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
