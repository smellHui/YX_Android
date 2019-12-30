package com.tepia.guangdong_module.amainguangdong.common;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.guangdong_module.R;
import com.tepia.base.utils.LogUtil;
import com.tepia.photo_picker.utils.SPUtils;
import com.tepia.base.utils.Utils;
import com.tepia.base.ConfigConsts;
import com.tepia.guangdong_module.amainguangdong.common.pickview.AndroidLifecycleUtils;
import com.tepia.guangdong_module.amainguangdong.common.pickview.OnItemClickListener;

import java.io.File;
import java.util.ArrayList;

/*************************************************************
 * Created by OCN.YAN                                        *
 * 主要功能:图片选择适配                                       *
 * 项目名:贵州水务                                           *
 * 包名:com.elegant.river_system.adapter                    *
 * 创建时间:2017年10月12日11:16                               *
 * 更新时间:2017年10月24日11:16                               *
 * 版本号:1.1.0                                              *
 *************************************************************/
public class PhotoSelectAdapter extends RecyclerView.Adapter<PhotoSelectAdapter.PhotoViewHolder> {

    private Context mContext;
    private ArrayList<String> photoPaths = new ArrayList<>();
    public final static int TYPE_ADD = 1;
    public final static int TYPE_PHOTO = 2;
    public final static int MAX = 5;

    private OnItemClickListener onItemClickListener;
    private ArrayList<String> netDataUrl = new ArrayList<>();
    private DeleteListener deleteListener;
    private boolean showType = false;

    public void setPhotoPaths(ArrayList<String> photoPaths) {
        this.photoPaths = photoPaths;
    }

    public ArrayList<String> getPhotoPaths() {
        return photoPaths;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 加载布局文件
     */
    public static View inflate(int id) {
        return View.inflate(Utils.getContext(), id, null);
    }

    boolean isCompleteOfTaskBean;

    public PhotoSelectAdapter(Context mContext, boolean mIsCompleteOfTaskBean) {
        this.photoPaths = new ArrayList<>();
        this.mContext = mContext;
        this.isCompleteOfTaskBean = mIsCompleteOfTaskBean;

    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        switch (viewType) {
            case TYPE_ADD:
                itemView = inflate(R.layout.item_add);
                break;
            case TYPE_PHOTO:
                itemView = inflate(R.layout.picker_select_photo);
                break;
            default:
                break;
        }
        return new PhotoViewHolder(itemView);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, final int position) {
        if (getItemViewType(position) == TYPE_PHOTO) {
            if (photoPaths.get(position).contains("http:")) {
                Glide.with(mContext).
                        load(photoPaths.get(position)).
                        thumbnail(0.5f)
                        .apply(new RequestOptions()
                                .centerCrop()
                                .placeholder(R.drawable.icon_empty)
                                .error(R.drawable.icon_empty)
                                .priority(Priority.HIGH)
                                .diskCacheStrategy(DiskCacheStrategy.NONE))
                        .into(holder.ivPhoto);
                LogUtil.e("图片地址---------：" + photoPaths.get(position));
                if (!showType) {
                    holder.vSelected.setVisibility(View.VISIBLE);
                }
                holder.vSelected.setImageResource(R.drawable.icon_x);
                if (!SPUtils.getInstance().getString(ConfigConsts.checkReservoirStatus, "0").equals("0")) {
                    holder.vSelected.setVisibility(View.INVISIBLE);

                }
               /* holder.vSelected.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (deleteListener != null) {
                            deleteListener.ondelete(netData.get(position));
                        }
                    }
                });*/
                holder.vSelected.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (deleteListener != null) {
                            deleteListener.ondelete(position - netDataUrl.size());
                        }
                    }
                });
            } else {
                Uri uri = Uri.fromFile(new File(photoPaths.get(position)));
                boolean canLoadImage = AndroidLifecycleUtils.canLoadImage(holder.ivPhoto.getContext());
                if (canLoadImage) {
                    Glide.with(mContext)
                            .load(uri)
                            .into(holder.ivPhoto);
                }
                if (!showType) {
                    holder.vSelected.setVisibility(View.VISIBLE);
                }
                holder.vSelected.setImageResource(R.drawable.icon_x);
                if (!SPUtils.getInstance().getString(ConfigConsts.checkReservoirStatus, "0").equals("0")) {
                    holder.vSelected.setVisibility(View.INVISIBLE);

                }

                holder.vSelected.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (deleteListener != null) {
                            deleteListener.ondelete(position - netDataUrl.size());
                        }
                    }
                });
            }

//            holder.vSelected.setImageResource(R.drawable.ic_next_grren_24dp);
            if (isCompleteOfTaskBean) {
                holder.vSelected.setVisibility(View.INVISIBLE);

            }
        }


        holder.itemView.setOnClickListener((v) -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, position);
            }
        });


    }


    @Override
    public int getItemCount() {
        if (!showType) {
            int count = photoPaths.size() + 1;
            if (count > MAX) {
                count = MAX;
            }
            return count;
        } else {
            int count = photoPaths.size();
            if (count > MAX) {
                count = MAX;
            }
            return count;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (!showType) {
            return (position == photoPaths.size() && position != MAX) ? TYPE_ADD : TYPE_PHOTO;
        } else {
            return TYPE_PHOTO;
        }
    }

    public void setLocalData(@NonNull ArrayList<String> localData) {
        photoPaths.clear();
        if (netDataUrl != null && netDataUrl.size() > 0) {
            photoPaths.addAll(netDataUrl);
        }
        if (localData.size() > 0) {
            photoPaths.addAll(localData);
        }
        notifyDataSetChanged();
    }

    public void setLocalData(@NonNull ArrayList<String> localData,boolean noCanAdd) {
        photoPaths.clear();
        if (netDataUrl != null && netDataUrl.size() > 0) {
            photoPaths.addAll(netDataUrl);
        }
        if (localData.size() > 0) {
            photoPaths.addAll(localData);
        }
        showType = noCanAdd;
        notifyDataSetChanged();
    }


    public static class PhotoViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivPhoto;
        private ImageView vSelected;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ivPhoto = itemView.findViewById(R.id.iv_photo);
            vSelected = itemView.findViewById(R.id.v_selected);
            if (vSelected != null) {
                vSelected.setVisibility(View.GONE);
            }

        }
    }

    public interface DeleteListener {

        void ondelete(int position);
    }

    public void setDeleteListener(DeleteListener deleteListener) {
        this.deleteListener = deleteListener;
    }
}
