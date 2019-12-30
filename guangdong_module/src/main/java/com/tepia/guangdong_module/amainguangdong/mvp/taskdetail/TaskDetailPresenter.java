package com.tepia.guangdong_module.amainguangdong.mvp.taskdetail;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tepia.base.http.BaseResponse;
import com.tepia.base.http.LoadingSubject;
import com.tepia.base.mvp.BasePresenterImpl;
import com.tepia.base.utils.AppManager;
import com.tepia.base.utils.LogUtil;
import com.tepia.base.utils.ToastUtils;
import com.tepia.base.view.dialog.loading.LoadingDialog;
import com.tepia.base.view.dialog.loading.SimpleLoadDialog;
import com.tepia.base.view.floatview.CollectionsUtil;
import com.tepia.guangdong_module.amainguangdong.route.TaskItemBean;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileBatchCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public class TaskDetailPresenter extends BasePresenterImpl<TaskDetailContract.View> implements TaskDetailContract.Presenter {

    private SubmitFormListener submitFormListener;

    public SubmitFormListener getSubmitFormListener() {
        return submitFormListener;
    }

    public void setSubmitFormListener(SubmitFormListener submitFormListener) {
        this.submitFormListener = submitFormListener;
    }

    public interface SubmitFormListener {
        void submitFormCallback();
    }

    public TaskDetailPresenter() {
    }

    public TaskDetailPresenter(SubmitFormListener submitFormListener) {
        this.submitFormListener = submitFormListener;
    }

    /**
     * 修改工单状态为执行中
     *
     * @param workOrderId
     * @param positionStr
     * @param isShow
     * @param msg
     */
    public void startExecute(String workOrderId, String positionStr, boolean isShow, String msg) {
        TaskManager.getInstance().startExecute(workOrderId, positionStr).subscribe(new LoadingSubject<BaseResponse>(isShow, msg) {
            @Override
            protected void _onNext(BaseResponse response) {
                if (mView != null) {
                    mView.startExecuteSucess();
                }
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.shortToast(message);

            }
        });
    }

    /**
     * 完成巡查
     *
     * @param workOrderId
     * @param workOrderRoute
     * @param isShow
     * @param msg
     */
    public void endExecute(String workOrderId, String workOrderRoute, boolean isShow, String msg) {
        TaskManager.getInstance().endExecute(workOrderId, workOrderRoute).subscribe(new LoadingSubject<BaseResponse>(isShow, msg) {
            @Override
            protected void _onNext(BaseResponse response) {
                LoadingDialog.with(AppManager.getInstance().getCurrentActivity()).dismiss();
                if (mView != null) {
                    mView.endExecuteSucess();
                }
                if (submitFormListener != null) {
                    submitFormListener.submitFormCallback();
                }
            }

            @Override
            protected void _onError(String message) {

                LoadingDialog.with(AppManager.getInstance().getCurrentActivity()).dismiss();
                LogUtil.e("endExecute:" + message);
                ToastUtils.shortToast("最后执行工单提交失败，请重试一次" + message);

            }
        });
    }


    public void commitTotal(List<TaskItemBean> localData, Context mContext) {
        if (CollectionsUtil.isEmpty(localData)) return;
        appReservoirWorkOrderItemCommitOneByOne(localData, 0, mContext);
    }

    SimpleLoadDialog simpleLoadDialog;

    public void appReservoirWorkOrderItemCommitOneByOne(List<TaskItemBean> localData, int count, Context mContext) {
        if (CollectionsUtil.isEmpty(localData)) return;
        int size = localData.size();

        if (count < localData.size()) {
            TaskItemBean bean = localData.get(count);
            List<String> files = new Gson().fromJson(bean.getBeforelist(), new TypeToken<List<String>>() {
            }.getType());
            if (files == null) {
                files = bean.getPhotoPaths();
                if (files == null)
                    files = new ArrayList<>();
            }
            if (simpleLoadDialog == null) {
                simpleLoadDialog = new SimpleLoadDialog(mContext, "正在提交第" + (count + 1) + "/" + size + "项离线数据", true);
            }
            simpleLoadDialog.show();
            simpleLoadDialog.setMessage("正在提交第" + (count + 1) + "/" + size + "项离线数据");
            appReservoirWorkOrderItemCommitOne(localData, count, files, false, "正在提交第" + (count + 1) + "/" + size + "项离线数据", mContext);
        } else {
            /**
             * 一项一项提交完成后执行
             */
            dismiss();
            if (mView != null) {
                mView.appReservoirWorkOrderItemCommitOneByOneSuccess();
            }
            if (submitFormListener != null) {
                submitFormListener.submitFormCallback();
            }
        }

    }

    private void dismiss() {
        if (simpleLoadDialog == null) return;
        simpleLoadDialog.dismiss();
    }


    /**
     * 一项一项提交
     *
     * @param localData
     * @param count
     * @param files
     * @param isShow
     * @param msg
     */
    public void appReservoirWorkOrderItemCommitOne(List<TaskItemBean> localData,
                                                   int count,
                                                   List<String> files,
                                                   boolean isShow,
                                                   String msg,
                                                   Context mContext
    ) {


        if (files.size() == 0) {
            TaskItemBean taskItemBean = localData.get(count);
            TaskManager.getInstance().appReservoirWorkOrderItemCommitOne(taskItemBean, files)
                    .subscribe(new LoadingSubject<BaseResponse>(isShow, msg) {
                        @Override
                        protected void _onNext(BaseResponse response) {
                            if (response.getCode() == 0) {
                                taskItemBean.setIsCommitLocal("1");
                                taskItemBean.saveOrUpdate("itemid=?", taskItemBean.getItemId());

                                appReservoirWorkOrderItemCommitOneByOne(localData, count + 1, mContext);
                            }
                        }

                        @Override
                        protected void _onError(String message) {
                            dismiss();
//                            LoadingDialog.with(AppManager.getInstance().getCurrentActivity()).dismiss();
                            if (message.contains("重复")) {
                                appReservoirWorkOrderItemCommitOneByOne(localData, count + 1, mContext);
                                return;
                            }
                            ToastUtils.shortToast(message);
                        }
                    });
        } else {
            TaskItemBean taskItemBean = localData.get(count);

            Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
            ArrayList<String> filelist = new ArrayList<>();
            filelist.addAll(files);
            Tiny.getInstance().source(filelist.toArray(new String[filelist.size()])).batchAsFile().withOptions(options).batchCompress(new FileBatchCallback() {
                @Override
                public void callback(boolean isSuccess, String[] outfile) {
                    //return the batch compressed file path
                    if (isSuccess) {
                        ArrayList<String> beforelist = new ArrayList<String>();
                        ArrayList<String> tempslist = new ArrayList<>();
                        Collections.addAll(tempslist, outfile);
                        beforelist.addAll(tempslist.subList(0, files.size()));

                        TaskManager.getInstance().appReservoirWorkOrderItemCommitOne(taskItemBean, tempslist)
                                .subscribe(new LoadingSubject<BaseResponse>(isShow, msg) {
                                    @Override
                                    protected void _onNext(BaseResponse response) {

                                        if (response.getCode() == 0) {
                                            taskItemBean.setIsCommitLocal("1");
                                            taskItemBean.saveOrUpdate("itemid=?", taskItemBean.getItemId());
                                            appReservoirWorkOrderItemCommitOneByOne(localData, count + 1, mContext);
                                        }
                                    }

                                    @Override
                                    protected void _onError(String message) {
                                        dismiss();
                                        if (message.contains("重复")) {
                                            appReservoirWorkOrderItemCommitOneByOne(localData, count + 1, mContext);
                                            return;
                                        }
                                        ToastUtils.shortToast(message);
                                    }
                                });
                    } else {
                        dismiss();
                        ToastUtils.shortToast("图片压缩失败");
                    }
                }
            });
        }
    }
}
