package com.abings.baby.ui.contacts;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.abings.baby.R;
import com.abings.baby.data.model.Contact;
import com.abings.baby.data.remote.RetrofitUtils;
import com.abings.baby.ui.adapter.ABRecyclerViewAdapter;
import com.abings.baby.ui.adapter.ABViewHolderHelper;
import com.abings.baby.utils.ImageLoaderUtil;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;

/**
 * 作者：黄斌 on 2016/2/21 13:55
 * 说明：
 */
public class ActivityRecyclerIndexAdapter extends ABRecyclerViewAdapter<Contact> {
    // 用来控制CheckBox的选中状况
    private static HashMap<Integer, Boolean> isSelected = new HashMap<Integer, Boolean>();
    private Boolean isShowCheckBox = false;

    public ActivityRecyclerIndexAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.recyler_item_indexview);
    }

    @Override
    public void setItemChildListener(ABViewHolderHelper viewHolderHelper) {
//        viewHolderHelper.setItemChildClickListener(R.id.content);
    }

    public Boolean getIsShowCheckBox() {
        return isShowCheckBox;
    }

    public void setIsShowCheckBox(Boolean isShowCheckBox) {
        this.isShowCheckBox = isShowCheckBox;
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
        CheckBox check_box = (CheckBox) viewHolderHelper.getConvertView().findViewById(R.id.check_box);
        viewHolderHelper.setText(R.id.name, model.getName());
        // 监听checkBox并根据原来的状态来设置新的状态
        check_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isSelected.put(position, isChecked);
            }
        });
        // 根据isSelected来设置checkbox的选中状况
        check_box.setChecked(getIsSelected().get(position));

        if (isShowCheckBox) {
            if (!model.isTeacher()) {
                viewHolderHelper.setVisibility(R.id.check_box, View.VISIBLE);
            }else{
                viewHolderHelper.setVisibility(R.id.check_box, View.GONE);
            }
        } else {
            viewHolderHelper.setVisibility(R.id.check_box, View.GONE);
        }
        if (model.isTeacher()) {
            viewHolderHelper.setText(R.id.phone, model.getClassTeacherItem().getMobile_phone());
            viewHolderHelper.setImageResource(R.id.photo, R.drawable.left_menu_header_image);
        } else {
            viewHolderHelper.setText(R.id.phone, "");
            ImageLoaderUtil.loadContactsHeadImage(mContext,RetrofitUtils.BASE_BABY_PHOTO_URL,model.getUserContact().getPhoto(),R.drawable.image_onload,R.drawable.left_menu_header_image,viewHolderHelper.getImageView(R.id.photo));

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

    @Override
    public void setDatas(List<Contact> datas) {
        if (datas != null) {
            mDatas = datas;
            initDate();
        } else {
            mDatas.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public void addMoreDatas(List<Contact> datas) {
        if (datas != null) {
            mDatas.addAll(mDatas.size(), datas);
            initDate();
            notifyItemRangeInserted(mDatas.size(), datas.size());
        }
    }


    // 初始化isSelected的数据
    private void initDate() {
        for (int i = 0; i < mDatas.size(); i++) {
            getIsSelected().put(i, false);
        }
    }

    public HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        this.isSelected = isSelected;
    }

    public int getCheckCount() {
        int num = 0;
        for (int i = 0; i < mDatas.size(); i++) {
            if (getIsSelected().get(i)) {
                num++;
            }
        }
        return num;
    }

    public int selectAll() {
        // 遍历list的长度，将MyAdapter中的map值全部设为true
        for (int i = 0; i < mDatas.size(); i++) {
            getIsSelected().put(i, true);
        }
        // 刷新listview和TextView的显示
        notifyDataSetChanged();
        // 数量设为list的长度
        return mDatas.size();
    }

    public int selectCancel() {
        // 遍历list的长度，将MyAdapter中的map值全部设为true
        for (int i = 0; i < mDatas.size(); i++) {
            getIsSelected().put(i, false);
        }
        // 刷新listview和TextView的显示
        notifyDataSetChanged();
        // 数量设为list的长度
        return mDatas.size();
    }
}