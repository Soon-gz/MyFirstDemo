package com.abings.baby.ui.infolist;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.abings.baby.R;
import com.abings.baby.widget.basepopup.BasePopupWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 大灯泡 on 2016/1/20.
 * 包含着listview的popup，使用builder模式，事件与tag进行绑定
 */
public class InfoListPopup extends BasePopupWindow {

    private ListView mListView;
    private OnListPopupItemClickListener mOnListPopupItemClickListener;

    private InfoListPopup(Activity context) {
        super(context);
    }

    private InfoListPopup(Activity context, Builder builder) {
        this(context);
        mListView = (ListView) mPopupView.findViewById(R.id.popup_list);
        setAdapter(context, builder);
    }

    public static class Builder {
        private List<Object> mItemEventList = new ArrayList<>();
        private Activity mContext;

        public Builder(Activity context) {
            mContext = context;
        }

        public Builder addItem(String itemTx) {
            mItemEventList.add(itemTx);
            return this;
        }

        public Builder addItem(int clickTag, int imgRes) {
            mItemEventList.add(new clickItemEvent(clickTag, imgRes));
            return this;
        }

        public List<Object> getItemEventList() {
            return mItemEventList;
        }

        public InfoListPopup build() {
            return new InfoListPopup(mContext, this);
        }

    }

    public static class clickItemEvent {
        private int clickTag;
        private int imgRes;

        public clickItemEvent(int clickTag, int imgRes) {
            this.clickTag = clickTag;
            this.imgRes = imgRes;
        }

        public int getClickTag() {
            return clickTag;
        }

        public void setClickTag(int clickTag) {
            this.clickTag = clickTag;
        }

        public int getImgRes() {
            return imgRes;
        }

        public void setItemTx(int imgRes) {
            this.imgRes = imgRes;
        }
    }

    //=============================================================adapter
    class ListPopupAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private Context mContext;
        private List<Object> mItemList;

        public ListPopupAdapter(Context context, @NonNull List<Object> itemList) {
            mContext = context;
            mItemList = itemList;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mItemList.size();
        }

        @Override
        public String getItem(int position) {
            if (mItemList.get(position) instanceof String) {
                return (String) mItemList.get(position);
            }
            return "";
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh = null;
            if (convertView == null) {
                vh = new ViewHolder();
                convertView = mInflater.inflate(R.layout.item_img_popup_list, parent, false);
                vh.img = (ImageView) convertView.findViewById(R.id.img);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            vh.img.setImageResource(((clickItemEvent) mItemList.get(position)).getImgRes());
            return convertView;
        }

        public List<Object> getItemList() {
            return this.mItemList;
        }


        class ViewHolder {
            public ImageView img;
        }
    }

    //=============================================================init
    private void setAdapter(Activity context, Builder builder) {
        if (builder.getItemEventList() == null || builder.getItemEventList().size() == 0) return;
        final ListPopupAdapter adapter = new ListPopupAdapter(context, builder.getItemEventList());
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mOnListPopupItemClickListener != null) {
                    Object clickObj = adapter.getItemList().get(position);
                    if (clickObj instanceof String) {
                        mOnListPopupItemClickListener.onItemClick(position);
                    }
                    if (clickObj instanceof clickItemEvent) {
                        int what = ((clickItemEvent) clickObj).clickTag;
                        mOnListPopupItemClickListener.onItemClick(what);
                    }
                }
            }
        });

    }

    //=============================================================super methods
    @Override
    protected Animation getShowAnimation() {
        return null;
    }

    @Override
    public Animator getShowAnimator() {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(ObjectAnimator.ofFloat(mAnimaView, "rotationX", 90f, 0f).setDuration(400), ObjectAnimator
                .ofFloat(mAnimaView, "translationY", 250f, 0f).setDuration(400), ObjectAnimator.ofFloat(mAnimaView,
                "alpha", 0f, 1f).setDuration(400 * 3 / 2));
        return set;
    }

    @Override
    protected View getClickToDismissView() {
        return mPopupView;
    }

    @Override
    public View getPopupView() {
        return getPopupViewById(R.layout.popup_info_list);
    }

    @Override
    public View getAnimaView() {
        return mPopupView.findViewById(R.id.popup_anima);
    }

    @Override
    public Animator getExitAnimator() {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(ObjectAnimator.ofFloat(mAnimaView, "rotationX", 0f, 90f).setDuration(400), ObjectAnimator
                .ofFloat(mAnimaView, "translationY", 0f, 250f).setDuration(400), ObjectAnimator.ofFloat(mAnimaView,
                "alpha", 1f, 0f).setDuration(400 * 3 / 2));
        return set;
    }

    private int[] viewLocation = new int[2];

    @Override
    public void showPopupWindow(View v) {
        try {
            v.getLocationOnScreen(viewLocation);
            mPopupWindow.showAsDropDown(v);
//            mPopupWindow.showAtLocation(v, Gravity.NO_GRAVITY, viewLocation[0],
//                    (int) (v.getHeight() * 1.5));
            if (getShowAnimation() != null && mAnimaView != null) {
                mAnimaView.clearAnimation();
                mAnimaView.startAnimation(getShowAnimation());
            }
            if (getShowAnimation() == null && getShowAnimator() != null && mAnimaView != null) {
                getShowAnimator().start();
            }
        } catch (Exception e) {
        }
    }

    //=============================================================interface

    public OnListPopupItemClickListener getOnListPopupItemClickListener() {
        return mOnListPopupItemClickListener;
    }

    public void setOnListPopupItemClickListener(OnListPopupItemClickListener onListPopupItemClickListener) {
        mOnListPopupItemClickListener = onListPopupItemClickListener;
    }

    public interface OnListPopupItemClickListener {
        void onItemClick(int what);
    }
}
