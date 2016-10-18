package com.abings.baby.ui.infolist.news;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.abings.baby.R;
import com.abings.baby.data.model.NewsDetail;
import com.abings.baby.data.remote.RetrofitUtils;
import com.bumptech.glide.Glide;

import java.util.List;

class StaggeredHomeAdapter extends
        RecyclerView.Adapter<StaggeredHomeAdapter.MyViewHolder> {

    private List<NewsDetail.ImagesBean> mDatas;
    private LayoutInflater mInflater;
    private Context context;


    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public void setDatas(List<NewsDetail.ImagesBean> datas){
        mDatas = datas;
    }
    public StaggeredHomeAdapter(Context context, List<NewsDetail.ImagesBean> datas) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(mInflater.inflate(
                R.layout.item_staggered_home, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        Glide.with(context).load(RetrofitUtils.BASE_NEWS_SMALL_IMG_URL + mDatas.get(position).getImage()).placeholder(R.drawable.a).error(R.drawable.a).into(holder.img);
        if ("video".equals(mDatas.get(position).getType())){
            holder.video.setVisibility(View.VISIBLE);
        }else{
            holder.video.setVisibility(View.GONE);
        }
        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    class MyViewHolder extends ViewHolder {

        ImageView img;
        ImageView video;

        public MyViewHolder(View view) {
            super(view);
            img = (ImageView) view.findViewById(R.id.img_news_info);
            video = (ImageView) view.findViewById(R.id.video_play);
        }
    }
}