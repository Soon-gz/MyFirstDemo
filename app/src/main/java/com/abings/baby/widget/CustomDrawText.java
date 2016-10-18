package com.abings.baby.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.abings.baby.R;

/**
 * 作者：黄斌 on 2016/2/28 11:07
 * 说明：
 */
public class CustomDrawText extends View {

    /**
     * 文本
     */
    private String mTitleText;
    /**
     * 文本的颜色
     */
    private int mTitleTextColor;
    /**
     * 文本的大小
     */
    private int mTitleTextSize;

    /**
     * 绘制时控制文本绘制的范围
     */
    private Rect mBound;
    private Paint mPaint;

    public CustomDrawText(Context context) {
        this(context, null);
    }


    public CustomDrawText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomDrawText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomDrawText, defStyle, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.CustomDrawText_DrawTextContext:
                    mTitleText = a.getString(attr);
                    break;
                case R.styleable.CustomDrawText_DrawTextColor:
                    // 默认颜色设置为黑色
                    mTitleTextColor = a.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CustomDrawText_DrawTextSize:
                    // 默认设置为16sp，TypeValue也可以把sp转化为px
                    mTitleTextSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;

            }

        }
        a.recycle();

        /**
         * 获得绘制文本的宽和高
         */
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // mPaint.setColor(mTitleTextColor);
        mBound = new Rect();
        mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mBound);
    }

    public void  setTitleText(String text){
        mTitleText = text;
    }


    private Bitmap bitmap;
    @Override
    protected void onDraw(Canvas canvas)
    {
        Paint paint = new Paint();
        paint.setTextSize(mTitleTextSize);
        Rect rect = new Rect();
        paint.getTextBounds(mTitleText,0,mTitleText.length(),rect);
        if (bitmap == null) {
            bitmap =  Bitmap.createBitmap(rect.width(),rect.height(), Bitmap.Config.ARGB_4444);

            Canvas ca = new Canvas(bitmap);
            paint.setColor(Color.YELLOW);
            ca.drawRect(0, 0, bitmap.getWidth(), bitmap.getHeight(), mPaint);

            paint.setColor(Color.RED);
            Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
            int baseline = rect.top + (rect.bottom - rect.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
           // ca.drawText("11月", 0, 0, paint);
            paint.setTextAlign(Paint.Align.CENTER);
            ca.drawText(mTitleText, rect.centerX(), baseline, paint);

        }

        Matrix matrix = new Matrix();
        // 计算缩放比例
        float scaleWidth = (float) (rect.width()/getMeasuredWidth());
        float scaleHeight = (float) (rect.width()/getMeasuredHeight() );
        matrix.postScale(scaleWidth, scaleHeight);
        canvas.drawBitmap(Bitmap.createBitmap(bitmap, 0, 0, getMeasuredWidth(), getMeasuredHeight(), matrix, true), 0, 0, null);
/*        Rect targetRect = new Rect(0, 0, getMeasuredWidth(), getMeasuredHeight());
        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);*/

 /*       float baseX = 0;
        float baseY = 0;
        mPaint.setColor(Color.RED);
        mPaint.setTextSize(mTitleTextSize);
        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
        int baseline = targetRect.top + (targetRect.bottom - targetRect.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
        mPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(mTitleText, targetRect.centerX(), baseline, mPaint);*/

        //canvas.drawText(mTitleText, getWidth() / 2 - mBound.width() / 2, getHeight() / 2 + mBound.height() / 2, mPaint);
        //mPaint.setTextAlign(Paint.Align.CENTER);
       // canvas.drawText(mTitleText, fontMetrics.centerX(), baseline, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = 0;
        int height = 0;

        /**
         * 设置宽度
         */
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        switch (specMode)
        {
            case MeasureSpec.EXACTLY:// 明确指定了
                width = getPaddingLeft() + getPaddingRight() + specSize;
                break;
            case MeasureSpec.AT_MOST:// 一般为WARP_CONTENT
                width = getPaddingLeft() + getPaddingRight() + mBound.width();
                break;
        }

        /**
         * 设置高度
         */
        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        switch (specMode)
        {
            case MeasureSpec.EXACTLY:// 明确指定了
                height = getPaddingTop() + getPaddingBottom() + specSize;
                break;
            case MeasureSpec.AT_MOST:// 一般为WARP_CONTENT
                height = getPaddingTop() + getPaddingBottom() + mBound.height();
                break;
        }

        setMeasuredDimension(width, height);
    }


}
