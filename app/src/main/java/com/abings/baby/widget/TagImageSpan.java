package com.abings.baby.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.text.style.ImageSpan;

/**
 * 作者：黄斌 on 2016/2/20 18:58
 * 说明：
 */
public class TagImageSpan extends ImageSpan {
    public int expandWidth = 0; //设置之后可能会出现显示不全的问题，可通过TextView的 padding解决
    public int expandHeight = 0;//设置之后可能会出现显示不全的问题，可通过TextView的 padding解决

    public TagImageSpan() {
        this( 0, 0);
    }

    public TagImageSpan( int expandWidth, int expandHeight) {
        super(getShape());
        this.expandWidth = expandWidth;
        this.expandHeight = expandHeight;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        int width = getWidth(text, start, end, paint);
        int hight = getHeight(paint);
        getDrawable().setBounds(0, 0, width, hight);
        float bgX = x - (expandWidth * 0.5F); //使得在水平方向居中
        int bgBottom = bottom + expandHeight / 2;//使得在垂直方向居中
        super.draw(canvas, text, start, end, bgX, top, y, bgBottom, paint);
        canvas.drawText(text.subSequence(start, end).toString(), x, y, paint);
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        return getWidth(text, start, end, paint);
    }

    /**
     * 计算span的宽度
     *
     * @param text
     * @param start
     * @param end
     * @param paint
     * @return
     */
    private int getWidth(CharSequence text, int start, int end, Paint paint) {
        return Math.round(paint.measureText(text, start, end)) + expandWidth;
    }

    /**
     * 计算span的高度
     *
     * @param paint
     * @return
     */
    private int getHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.ascent) + expandHeight;
    }

    /**
     * 生成shape 可以通过xml实现
     * @return
     */
    private static GradientDrawable getShape() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(10);
        drawable.setColor(Color.parseColor("#d8d8d8"));
        drawable.setStroke(1, Color.parseColor("#b2b2b2"));
        return drawable;
    }
}
