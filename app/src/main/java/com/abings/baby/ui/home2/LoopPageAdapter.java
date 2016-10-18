package com.abings.baby.ui.home2;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.bigkoo.convenientbanner.R;
import com.bigkoo.convenientbanner.holder.Holder;
import com.socks.library.KLog;

import java.util.List;

/**
 * Created by Sai on 15/7/29.
 */
public class LoopPageAdapter<T> extends PagerAdapter {
    protected List<T> mDatas;
    protected ViewHolderCreator holderCreator;
    //    private View.OnClickListener onItemClickListener;
    private boolean canLoop = true;
    private LoopViewPager viewPager;
    private final int MULTIPLE_COUNT = 300;


    public int toRealPosition(int position) {
        int realCount = getRealCount();
        if (realCount == 0) return 0;
        int realPosition = position % realCount;
        return realPosition;
    }

    @Override
    public int getCount() {
        return canLoop ? getRealCount() * MULTIPLE_COUNT : getRealCount();
    }

    public int getRealCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int realPosition = toRealPosition(position);
        View view = getView(realPosition, null, container);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        int realPosition = toRealPosition(position);
        View view = (View) mDatas.get(realPosition % mDatas.size());
        ((ViewPager) container).removeView(view);
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        int position = viewPager.getCurrentItem();
        if (position == 0) {
            position = viewPager.getFristItem();
        } else if (position == getCount() - 1) {
            position = viewPager.getLastItem();
        }
        try {
            viewPager.setCurrentItem(position, false);
        } catch (IllegalStateException e) {
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void setCanLoop(boolean canLoop) {
        this.canLoop = canLoop;
    }

    public void setViewPager(LoopViewPager viewPager) {
        this.viewPager = viewPager;
    }

    public LoopPageAdapter(ViewHolderCreator holderCreator, List<T> datas) {
        this.holderCreator = holderCreator;
        this.mDatas = datas;
    }

    public View getView(int position, View view, ViewGroup container) {
        Holder holder = null;
        if (view == null) {
            holder = (Holder) holderCreator.createHolder();
            view = holder.createView(container.getContext());
            view.setTag(R.id.cb_item_tag, holder);
        } else {
            holder = (Holder<T>) view.getTag(R.id.cb_item_tag);
        }
        if (mDatas != null && !mDatas.isEmpty())
            holder.UpdateUI(container.getContext(), position, mDatas.get(position));
        return view;
    }


    private View mCurrentView;

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        mCurrentView = (View) object;
        KLog.e("mCurrentView = " + mCurrentView);
        super.setPrimaryItem(container, position, object);
    }

    public View getPrimaryItem() {
        return mCurrentView;
    }
}
