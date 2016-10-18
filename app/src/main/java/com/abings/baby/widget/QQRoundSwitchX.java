package com.abings.baby.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.abings.baby.R;


public class QQRoundSwitchX extends View {

    private static final int SELECT_COLOR = 0xffFEFEFE;
    private static final int NO_SELECT_COLOR = 0xffFA5746;

    /* 边框的宽度相对于高的比例 */
    private static final float BOARD_WIDTH_RATE = 1F/20F;
    /* 字体大小相对于高度的比例 */
    private static final float TEXT_SIZE_RATE = 3F/7F;
    /* 宽相对于高的比例 */
    private static final float WIDTH_RATE = 4;
    /* 文字起始x位置相对于当前长方形的比例 */
    private static final float TEXT_START_X_RATE = 1F/4F;

    private Paint mBoardPaint;
    private TextPaint mTextPaint;
    private Paint mFillPaint;

    private int angle;
    private int selectColor;
    private int noSelectColor;
    private String leftText;
    private String rightText;

    private float mWidth;
    private float mHeight;

    private float mBoardWidth;// mBoardWidth = mHeight*BOARD_WIDTH_RATE
    private float mRadius;
    /* 圆弧到长方形边框的距离 */
    private float mArcToBoard;
    /* 中间长方形的长*/
    private float mRectWidth;

    private boolean leftSelected;

    private RectF boardArcRectF;
    private RectF arcRectF;
    private RectF rectRectF;

    public QQRoundSwitchX(Context context) {
        this(context, null);
    }

    public QQRoundSwitchX(Context context, AttributeSet attrs) {
        super(context, attrs);

        //初始化，获得xml属性值设置
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.QQRoundSwitchX);
        selectColor = ta.getColor(R.styleable.QQRoundSwitchX_selected_color, SELECT_COLOR);
        noSelectColor = ta.getColor(R.styleable.QQRoundSwitchX_no_selected_color, NO_SELECT_COLOR);
        leftText = ta.getString(R.styleable.QQRoundSwitchX_left_text);
        rightText = ta.getString(R.styleable.QQRoundSwitchX_right_text);
        angle = ta.getInteger(R.styleable.QQRoundSwitchX_angle, 180);
        ta.recycle();
        //初始化画笔
        initPaints();

        //初始化三个矩形
        boardArcRectF = new RectF();
        arcRectF = new RectF();
        rectRectF = new RectF();
    }

    private void initPaints() {

        mBoardPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//抗锯齿
        mBoardPaint.setStyle(Paint.Style.STROKE);//空心
        mBoardPaint.setColor(selectColor);//设置画笔颜色

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);//抗锯齿
        mTextPaint.setTextAlign(Paint.Align.CENTER);//文字居中

        mFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//抗锯齿
        mFillPaint.setStyle(Paint.Style.FILL);//实心
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);

        if (mHeight*WIDTH_RATE <= mWidth) {
            mWidth = mHeight*WIDTH_RATE;
        }else {
            mHeight = mWidth/WIDTH_RATE;
        }
        int width = (int)mWidth;
        int height = (int)mHeight;
        setMeasuredDimension(width, height);
        mWidth = width;
        mHeight = height;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mBoardWidth = mHeight*BOARD_WIDTH_RATE;
        mTextPaint.setTextSize(mHeight*TEXT_SIZE_RATE);
        mBoardPaint.setStrokeWidth(mBoardWidth);

        double radian = angle*1f/180*Math.PI/2;
        mRadius = (float) (mHeight/2/Math.sin(radian));
        mArcToBoard = (float) (mRadius - mRadius*Math.cos(radian));
        mRectWidth = mWidth - mArcToBoard*2;

        float halfBoardWidth = mBoardWidth*1f/2;

        arcRectF.left = mBoardWidth;
        arcRectF.right = mRadius*2-mBoardWidth;
        arcRectF.top = -(mRadius-mHeight/2-mBoardWidth);
        arcRectF.bottom = mHeight-arcRectF.top;

        boardArcRectF.left = halfBoardWidth;
        boardArcRectF.right = mRadius*2-halfBoardWidth;
        boardArcRectF.top = -(mRadius-mHeight/2-halfBoardWidth);
        boardArcRectF.bottom = mHeight-arcRectF.top+halfBoardWidth;

        rectRectF.left = mArcToBoard-1;
        rectRectF.right = mWidth/2+1;
        rectRectF.top = mBoardWidth-1;
        rectRectF.bottom = mHeight-mBoardWidth+1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 先画左边
        drawOneSide(canvas, leftSelected, true);
        canvas.save();
        canvas.rotate(180, mWidth/2, mHeight/2);
        drawOneSide(canvas, !leftSelected, false);
        canvas.restore();
    }

    private void drawOneSide(Canvas canvas, boolean isSelected, boolean isLeft) {
        // 先画边框
        // 先画圆弧边框
        canvas.drawArc(boardArcRectF, 180-angle/2, angle, false, mBoardPaint);
        // 再画上下两条横线
        canvas.drawLine(mArcToBoard, mBoardWidth/2, mWidth/2, mBoardWidth/2, mBoardPaint);
        canvas.drawLine(mArcToBoard, mHeight-mBoardWidth/2, mWidth/2, mHeight-mBoardWidth/2, mBoardPaint);
        // 再填充内部颜色
        // 先填充圆弧
        if (isSelected) {
            mFillPaint.setColor(selectColor);
        }else {
            mFillPaint.setColor(noSelectColor);
        }
        canvas.drawArc(arcRectF, 180-angle*1f/2, angle, false, mFillPaint);
        // 再填充方形
        canvas.drawRect(rectRectF, mFillPaint);

        // 最后写字
        String text;
        if (isLeft) {
            text = leftText;
        }else {
            canvas.save();
            canvas.rotate(180, mWidth/4, mHeight/2);
            text = rightText;
        }
        if (isSelected) {
            mTextPaint.setColor(noSelectColor);
        }else {
            mTextPaint.setColor(selectColor);
        }
        canvas.drawText(text,
                mWidth/4,
                mHeight/2-(mTextPaint.descent()+mTextPaint.ascent())/2,
                mTextPaint);
        if (!isLeft) {
            canvas.restore();
        }
    }
}
