package com.yangj.dahemodule.activity.patrol;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.tepia.base.mvp.BaseActivity;
import com.tepia.base.utils.ToastUtils;
import com.tepia.base.utils.UUIDUtil;
import com.tepia.base.view.floatview.CollectionsUtil;
import com.tepia.guangdong_module.amainguangdong.route.TaskBean;
import com.tepia.guangdong_module.amainguangdong.route.TaskItemBean;
import com.tepia.guangdong_module.amainguangdong.utils.SqlManager;
import com.tepia.photo_picker.PhotoPicker;
import com.yangj.dahemodule.R;
import com.yangj.dahemodule.common.HttpManager;
import com.yangj.dahemodule.view.FeedbackSelectPhotoView;
import com.yangj.dahemodule.view.FeedbackTxtView;
import com.yangj.dahemodule.wrap.PatrolAddProblemWrap;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import static com.yangj.dahemodule.util.UiHelper.EXCUTE_LATITUDE;
import static com.yangj.dahemodule.util.UiHelper.EXCUTE_LONGITUDE;
import static com.yangj.dahemodule.util.UiHelper.PATROL_PROBLEM_ID;
import static com.yangj.dahemodule.util.UiHelper.WORK_ORDER_ID;

/**
 * Author:xch
 * Date:2019/12/18
 * Description:问题反馈
 */
public class PatrolFeedBackActivity extends BaseActivity {

    private Button uploadBtn;
    private Button deleteBtn;
    private FeedbackTxtView feedbackTxtView;
    private FeedbackSelectPhotoView feedbackSelectPhotoView;

    private String itemId;
    private String workOrderId;
    private String excuteLongitude;//执行点经度
    private String excuteLatitude;

    private TaskBean taskBean;
    private TaskItemBean taskItemBean;
    private boolean isXC;
    private List<String> photoPaths;

    @Override
    public int getLayoutId() {
        return R.layout.activity_patrol_feed_back;
    }

    @Override
    public void initData() {
        isXC = HttpManager.getInstance().isXC();
        Intent intent = getIntent();
        if (intent != null) {
            itemId = intent.getStringExtra(PATROL_PROBLEM_ID);
            workOrderId = intent.getStringExtra(WORK_ORDER_ID);
            if (TextUtils.isEmpty(itemId)) {
                excuteLatitude = intent.getStringExtra(EXCUTE_LATITUDE);
                excuteLongitude = intent.getStringExtra(EXCUTE_LONGITUDE);
            } else {
                taskBean = SqlManager.getInstance().queryTaskSqlByWorkOrderId(workOrderId);
                taskItemBean = SqlManager.getInstance().queryTaskItemByItemId(workOrderId, itemId);
                if (taskItemBean == null) {
                    ToastUtils.shortToast("patrolProblemBean  数据库为空");
                    finish();
                }
            }
        }
    }

    @Override
    public void initView() {
        setLeftTitle("问题反馈");
        showBack();

        uploadBtn = findViewById(R.id.btn_add_upload);
        deleteBtn = findViewById(R.id.btn_delete);

        feedbackTxtView = findViewById(R.id.view_feed_back_txt);
        feedbackSelectPhotoView = findViewById(R.id.view_feed_back_select_photo);

        if (taskItemBean != null) {
            feedbackTxtView.setInputTxt(taskItemBean.getExecuteResultDescription());
            photoPaths = taskItemBean.getPhotoPaths();
        }
        if (isXC) {
            feedbackTxtView.setInputFocusable(true);
            if (taskItemBean != null) {
                deleteBtn.setVisibility(View.VISIBLE);
                if (taskBean.isHasExecuted()){
                    noCanEditView();
                }else {
                    feedbackSelectPhotoView.setPhotoPaths(photoPaths,false);
                }
            }
        } else {
            noCanEditView();
        }
    }

    /**
     * 页面只能看不能编辑
     */
    private void noCanEditView() {
        if (CollectionsUtil.isEmpty(photoPaths)) {
            feedbackSelectPhotoView.setNoCanSelectPhoto();
        }
        feedbackSelectPhotoView.setPhotoPaths(photoPaths,true);
        feedbackTxtView.setInputFocusable(false);
        deleteBtn.setVisibility(View.GONE);
        uploadBtn.setVisibility(View.GONE);
    }

    @Override
    protected void initListener() {
        uploadBtn.setOnClickListener(v -> uploadProblem());
        deleteBtn.setOnClickListener(v -> deleteProblem());
    }

    private void deleteProblem() {
        taskItemBean.deleteAsync().listen(success -> {
            EventBus.getDefault().post(new PatrolAddProblemWrap(true));
            finish();
        });
    }

    @Override
    protected void initRequestData() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (data != null) {
                List<String> photoPaths = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                if (photoPaths == null) return;
                feedbackSelectPhotoView.setPhotoPaths(photoPaths);
            }
        }
    }

    private void uploadProblem() {
        String problemContent = feedbackTxtView.getInputTxt();
        List<String> photoPaths = feedbackSelectPhotoView.getPhotoPaths();
        if (TextUtils.isEmpty(problemContent)) {
            ToastUtils.shortToast("问题描述不能为空");
            return;
        }
        if (CollectionsUtil.isEmpty(photoPaths)) {
            ToastUtils.shortToast("现场图片不能为空");
            return;
        }
        if (taskItemBean == null) {
            taskItemBean = new TaskItemBean();
            taskItemBean.setWorkOrderId(workOrderId);
            taskItemBean.setExcuteLatitude(excuteLatitude);
            taskItemBean.setExcuteLongitude(excuteLongitude);
            taskItemBean.setPositionId("1");
            taskItemBean.setSuperviseItemCode("1");
            taskItemBean.setItemId(UUIDUtil.getUUID32());
            taskItemBean.setExecuteResultType("1");
            taskItemBean.setIsCommitLocal("0");
        }
        taskItemBean.setExecuteResultDescription(problemContent);
        taskItemBean.setPhotoPaths(photoPaths);
        taskItemBean.saveOrUpdateAsync("itemId=?", itemId + "")
                .listen(success -> {
                    EventBus.getDefault().post(new PatrolAddProblemWrap(taskItemBean.getItemId()));
                    finish();
                });

    }
}
