package com.abings.baby.ui.message.unread;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.abings.baby.R;
import com.abings.baby.data.model.MessageSendItem;
import com.abings.baby.data.model.UnreadContact;
import com.abings.baby.data.remote.RetrofitUtils;
import com.abings.baby.ui.adapter.ABRecyclerViewAdapter;
import com.abings.baby.ui.adapter.ABViewHolderHelper;
import com.abings.baby.utils.StringUtils;
import com.abings.baby.utils.TLog;
import com.abings.baby.widget.UnReadProgress;
import com.bumptech.glide.Glide;

/**
 * Created by Administrator on 2016/2/1.
 */
public class RecyclerViewAdapterUnread extends ABRecyclerViewAdapter<UnreadContact> {

    private boolean mIsIgnoreChange = true;
    private Context context;

    public RecyclerViewAdapterUnread(RecyclerView recyclerView, Context context) {
        super(recyclerView, R.layout.recyler_unread_item);
        this.context = context;
    }

    @Override
    public void setItemChildListener(final ABViewHolderHelper viewHolderHelper) {
    }

    public int String2Number(String string) {
        if (StringUtils.isEmpty(string)) {
            return 0;
        } else {
            return Integer.parseInt(string);
        }
    }

    @Override
    protected void fillData(ABViewHolderHelper viewHolderHelper, int position, final UnreadContact model) {
        if(null!=model.getBABY_NAME()){
            viewHolderHelper.setText(R.id.content_person, model.getBABY_NAME());
            if (!model.getRelation_desc().isEmpty()) {
                viewHolderHelper.setText(R.id.content_person_rel, model.getRelation_desc());
            } else {
                viewHolderHelper.getTextView(R.id.content_person_rel).setVisibility(View.GONE);
            }
            Glide.with(context).load(RetrofitUtils.BASE_BABY_PHOTO_URL + model.getBABY_PHOTO()).placeholder(R.drawable.image_onload).error(R.drawable.msg_content).dontAnimate()
                    .thumbnail(0.1f).into(viewHolderHelper.getImageView(R.id.content_photo));
//        viewHolderHelper.getView(R.id.phone_icon).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + model.getMobile_phone()));
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
//            }
//        });
        }else{
            viewHolderHelper.getTextView(R.id.content_person_rel).setVisibility(View.GONE);
            viewHolderHelper.setText(R.id.content_person, model.getName());
            Glide.with(context).load(RetrofitUtils.BASE_TEACHER_PHOTO_URL + model.getPhoto()).placeholder(R.drawable.image_onload).error(R.drawable.msg_content).dontAnimate()
                    .thumbnail(0.1f).into(viewHolderHelper.getImageView(R.id.content_photo));
        }
        viewHolderHelper.setText(R.id.content_phone, model.getMobile_phone());
    }

    public boolean isIgnoreChange() {
        return mIsIgnoreChange;
    }
}
