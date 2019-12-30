package com.tepia.guangdong_module.amainguangdong.xunchaview.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.example.guangdong_module.R;
import com.example.guangdong_module.databinding.ActivityDangerReportBinding;
import com.tepia.base.CacheConsts;
import com.tepia.base.ConfigConsts;
import com.tepia.base.http.BaseResponse;
import com.tepia.base.http.LoadingSubject;
import com.tepia.base.mvp.BaseActivity;
import com.tepia.base.utils.LogUtil;
import com.tepia.base.utils.NetUtil;
import com.tepia.base.utils.ResUtils;
import com.tepia.base.utils.ToastUtils;
import com.tepia.base.utils.Utils;
import com.tepia.base.view.dialog.basedailog.ActionSheetDialog;
import com.tepia.base.view.floatview.CollectionsUtil;
import com.tepia.guangdong_module.amainguangdong.common.PhotoSelectAdapter;
import com.tepia.guangdong_module.amainguangdong.common.UserManager;
import com.tepia.guangdong_module.amainguangdong.common.pickview.PhotoRecycleViewAdapter;
import com.tepia.guangdong_module.amainguangdong.model.DangerousPosition;
import com.tepia.guangdong_module.amainguangdong.model.UserInfo;
import com.tepia.guangdong_module.amainguangdong.model.UtilDataBaseOfGD;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.DangerBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.ReservoirBean;
import com.tepia.guangdong_module.amainguangdong.mvp.taskdetail.TaskManager;
import com.tepia.guangdong_module.amainguangdong.utils.EmptyLayoutUtil;
import com.tepia.guangdong_module.amainguangdong.xunchaview.adapter.AdapterWorker;
import com.tepia.photo_picker.PhotoPicker;
import com.tepia.photo_picker.PhotoPreview;
import com.tepia.photo_picker.entity.CheckTaskPicturesBean;
import com.tepia.photo_picker.utils.SPUtils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by      Android studio
 *
 * @author :ly (from Center Of Wuhan)
 * 创建时间 :2019-5-5
 * 更新时间 :
 * Version :1.0
 * 功能描述 :险情报告
 **/
public class DangerReportActivity extends BaseActivity {

    ActivityDangerReportBinding mBinding;
    AdapterWorker adapterWorker;

    private PhotoSelectAdapter photoRecycleViewAdapterBefore;
    public ArrayList<String> selectPhotosBefore = new ArrayList<>();

    private DangerBean dangerBean;
    private ReservoirBean reservoirBean;

    String userCode;
    String reservoirId;
    private DangerousPosition dangerousPosition;
    private List<DangerousPosition> dangerousPositions;
    private List<String> items;
    private List<UserInfo> userInfos;


    @Override
    public int getLayoutId() {
        return R.layout.activity_danger_report;
    }

    @Override
    public void initData() {
        mBinding = DataBindingUtil.bind(mRootView);
        SPUtils.getInstance().putString(CacheConsts.bizType, ConfigConsts.picType_danger);
        reservoirBean = UserManager.getInstance().getDefaultReservoir();
        userCode = SPUtils.getInstance().getString(CacheConsts.userCode, "");
        reservoirId = SPUtils.getInstance().getString(CacheConsts.reservoirId, "");
        dangerBean = DataSupport.where("userCode=? and reservoirId=?", userCode, reservoirId).findFirst(DangerBean.class);

        items = new ArrayList<>();
        userInfos = UserManager.getInstance().getUserInfos();
        dangerousPositions = UserManager.getInstance().getDangerousPositions();
        if (!CollectionsUtil.isEmpty(dangerousPositions)) {
            for (int i = 0; i < dangerousPositions.size(); i++) {
                items.add(dangerousPositions.get(i).getStructurePath());
            }
        }

        mBinding.loHeader.tvReservoirName.setText(reservoirBean.getReservoir());
    }

    @Override
    public void initView() {
        setCenterTitle("险情报告");
        showBack();
        mBinding.layoutTrouble.rootTroubleLy.setVisibility(View.GONE);
        initRec();
        initPhotoListView();
        mBinding.layoutDes.selectTv.setOnClickListener(v -> showDialog());

        if (dangerBean != null) {
            mBinding.layoutDes.selectTv.setText(dangerBean.getPositionName());
            mBinding.layoutDes.desEt.setText(dangerBean.getProblemDescription());
        }
        String contentOfwater = SPUtils.getInstance().getString(CacheConsts.tianqiAndwaterlevel, "");
        if (TextUtils.isEmpty(contentOfwater)) {
            mBinding.layoutDes.tianqiDesTv.setVisibility(View.GONE);
        }
        mBinding.layoutDes.tianqiDesTv.setText(contentOfwater);

    }

