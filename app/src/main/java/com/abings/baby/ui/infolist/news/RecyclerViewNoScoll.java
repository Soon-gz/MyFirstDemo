package com.abings.baby.ui.infolist.news;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by HaomingXu on 2016/5/8.
 */
public class RecyclerViewNoScoll extends RecyclerView {
    private boolean haveScrollbar = false;
    public RecyclerViewNoScoll(Context context) {
        super(context);
    }

    public RecyclerViewNoScoll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewNoScoll(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setHaveScrollbar(boolean haveScrollbar) {
        this.haveScrollbar = haveScrollbar;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (haveScrollbar == false) {
            int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
