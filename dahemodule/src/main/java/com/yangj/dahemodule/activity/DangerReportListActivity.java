package com.yangj.dahemodule.activity;

import com.tepia.base.mvp.BaseActivity;
import com.yangj.dahemodule.R;
import com.yangj.dahemodule.fragment.dangerReport.DangerReportListFragment;
import com.yangj.dahemodule.view.SearchToolBar;

/**
 * Author:xch
 * Date:2019/9/4
 * Description:
 */
public class DangerReportListActivity extends BaseActivity implements SearchToolBar.DataSelectListener {

    private DangerReportListFragment fragment;
    private SearchToolBar searchToolBar;

    @Override
    public int getLayoutId() {
        return R.layout.activity_danger_report_list;
    }

    @Override
    public void initData() {
    }

    @Override
    public void initView() {

        searchToolBar = findViewById(R.id.view_search_tool_bar);
        searchToolBar.setDataSelectListener(this);
        fragment = DangerReportListFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();

    }

    @Override
    protected void initListener() {

    }

    @Override
    public void setStatusBarTextDark() {
        searchToolBar.setImmersionBar(getmImmersionBar());
        super.setStatusBarTextDark();
    }

    @Override
    protected void initRequestData() {

    }

    @Override
    public void onDataSelectPickListener(String startTime, String endTime, int cate) {
        fragment.refresh(startTime, endTime, cate);
    }
}
