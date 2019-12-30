package com.yangj.dahemodule.activity;

import com.tepia.base.mvp.BaseActivity;
import com.yangj.dahemodule.R;
import com.yangj.dahemodule.fragment.DealFragment;

import static com.yangj.dahemodule.fragment.DealFragment.DEAL_COMPLETE;

/**
 * Author:xch
 * Date:2019/10/17
 * Description:
 */
public class MyDealActivity extends BaseActivity {

    private DealFragment fragment;

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_deal;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        setLeftTitle("险情上报记录");
        showBack();
        fragment = DealFragment.newInstance(DEAL_COMPLETE);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initRequestData() {

    }
}
