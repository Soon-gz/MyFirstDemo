package com.abings.baby.ui.infolist.news;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.abings.baby.R;
import com.abings.baby.data.model.NewsDetail;
import com.abings.baby.data.remote.RetrofitUtils;
import com.abings.baby.ui.adapter.ABRecyclerViewAdapter;
import com.abings.baby.ui.adapter.ABViewHolderHelper;
import com.bumptech.glide.Glide;

/**
 * Created by Administrator on 2016/2/1.
 */
public class RecyclerViewAdapterInfoList extends ABRecyclerViewAdapter<NewsDetail.ImagesBean> {

    private ItemTouchHelper mItemTouchHelper;
    private boolean mIsIgnoreChange = true;

    public RecyclerViewAdapterInfoList(RecyclerView recyclerView) {
        super(recyclerView, R.layout.view_image2);
    }

    public void setItemTouchHelper(ItemTouchHelper itemTouchHelper) {
        mItemTouchHelper = itemTouchHelper;
    }

    @Override
    public void setItemChildListener(final ABViewHolderHelper viewHolderHelper) {
    }

    @Override
    protected void fillData(ABViewHolderHelper viewHolderHelper, int position, NewsDetail.ImagesBean model) {
        Glide.with(mContext).load(RetrofitUtils.BASE_NEWS_SMALL_IMG_URL + model.getImage()).placeholder(R.drawable.no_stow_pic).error(R.drawable.no_stow_pic).into(viewHolderHelper.getImageView(R.id.iv_image_view));
    }
}
