package com.tepia.guangdong_module.amainguangdong.common;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.text.Html;
import android.text.SpannableString;
import android.view.View;

import com.example.guangdong_module.R;
import com.example.guangdong_module.databinding.FragmentDialogTjBinding;
import com.example.guangdong_module.databinding.FragmentDialogWeatherBinding;
import com.tepia.base.mvp.BaseDialogFragment;
import com.tepia.guangdong_module.amainguangdong.model.WeatherWarnBean;

/**
 * Created by      android studio
 *
 * @author :      zhang xinhua
 * Date            :       2019/1/3
 * Time            :       14:43
 * Version         :       1.0
 * 功能描述        :
 **/
public class WeatherDialogFragment extends BaseDialogFragment {
    private FragmentDialogWeatherBinding mBinding;
    public WeatherWarnBean.DataBean dataBean;
    private View.OnClickListener onClickListener;
    private View.OnClickListener onClickListener1;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_dialog_weather;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view) {
        mBinding = DataBindingUtil.bind(view);
        mBinding.categoryCnnameTv.setText(dataBean.getCategoryCnname()+dataBean.getDepartmentlevelCnname()+"预警"+"\n"+dataBean.getPublishdate());


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mBinding.tvMessage.setText(Html.fromHtml(dataBean.getContent(),0));

        }else {
            mBinding.tvMessage.setText(Html.fromHtml(dataBean.getContent()));

        }
        mBinding.btCancel.setOnClickListener(onClickListener1);
        mBinding.btSure.setOnClickListener(onClickListener);
    }

    @Override
    protected void initRequestData() {

    }

    public void setListener(View.OnClickListener onClickListener, View.OnClickListener onClickListener1) {
        this.onClickListener = onClickListener;
        this.onClickListener1 = onClickListener1;
    }
}
