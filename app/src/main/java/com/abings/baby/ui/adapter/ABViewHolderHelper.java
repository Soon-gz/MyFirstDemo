/**
 * Copyright 2015 bingoogolapple
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.abings.baby.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 描述:为AdapterView和RecyclerView的item设置常见属性（链式编程）
 */
public class ABViewHolderHelper implements View.OnClickListener, View.OnLongClickListener, CompoundButton.OnCheckedChangeListener {
    protected final SparseArray<View> mViews;
    protected ABOnItemChildClickListener mOnItemChildClickListener;
    protected ABOnItemChildLongClickListener mOnItemChildLongClickListener;
    protected ABOnItemChildCheckedChangeListener mOnItemChildCheckedChangeListener;
    protected View mConvertView;
    protected Context mContext;
    protected int mPosition;
    protected ABRecyclerViewHolder mRecyclerViewHolder;
    protected RecyclerView mRecyclerView;

    protected ViewGroup mAdapterView;
    /**
     * 留着以后作为扩充对象
     */
    protected Object mObj;

    public ABViewHolderHelper(ViewGroup adapterView, View convertView) {
        mViews = new SparseArray<>();
        mAdapterView = adapterView;
        mConvertView = convertView;
        mContext = convertView.getContext();
    }

    public ABViewHolderHelper(RecyclerView recyclerView, View convertView) {
        mViews = new SparseArray<>();
        mRecyclerView = recyclerView;
        mConvertView = convertView;
        mContext = convertView.getContext();
    }

    public void setRecyclerViewHolder(ABRecyclerViewHolder recyclerViewHolder) {
        mRecyclerViewHolder = recyclerViewHolder;
    }

