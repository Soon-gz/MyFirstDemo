package com.abings.baby.ui.aboutme;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;

import com.abings.baby.R;
import com.abings.baby.ui.aboutme.user.BottomPickerDateDialog;
import com.abings.baby.ui.home2.CustomActionSheetDialog;
import com.flyco.dialog.listener.OnOperItemClickL;

/**
 * Created by Administrator on 2016/7/24.
 */
public class BottomDialogUtils {

    public static CustomActionSheetDialog getBottomListDialog(Activity activity,String[] items,@NonNull  final OnItemClickListener onItemClickListener) {
        final CustomActionSheetDialog dialog = new CustomActionSheetDialog(activity, items, null);
        dialog.isTitleShow(false).isCancelShow(false)
                .itemTextColor(activity.getResources().getColor(R.color.ese_white))
                .itemPressColor(activity.getResources().getColor(R.color.ese_orange))
                .lvBgColor(activity.getResources().getColor(R.color.baby_orange))
                .show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                onItemClickListener.onItemClick(parent,view,position,id);
                dialog.dismiss();
            }
        });
        return dialog;
    }

    public interface OnItemClickListener{
        void onItemClick(AdapterView<?> parent, View view, int position, long id);
    }

    /**
     *
     * @param activity
     * @param birthday 2016-07-25;如果为null,则默认今天的日期
     * @param onDateChangedListener
     * @return
     */
    public static BottomPickerDateDialog getBottomDatePickerDialog(Activity activity,String birthday, BottomPickerDateDialog.BottomOnDateChangedListener onDateChangedListener) {
        int year = -1;
        int month = -1;
        int day = -1;

        if(birthday!=null&&birthday.contains("-") ){
            String[] dates = birthday.split("-");
            if(dates.length==3){
                year = Integer.valueOf(dates[0]);
                month = Integer.valueOf(dates[1]);
                day = Integer.valueOf(dates[2]);
            }
        }

        final BottomPickerDateDialog dialog = new BottomPickerDateDialog(activity,null,year,month,day,onDateChangedListener);
        dialog.show();
        return dialog;
    }
}
