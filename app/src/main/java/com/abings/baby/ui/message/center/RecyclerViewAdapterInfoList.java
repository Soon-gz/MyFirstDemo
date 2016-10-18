package com.abings.baby.ui.message.center;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.abings.baby.R;
import com.abings.baby.data.model.MessageItem;
import com.abings.baby.data.remote.RetrofitUtils;
import com.abings.baby.ui.adapter.ABRecyclerViewAdapter;
import com.abings.baby.ui.adapter.ABViewHolderHelper;
import com.abings.baby.utils.DateUtil;
import com.abings.baby.utils.TLog;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2016/2/1.
 */
public class RecyclerViewAdapterInfoList extends ABRecyclerViewAdapter<MessageItem> {

    private boolean mIsIgnoreChange = true;
    private Context context;

    public RecyclerViewAdapterInfoList(RecyclerView recyclerView,Context context) {
        super(recyclerView, R.layout.recyler_item_message);
        this.context = context;
    }

    @Override
    public void setItemChildListener(final ABViewHolderHelper viewHolderHelper) {
    }

    @Override
    protected void fillData(ABViewHolderHelper viewHolderHelper, int position, MessageItem model) {

        viewHolderHelper.setText(R.id.content_person, model.getSender_name());
        viewHolderHelper.setText(R.id.content, model.getContent());
        TLog.getInstance().i("json",model.getCreate_datetime());
        String time= model.getCreate_datetime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss", Locale.CHINA);
        Date date=null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm", Locale.CHINA);
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA);
        if(DateUtil.isToday(date.getTime())){
            viewHolderHelper.setText(R.id.time, sdf1.format(date));
        }else{
            viewHolderHelper.setText(R.id.time, sdf2.format(date));
        }
        viewHolderHelper.setText(R.id.subject, model.getSubject());
        Glide.with(context).load(RetrofitUtils.BASE_TEACHER_PHOTO_URL + model.getPhoto()).placeholder(R.drawable.image_onload).error(R.drawable.msg_content).dontAnimate()
                .thumbnail(0.1f).into(viewHolderHelper.getImageView(R.id.content_photo));
        if ("False".equals(model.getReaded())) {
            viewHolderHelper.setVisibility(R.id.read_state, View.VISIBLE);
            viewHolderHelper.setImageResource(R.id.read_state, R.drawable.message_angle_icon);
        } else {
            viewHolderHelper.setVisibility(R.id.read_state, View.INVISIBLE);
        }
    }

    public boolean isIgnoreChange() {
        return mIsIgnoreChange;
    }
}
