package com.tepia.guangdong_module.amainguangdong.xunchaview.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.AdapterView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.guangdong_module.R;
import com.example.guangdong_module.databinding.ActivityReserviorSearchListBinding;
import com.tepia.base.common.DictMapEntity;
import com.tepia.base.http.LoadingSubject;
import com.tepia.base.mvp.MVPBaseActivity;
import com.tepia.base.utils.LogUtil;
import com.tepia.base.utils.ResUtils;
import com.tepia.base.utils.ToastUtils;
import com.tepia.base.view.dialog.basedailog.ActionSheetDialog;
import com.tepia.base.view.dialog.basedailog.OnOpenItemClick;
import com.tepia.base.view.floatview.CollectionsUtil;
import com.tepia.guangdong_module.amainguangdong.common.DictMapManager;
import com.tepia.guangdong_module.amainguangdong.common.UserManager;
import com.tepia.guangdong_module.amainguangdong.model.adminstatistics.RealTimeMonitorResponse;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.AreaBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.ReserviorListBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.UserReservoirCount;
import com.tepia.guangdong_module.amainguangdong.mvp.reserviorlistmvp.ReserviorManager;
import com.tepia.guangdong_module.amainguangdong.mvp.reserviorlistmvp.ReservoirListContract;
import com.tepia.guangdong_module.amainguangdong.mvp.reserviorlistmvp.ReservoirListPresenter;
import com.tepia.guangdong_module.amainguangdong.utils.EmptyLayoutUtil;
import com.tepia.guangdong_module.amainguangdong.xunchaview.adapter.AdapterReserList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by      Android studio
 *
 * @author :ly (from Center Of Wuhan)
 * 创建时间 : 2019-5-30
 * 更新时间 :
 * Version :1.0
 * 功能描述 : reservior information list
 **/
public class ReserviorSearchListActivity extends MVPBaseActivity<ReservoirListContract.View, ReservoirListPresenter> implements ReservoirListContract.View<ReserviorListBean> {

    ActivityReserviorSearchListBinding mBinding;

    AdapterReserList netOfadapterPatrolWorkOrderList;
    List<ReserviorListBean.DataBean.ListBean> netOftaskBeanList;

    private int netOfpageSize = 10;
    private int netOfcurrentPage = 1;
    private int netOfmCurrentCounter = 0;
    private boolean netOffirst;
    private boolean netOfisloadmore;

    String reserior, areaCode, reservoirType;
    //    List<String> options1Items, options2Items, options3Items;
    private ArrayList<AreaBean.DataBeanX.DataBean> options1Items = new ArrayList<>(); //省
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();//市
    private ArrayList<ArrayList<String>> options2ItemsOfareaCode = new ArrayList<>();//市
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();//
    private ArrayList<ArrayList<ArrayList<String>>> options3ItemsOfareacode = new ArrayList<>();//


    @Override
    public int getLayoutId() {
        return R.layout.activity_reservior_search_list;
    }


    @Override
    public void initData() {
        mBinding = DataBindingUtil.bind(mRootView);
        netOftaskBeanList = new ArrayList<>();
        mBinding.levelTv.setText("全部");

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCenterTitle("水库信息列表");
        showBack();
        initRecOfnet();
        search(true);
    }

    @Override
    public void initView() {


    }


