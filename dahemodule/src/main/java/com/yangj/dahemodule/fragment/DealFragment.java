package com.yangj.dahemodule.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.tepia.base.common.CommonFragmentPagerAdapter;
import com.tepia.base.mvp.BaseCommonFragment;
import com.yangj.dahemodule.R;
import com.yangj.dahemodule.fragment.deal.DealsFragment;
import com.yangj.dahemodule.model.TabEntity;

import java.util.ArrayList;

/**
 * Author:xch
 * Date:2019/10/12
 * Description:险情处置
 */
public class DealFragment extends BaseCommonFragment {

    public static final String DEAL_TYPE = "dealType";
    public static final int DEAL_DOING = 3;
    public static final int DEAL_COMPLETE = DEAL_DOING << 1;

    private CommonTabLayout mTabLayout;
    private TextView tv_tab;
    private ViewPager viewPager;
    private CommonFragmentPagerAdapter pagerAdapter;
    private DealsFragment one, three, four;

    private int dealType;

    public static DealFragment newInstance(int dealType) {
        DealFragment fragment = new DealFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(DEAL_TYPE, dealType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_deal;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            dealType = bundle.getInt(DEAL_TYPE, DEAL_DOING);
        }
    }

    @Override
    protected void initView(View view) {
        tv_tab = findView(R.id.tv_tab);
        tv_tab.setVisibility(dealType == DEAL_DOING ? View.VISIBLE : View.GONE);
        mTabLayout = findView(R.id.tab_layout);
        viewPager = findView(R.id.vp);

        ArrayList<CustomTabEntity> tabEntities = new ArrayList<>();
        tabEntities.add(new TabEntity("全部"));
        tabEntities.add(new TabEntity("APP上报"));
        tabEntities.add(new TabEntity("巡查发现"));
        mTabLayout.setTabData(tabEntities);
        pagerAdapter = new CommonFragmentPagerAdapter(getChildFragmentManager());
        one = DealsFragment.launch("", dealType);
        three = DealsFragment.launch("2", dealType);
        four = DealsFragment.launch("1", dealType);
        pagerAdapter.addFragment(one);
        pagerAdapter.addFragment(three);
        pagerAdapter.addFragment(four);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
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
            public void onPageSelected(int i) {
                mTabLayout.setCurrentTab(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    protected void initRequestData() {

    }
}
