package com.abings.baby.ui.message.center;

import com.abings.baby.data.model.MessageSendItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2016/9/20.
 */
public class SendMessageItemComparator implements Comparator<MessageSendItem> {
    @Override
    public int compare(MessageSendItem item1, MessageSendItem item2) {
        String time1= item1.getCreate_datetime().split("\\.")[0];
        String time2= item2.getCreate_datetime().split("\\.")[0];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
        Date date1 = null,date2 = null;
        try {
            date1 = sdf.parse(time1);
            date2 = sdf.parse(time2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //如果此 Date2 在 Date1 参数之前，则返回小于 0 的值；如果此 Date2 在 Date1 参数之后，则返回大于 0 的值。
       return date2.compareTo(date1);
    }
}