    /**
     * 初始化服务器rec
     */
    private void initRecOfnet() {
        mBinding.tongjiRec.setLayoutManager(new LinearLayoutManager(this));
        netOfadapterPatrolWorkOrderList = new AdapterReserList(R.layout.reservoir_list_layout, netOftaskBeanList);
        mBinding.tongjiRec.setAdapter(netOfadapterPatrolWorkOrderList);

        netOfadapterPatrolWorkOrderList.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {

                mBinding.tongjiRec.postDelayed(() -> {
                    netOfcurrentPage++;
                    netOffirst = false;
                    netOfisloadmore = true;
                    mPresenter.listReservoirInfo(reserior, reservoirType, areaCode, netOfcurrentPage, netOfpageSize, false);


                }, 1000);


            }
        }, mBinding.tongjiRec);
    }

    private void search(boolean isshowloadiing) {
        if (netOfadapterPatrolWorkOrderList != null) {

            netOfadapterPatrolWorkOrderList.setEnableLoadMore(false);
            netOftaskBeanList.clear();
            netOfadapterPatrolWorkOrderList.notifyDataSetChanged();
            netOfcurrentPage = 1;
            netOffirst = true;
            netOfisloadmore = false;
            if (mPresenter != null) {
                mPresenter.listReservoirInfo(reserior, reservoirType, areaCode, netOfcurrentPage, netOfpageSize, isshowloadiing);


            }
        }

    }

    @Override
    protected void initListener() {
        mBinding.nameTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null) {
                    reserior = s.toString();
                }
            }
        });

        mBinding.levelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        mBinding.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search(true);
            }
        });

        mBinding.areaTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (initAreaDataSuccess) {
                    showPickerView();
                }
            }
        });

        ArrayList<AreaBean.DataBeanX.DataBean> jsonBean = UserManager.getInstance().getArea();

        if (CollectionsUtil.isEmpty(jsonBean)) {
            UserManager.getInstance_ADMIN().getAreaSelect().subscribe(new LoadingSubject<AreaBean>(true, "获取地区数据....") {
                @Override
                protected void _onNext(AreaBean areaBean) {
                    if (areaBean != null) {
                        if (areaBean.getCode() == 0) {
                            ArrayList<AreaBean.DataBeanX.DataBean> jsonBean = areaBean.getData().getData();
                            UserManager.getInstance().savaArea(jsonBean);
                            initJsonData(jsonBean);

                        }
                    }
                }

                @Override
                protected void _onError(String message) {
                    LogUtil.e(message);

                }
            });

        } else {
            initJsonData(jsonBean);
        }


    }

    /**
     * 省市三级联动
     */
    private void showPickerView() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                //options1Items.get(options1).getPickerViewText() + ">"
                //                        +

                if (options1Items.size() > 0 && options2Items.size() > 0 && options3Items.size() > 0) {
                    String tx = options2Items.get(options1).get(options2) + ">"
                            + options3Items.get(options1).get(options2).get(options3);
                    areaCode = options3ItemsOfareacode.get(options1).get(options2).get(options3);
                    mBinding.areaTv.setText(tx);

                } else if (options1Items.size() > 0 && options2Items.size() > 0 && options3Items.size() == 0) {
                    String tx = options2Items.get(options1).get(options2) + ">";
                    areaCode = options2ItemsOfareaCode.get(options1).get(options2);
                    mBinding.areaTv.setText(tx);

                } else if (options1Items.size() > 0 && options2Items.size() == 0 && options3Items.size() == 0) {
                    String tx = options1Items.get(options1).getLabel();
                    areaCode = options1Items.get(options1).getAreaCode();
                    mBinding.areaTv.setText(tx);


                } else {
                    areaCode = "";
                    mBinding.areaTv.setText("");
                }

            }
        }).build();

        if (options1Items.size() > 0 && options2Items.size() > 0 && options3Items.size() > 0) {
            pvOptions.setPicker(options1Items, options2Items, options3Items);

        } else if (options1Items.size() > 0 && options2Items.size() > 0 && options3Items.size() == 0) {
            pvOptions.setPicker(options1Items, options2Items);

        } else if (options1Items.size() > 0 && options2Items.size() == 0 && options3Items.size() == 0) {
            pvOptions.setPicker(options1Items);
        } else {
            ToastUtils.shortToast("暂无可选乡镇");
            return;
        }
        pvOptions.show();
    }

    private boolean initAreaDataSuccess;

    private void initJsonData(ArrayList<AreaBean.DataBeanX.DataBean> jsonBean) {//解析数据 （省市区三级联动）


        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;
        //遍历省份
        for (int i = 0; i < jsonBean.size(); i++) {
            //该省的城市列表（第二级）
            ArrayList<String> CityList = new ArrayList<>();
            ArrayList<String> CityListOfareaCode = new ArrayList<>();
            //该省的所有地区列表（第三级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();
            ArrayList<ArrayList<String>> Province_AreaListOfareacode = new ArrayList<>();
            //遍历该省份的所有城市
            List<AreaBean.DataBeanX.DataBean.ChildrenBeanX> childrenBeanXList = jsonBean.get(i).getChildren();
            if (childrenBeanXList != null) {
                for (int c = 0; c < childrenBeanXList.size(); c++) {
                    String CityName = childrenBeanXList.get(c).getLabel();
                    String CityareaCode = childrenBeanXList.get(c).getAreaCode();
                    //添加城市
                    CityList.add(CityName);
                    CityListOfareaCode.add(CityareaCode);
                    //该城市的所有地区列表
                    ArrayList<String> City_AreaList = new ArrayList<>();
                    ArrayList<String> City_AreaListOfareaCode = new ArrayList<>();

                    //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                    if (CollectionsUtil.isEmpty(childrenBeanXList.get(c).getChildren())) {
                        City_AreaList.add("");
                        City_AreaListOfareaCode.add("");
                    } else {
                        for (AreaBean.DataBeanX.DataBean.ChildrenBeanX.ChildrenBean childrenBean : childrenBeanXList.get(c).getChildren()) {
                            City_AreaList.add(childrenBean.getAreaName());
                            City_AreaListOfareaCode.add(childrenBean.getAreaCode());
                        }
                    }
                    //添加该省所有地区数据
                    Province_AreaList.add(City_AreaList);
                    Province_AreaListOfareacode.add(City_AreaListOfareaCode);
                }
            }

            /**
             * 添加城市数据
             */
            if (!CollectionsUtil.isEmpty(CityList)) {
                options2Items.add(CityList);
                options2ItemsOfareaCode.add(CityListOfareaCode);

            }

            /**
             * 添加地区数据
             */
            if (!CollectionsUtil.isEmpty(Province_AreaList)) {
                options3Items.add(Province_AreaList);
                options3ItemsOfareacode.add(Province_AreaListOfareacode);
            }

        }
        initAreaDataSuccess = true;
    }


    private void showDialog() {
        DictMapEntity dictMapEntity = DictMapManager.getInstance().getmDictMap();
        List<DictMapEntity.ArrayBean.NameValueBean> nameValueBeanList = dictMapEntity.getArray().getReservoir_type();
        int size = nameValueBeanList.size();
        String[] stringItems = new String[size + 1];
        stringItems[0] = "全部";
        for (int i = 0; i < size; i++) {
            DictMapEntity.ArrayBean.NameValueBean nameValueBean = nameValueBeanList.get(i);
            stringItems[i + 1] = nameValueBean.getName();

        }
        final ActionSheetDialog dialog = new ActionSheetDialog(this, stringItems, null);
        dialog.title("请选择级别")
                .titleTextSize_SP(14.5f)
                .widthScale(0.8f)
                .show();
        dialog.setOnOpenItemClickL(new OnOpenItemClick() {
            @Override
            public void onOpenItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    reservoirType = "";
                    mBinding.levelTv.setText(stringItems[position]);
                } else {
                    reservoirType = nameValueBeanList.get(position - 1).getValue();
                    mBinding.levelTv.setText(stringItems[position]);
                }
                dialog.dismiss();

            }
        });
    }

    @Override
    protected void initRequestData() {

        ReserviorManager.getInstance().getUserReservoirCount().subscribe(new LoadingSubject<UserReservoirCount>(false, "") {
            @Override
            protected void _onNext(UserReservoirCount userReservoirCount) {
                if (userReservoirCount != null) {
                    if (userReservoirCount.getCode() == 0) {
                        UserReservoirCount.DataBean dataBean = userReservoirCount.getData();
                        String contentInspection =  "<font color=\"#373737\">共管理水库%1$d座，其中小(1)型%2$d座，小(2)型%3$d座，其中</font>"
                                + "<font color=\"#D43339\">%4$d</font>"
                                + "<font color=\"#373737\">座超汛限运行</font>";
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            mBinding.tongjiTv.setText(
                                    Html.fromHtml(String.format(contentInspection, dataBean.getAllCount()
                                            , dataBean.getSmallOneCount(), dataBean.getSmallTwoCount(),dataBean.getOverLevelConut()), Html.FROM_HTML_MODE_LEGACY));
                        } else {
                            mBinding.tongjiTv.setText(Html.fromHtml(String.format(contentInspection, dataBean.getAllCount()
                                    , dataBean.getSmallOneCount(), dataBean.getSmallTwoCount(),dataBean.getOverLevelConut())));
                        }
                        String str = String.format("共管理水库%d座，其中小(1)型%d座，小(2)型%d座，其中", dataBean.getAllCount()
                                , dataBean.getSmallOneCount(), dataBean.getSmallTwoCount());
                        String strtwo = String.format("%d", dataBean.getOverLevelConut());
                        String strthree = "座超汛限运行";



                      /*  SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                        spannableStringBuilder.append(str);
                        spannableStringBuilder.append(strtwo);
                        spannableStringBuilder.append(strthree);
                        ForegroundColorSpan colorSpan = new ForegroundColorSpan(ResUtils.getColor(R.color.common_red));
                        spannableStringBuilder.setSpan(colorSpan, str.length(), (str + strtwo).length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mBinding.tongjiTv.setText(spannableStringBuilder.toString());
                            }
                        });*/
                    }
                }
            }

            @Override
            protected void _onError(String message) {

            }
        });
    }

    private void netOfshowData(ReserviorListBean taskBeanFromNet) {
        List<ReserviorListBean.DataBean.ListBean> data = taskBeanFromNet.getData().getList();
        int totalSize = taskBeanFromNet.getData().getTotal();
        if (netOffirst) {
            //首次加载
            if (data == null || data.size() == 0) {
//                        showEmptyView();
                netOfadapterPatrolWorkOrderList.setEmptyView(EmptyLayoutUtil.showNew("暂无数据"));
            } else {
                netOftaskBeanList.clear();
                //首次加载
                netOftaskBeanList.addAll(data);
                netOfadapterPatrolWorkOrderList.notifyDataSetChanged();

            }
            netOfadapterPatrolWorkOrderList.setEnableLoadMore(true);
        } else if (netOfisloadmore) {

            netOfadapterPatrolWorkOrderList.addData(data);
            netOfmCurrentCounter = netOfadapterPatrolWorkOrderList.getData().size();
            if (netOfmCurrentCounter >= totalSize) {
                //数据全部加载完毕
                netOfadapterPatrolWorkOrderList.loadMoreEnd();
                return;
            }
            netOfadapterPatrolWorkOrderList.loadMoreComplete();

        }
    }

    @Override
    public void success(ReserviorListBean reserviorListBean) {
        netOfshowData(reserviorListBean);
    }

    @Override
    public void failure(String mes) {
        netOfadapterPatrolWorkOrderList.setEmptyView(EmptyLayoutUtil.showNew(mes));

    }

    @Override
    public void showRealTimeMonitor(RealTimeMonitorResponse data) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (options1Items != null) {
            options1Items.clear();
        }
        if (options2Items != null) {
            options2Items.clear();
            options2ItemsOfareaCode.clear();
        }
        if (options3Items != null) {
            options3Items.clear();
            options3ItemsOfareacode.clear();
        }

        if (netOftaskBeanList != null) {
            netOftaskBeanList.clear();
        }
    }
}
