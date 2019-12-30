package com.tepia.guangdong_module.amainguangdong.xunchaview.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.guangdong_module.R;
import com.google.gson.Gson;
import com.tepia.base.mvp.BaseActivity;
import com.tepia.base.utils.SPUtils;
import com.tepia.base.utils.TimeFormatUtils;
import com.tepia.guangdong_module.amainguangdong.common.UserManager;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.ReservoirBean;
import com.tepia.guangdong_module.amainguangdong.utils.EmptyLayoutUtil;
import com.tepia.guangdong_module.amainguangdong.xunchaview.adapter.OfflineMapListAdapter;
import com.tepia.guangdong_module.APPCostant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by      Android studio
 *
 * @author :wwj (from Center Of Wuhan)
 * Date    :2019/5/16
 * Version :1.0
 * 功能描述 : 离线地图包管理
 **/
public class OfflineMapListActivity extends BaseActivity {
    private String downloadSpName = "downloadMap";
    public static String isKeyBack = "isKeyBack";
    public static final int resultCode = 1002;
    private EditText etSearch;
    private RecyclerView rvSelectReservoir;
    private ArrayList<ReservoirBean> searchReservoirList = new ArrayList<>();
    private ArrayList<ReservoirBean> reservoirList = new ArrayList<>();
    private OfflineMapListAdapter mySelectReservoirAdapter;
    private Map<String, Boolean> downloadMap;

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_reservoir;
    }

    @Override
    public void initData() {
        downloadMap = new HashMap();
        reservoirList = UserManager.getInstance().getLocalReservoirList();
        setReservoirList();
    }

    @Override
    public void initView() {
        setCenterTitle("离线地图包管理");
        TextView tvLeftText = findViewById(R.id.tv_left_text);
        tvLeftText.setVisibility(View.VISIBLE);
        tvLeftText.setOnClickListener(v -> {
//            Intent intent = new Intent();
//            intent.putExtra(isKeyBack, true);
//            OfflineMapListActivity.this.setResult(resultCode, intent);
            finish();
        });
        etSearch = findViewById(R.id.et_search);
        rvSelectReservoir = findViewById(R.id.rv_select_reservoir);
        initRecyclerView();
        initSearch();
    }

    private void initSearch() {
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
                        mySelectReservoirAdapter.setEmptyView(EmptyLayoutUtil.show("暂无数据"));
                        mySelectReservoirAdapter.notifyDataSetChanged();
                    }
                } else {
                    mySelectReservoirAdapter.setEmptyView(EmptyLayoutUtil.show("暂无数据"));
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
        mySelectReservoirAdapter = new OfflineMapListAdapter(R.layout.lv_select_reservoir_item, searchReservoirList);
        mySelectReservoirAdapter.addFooterView(EmptyLayoutUtil.getFootView());
        mySelectReservoirAdapter.setmOnDownClickListener(item -> {
            boolean offlineMap = item.isOfflineMap();
            if (offlineMap) {
                new AlertDialog.Builder(getContext())
                        .setTitle("是否重新下载离线地图包?")
                        .setMessage("重新下载将会删除本地已下载地图包并且消耗大量流量。")
                        .setCancelable(true)
                        .setPositiveButton("确定", (dialog, which) -> {
                            goDownLoadActivity(item);
                        })
                        .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                        .show();
            } else {
                goDownLoadActivity(item);
            }
        });
        rvSelectReservoir.setAdapter(mySelectReservoirAdapter);
        if (reservoirList != null) {
            searchReservoirList.addAll(reservoirList);
            mySelectReservoirAdapter.notifyDataSetChanged();
        } else {
            mySelectReservoirAdapter.setEmptyView(EmptyLayoutUtil.show("暂无数据"));
            mySelectReservoirAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 跳转地图离线包下载界面
     *
     * @param bean
     */
    private void goDownLoadActivity(ReservoirBean bean) {
        if (bean != null) {
            String reservoirCode = bean.getReservoirCode();
            String fileName = reservoirCode + ".tpk";
            Intent intent1 = new Intent(OfflineMapListActivity.this, OffLineMapDownLoadActivity.class);
            intent1.putExtra("fileName", fileName);
            intent1.putExtra("filePath", APPCostant.BASE_DOWN_MAP_URL + fileName);
            intent1.putExtra("bean", bean);
            startActivityForResult(intent1, DownLoadActivity.resultCode);
        }
    }

    /**
     * 设置书库列表数据
     */
    private void setReservoirList() {
        //文件存在
        String spDownloadStr = SPUtils.getInstance().getString(downloadSpName, "");
        if (reservoirList != null && reservoirList.size() > 0) {
            if (null == spDownloadStr || "".equals(spDownloadStr)) {
                //没有水库下载了离线地图包
            } else {
                downloadMap = new Gson().fromJson(spDownloadStr, downloadMap.getClass());
                if (downloadMap != null && downloadMap.size() > 0) {
                    for (Map.Entry<String, Boolean> entry : downloadMap.entrySet()) {
                        String key = entry.getKey();
                        for (ReservoirBean reservoirBean : reservoirList) {
                            String reservoirCode = reservoirBean.getReservoirCode() + ".tpk";
                            if (key.equals(reservoirCode)) {
                                Boolean isLoadComplete = entry.getValue();
                                reservoirBean.setOfflineMap(isLoadComplete);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initRequestData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case DownLoadActivity.resultCode:
                setReservoirList();
                if (reservoirList != null) {
                    searchReservoirList.clear();
                    searchReservoirList.addAll(reservoirList);
                    mySelectReservoirAdapter.notifyDataSetChanged();
                } else {
                    mySelectReservoirAdapter.setEmptyView(EmptyLayoutUtil.show("暂无数据"));
                    mySelectReservoirAdapter.notifyDataSetChanged();
                }
                break;
            default:
                break;
        }
    }
}
