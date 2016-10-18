package com.abings.baby.ui.contacts;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;

import com.abings.baby.R;
import com.abings.baby.data.model.Contact;
import com.abings.baby.data.remote.RetrofitUtils;
import com.abings.baby.ui.adapter.ABRecyclerViewAdapter;
import com.abings.baby.ui.adapter.ABViewHolderHelper;
import com.bumptech.glide.Glide;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.List;

/**
 * 作者：黄斌 on 2016/2/21 13:55
 * 说明：
 */
public class RecyclerIndexAdapter extends ABRecyclerViewAdapter<Contact> {

    public RecyclerIndexAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.recyler_item_indexview);
    }

    @Override
    public void setItemChildListener(ABViewHolderHelper viewHolderHelper) {
//        viewHolderHelper.setItemChildClickListener(R.id.content);
    }


    @Override
    public void fillData(ABViewHolderHelper viewHolderHelper, final int position, Contact model) {
        int section = getSectionForPosition(position);
        if (position == getPositionForSection(section)) {
            viewHolderHelper.setVisibility(R.id.tv_item_indexview_catalog, View.VISIBLE);
            viewHolderHelper.setText(R.id.tv_item_indexview_catalog, "" + model.getFirstChar());

            if ("#".equals("" + model.getFirstChar())) {
                viewHolderHelper.setText(R.id.tv_item_indexview_catalog, "老师");
            }
        } else {
            viewHolderHelper.setVisibility(R.id.tv_item_indexview_catalog, View.GONE);
        }
        viewHolderHelper.setText(R.id.name, model.getName());

        if (model.isTeacher()) {
            viewHolderHelper.setText(R.id.phone, model.getClassTeacherItem().getMobile_phone());
            viewHolderHelper.setImageResource(R.id.photo, R.drawable.image_onload);
        } else {
            KLog.e("URL = " + RetrofitUtils.BASE_BABY_PHOTO_URL + model.getUserContact().getPhoto());
            Glide.with(mContext).load(RetrofitUtils.BASE_BABY_PHOTO_URL + model.getUserContact().getPhoto()).placeholder(R.drawable.image_onload).error(R.drawable.image_empty_failed).dontAnimate()
                    .thumbnail(0.1f).into(viewHolderHelper.getImageView(R.id.photo));
//            if (model.getUserContact().getImages() != null && model.getUserContact().getImages().size() > 0) {
//                viewHolderHelper.setText(R.id.phone, model.getUserContact().getImages().get(0).getMobile_phone());
//            }
        }

    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return mDatas.get(position).getFirstChar();
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getItemCount(); i++) {
            char firstChar = mDatas.get(i).getFirstChar();
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }
}