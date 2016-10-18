package com.abings.baby.ui.aboutme;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * Created by HaomingXu on 2016/5/25.
 */
public class NoScrollExpandable extends ExpandableListView {
    public NoScrollExpandable(Context context) {
        super(context);
    }

    public NoScrollExpandable(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollExpandable(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NoScrollExpandable(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