    @Override
    protected void initListener() {
        mBinding.reportTv.setOnClickListener(v -> {
            String problemDescription = mBinding.layoutDes.desEt.getText().toString();
            String problemDescriTianqi = mBinding.layoutDes.tianqiDesTv.getText().toString();
            if (dangerousPosition == null) {
                ToastUtils.shortToast("请选择险情部位");
                return;
            }

            if (TextUtils.isEmpty(problemDescription)) {
                ToastUtils.shortToast("请填写险情信息");
                return;
            }

            if (dangerBean == null) {
                dangerBean = new DangerBean();
                dangerBean.setPositionName(dangerousPosition.getStructureName());
                dangerBean.setProblemDescription(problemDescription + "\n" + problemDescriTianqi);
                dangerBean.setReservoir(reservoirBean.getReservoir());
                dangerBean.setReservoirId(reservoirId);
                dangerBean.setPositionId(dangerousPosition.getId());
                dangerBean.setUserCode(userCode);
            } else {
                dangerBean.setPositionName(dangerousPosition.getStructureName());
                dangerBean.setPositionId(dangerousPosition.getId());
                dangerBean.setProblemDescription(problemDescription);
                dangerBean.saveOrUpdate("userCode=? and reservoirId=?", userCode, reservoirId);
            }

            TaskManager.getInstance().reportProblem(dangerBean, selectPhotosBefore)
                    .subscribe(new LoadingSubject<BaseResponse>(true, ResUtils.getString(R.string.data_loading)) {
                        @Override
                        protected void _onNext(BaseResponse baseResponse) {
                            if (baseResponse != null) {
                                if (baseResponse.getCode() == 0) {
                                    ToastUtils.shortToast("险情报告提交成功");
                                    if (dangerBean.isSaved()) {
                                        dangerBean.delete();
                                    }
                                    DataSupport.deleteAll(CheckTaskPicturesBean.class, "bizType=? and userCode=? and reservoirId=?",
                                            ConfigConsts.picType_danger, userCode, reservoirId);
                                    finish();
                                }
                            }
                        }

                        @Override
                        protected void _onError(String message) {
                            LogUtil.e(message + " ");
                            dangerBean.setHasReport("1");
                            dangerBean.save();
                            if (!NetUtil.isNetworkConnected(Utils.getContext())) {
                                ToastUtils.shortToast("当前无网络，数据已保存，连网时将自动上传");
                                finish();
                                return;
                            }
                            ToastUtils.shortToast(message + " 数据已保存，连网时将自动上传");
                            finish();


                        }
                    });
        });
    }

    @Override
    protected void initRequestData() {

    }

