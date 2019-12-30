package com.yangj.dahemodule.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bilibili.boxing_impl.view.SpacesItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.esri.arcgisruntime.geometry.Point;
import com.lxj.xpopup.impl.FullScreenPopupView;
import com.tepia.base.view.dialog.permissiondialog.Px2dpUtils;
import com.tepia.base.view.floatview.CollectionsUtil;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.RoutePosition;
import com.tepia.guangdong_module.amainguangdong.route.TaskItemBean;
import com.tepia.guangdong_module.amainguangdong.utils.SqlManager;
import com.yangj.dahemodule.R;
import com.yangj.dahemodule.adapter.PartCardAdapter;
import com.yangj.dahemodule.adapter.PatrolControlAdapter;
import com.yangj.dahemodule.util.UiHelper;
import com.yarolegovich.discretescrollview.DSVOrientation;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import org.litepal.crud.callback.UpdateOrDeleteCallback;

import java.util.List;

/**
 * Author:xch
 * Date:2019/12/2
 * Description:
 */
public class PatrolUpControlView extends FullScreenPopupView implements
        DiscreteScrollView.OnItemChangedListener,
        DiscreteScrollView.ScrollStateChangeListener {

    private boolean isCompleteOfTaskBean;
    private int patrolIndex;
    private Point locationPoint;
    private List<RoutePosition> routePositions;
    private DiscreteScrollView partPicker;
    private ForecastView forecastView;
    private PartCardAdapter partCardAdapter;

    private RecyclerView rv;
    private PatrolControlAdapter patrolControlAdapter;

    public PatrolUpControlView(@NonNull Context context, boolean isCompleteOfTaskBean, List<RoutePosition> routePositions) {
        super(context);
        this.isCompleteOfTaskBean = isCompleteOfTaskBean;
        this.routePositions = routePositions;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.activity_patrol_up_control;
    }

    @Override
    protected void onCreate() {
        forecastView = findViewById(R.id.view_forecast);
        rv = findViewById(R.id.rv);
        findViewById(R.id.tv_left_text).setOnClickListener(v -> super.dismiss());
        patrolControlAdapter = new PatrolControlAdapter(isCompleteOfTaskBean);
        patrolControlAdapter.setOnItemChildClickListener(this::onItemChildClick);
        rv.setLayoutManager(new WrapLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rv.addItemDecoration(new SpacesItemDecoration(2));
        int zeroPx = Px2dpUtils.dip2px(getContext(), 0);
        rv.setPadding(zeroPx, zeroPx, zeroPx, zeroPx);
        rv.setAdapter(patrolControlAdapter);

        partPicker = findViewById(R.id.part_picker);
        partPicker.setSlideOnFling(true);
        partCardAdapter = new PartCardAdapter();
        partPicker.setAdapter(partCardAdapter);
        partPicker.addOnItemChangedListener(this);
        partPicker.addScrollStateChangeListener(this);
        partPicker.setOrientation(DSVOrientation.HORIZONTAL);
        partPicker.setItemTransitionTimeMillis(400);
        partPicker.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());

        if (!CollectionsUtil.isEmpty(routePositions)) {
            partCardAdapter.setNewData(routePositions);
            forecastView.setForecast(routePositions.get(0));
        }
    }

    @Override
    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder holder, int position) {
        forecastView.setForecast(routePositions.get(position));
        this.patrolIndex = position;
        RoutePosition routePosition = routePositions.get(position);
        if (routePosition == null) {
            patrolControlAdapter.setNewData(null);
            return;
        }
        List<TaskItemBean> taskItemBeans = SqlManager.getInstance().queryTaskItem(routePosition.getWorkOrderId(), routePosition.getPositionId());
        patrolControlAdapter.setNewData(taskItemBeans);
    }

    @Override
    public void onScrollStart(@NonNull RecyclerView.ViewHolder currentItemHolder, int adapterPosition) {

    }

    @Override
    public void onScrollEnd(@NonNull RecyclerView.ViewHolder currentItemHolder, int adapterPosition) {

    }

    @Override
    public void onScroll(float position, int currentIndex, int newIndex, @Nullable RecyclerView.ViewHolder currentHolder, @Nullable RecyclerView.ViewHolder newCurrent) {
        RoutePosition current = routePositions.get(currentIndex);
        if (newIndex >= 0 && newIndex < partCardAdapter.getItemCount()) {
            RoutePosition next = routePositions.get(newIndex);
            forecastView.onScroll(1f - Math.abs(position), current, next);
        }
    }

    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        TaskItemBean taskItemBean = (TaskItemBean) adapter.getItem(position);
        if (taskItemBean == null) return;
        if (locationPoint != null) {
            taskItemBean.setExcuteLatitude(locationPoint.getY() + "");
            taskItemBean.setExcuteLongitude(locationPoint.getX() + "");
        }
        int id = view.getId();
        if (id == R.id.btn_normal) {
            UiHelper.goToPatrolNormalFeedView(getContext(), taskItemBean, position, patrolIndex);
//            updateStatuClick(taskItemBean, (rowsAffected) -> {
//                controlCallback(position, taskItemBean);
//                if (updatePatrolPickListener != null) {
//                    updatePatrolPickListener.updatePatrolPick(patrolIndex);
//                }
//            });
        }
        if (id == R.id.btn_abnormal) {
            UiHelper.goToTroubleRecordView(getContext(), taskItemBean, position, patrolIndex);
        }
        if (id == R.id.btn_to_normal) {
            UiHelper.goToPatrolNormalFeedView(getContext(), taskItemBean, position, patrolIndex);
//            updateStatuClick(taskItemBean, (rowsAffected) -> {
//                controlCallback(position, taskItemBean);
//                if (updatePatrolPickListener != null) {
//                    updatePatrolPickListener.updatePatrolPick(patrolIndex);
//                }
//            });
        }
        if (id == R.id.btn_to_abnormal) {
            UiHelper.goToTroubleRecordView(getContext(), taskItemBean, position, patrolIndex);
        }
        if (id == R.id.drag_item) {
            if (taskItemBean.isNormal()) {
                UiHelper.goToPatrolNormalFeedView(getContext(), taskItemBean, position, patrolIndex);
            }else {
                if (taskItemBean.getExecuteResultType() != null){
                    UiHelper.goToTroubleRecordView(getContext(), taskItemBean, position, patrolIndex);
                }
            }
        }
    }

    /**
     * 更新数据库数据，然后返回回调方法
     *
     * @param taskItemBean
     * @param updateOrDeleteCallback
     */
    private void updateStatuClick(@NonNull TaskItemBean taskItemBean, UpdateOrDeleteCallback updateOrDeleteCallback) {
        taskItemBean.setExecuteResultType("0");
        taskItemBean.setCompleteStatus("1");
        SqlManager.getInstance().updateTaskItemAsyn(taskItemBean, updateOrDeleteCallback);
    }

    public void setLocationPoint(Point locationPoint) {
        this.locationPoint = locationPoint;
    }


    public void controlCallback(int position, @NonNull TaskItemBean taskItemBean) {
        patrolControlAdapter.setData(position, taskItemBean);
        partCardAdapter.notifyItemChanged(patrolIndex);
        forecastView.setForecast(routePositions.get(patrolIndex));
    }

    public void scrollToPosition(int position) {
        partPicker.scrollToPosition(position);
        onCurrentItemChanged(null, position);
    }

    private UpdatePatrolPickListener updatePatrolPickListener;

    public void setUpdatePatrolPickListener(UpdatePatrolPickListener updatePatrolPickListener) {
        this.updatePatrolPickListener = updatePatrolPickListener;
    }

    public interface UpdatePatrolPickListener {
        void updatePatrolPick(int patrolIndex);
    }

}
