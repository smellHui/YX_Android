package com.yangj.dahemodule.activity.patrol;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.tepia.base.mvp.BaseActivity;
import com.tepia.base.utils.ToastUtils;
import com.tepia.base.view.floatview.CollectionsUtil;
import com.tepia.guangdong_module.amainguangdong.route.TaskBean;
import com.tepia.guangdong_module.amainguangdong.route.TaskItemBean;
import com.tepia.guangdong_module.amainguangdong.utils.SqlManager;
import com.tepia.guangdong_module.amainguangdong.wrap.PatroltemEvent;
import com.tepia.photo_picker.PhotoPicker;
import com.yangj.dahemodule.R;
import com.yangj.dahemodule.common.HttpManager;
import com.yangj.dahemodule.view.FeedbackSelectPhotoView;
import com.yangj.dahemodule.view.FeedbackTxtView;
import com.yangj.dahemodule.view.patrol.PatrolReportInfoView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import static com.tepia.guangdong_module.amainguangdong.xunchaview.activity.TroubleRecordActivity.PICKER_INDEX;
import static com.tepia.guangdong_module.amainguangdong.xunchaview.activity.TroubleRecordActivity.TASK_ITEM_BEAN;
import static com.tepia.guangdong_module.amainguangdong.xunchaview.activity.TroubleRecordActivity.TROUBLE_POSITION;

/**
 * Author:xch
 * Date:2019/12/24
 * Description:正常时反馈
 */
public class PatrolNormalFeedActivity extends BaseActivity {

    private PatrolReportInfoView patrolReportInfoView;
    private FeedbackTxtView feedbackTxtView;
    private FeedbackSelectPhotoView feedbackSelectPhotoView;
    private Button uploadBtn;

    private TaskItemBean taskItemBean;
    private TaskBean taskBean;
    private int patrolPosition;
    private int patrolIndex;
    private boolean isXC;
    private List<String> photoPaths;

    @Override
    public int getLayoutId() {
        return R.layout.activity_patrol_normal_feel;
    }

    @Override
    public void initData() {
        isXC = HttpManager.getInstance().isXC();

        Intent intent = getIntent();
        if (intent != null) {
            taskItemBean = (TaskItemBean) intent.getSerializableExtra(TASK_ITEM_BEAN);
            patrolPosition = intent.getIntExtra(TROUBLE_POSITION, -1);
            patrolIndex = intent.getIntExtra(PICKER_INDEX, -1);
            if (taskItemBean == null) {
                finish();
                return;
            }
            taskBean = SqlManager.getInstance().queryTaskSqlByWorkOrderId(taskItemBean.getWorkOrderId());
        }
    }

    @Override
    public void initView() {
        setLeftTitle("正常记录");
        showBack();

        patrolReportInfoView = findViewById(R.id.view_patrol_report_info);
        patrolReportInfoView.setData(taskBean.getRouteName());
        feedbackTxtView = findViewById(R.id.view_feed_back_txt);
        feedbackSelectPhotoView = findViewById(R.id.view_feed_back_select_photo);
        uploadBtn = findViewById(R.id.btn_add_update);

        feedbackTxtView.setInputTxt(taskItemBean.getExecuteResultDescription());
        photoPaths = taskItemBean.getPhotoPaths();
        if (isXC) {
            feedbackTxtView.setInputFocusable(true);
            feedbackSelectPhotoView.setPhotoPaths(taskItemBean.getPhotoPaths());
            if (taskBean.isHasExecuted()) {
                noCanEditView();
            } else {
                feedbackSelectPhotoView.setPhotoPaths(photoPaths, false);
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
        feedbackSelectPhotoView.setPhotoPaths(photoPaths, true);
        feedbackTxtView.setInputFocusable(false);
        uploadBtn.setVisibility(View.GONE);
    }

    @Override
    protected void initListener() {
        uploadBtn.setOnClickListener(v -> {
            String content = feedbackTxtView.getInputTxt();
            List<String> photoPaths = feedbackSelectPhotoView.getPhotoPaths();
//            if (TextUtils.isEmpty(content)) {
//                ToastUtils.shortToast("记录描述不能为空");
//                return;
//            }
//            if (CollectionsUtil.isEmpty(photoPaths)) {
//                ToastUtils.shortToast("现场图片不能为空");
//                return;
//            }
            if (patrolPosition == -1) return;
            taskItemBean.setExecuteResultDescription(content);
            taskItemBean.setPhotoPaths(photoPaths);
            taskItemBean.setExecuteResultType("0");
            taskItemBean.setCompleteStatus("1");
            SqlManager.getInstance().updateTaskItemAsyn(taskItemBean, (updateOrDeleteCallback) -> {
                ToastUtils.shortToast("提交成功");
                EventBus.getDefault().post(new PatroltemEvent(patrolPosition, patrolIndex, taskItemBean));
                finish();
            });
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
}
