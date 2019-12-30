package com.tepia.guangdong_module.amainguangdong.common;

import android.databinding.DataBindingUtil;
import android.text.SpannableString;
import android.view.View;

import com.example.guangdong_module.R;
import com.example.guangdong_module.databinding.FragmentDialogTjBinding;
import com.tepia.base.mvp.BaseDialogFragment;

/**
 * Created by      android studio
 *
 * @author :      zhang xinhua
 * Date            :       2019/1/3
 * Time            :       14:43
 * Version         :       1.0
 * 功能描述        :
 **/
public class TjDialogFragment extends BaseDialogFragment {
    private FragmentDialogTjBinding mBinding;
    public SpannableString tip;
    private View.OnClickListener onClickListener;
    private View.OnClickListener onClickListener1;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_dialog_tj;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view) {
        mBinding = DataBindingUtil.bind(view);
        mBinding.tvMessage.setText(tip);
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
