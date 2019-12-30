package com.yangj.dahemodule.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tepia.base.ConfigConsts;
import com.tepia.guangdong_module.amainguangdong.route.TaskItemBean;
import com.tepia.guangdong_module.amainguangdong.xunchaview.activity.StartInspectionActivity;
import com.tepia.guangdong_module.amainguangdong.xunchaview.activity.TroubleRecordActivity;
import com.yangj.dahemodule.activity.DangerReportDetailActivity;
import com.yangj.dahemodule.activity.ScanActivity;
import com.yangj.dahemodule.activity.WebActivity;
import com.yangj.dahemodule.activity.patrol.NoPathMapControlActivity;
import com.yangj.dahemodule.activity.patrol.PatrolFeedBackActivity;
import com.yangj.dahemodule.activity.patrol.PatrolMapControlActivity;
import com.yangj.dahemodule.activity.patrol.PatrolNormalFeedActivity;
import com.yangj.dahemodule.activity.patrol.PatrolRecordActivity;

import static com.tepia.guangdong_module.amainguangdong.xunchaview.activity.TroubleRecordActivity.PICKER_INDEX;
import static com.tepia.guangdong_module.amainguangdong.xunchaview.activity.TroubleRecordActivity.TASK_ITEM_BEAN;
import static com.tepia.guangdong_module.amainguangdong.xunchaview.activity.TroubleRecordActivity.TROUBLE_POSITION;

/**
 * Author:xch
 * Date:2019/9/9
 * Description:
 */
public class UiHelper {

    public static final String WORK_ORDER_ID = "work_order_id";
    public static final String PATROL_PROBLEM_ID = "patrol_problem_id";
    public static final String PATROL_PROBLEM_POINT = "patrol_problem_point";
    public static final String EXCUTE_LONGITUDE = "excute_longitude";
    public static final String EXCUTE_LATITUDE = "excute_latitude";

    public static void goToStartInspectionView(Context ctx, String workOrderId) {
        Intent intent = new Intent(ctx, StartInspectionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ConfigConsts.Workorderid, workOrderId);
        intent.putExtras(bundle);
        ctx.startActivity(intent);
    }

    public static void goToPatrolMapControlView(Context ctx, String workOrderId) {
//        Intent intent = new Intent(ctx, PatrolMapControlActivity.class);
        Intent intent = new Intent(ctx, PatrolMapControlActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ConfigConsts.Workorderid, workOrderId);
        intent.putExtras(bundle);
        ctx.startActivity(intent);
    }

    /**
     * 跳转险情上报详情页
     *
     * @param ctx
     * @param reportId
     */
    public static void goToDangerReportDetailView(Context ctx, String reportId) {
        Intent intent = new Intent(ctx, DangerReportDetailActivity.class);
        intent.putExtra("reportId", reportId);
        ctx.startActivity(intent);
    }

    /**
     * 巡查记录详情
     *
     * @param ctx
     * @param omRecordCode
     */
    public static void goToPatrolRecordView(Context ctx, String omRecordCode) {
        Intent intent = new Intent(ctx, PatrolRecordActivity.class);
        intent.putExtra("omRecordCode", omRecordCode);
        ctx.startActivity(intent);
    }

    /**
     * 扫一扫页面
     *
     * @param ctx
     */
    public static void goToScanView(Context ctx) {
        Intent intent = new Intent(ctx, ScanActivity.class);
        ctx.startActivity(intent);
    }

    /**
     * 问题反馈页面
     *
     * @param ctx
     * @param workOrderId
     */
    public static void goToPatrolFeedBackView(Context ctx, String workOrderId, String latitude, String longitude) {
        Intent intent = new Intent(ctx, PatrolFeedBackActivity.class);
        intent.putExtra(WORK_ORDER_ID, workOrderId);
        intent.putExtra(EXCUTE_LATITUDE, latitude);
        intent.putExtra(EXCUTE_LONGITUDE, longitude);
        ctx.startActivity(intent);
    }

    public static void goToPatrolFeedBackView(Context ctx, String workOrderId, String problemId) {
        Intent intent = new Intent(ctx, PatrolFeedBackActivity.class);
        intent.putExtra(WORK_ORDER_ID, workOrderId);
        intent.putExtra(PATROL_PROBLEM_ID, problemId);
        ctx.startActivity(intent);
    }

    public static void goToNoPathMapControlView(Context ctx, String workOrderId) {
        Intent intent = new Intent(ctx, NoPathMapControlActivity.class);
        intent.putExtra(WORK_ORDER_ID, workOrderId);
        ctx.startActivity(intent);
    }

    public static void goToWebView(Context ctx, String url) {
        Intent intent = new Intent(ctx, WebActivity.class);
        intent.putExtra("url", url);
        ctx.startActivity(intent);
    }

    public static void goToTroubleRecordView(Context ctx, TaskItemBean taskItemBean) {
        goToTroubleRecordView(ctx, taskItemBean, -1, -1);
    }

    public static void goToTroubleRecordView(Context ctx, TaskItemBean taskItemBean, int position, int patrolIndex) {
        Intent intent = new Intent(ctx, TroubleRecordActivity.class);
        intent.putExtra(TASK_ITEM_BEAN, taskItemBean);
        if (position != -1)
            intent.putExtra(TROUBLE_POSITION, position);
        if (patrolIndex != -1)
            intent.putExtra(PICKER_INDEX, patrolIndex);
        ctx.startActivity(intent);
    }

    public static void goToPatrolNormalFeedView(Context ctx, TaskItemBean taskItemBean, int position, int patrolIndex) {
        Intent intent = new Intent(ctx, PatrolNormalFeedActivity.class);
        intent.putExtra(TASK_ITEM_BEAN, taskItemBean);
        if (position != -1)
            intent.putExtra(TROUBLE_POSITION, position);
        if (patrolIndex != -1)
            intent.putExtra(PICKER_INDEX, patrolIndex);
        ctx.startActivity(intent);
    }
}
