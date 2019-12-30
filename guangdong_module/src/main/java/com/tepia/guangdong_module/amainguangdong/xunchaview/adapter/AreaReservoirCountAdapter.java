package com.tepia.guangdong_module.amainguangdong.xunchaview.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.guangdong_module.R;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.AreaReservoirCountBean;
import com.tepia.guangdong_module.amainguangdong.widget.BarPercentView;

import java.util.List;

/**
 * Created by Android Studio
 *
 * @author : Arthur
 * Date :    2019/8/14
 * Time :    14:55
 * Describe :
 */
public class AreaReservoirCountAdapter extends RecyclerView.Adapter<AreaReservoirCountAdapter.ViewHolder> {
    private Context mcontext;
    private List<AreaReservoirCountBean.DataBean> mlist;
    private int mTotalCount;

    public AreaReservoirCountAdapter(Context context, List<AreaReservoirCountBean.DataBean> list, int totalCount) {
        mcontext = context;
        mlist = list;
        mTotalCount = totalCount;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(mcontext).inflate(R.layout.item_areareservoircount, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        AreaReservoirCountBean.DataBean bean = mlist.get(i);
        viewHolder.nameOfareaTv.setText(bean.getAreaName());
        // 水库数量
        int reservoirCount = bean.getAllCount();
        viewHolder.tv_allcount_num.setText(reservoirCount+"");
        // 监测数量
        int monitorCount = bean.getMonitorCount();
        viewHolder.tv_monitorcount_num.setText(monitorCount+"");
        // 上报数量
        int reportCount = bean.getReportCount();
        viewHolder.tv_uploadcount_num.setText(reportCount+"");


        // 计算水库数量百分比
        // 当前水库数量大于水库总数
        if (reservoirCount > mTotalCount) {
            viewHolder.bar_percent_allcount.setPercentage(100);
        } else {
            int percentTotalCount = 0;
            if (mTotalCount != 0) {
                percentTotalCount = reservoirCount * 100 / mTotalCount ;
                if (percentTotalCount == 0 && reservoirCount > 0) {
                    percentTotalCount = 1;
                }
            }
            viewHolder.bar_percent_allcount.setPercentage(percentTotalCount);
        }

        // 计算监测数量百分比
        int percentMonitor = 0;
        if (mTotalCount != 0) {
            if (reservoirCount != 0){
                percentMonitor = monitorCount * 100 / reservoirCount;
            }
            if (percentMonitor == 0 && monitorCount > 0) {
                percentMonitor = 1;
            }
        }
        viewHolder.bar_percent_monitorcount.setPercentage(percentMonitor);

        // 计算正常上报数量百分比
        int percentUpload = 0;
        if (mTotalCount != 0) {
            if (reservoirCount != 0){
                percentUpload = reportCount * 100 / reservoirCount;
            }

            if (percentUpload == 0 && reportCount > 0) {
                percentUpload = 1;
            }
        }
        viewHolder.bar_percent_uploadcount.setPercentage(percentUpload);
    }

    @Override
    public int getItemCount() {
        return mlist == null ? 0 : mlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private BarPercentView bar_percent_allcount;
        private BarPercentView bar_percent_monitorcount;
        private BarPercentView bar_percent_uploadcount;
        private TextView nameOfareaTv,tv_allcount_num, tv_monitorcount_num, tv_uploadcount_num;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bar_percent_allcount = itemView.findViewById(R.id.bar_percent_allcount);
            bar_percent_monitorcount = itemView.findViewById(R.id.bar_percent_monitorcount);
            bar_percent_uploadcount = itemView.findViewById(R.id.bar_percent_uploadcount);
            nameOfareaTv = itemView.findViewById(R.id.nameOfareaTv);
            tv_allcount_num = itemView.findViewById(R.id.tv_allcount_num);
            tv_monitorcount_num = itemView.findViewById(R.id.tv_monitorcount_num);
            tv_uploadcount_num = itemView.findViewById(R.id.tv_uploadcount_num);
        }
    }

}
