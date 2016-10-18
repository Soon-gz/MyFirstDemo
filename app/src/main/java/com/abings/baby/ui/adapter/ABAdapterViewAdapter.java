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
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 在AdapterView和RecyclerView中通用的Adapter和ViewHolder
 * 描述:AdapterView适配器
 */
public abstract class ABAdapterViewAdapter<M> extends BaseAdapter {
    protected final int mItemLayoutId;
    protected Context mContext;
    protected List<M> mDatas;
    protected ABOnItemChildClickListener mOnItemChildClickListener;
    protected ABOnItemChildLongClickListener mOnItemChildLongClickListener;
    protected ABOnItemChildCheckedChangeListener mOnItemChildCheckedChangeListener;

    public ABAdapterViewAdapter(Context context, int itemLayoutId) {
        mContext = context;
        mItemLayoutId = itemLayoutId;
        mDatas = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public M getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ABAdapterViewHolder viewHolder = ABAdapterViewHolder.dequeueReusableAdapterViewHolder(convertView, parent, mItemLayoutId);
        viewHolder.getViewHolderHelper().setPosition(position);
        viewHolder.getViewHolderHelper().setOnItemChildClickListener(mOnItemChildClickListener);
        viewHolder.getViewHolderHelper().setOnItemChildLongClickListener(mOnItemChildLongClickListener);
        viewHolder.getViewHolderHelper().setOnItemChildCheckedChangeListener(mOnItemChildCheckedChangeListener);
        setItemChildListener(viewHolder.getViewHolderHelper());

        fillData(viewHolder.getViewHolderHelper(), position, getItem(position));
        return viewHolder.getConvertView();
    }

    /**
     * 涓篿tem鐨勫瀛愯妭鐐硅缃洃鍚櫒锛屽苟涓嶆槸姣忎竴涓暟鎹垪琛ㄩ兘瑕佷负item鐨勫瓙鎺т欢娣诲姞浜嬩欢鐩戝惉鍣紝鎵?浠ヨ繖閲岄噰鐢ㄤ簡绌哄疄鐜帮紝闇?瑕佽缃簨浠剁洃鍚櫒鏃堕噸鍐欒鏂规硶鍗冲彲
     *
     * @param viewHolderHelper
     */
    protected void setItemChildListener(ABViewHolderHelper viewHolderHelper) {
    }

    /**
     * 濉厖item鏁版嵁
     *
     * @param viewHolderHelper
     * @param position
     * @param model
     */
    protected abstract void fillData(ABViewHolderHelper viewHolderHelper, int position, M model);

    /**
     * 璁剧疆item涓殑瀛愭帶浠剁偣鍑讳簨浠剁洃鍚櫒
     *
     * @param onItemChildClickListener
     */
    public void setOnItemChildClickListener(ABOnItemChildClickListener onItemChildClickListener) {
        mOnItemChildClickListener = onItemChildClickListener;
    }

    /**
     * 璁剧疆item涓殑瀛愭帶浠堕暱鎸変簨浠剁洃鍚櫒
     *
     * @param onItemChildLongClickListener
     */
    public void setOnItemChildLongClickListener(ABOnItemChildLongClickListener onItemChildLongClickListener) {
        mOnItemChildLongClickListener = onItemChildLongClickListener;
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
     * 鑾峰彇鏁版嵁闆嗗悎
     *
     * @return
     */
    public List<M> getDatas() {
        return mDatas;
    }

    /**
     * 鍦ㄩ泦鍚堝ご閮ㄦ坊鍔犳柊鐨勬暟鎹泦鍚堬紙涓嬫媺浠庢湇鍔″櫒鑾峰彇鏈?鏂扮殑鏁版嵁闆嗗悎锛屼緥濡傛柊娴井鍗氬姞杞芥渶鏂扮殑鍑犳潯寰崥鏁版嵁锛?
     *
     * @param datas
     */
    public void addNewDatas(List<M> datas) {
        if (datas != null) {
            mDatas.addAll(0, datas);
            notifyDataSetChanged();
        }
    }

    /**
     * 鍦ㄩ泦鍚堝熬閮ㄦ坊鍔犳洿澶氭暟鎹泦鍚堬紙涓婃媺浠庢湇鍔″櫒鑾峰彇鏇村鐨勬暟鎹泦鍚堬紝渚嬪鏂版氮寰崥鍒楄〃涓婃媺鍔犺浇鏇存櫄鏃堕棿鍙戝竷鐨勫井鍗氭暟鎹級
     *
     * @param datas
     */
    public void addMoreDatas(List<M> datas) {
        if (datas != null) {
            mDatas.addAll(mDatas.size(), datas);
            notifyDataSetChanged();
        }
    }

    /**
     * 璁剧疆鍏ㄦ柊鐨勬暟鎹泦鍚堬紝濡傛灉浼犲叆null锛屽垯娓呯┖鏁版嵁鍒楄〃锛堢涓?娆′粠鏈嶅姟鍣ㄥ姞杞芥暟鎹紝鎴栬?呬笅鎷夊埛鏂板綋鍓嶇晫闈㈡暟鎹〃锛?
     *
     * @param datas
     */
    public void setDatas(List<M> datas) {
        if (datas != null) {
            mDatas = datas;
        } else {
            mDatas.clear();
        }
        notifyDataSetChanged();
    }

    /**
     * 娓呯┖鏁版嵁鍒楄〃
     */
    public void clear() {
        mDatas.clear();
        notifyDataSetChanged();
    }

    /**
     * 鍒犻櫎鎸囧畾绱㈠紩鏁版嵁鏉＄洰
     *
     * @param position
     */
    public void removeItem(int position) {
        mDatas.remove(position);
        notifyDataSetChanged();
    }

    /**
     * 鍒犻櫎鎸囧畾鏁版嵁鏉＄洰
     *
     * @param model
     */
    public void removeItem(M model) {
        mDatas.remove(model);
        notifyDataSetChanged();
    }

    /**
     * 鍦ㄦ寚瀹氫綅缃坊鍔犳暟鎹潯鐩?
     *
     * @param position
     * @param model
     */
    public void addItem(int position, M model) {
        mDatas.add(position, model);
        notifyDataSetChanged();
    }

    /**
     * 鍦ㄩ泦鍚堝ご閮ㄦ坊鍔犳暟鎹潯鐩?
     *
     * @param model
     */
    public void addFirstItem(M model) {
        addItem(0, model);
    }

    /**
     * 鍦ㄩ泦鍚堟湯灏炬坊鍔犳暟鎹潯鐩?
     *
     * @param model
     */
    public void addLastItem(M model) {
        addItem(mDatas.size(), model);
    }

    /**
     * 鏇挎崲鎸囧畾绱㈠紩鐨勬暟鎹潯鐩?
     *
     * @param location
     * @param newModel
     */
    public void setItem(int location, M newModel) {
        mDatas.set(location, newModel);
        notifyDataSetChanged();
    }

    /**
     * 鏇挎崲鎸囧畾鏁版嵁鏉＄洰
     *
     * @param oldModel
     * @param newModel
     */
    public void setItem(M oldModel, M newModel) {
        setItem(mDatas.indexOf(oldModel), newModel);
    }

    /**
     * 浜ゆ崲涓や釜鏁版嵁鏉＄洰鐨勪綅缃?
     *
     * @param fromPosition
     * @param toPosition
     */
    public void moveItem(int fromPosition, int toPosition) {
        Collections.swap(mDatas, fromPosition, toPosition);
        notifyDataSetChanged();
    }
}