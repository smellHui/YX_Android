package com.tepia.guangdong_module.amainguangdong.xunchaview.fragment;


/*************************************************************
 * Created by OCN.YAN                                        *
 * 主要功能:基类                                              *
 * 项目名:贵州水务                                            *
 * 包名:com.elegant.river_system.vm.base                        *
 * 创建时间:2017年10月12日11:16                               *
 * 更新时间:2017年10月24日11:16                               *
 * 版本号:1.1.0                                              *
 *************************************************************/
public class TabMainFragmentFactory {


    private static TabMainFragmentFactory mInstance;

    public static TabMainFragmentFactory getInstance() {
        if (mInstance == null) {
            synchronized (TabMainFragmentFactory.class) {
                if (mInstance == null) {
                    mInstance = new TabMainFragmentFactory();
                }
            }
        }
        return mInstance;
    }

    /**
     * 工作模块
     */
    public MainFragment mainFragment;

    public MainFragment getMainFragment() {
        if (mainFragment == null) {
            synchronized (TabMainFragmentFactory.class) {
                if (mainFragment == null) {
                    mainFragment = new MainFragment();
                }
            }
        }
        return mainFragment;
    }

    /**
     * 行政统计页面
     */
    public AdminStatisticsFragment adminStatisticsFragment;

    public AdminStatisticsFragment getAdminStatisticsFragment() {
        if (adminStatisticsFragment == null) {
            synchronized (TabMainFragmentFactory.class) {
                if (adminStatisticsFragment == null) {
                    adminStatisticsFragment = new AdminStatisticsFragment();
                }
            }
        }
        return adminStatisticsFragment;
    }



    private YunweiFragment yunweiFragment;

    public YunweiFragment getYunweiFragment() {
        if (yunweiFragment == null) {
            synchronized (TabMainFragmentFactory.class) {
                if (yunweiFragment == null) {
                    yunweiFragment = new YunweiFragment();
                }
            }
        }
        return yunweiFragment;
    }

    /**
     * 我的
     */
    private SettingFragment settingFragment;

    public SettingFragment getSettingFragment() {
        if (settingFragment == null) {
            synchronized (TabMainFragmentFactory.class) {
                if (settingFragment == null) {
                    settingFragment = new SettingFragment();
                }
            }
        }
        return settingFragment;
    }


    /**
     * 
     */
    public void clearFragment(){
        if(mainFragment != null){
            mainFragment = null;
        }
        if (yunweiFragment != null) {
            yunweiFragment = null;
        }
        if (settingFragment != null) {
            settingFragment = null;
        }

        if (adminStatisticsFragment != null) {
            adminStatisticsFragment = null;
        }


    }


}
