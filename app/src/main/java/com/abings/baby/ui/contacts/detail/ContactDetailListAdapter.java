package com.abings.baby.ui.contacts.detail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.abings.baby.R;
import com.abings.baby.WineApplication;
import com.abings.baby.data.model.UserContact;
import com.abings.baby.data.remote.RetrofitUtils;
import com.abings.baby.ui.message.send.SendMsgActivity;
import com.abings.baby.utils.StringUtils;
import com.bumptech.glide.Glide;

public class ContactDetailListAdapter extends BaseAdapter {


    public class MyViewHoler {
        ImageView photo;
        TextView relation;
        TextView phone;
        ImageView sendMsg;
        ImageView call;
    }

    private Context context = null;
    public UserContact userContact = null;

    @SuppressLint("UseSparseArrays")
    public ContactDetailListAdapter(Context context, UserContact userContact) {
        this.context = context;
        this.userContact = userContact;
    }

    @Override
    public int getCount() {
        return userContact.getImages().size();
    }

    @Override
    public Object getItem(int arg0) {
        return userContact.getImages().get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup arg2) {
        MyViewHoler holder = null;

        if (view == null) {
            holder = new MyViewHoler();
            view = LayoutInflater.from(context).inflate(R.layout.dialog_contactlist_item, null);
            holder.photo = (ImageView) view.findViewById(R.id.photo);
            holder.call = (ImageView) view.findViewById(R.id.call);
            holder.relation = (TextView) view.findViewById(R.id.relation);
            holder.phone = (TextView) view.findViewById(R.id.phone);
            holder.sendMsg = (ImageView) view.findViewById(R.id.send_msg);
            view.setTag(holder);

        }
        holder = (MyViewHoler) view.getTag();
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + userContact.getImages().get(position).getMobile_phone());
                intent.setData(data);
                context.startActivity(intent);
            }
        });
        if (!WineApplication.getInstance().isTeacher()){
            holder.sendMsg.setVisibility(View.INVISIBLE);
        }
        holder.sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SendMsgActivity.class);
                intent.putExtra("contact", userContact.getImages().get(position));
                context.startActivity(intent);
            }
        });
        holder.phone.setText(userContact.getImages().get(position).getMobile_phone());
        if (StringUtils.isEmpty(userContact.getImages().get(position).getRelation_desc())){
            holder.relation.setText(userContact.getName() + "的家长");
        }else{
            holder.relation.setText(userContact.getName() + "的" + userContact.getImages().get(position).getRelation_desc());
        }
        Glide.with(context).load(RetrofitUtils.BASE_USER_PHOTO_URL + userContact.getImages().get(position).getPhoto()).placeholder(R.drawable.image_onload).error(R.drawable.left_menu_header_image).dontAnimate().thumbnail
                (0.1f).into(holder.photo);
        return view;
    }

    public void notifyDataSetChanged(UserContact userContact) {
        if (userContact.getImages() != null && userContact.getImages().size() > 0) {
            this.userContact = userContact;
            super.notifyDataSetChanged();
        }

    }

}