    public ABRecyclerViewHolder getRecyclerViewHolder() {
        return mRecyclerViewHolder;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    public int getPosition() {
        if (mRecyclerViewHolder != null) {
            return mRecyclerViewHolder.getAdapterPosition();
        }
        return mPosition;
    }

    /**
     * 设置item子控件点击事件监听器
     *
     * @param onItemChildClickListener
     */
    public void setOnItemChildClickListener(ABOnItemChildClickListener onItemChildClickListener) {
        mOnItemChildClickListener = onItemChildClickListener;
    }

    /**
     *  为id为viewId的item子控件设置点击事件监听器
     *
     * @param viewId
     */
    public void setItemChildClickListener(@IdRes int viewId) {
        getView(viewId).setOnClickListener(this);
    }

    /**
     * 设置item子控件长按事件监听器
     *
     * @param onItemChildLongClickListener
     */
    public void setOnItemChildLongClickListener(ABOnItemChildLongClickListener onItemChildLongClickListener) {
        mOnItemChildLongClickListener = onItemChildLongClickListener;
    }

    /**
     * 涓篿d涓簐iewId鐨刬tem瀛愭帶浠惰缃暱鎸変簨浠剁洃鍚櫒
     *
     * @param viewId
     */
    public void setItemChildLongClickListener(@IdRes int viewId) {
        getView(viewId).setOnLongClickListener(this);
    }

    /**
     * 璁剧疆item瀛愭帶浠堕?変腑鐘舵?佸彉鍖栦簨浠剁洃鍚櫒
     *
     * @param onItemChildCheckedChangeListener
     */
    public void setOnItemChildCheckedChangeListener(ABOnItemChildCheckedChangeListener onItemChildCheckedChangeListener) {
        mOnItemChildCheckedChangeListener = onItemChildCheckedChangeListener;
    }

    /**
     * 涓篿d涓簐iewId鐨刬tem瀛愭帶浠惰缃?変腑鐘舵?佸彉鍖栦簨浠剁洃鍚櫒
     *
     * @param viewId
     */
    public void setItemChildCheckedChangeListener(@IdRes int viewId) {
        if (getView(viewId) instanceof CompoundButton) {
            ((CompoundButton) getView(viewId)).setOnCheckedChangeListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (mOnItemChildClickListener != null) {
            if (mRecyclerView != null) {
                mOnItemChildClickListener.onItemChildClick(mRecyclerView, v, getPosition());
            } else if (mAdapterView != null) {
                mOnItemChildClickListener.onItemChildClick(mAdapterView, v, getPosition());
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mOnItemChildLongClickListener != null) {
            if (mRecyclerView != null) {
                return mOnItemChildLongClickListener.onItemChildLongClick(mRecyclerView, v, getPosition());
            } else if (mAdapterView != null) {
                return mOnItemChildLongClickListener.onItemChildLongClick(mAdapterView, v, getPosition());
            }
        }
        return false;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (mOnItemChildCheckedChangeListener != null) {
            if (mRecyclerView != null) {
                mOnItemChildCheckedChangeListener.onItemChildCheckedChanged(mRecyclerView, buttonView, getPosition(), isChecked);
            } else if (mAdapterView != null) {
                mOnItemChildCheckedChangeListener.onItemChildCheckedChanged(mAdapterView, buttonView, getPosition(), isChecked);
            }
        }
    }

    /**
     * 通过控件的Id获取对应的控件，如果没有则加入mViews，则从item根控件中查找并保存到mViews中
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(@IdRes int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 通过ImageView的Id获取ImageView
     *
     * @param viewId
     * @return
     */
    public ImageView getImageView(@IdRes int viewId) {
        return getView(viewId);
    }

    /**
     * 通过TextView的Id获取TextView
     *
     * @param viewId
     * @return
     */
    public TextView getTextView(@IdRes int viewId) {
        return getView(viewId);
    }

    /**
     *  获取item的根控件
     *
     * @return
     */
    public View getConvertView() {
        return mConvertView;
    }

    public void setObj(Object obj) {
        mObj = obj;
    }

    public Object getObj() {
        return mObj;
    }

    /**
     * 设置对应id的控件的文本内容
     *
     * @param viewId
     * @param text
     * @return
     */
    public ABViewHolderHelper setText(@IdRes int viewId, CharSequence text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }

    /**
     * 设置对应id的控件的文本内容
     *
     * @param viewId
     * @param stringResId  字符串资源id
     * @return
     */
    public ABViewHolderHelper setText(@IdRes int viewId, @StringRes int stringResId) {
        TextView view = getView(viewId);
        view.setText(stringResId);
        return this;
    }

    /**
     * 设置对应id的控件的html文本内容
     *
     * @param viewId
     * @param source html
     * @return
     */
    public ABViewHolderHelper setHtml(@IdRes int viewId, String source) {
        TextView view = getView(viewId);
        view.setText(Html.fromHtml(source));
        return this;
    }

    /**
     * 设置对应id的控件是否选中
     *
     * @param viewId
     * @param checked
     * @return
     */
    public ABViewHolderHelper setChecked(@IdRes int viewId, boolean checked) {
        Checkable view = getView(viewId);
        view.setChecked(checked);
        return this;
    }

    public ABViewHolderHelper setTag(@IdRes int viewId, Object tag) {
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }

    public ABViewHolderHelper setTag(@IdRes int viewId, int key, Object tag) {
        View view = getView(viewId);
        view.setTag(key, tag);
        return this;
    }

    public ABViewHolderHelper setVisibility(@IdRes int viewId, int visibility) {
        View view = getView(viewId);
        view.setVisibility(visibility);
        return this;
    }

    public ABViewHolderHelper setImageBitmap(@IdRes int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    public ABViewHolderHelper setImageDrawable(@IdRes int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    /**
     * @param viewId
     * @param textColorResId 颜色资源id
     * @return
     */
    public ABViewHolderHelper setTextColorRes(@IdRes int viewId, @ColorRes int textColorResId) {
        TextView view = getView(viewId);
        view.setTextColor(mContext.getResources().getColor(textColorResId));
        return this;
    }

    /**
     * @param viewId
     * @param textColor 颜色值
     * @return
     */
    public ABViewHolderHelper setTextColor(@IdRes int viewId, int textColor) {
        TextView view = getView(viewId);
        view.setTextColor(textColor);
        return this;
    }

    /**
     * @param viewId
     * @param backgroundResId 背景资源id
     * @return
     */
    public ABViewHolderHelper setBackgroundRes(@IdRes int viewId, int backgroundResId) {
        View view = getView(viewId);
        view.setBackgroundResource(backgroundResId);
        return this;
    }

    /**
     * @param viewId
     * @param color  颜色值
     * @return
     */
    public ABViewHolderHelper setBackgroundColor(@IdRes int viewId, int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    /**
     * @param viewId
     * @param colorResId 颜色值资源id
     * @return
     */
    public ABViewHolderHelper setBackgroundColorRes(@IdRes int viewId, @ColorRes int colorResId) {
        View view = getView(viewId);
        view.setBackgroundColor(mContext.getResources().getColor(colorResId));
        return this;
    }

    /**
     * @param viewId
     * @param imageResId 图像资源id
     * @return
     */
    public ABViewHolderHelper setImageResource(@IdRes int viewId, @DrawableRes int imageResId) {
        ImageView view = getView(viewId);
        view.setImageResource(imageResId);
        return this;
    }

}