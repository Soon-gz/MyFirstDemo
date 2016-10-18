package com.abings.baby.ui.home2;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.abings.baby.ui.adapter.HomeItemFragmentAdapter;
import com.socks.library.KLog;

/**
 * 可以设置禁止滑动的 ViewPager(单向禁止：左滑动)
 * 核心方法：setScrollble()
 *
 * @author alan
 */
public class CustomViewpager extends ViewPager {
    private boolean left = false;
    private boolean right = false;
    private boolean isScrolling = false;
    private int lastValue = -1;
    private int mPreviousPosition = 0;
    private ChangeViewCallback changeViewCallback = null;
    private HomeItemFragmentAdapter mAdapter;
    private boolean canLoop = true;

    public CustomViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomViewpager(Context context) {
        super(context);
        init();
    }

    public void setAdapter(HomeItemFragmentAdapter adapter, boolean canLoop) {
        mAdapter = (HomeItemFragmentAdapter) adapter;
        mAdapter.setCanLoop(canLoop);
        super.setAdapter(mAdapter);
        setCurrentItem(getLastItem(), false);
        setIsCanLeftScroll(false);
    }

    public int getFristItem() {
        return canLoop ? mAdapter.getRealCount() : 0;
    }

    public int getLastItem() {
        return mAdapter.getRealCount() - 1;
    }

    public int getRealItem() {
        return mAdapter != null ? mAdapter.toRealPosition(super.getCurrentItem()) : 0;
    }

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
     * init method .
     */
    private void init() {
        setOnPageChangeListener(listener);
    }

    /**
     * listener ,to get move direction .
     */
    public OnPageChangeListener listener = new OnPageChangeListener() {
        @Override
        public void onPageScrollStateChanged(int arg0) {
            if (arg0 == ViewPager.SCROLL_STATE_DRAGGING) {
                isScrolling = true;
            } else {
                isScrolling = false;
            }

//            KLog.e("meityitianViewPager  onPageScrollStateChanged : arg0:" + arg0);
//            if (arg0 == ViewPager.SCROLL_STATE_SETTLING)
            if (arg0 == ViewPager.SCROLL_STATE_IDLE) {
//                KLog.e("meityitianViewPager  onPageScrollStateChanged  direction left ? " + left);
//                KLog.e("meityitianViewPager  onPageScrollStateChanged  direction right ? " + right);
                //notify ....
                if (changeViewCallback != null) {

//                    if (!isCanLeftScroll) {
//                        left = false;
//                    }

                    changeViewCallback.changeView(left, right);
                    if (left && isCanLeftScroll) {
                        KLog.e("left");
                        if (getCurrentItem() == getAdapter().getCount() - 1) {
                            setCurrentItem(0, false);
                        }
                    } else if (right) {
                        KLog.e("right");
                        if (getCurrentItem() == 0) {
                            setCurrentItem(getAdapter().getCount() - 1, false);
                        }
                    }
                }
                right = left = false;
            }

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            if (isScrolling) {
                if (lastValue > arg2) {
                    // 递减，向右侧滑动
                    right = true;
                    left = false;
                } else if (lastValue < arg2) {
                    // 递减，向右侧滑动
                    right = false;
                    left = true;
                } else if (lastValue == arg2) {
                    right = left = false;
                }
            }
            KLog.e("meityitianViewPager onPageScrolled  last :arg2  ," + lastValue + ":" + arg2);
            lastValue = arg2;
        }

        @Override
        public void onPageSelected(int position) {
            KLog.e("meityitianViewPager onPageScrolled  last :mPreviousPosition  = " + mPreviousPosition + " " +
                    "，position = " +
                    position);
            int realPosition = mAdapter.toRealPosition(position);
            if (mPreviousPosition != realPosition) {
                mPreviousPosition = realPosition;
                if (changeViewCallback != null) {
                    changeViewCallback.getCurrentPageIndex(position);
                }
            }
        }
    };

    public boolean isScrollble() {
        return isCanScroll;
    }

    /**
     * 设置 是否可以滑动
     *
     * @param isCanScroll
     */
    public void setScrollble(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }

    private boolean isCanScroll = true;

    public boolean isCanLeftScroll() {
        return isCanLeftScroll;
    }

    public void setIsCanLeftScroll(boolean isCanLeftScroll) {
        this.isCanLeftScroll = isCanLeftScroll;
    }

    public boolean isCanRightScroll() {
        return isCanRightScroll;
    }

    public void setIsCanRightScroll(boolean isCanRightScroll) {
        this.isCanRightScroll = isCanRightScroll;
    }

    private boolean isCanLeftScroll = true;
    private boolean isCanRightScroll = true;
    /**
     * 上一次x坐标
     */
    private float beforeX;

    //----------禁止左右滑动------------------
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isCanScroll) {
            return super.onTouchEvent(ev);
        } else {
            return false;
        }

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (isCanScroll) {
            return super.onInterceptTouchEvent(arg0);
        } else {
            return false;
        }

    }

    //-------------------------------------------
    //-----禁止左滑-------左滑：上一次坐标 > 当前坐标
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isCanScroll) {
            return super.dispatchTouchEvent(ev);
        } else {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN://按下如果‘仅’作为‘上次坐标’，不妥，因为可能存在左滑，motionValue大于0
                    // 的情况（来回滑，只要停止坐标在按下坐标的右边，左滑仍然能滑过去）
                    beforeX = ev.getX();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float motionValue = ev.getX() - beforeX;
                    if (!isCanLeftScroll) {
                        if (motionValue < 0) {//禁止左滑
                            return true;
                        }
                    } else if (!isCanRightScroll) {
                        if (motionValue > 0) {//禁止右滑
                            return true;
                        }
                    }
                    beforeX = ev.getX();//手指移动时，再把当前的坐标作为下一次的‘上次坐标’，解决上述问题
                    break;
                default:
                    break;
            }
            return super.dispatchTouchEvent(ev);
        }

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