    private void initRec() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mBinding.layoutTel.rvWorker.setLayoutManager(layoutManager);
        adapterWorker = new AdapterWorker(R.layout.lv_tab_main_worker_item, userInfos);
        mBinding.layoutTel.rvWorker.setAdapter(adapterWorker);
        mBinding.layoutTel.rvWorker.setNestedScrollingEnabled(false);
        if (CollectionsUtil.isEmpty(userInfos)) {
            adapterWorker.setEmptyView(EmptyLayoutUtil.showNew("暂无数据"));
        }
        adapterWorker.setOnItemClickListener((adapter, view, position) -> {
            UserInfo userInfo = (UserInfo) adapter.getItem(position);
            if (userInfo != null) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + userInfo.getPhone());
                intent.setData(data);
                startActivity(intent);
            } else {
                ToastUtils.shortToast("暂无手机号码");
            }
        });
    }

    /**
     * 初始化 图片选择
     */
    List<CheckTaskPicturesBean> checkTaskPicturesBeanList = new ArrayList<>();

    private void initPhotoListView() {
        {
            photoRecycleViewAdapterBefore = new PhotoSelectAdapter(getContext(), false);
            mBinding.layoutPic.rvAddPhotoBefore.setLayoutManager(new StaggeredGridLayoutManager(4, OrientationHelper.VERTICAL));
            mBinding.layoutPic.rvAddPhotoBefore.setAdapter(photoRecycleViewAdapterBefore);

            selectPhotosBefore.clear();
            checkTaskPicturesBeanList = UtilDataBaseOfGD.getInstance().getCheckTaskPicturesBeanOfDanger(ConfigConsts.picType_danger, userCode, reservoirId);
            for (CheckTaskPicturesBean checkTaskPicturesBean : checkTaskPicturesBeanList) {
                if (checkTaskPicturesBean != null) {
                    selectPhotosBefore.add(checkTaskPicturesBean.getFilePath());
                }
            }
            photoRecycleViewAdapterBefore.setLocalData(selectPhotosBefore);
            mBinding.layoutPic.tvPhotoNumBefore.setText(photoRecycleViewAdapterBefore.getPhotoPaths().size() + "/5");

            photoRecycleViewAdapterBefore.setOnItemClickListener((view, position) -> {
                if (photoRecycleViewAdapterBefore.getItemViewType(position) == PhotoRecycleViewAdapter.TYPE_ADD) {
                    PhotoPicker.builder()
                            .setPhotoCount(5)
                            .setShowCamera(true)
                            .setPreviewEnabled(true)
                            .setSelected(selectPhotosBefore)
                            .start(DangerReportActivity.this, 100);
                } else {
                    PhotoPreview.builder()
                            .setPhotos(photoRecycleViewAdapterBefore.getPhotoPaths())
                            .setCurrentItem(position)
                            .setShowDeleteButton(false)
                            .start(DangerReportActivity.this, 101);
                }
            });

            photoRecycleViewAdapterBefore.setDeleteListener(position -> {
                if (selectPhotosBefore.size() > 0 && selectPhotosBefore.size() > position) {
                    String bizType = SPUtils.getInstance().getString(CacheConsts.bizType, "");
                    String userCode = SPUtils.getInstance().getString(CacheConsts.userCode, "");
                    String reservoirId = SPUtils.getInstance().getString(CacheConsts.reservoirId, "");
                    UtilDataBaseOfGD.getInstance().deletePic(bizType, selectPhotosBefore.get(position), userCode, reservoirId);
                    selectPhotosBefore.remove(position);

                }

                photoRecycleViewAdapterBefore.setLocalData(selectPhotosBefore);
                mBinding.layoutPic.tvPhotoNumBefore.setText(photoRecycleViewAdapterBefore.getPhotoPaths().size() + "/5");

            });

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            List<String> photos = null;
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            }

            if (photos != null) {
                checkTaskPicturesBeanList = UtilDataBaseOfGD.getInstance().getCheckTaskPicturesBeanOfDanger(ConfigConsts.picType_danger, userCode, reservoirId);
                selectPhotosBefore.clear();

                for (CheckTaskPicturesBean checkTaskPicturesBean : checkTaskPicturesBeanList) {
                    if (checkTaskPicturesBean != null) {
                        selectPhotosBefore.add(checkTaskPicturesBean.getFilePath());
                    }
                }
                photoRecycleViewAdapterBefore.setLocalData(selectPhotosBefore);
                mBinding.layoutPic.tvPhotoNumBefore.setText(photoRecycleViewAdapterBefore.getPhotoPaths().size() + "/5");

            }

        }


    }

    /**
     * 框
     */

    private void showDialog() {
        /*ActionSheet.createBuilder(this, getSupportFragmentManager())
                .setCancelButtonTitle("取消")
                .setOtherButtonTitles(items)
                .setCancelableOnTouchOutside(true)
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

                    }

                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                        mBinding.layoutDes.selectTv.setText(items[index]);
                    }
                })
                .show();*/


        ActionSheetDialog dialog = new ActionSheetDialog(this, items.toArray(new String[items.size()]), null);
        dialog.title("请选择部位")
                .titleTextSize_SP(14.5f)
                .widthScale(0.8f)
                .show();

        dialog.setOnOpenItemClickL((parent, view, position, id) -> {
            dangerousPosition = dangerousPositions.get(position);
            mBinding.layoutDes.selectTv.setText(dangerousPosition.getStructurePath());
            dialog.dismiss();
        });
    }


}
