package com.abings.baby.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abings.baby.R;

/**
 * Created by shuwen on 2016/9/5.
 */
public class UnReadProgress extends LinearLayout {

    private TextView readed;
    private TextView unread;
    //设置已读未读数
    private int readNum = 1;
    private int unreadNum = 1;
    //设置已读未读权重
    private int readWeight = 1;
    private int unReadWeight = 1;
    //控件的高度(默认100像素)
    private int height = 100;
    //最大公约数
    private int maxCommonDivisor = 1;

    public UnReadProgress(Context context) {
        this(context, null);
    }

    public UnReadProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UnReadProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.unreadprogressview, this);
        initViews();
        setWeght(readWeight, unReadWeight);
    }


    //设置权重
    public void setWeght(int readWeight,int unreadWeight){
        LayoutParams paramsRead = new LayoutParams(0,LayoutParams.MATCH_PARENT,readWeight);
        LayoutParams paramsUnread = new LayoutParams(0,LayoutParams.MATCH_PARENT,unreadWeight);
        readed.setLayoutParams(paramsRead);
        unread.setLayoutParams(paramsUnread);
    }

    private void initViews() {
        readed = (TextView) findViewById(R.id.read);
        unread = (TextView) findViewById(R.id.unread);
    }

    //设置已读,未读数
    public void setReadAndUnReadNum(int readNum,int  unreadNum) {
        this.readNum = readNum;
        this.unreadNum = unreadNum;
        maxCommonDivisor = maxCommonDivisor(readNum,unreadNum);
        readNumisZero(readNum);
        unreadNumisZero(unreadNum);
        readed.setText(String.valueOf(readNum));
        readed.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        unread.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        unread.setText(String.valueOf(unreadNum));
        readWeight = readNum/maxCommonDivisor;
        unReadWeight = unreadNum/maxCommonDivisor;
        setWeght(readWeight,unReadWeight);
    }

    public void readNumisZero(int readNum){
        if (readNum == 0){
            unread.setGravity(Gravity.CENTER);
        }
    }

    public void unreadNumisZero(int unreadNum){
        if (unreadNum == 0){
            readed.setGravity(Gravity.CENTER);
        }
    }

    //设置控件的高度（以像素Px为单位）
    public void setHeight(int height) {
        this.height = height;
    }

    //单独设置已读数量
    public void setReadNum(int readNum) {
        this.readNum = readNum;
    }

    //单独设置未读数量
    public void setUnreadNum(int unreadNum) {
        this.unreadNum = unreadNum;
    }

    //单独设置最大公约数
    public void setMaxCommonDivisor(int maxCommonDivisor) {
        this.maxCommonDivisor = maxCommonDivisor;
    }

    //设置已读的权重
    public void setReadWeight(int readWeight) {
        this.readWeight = readWeight;
        setWeght(readWeight,unReadWeight);
    }

    //设置未读的权重
    public void setUnReadWeight(int unReadWeight) {
        this.unReadWeight = unReadWeight;
        setWeght(readWeight,unReadWeight);
    }


    // 递归法求最大公约数
    public int maxCommonDivisor(int m, int n) {
        if (m == 0 || n == 0){
            return 1;
        }
        if (m < n) {// 保证m>n,若m<n,则进行数据交换
            int temp = m;
            m = n;
            n = temp;
        }
        if (m % n == 0) {// 若余数为0,返回最大公约数
            return n;
        } else { // 否则,进行递归,把n赋给m,把余数赋给n
            return maxCommonDivisor(n, m % n);
        }
    }
}
