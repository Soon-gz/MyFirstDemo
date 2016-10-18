package com.abings.baby.ui.message.center;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abings.baby.R;
import com.abings.baby.WineApplication;
import com.abings.baby.data.model.MessageSendItem;
import com.abings.baby.data.remote.RetrofitUtils;
import com.abings.baby.ui.adapter.ABRecyclerViewAdapter;
import com.abings.baby.ui.adapter.ABViewHolderHelper;
import com.abings.baby.ui.message.unread.UnreadActivity;
import com.abings.baby.utils.DateUtil;
import com.abings.baby.utils.StringUtils;
import com.abings.baby.utils.TLog;
import com.abings.baby.widget.UnReadProgress;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2016/2/1.
 */
public class RecyclerViewAdapterInfoList_send extends ABRecyclerViewAdapter<MessageSendItem> {

    private boolean mIsIgnoreChange = true;
    private Context context;
    private UnReadProgress unReadProgress;

    public RecyclerViewAdapterInfoList_send(RecyclerView recyclerView, Context context) {
        super(recyclerView, R.layout.recyler_item_message_send);
        this.context = context;
    }

    @Override
    public void setItemChildListener(final ABViewHolderHelper viewHolderHelper) {
    }

    public int String2Number(String string){
        if (StringUtils.isEmpty(string)){
            return 0;
        }else{
            return Integer.parseInt(string);
        }
    }

    @Override
    protected void fillData(ABViewHolderHelper viewHolderHelper, int position, final MessageSendItem model) {
        TextView person=(TextView) viewHolderHelper.getView(R.id.content_person);
        ImageView pic=(ImageView) viewHolderHelper.getView(R.id.content_photo);
        if(model.getIsSchool()!=null) {
            person.setText("校内消息");
            pic.setImageResource(R.drawable.msg_school);
        }else{
            person.setText(WineApplication.getInstance().getNowClass().getClass_name());
            pic.setImageResource(R.drawable.msg_class);
        }
        viewHolderHelper.setText(R.id.content, model.getContent());
        String time= model.getCreate_datetime().split("\\.")[0];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
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
        ((UnReadProgress)viewHolderHelper.getView(R.id.progress_unread)).setReadAndUnReadNum(String2Number(model.getTOTAL_READED()),String2Number(model.getTOTAL_UNREADED()));
        viewHolderHelper.getView(R.id.progress_unread).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(model.getTOTAL_UNREADED().equals("")){
                    Toast.makeText(context,R.string.readed,Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent=new Intent(context,UnreadActivity.class);
                    intent.putExtra("MSI",model);
                    context.startActivity(intent);
                }
            }
        });
//        Glide.with(context).load(RetrofitUtils.BASE_TEACHER_PHOTO_URL + WineApplication.getInstance().getPhoto_url()).placeholder(R.drawable.image_onload).error(R.drawable.msg_content).dontAnimate()
//                .thumbnail(0.1f).into(viewHolderHelper.getImageView(R.id.content_photo));
    }

    public boolean isIgnoreChange() {
        return mIsIgnoreChange;
    }
}
