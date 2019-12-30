package com.tepia.guangdong_module.amainguangdong.xunchaview.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.guangdong_module.R;
import com.tepia.base.AppRoutePath;
import com.tepia.base.mvp.BaseActivity;
import com.tepia.base.utils.TimeFormatUtils;
import com.tepia.guangdong_module.amainguangdong.common.UserManager;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.ReservoirBean;
import com.tepia.guangdong_module.amainguangdong.utils.EmptyLayoutUtil;
import com.tepia.guangdong_module.amainguangdong.xunchaview.adapter.MySelectReservoirAdapter;

import java.util.ArrayList;

/**
 * Created by      Android studio
 *
 * @author :wwj (from Center Of Wuhan)
 * Date    :2018/10/16
 * Version :1.0
 * 功能描述 :搜索水库/离线数据包
 **/
@Route(path = AppRoutePath.app_select_reservor_downoffline)
public class ChoiceReservoirActivity extends BaseActivity {
    String offlineFlag;

    public static final int resultCode = 1001;
    public static String isAllReservoir = "isAllReservoir";
    public static String isFromYunWei = "isFromYunWei";

    private EditText etSearch;
    private RecyclerView rvSelectReservoir;
    private ArrayList<ReservoirBean> localReservoirList;
    private ArrayList<ReservoirBean> reservoirList = new ArrayList<>();
    private ArrayList<ReservoirBean> searchReservoirList = new ArrayList<>();
    private MySelectReservoirAdapter mySelectReservoirAdapter;
    private boolean isSelectAll;
    public static String isKeyBack = "isKeyBack";
    private boolean isFromYunWeiF;

    ReservoirBean selectedResrvoir;


    public static Intent setIntent(Intent intent, boolean isSelectAll) {
        intent.putExtra("isSelectAll", isSelectAll);
        return intent;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_reservoir;
    }

    @Override
    public void initView() {
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("offlineFlag")) {
            offlineFlag = getIntent().getExtras().getString("offlineFlag");
        }
        if ("10".equals(offlineFlag)) {
            setCenterTitle("离线数据包管理");
            showBack();
            selectedResrvoir = (ReservoirBean) getIntent().getSerializableExtra("selectedResrvoir");
        }else {
            setCenterTitle("选择水库");
        }
        TextView tvLeftText = findViewById(R.id.tv_left_text);
        tvLeftText.setVisibility(View.VISIBLE);
        tvLeftText.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra(isKeyBack, true);
            ChoiceReservoirActivity.this.setResult(resultCode, intent);
            finish();
        });
        isSelectAll = getIntent().getBooleanExtra("isSelectAll", false);
        isFromYunWeiF = getIntent().getBooleanExtra(isFromYunWei, false);
        etSearch = findViewById(R.id.et_search);
        rvSelectReservoir = findViewById(R.id.rv_select_reservoir);
        initRecyclerView();
        initSearch();
    }

    private void initSearch() {
        //回车不换行
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchStr = s.toString().replaceAll(" ", "");
                if (null != reservoirList && reservoirList.size() > 0) {
                    searchReservoirList.clear();
                    for (int i = 0; i < reservoirList.size(); i++) {
                        String reservoir = reservoirList.get(i).getReservoir();
                        if (reservoir.contains(searchStr)) {
                            searchReservoirList.add(reservoirList.get(i));
                        }
                    }
                    if (null != searchReservoirList && searchReservoirList.size() > 0) {
                        mySelectReservoirAdapter.notifyDataSetChanged();
                    } else {
                        mySelectReservoirAdapter.setEmptyView(EmptyLayoutUtil.showNew("暂无数据"));
                        mySelectReservoirAdapter.notifyDataSetChanged();
                    }
                } else {
                    mySelectReservoirAdapter.setEmptyView(EmptyLayoutUtil.showNew("暂无数据"));
                    mySelectReservoirAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initRecyclerView() {
        rvSelectReservoir.setLayoutManager(new LinearLayoutManager(this));
        String currentTime = TimeFormatUtils.getUserDate("yyyy-MM-dd");
        mySelectReservoirAdapter = new MySelectReservoirAdapter(R.layout.lv_select_reservoir_item, searchReservoirList,currentTime,offlineFlag);
        rvSelectReservoir.setAdapter(mySelectReservoirAdapter);
        if (null != localReservoirList) {
            /*if (isSelectAll) {
                ReservoirBean reservoirBean = new ReservoirBean();
                reservoirBean.setReservoir("全部");
                searchReservoirList.add(reservoirBean);
            }*/
            if (selectedResrvoir != null) {
                searchReservoirList.add(selectedResrvoir);
            }else {
                searchReservoirList.addAll(localReservoirList);
            }
            reservoirList.addAll(searchReservoirList);
            mySelectReservoirAdapter.notifyDataSetChanged();
        }
        mySelectReservoirAdapter.setOnItemClickListener((adapter, view, position) -> {
            /*if ("10".equals(offlineFlag)) {
                if (null != searchReservoirList && searchReservoirList.size() >= (position)) {
                    ReservoirBean reservoirBean = searchReservoirList.get(position);
                    if (!"全部".equals(reservoirBean.getReservoir())){
                        mySelectReservoirAdapter.getAllReservoirData();
                    }
                }
                return;
            }*/

            if ("10".equals(offlineFlag)) {
                return;
            }

            if (null != searchReservoirList && searchReservoirList.size() >= (position)) {
                ReservoirBean reservoirBean = searchReservoirList.get(position);
                if ("全部".equals(reservoirBean.getReservoir())) {
                    Intent intent = new Intent();
                    intent.putExtra(isAllReservoir, true);
                    ChoiceReservoirActivity.this.setResult(resultCode, intent);
                    finish();
                    return;
                }else{
                    if (isFromYunWeiF){
                        Intent intent = new Intent();
                        String reservoirId = reservoirBean.getReservoirId();
                        String reservoir = reservoirBean.getReservoir();
                        intent.putExtra("reservoirId",reservoirId);
                        intent.putExtra("reservoir",reservoir);
                        ChoiceReservoirActivity.this.setResult(resultCode, intent);
                        finish();
                        return;
                    }
                }
                UserManager.getInstance().saveDefaultReservoir(reservoirBean);
                ChoiceReservoirActivity.this.setResult(resultCode);
                finish();
            }
        });

       /* mySelectReservoirAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                ToastUtils.shortToast("删除");
                List<ReservoirBean> reservoirBeanList = DataSupport.where("reservoirId = ?",localReservoirList.get(position).getReservoirId()).find(ReservoirBean.class);


                return false;
            }
        });*/


    }



    @Override
    public void initData() {
        localReservoirList = UserManager.getInstance().getLocalReservoirList();
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initRequestData() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.putExtra(isKeyBack, true);
            ChoiceReservoirActivity.this.setResult(resultCode, intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
