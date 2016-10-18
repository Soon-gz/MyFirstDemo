package com.abings.baby.ui.home2;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;


public class LoopViewPager extends ViewPager {
    OnPageChangeListener mOuterPageChangeListener;
    private LoopPageAdapter mAdapter;

    private boolean isCanScroll = true;
    private boolean canLoop = true;
    private boolean isFirstMove = false;

    public void setAdapter(PagerAdapter adapter, boolean canLoop) {
        mAdapter = (LoopPageAdapter) adapter;
        mAdapter.setCanLoop(canLoop);
        mAdapter.setViewPager(this);
        super.setAdapter(mAdapter);
//        setOffscreenPageLimit(mAdapter.getRealCount() - 1);
        setCurrentItem(getFristItem(), false);
    }

    public int getFristItem() {
        return canLoop ? mAdapter.getRealCount() : 0;
    }

    public int getLastItem() {
        return mAdapter.getRealCount() - 1;
    }

    public boolean isCanScroll() {
        return isCanScroll;
    }

    public void setCanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }

    private float oldX = 0, newX = 0;
    private static final float sens = 5;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN://按下如果‘仅’作为‘上次坐标’，不妥，因为可能存在左滑，motionValue大于0
                // 的情况（来回滑，只要停止坐标在按下坐标的右边，左滑仍然能滑过去）
                oldX = ev.getX();
                break;
            case MotionEvent.ACTION_UP:
                newX = ev.getX();
                Log.i("TAG00","新位置："+newX+"，旧位置："+oldX);
                if (isScrolling) {
                    if (newX > oldX) {
                        // 递减，向右侧滑动
                        right = true;
                        left = false;
                        Log.i("TAG00", "《-----向左");
                    } else if (newX < oldX) {
                        // 递减，向右侧滑动
                        right = false;
                        left = true;
                        Log.i("TAG00", "向右------》");
                    } else if (newX == oldX) {
                        right = left = false;
                        Log.i("TAG00", "没移动-----");
                    }
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }
//
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        if (isCanScroll) return super.onInterceptTouchEvent(ev);
//        else return false;
//    }

    public void setIsCanLeftScroll(boolean isCanLeftScroll) {
        this.isCanLeftScroll = isCanLeftScroll;
    }

    private boolean isCanLeftScroll = false;
    /**
     * 上一次x坐标
     */
    private float beforeX;

    //-------------------------------------------
    //-----禁止左滑-------左滑：上一次坐标 > 当前坐标


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isCanScroll) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN://按下如果‘仅’作为‘上次坐标’，不妥，因为可能存在左滑，motionValue大于0
                    // 的情况（来回滑，只要停止坐标在按下坐标的右边，左滑仍然能滑过去）
                    beforeX = ev.getX();
                    oldX = ev.getX();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float motionValue = ev.getX() - beforeX;
                    if (!isCanLeftScroll) {
                        if (motionValue < 0) {//禁止左滑
                            return false;
                        }
                    }
                    beforeX = ev.getX();//手指移动时，再把当前的坐标作为下一次的‘上次坐标’，解决上述问题
                    break;
                default:
                    break;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    public LoopPageAdapter getAdapter() {
        return mAdapter;
    }

    public int getRealItem() {
        return mAdapter != null ? mAdapter.toRealPosition(super.getCurrentItem()) : 0;
    }

    public Object getCurItem() {
        return mAdapter.mDatas != null ? mAdapter.mDatas.get(getRealItem()) : null;
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mOuterPageChangeListener = listener;
    }


    public LoopViewPager(Context context) {
        super(context);
        init();
    }

    public LoopViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        super.setOnPageChangeListener(onPageChangeListener);
    }

    private boolean left = false;
    private boolean right = false;
    private boolean isScrolling = false;
    private int lastValue = -1;
    private ChangeViewCallback changeViewCallback = null;
    private OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
        private float mPreviousPosition = -1;

        @Override
        public void onPageSelected(int position) {
            int realPosition = mAdapter.toRealPosition(position);
            if (mPreviousPosition != realPosition) {
                mPreviousPosition = realPosition;
                if (mOuterPageChangeListener != null) {
                    mOuterPageChangeListener.onPageSelected(realPosition);

                }
                if (changeViewCallback != null) {
                    changeViewCallback.getCurrentPageIndex(realPosition);
                }
            }
        }
private Object obj =  new Object();
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            synchronized (obj) {
                int realPosition = position;
                if (mOuterPageChangeListener != null) {
                    if (realPosition != mAdapter.getRealCount() - 1) {
                        mOuterPageChangeListener.onPageScrolled(realPosition, positionOffset, positionOffsetPixels);
                    } else {
                        if (positionOffset > .5) {
                            mOuterPageChangeListener.onPageScrolled(0, 0, 0);
                        } else {
                            mOuterPageChangeListener.onPageScrolled(realPosition, 0, 0);
                        }
                    }
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                isScrolling = true;
            } else {
                isScrolling = false;
            }
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                if (changeViewCallback != null) {
                    changeViewCallback.changeView(left, right);
                }
                right = left = false;
            }
            if (mOuterPageChangeListener != null) {
                mOuterPageChangeListener.onPageScrollStateChanged(state);
            }
        }
    };

    public boolean isCanLoop() {
        return canLoop;
    }

    public void setCanLoop(boolean canLoop) {
        this.canLoop = canLoop;
        if (canLoop == false) {
            setCurrentItem(getRealItem(), false);
        }
        if (mAdapter == null) return;
        mAdapter.setCanLoop(canLoop);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 得到是否向右侧滑动
     *
     * @return true 为右滑动
     */
    public boolean getMoveRight() {
        return right;
    }

    /**
     * 得到是否向左侧滑动
     *
     * @return true 为左做滑动
     */
    public boolean getMoveLeft() {
        return left;
    }

    /**
     * 滑动状态改变回调
     *
     * @author zxy
     */
    public interface ChangeViewCallback {
        /**
         * 切换视图 ？决定于left和right 。
         *
         * @param left
         * @param right
         */
        public void changeView(boolean left, boolean right);

        public void getCurrentPageIndex(int index);
    }

    /**
     * set ...
     *
     * @param callback
     */
    public void setChangeViewCallback(ChangeViewCallback callback) {
        changeViewCallback = callback;
    }
}
