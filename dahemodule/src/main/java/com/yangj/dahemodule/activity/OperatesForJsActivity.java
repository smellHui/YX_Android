package com.yangj.dahemodule.activity;

import com.tepia.base.mvp.BaseActivity;
import com.yangj.dahemodule.R;
import com.yangj.dahemodule.fragment.operate.OperatesFragment;
import com.yangj.dahemodule.view.SearchToolBar;

import static com.yangj.dahemodule.fragment.operate.OperatesFragment.ALL_OPERATE;

/**
 * Author:xch
 * Date:2019/10/17
 * Description:
 */
public class OperatesForJsActivity extends BaseActivity implements SearchToolBar.DataSelectListener {
    private SearchToolBar searchToolBar;
    private String startTime, endTime;
    private OperatesFragment fragment;

    @Override
    public int getLayoutId() {
        return R.layout.activity_operates_for_js;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        searchToolBar = findViewById(R.id.view_search_tool_bar);
        searchToolBar.setDataSelectListener(this);
        fragment = OperatesFragment.launch(ALL_OPERATE);
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

    @Override
    public void onDataSelectPickListener(String startTime, String endTime, int cate) {
        fragment.refresh(startTime, endTime, cate);
    }
}
