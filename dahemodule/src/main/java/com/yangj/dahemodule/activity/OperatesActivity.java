package com.yangj.dahemodule.activity;

import android.support.v4.view.ViewPager;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.tepia.base.common.CommonFragmentPagerAdapter;
import com.tepia.base.mvp.BaseActivity;
import com.yangj.dahemodule.R;
import com.yangj.dahemodule.fragment.operate.OperatesFragment;
import com.yangj.dahemodule.model.TabEntity;
import com.yangj.dahemodule.view.SearchToolBar;

import java.util.ArrayList;

import static com.yangj.dahemodule.fragment.operate.OperatesFragment.ALL_OPERATE;
import static com.yangj.dahemodule.fragment.operate.OperatesFragment.MINE_OPERATE;

/**
 * Author:xch
 * Date:2019/8/27
 * Description:
 */
public class OperatesActivity extends BaseActivity implements SearchToolBar.DataSelectListener {

    private SearchToolBar searchToolBar;
    private CommonTabLayout mTabLayout;
    private ViewPager viewPager;
    private CommonFragmentPagerAdapter pagerAdapter;
    private OperatesFragment myOperatesFragment, allOperatesFragment;
    private int nowIndex;

    @Override
    public int getLayoutId() {
        return R.layout.activity_operates;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        searchToolBar = findViewById(R.id.view_search_tool_bar);
        mTabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.vp);

        searchToolBar.setDataSelectListener(this);

        ArrayList<CustomTabEntity> tabEntities = new ArrayList<>();
        tabEntities.add(new TabEntity("我的巡查"));
        tabEntities.add(new TabEntity("全部巡查"));
        mTabLayout.setTabData(tabEntities);
        pagerAdapter = new CommonFragmentPagerAdapter(getSupportFragmentManager());
        myOperatesFragment = OperatesFragment.launch(MINE_OPERATE);
        allOperatesFragment = OperatesFragment.launch(ALL_OPERATE);
        pagerAdapter.addFragment(myOperatesFragment);
        pagerAdapter.addFragment(allOperatesFragment);
        viewPager.setAdapter(pagerAdapter);
        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                nowIndex = position;
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                nowIndex = position;
                mTabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    public void setStatusBarTextDark() {
        searchToolBar.setImmersionBar(getmImmersionBar());
        super.setStatusBarTextDark();
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initRequestData() {

    }

    @Override
    public void onDataSelectPickListener(String startTime, String endTime, int cate) {
        if (nowIndex == 0) {
            myOperatesFragment.refresh(startTime, endTime, cate);
        } else {
            allOperatesFragment.refresh(startTime, endTime, cate);
        }
    }
}
