package com.abings.baby.ui.aboutme.user;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import com.abings.baby.R;
import com.flyco.dialog.widget.base.BottomBaseDialog;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by zwj on 2016/7/25.
 */
public class BottomPickerDateDialog extends BottomBaseDialog<BottomPickerDateDialog> {
    private Context mContext;
    private BottomOnDateChangedListener mBottomOnDateChangedListener;

    private int mYear = -1;
    private int mMonth = -1;
    private int mDay = -1;

    public BottomPickerDateDialog(Context context, View animateView, int year, int month, int day, BottomOnDateChangedListener bottomOnDateChangedListener) {
        super(context, animateView);
        this.mContext = context;
        this.mYear = year;
        this.mMonth = month-1;
        this.mDay = day;
        this.mBottomOnDateChangedListener = bottomOnDateChangedListener;
    }




    @Override
    public View onCreateView() {

        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_datepicker_aboutme, null);
        DatePicker datePicker = (DatePicker) view.findViewById(R.id.custom_datepicker_view);
        setDatePickerDividerColor(datePicker);
        datePicker.setMaxDate(System.currentTimeMillis());
        datePicker.init(mYear, mMonth, mDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mBottomOnDateChangedListener.onDateChanged(view, year, monthOfYear+1, dayOfMonth);
            }
        });
        return view;
    }

    @Override
    public void setUiBeforShow() {
        if (mYear > 0 && mMonth > 0 && mDay > 0) {
            return;
        }
        Calendar mycalendar = Calendar.getInstance(Locale.CHINA);
        Date mydate = new Date(); //获取当前日期Date对象
        mycalendar.setTime(mydate);////为Calendar对象设置时间为当前日期
        //获取Calendar对象中的年
        mYear = mycalendar.get(Calendar.YEAR);
        //获取Calendar对象中的月
        mMonth = mycalendar.get(Calendar.MONTH);
        //获取这个月的第几天
        mDay = mycalendar.get(Calendar.DAY_OF_MONTH);
    }

    public interface BottomOnDateChangedListener {
        void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth);
    }


    private void setDatePickerDividerColor(DatePicker datePicker) {
        // Divider changing:

        // 获取 mSpinners
        LinearLayout llFirst = (LinearLayout) datePicker.getChildAt(0);

        // 获取 NumberPicker
        LinearLayout mSpinners = (LinearLayout) llFirst.getChildAt(0);
        for (int i = 0; i < mSpinners.getChildCount(); i++) {
            NumberPicker picker = (NumberPicker) mSpinners.getChildAt(i);

            Field[] pickerFields = NumberPicker.class.getDeclaredFields();
            for (Field pf : pickerFields) {
                if (pf.getName().equals("mSelectionDivider")) {
                    pf.setAccessible(true);
                    try {
                        pf.set(picker, new ColorDrawable(mContext.getResources().getColor(R.color.black)));
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (Resources.NotFoundException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }
}
