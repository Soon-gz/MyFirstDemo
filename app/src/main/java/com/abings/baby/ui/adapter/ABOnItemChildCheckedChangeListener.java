package com.abings.baby.ui.adapter;

import android.view.ViewGroup;
import android.widget.CompoundButton;

/**
 * 描述:AdapterView和RecyclerView的item中子控件选中状态变化事件监听器
 */
public interface ABOnItemChildCheckedChangeListener {
    void onItemChildCheckedChanged(ViewGroup parent, CompoundButton childView, int position, boolean isChecked);
}