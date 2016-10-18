package com.abings.baby.ui.waterfall;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abings.baby.R;
import com.abings.baby.data.model.WaterFallGridHeaderItem;
import com.abings.baby.data.remote.RetrofitUtils;
import com.abings.baby.utils.DateUtil;
import com.abings.baby.utils.StringUtils;
import com.bumptech.glide.Glide;
import com.tonicartos.superslim.GridSLM;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 */
public class WaterfallGridAdapter extends RecyclerView.Adapter<WaterfallGridAdapter.CountryViewHolder> {


    public class CountryViewHolder extends RecyclerView.ViewHolder {
        ImageView videoImag;
        ImageView mImageView;
        TextView today;
        TextView dateMonth;
        TextView dateDay;

        CountryViewHolder(View view) {
            super(view);
            videoImag= (ImageView) view.findViewById(R.id.video);
            mImageView = (ImageView) view.findViewById(R.id.img);
//            mHeaderName = (TextView) view.findViewById(R.id.header_name);

            today = (TextView) view.findViewById(R.id.today);
            dateMonth = (TextView) view.findViewById(R.id.date_month);
            dateDay = (TextView) view.findViewById(R.id.date_day);
        }
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    private static final int VIEW_TYPE_HEADER = 0x01;

    private static final int VIEW_TYPE_CONTENT = 0x00;
    private static final int VIEW_TYPE_VIDEO = 0x02;

    private static final int LINEAR = 0;

    private List<WaterFallGridHeaderItem> mDatas = new ArrayList<WaterFallGridHeaderItem>();

    private int mHeaderDisplay;

    private boolean mMarginsFixed = true;

    private final Context mContext;

    public WaterfallGridAdapter(Context context) {
        mContext = context;
    }

    public boolean isItemHeader(int position) {
        return mDatas.get(position).isHeader;
    }

    @Override
    public CountryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.waterfall_grid_header, parent, false);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.waterfall_grid_item, parent, false);
        }
        return new CountryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CountryViewHolder holder, final int position) {
        final WaterFallGridHeaderItem item = mDatas.get(position);
        final View itemView = holder.itemView;
        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(itemView, position);
                }
            });
        }

        final GridSLM.LayoutParams lp = GridSLM.LayoutParams.from(itemView.getLayoutParams());
        // Overrides xml attrs, could use different layouts too.
        if (item.isHeader) {
                Date date = DateUtil.getTimeFromString(item.getHeader(), "yyyy/MM/dd");
                if (DateUtil.isToday(date.getTime())) {
                    holder.dateDay.setVisibility(View.GONE);
                    holder.dateMonth.setVisibility(View.GONE);
                    holder.today.setVisibility(View.VISIBLE);
                } else {
                    holder.dateDay.setVisibility(View.VISIBLE);
                    holder.dateMonth.setVisibility(View.VISIBLE);
                    holder.today.setVisibility(View.GONE);
                    holder.dateMonth.setText(DateUtil.getCNdate(DateUtil.getMonth(date.getTime())));
                    holder.dateDay.setText(DateUtil.getDay(date.getTime()) + "");
                }
            lp.headerDisplay = mHeaderDisplay;
            if (lp.isHeaderInline() || (mMarginsFixed && !lp.isHeaderOverlay())) {
                lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            } else {
                lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            }

            lp.headerEndMarginIsAuto = !mMarginsFixed;
            lp.headerStartMarginIsAuto = !mMarginsFixed;
        } else {
            if (holder.getItemViewType() == VIEW_TYPE_VIDEO){
                holder.videoImag.setVisibility(View.VISIBLE);
            }else {
                holder.videoImag.setVisibility(View.GONE);
            }
            if (StringUtils.isEmpty(item.getItem().getFk_grade_class_id()) && StringUtils.isEmpty(item.getItem().getImage())) {
//                holder.mImageView.setImageResource(R.drawable.add_photo);
                Glide.with(mContext).load(R.drawable.add_photo).placeholder(R.drawable.image_onload).error(R.drawable.image_empty_failed)
                        .dontAnimate().thumbnail(0.1f).into(holder.mImageView);
            } else {
                Glide.with(mContext).load(RetrofitUtils.BASE_NEWS_SMALL_IMG_URL + item.getItem().getImage()).placeholder(R.drawable.image_onload).error(R.drawable.a)
                        .dontAnimate().thumbnail(0.1f).into(holder.mImageView);
            }

        }
        lp.setSlm(GridSLM.ID);
        lp.setNumColumns(4);
        lp.setColumnWidth(mContext.getResources().getDimensionPixelSize(R.dimen.grid_column_width));
        lp.setFirstPosition(item.getSectionFirstPosition());
        itemView.setLayoutParams(lp);
    }

    @Override
    public int getItemViewType(int position) {
        if (mDatas.get(position).isHeader ){
            return  VIEW_TYPE_HEADER;
        }else if ("video".equals(mDatas.get(position).getItem().getType())){
            return  VIEW_TYPE_VIDEO;
        }else{
            return  VIEW_TYPE_CONTENT;
        }
    }

    public WaterFallGridHeaderItem getItem(int position) {
        return mDatas.get(position);
    }

    /**
     * 获取数据集合
     *
     * @return
     */
    public List<WaterFallGridHeaderItem> getDatas() {
        return mDatas;
    }

    @Override
    public int getItemCount() {
        if (mDatas != null) {
            return mDatas.size();
        } else return 0;
    }

    public void setHeaderDisplay(int headerDisplay) {
        mHeaderDisplay = headerDisplay;
        notifyHeaderChanges();
    }

    public void setMarginsFixed(boolean marginsFixed) {
        mMarginsFixed = marginsFixed;
        notifyHeaderChanges();
    }

    private void notifyHeaderChanges() {
        if (mDatas != null) {
            for (int i = 0; i < mDatas.size(); i++) {
                WaterFallGridHeaderItem item = mDatas.get(i);
                if (item.isHeader) {
                    notifyItemChanged(i);
                }
            }
        }

    }

    public void notifyDataChanges(List<WaterFallGridHeaderItem> items) {
        mDatas = items;
        notifyDataSetChanged();
    }

    /**
     * 在集合尾部添加更多数据集合（上拉从服务器获取更多的数据集合，例如新浪微博列表上拉加载更晚时间发布的微博数据）
     *
     * @param datas
     */
    public void addMoreDatas(List<WaterFallGridHeaderItem> datas) {
        if (datas != null) {
            mDatas.clear();
            mDatas.addAll(datas);
            notifyDataSetChanged();
        }
    }

    /**
     * 设置全新的数据集合，如果传入null，则清空数据列表（第�?次从服务器加载数据，或�?�下拉刷新当前界面数据表�?
     *
     * @param datas
     */
    public void setDatas(List<WaterFallGridHeaderItem> datas) {
        if (datas != null) {
            mDatas.addAll(datas);
        } else {
            mDatas.clear();
        }
        notifyDataSetChanged();
    }

    /**
     * 清空数据列表
     */
    public void clear() {
        mDatas.clear();
        notifyDataSetChanged();
    }

}
