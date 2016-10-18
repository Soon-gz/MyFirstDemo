package com.abings.baby.ui.publish;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.abings.baby.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/30.
 */
public class GridAdapter extends BaseAdapter {
    private ArrayList<String> listUrls;
    private LayoutInflater inflater;
    private Context mContext;
    public static String ADD = "add";

    public GridAdapter(Context context, ArrayList<String> listUrls) {
        this.listUrls = listUrls;
        if (listUrls.size() == 7) {
            listUrls.remove(listUrls.size() - 1);
        }
        mContext = context;
        inflater = LayoutInflater.from(mContext);
    }

    public int getCount() {
        return listUrls.size();
    }

    @Override
    public String getItem(int position) {
        return listUrls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.grid_item_image, parent, false);
            holder.image = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final String path = listUrls.get(position);
        if (path.equals(ADD)) {
            holder.image.setImageResource(R.drawable.public_add);
        } else {
            Glide.with(mContext).load(path).placeholder(R.drawable.image_onload).error(R.drawable.image_empty_failed)
                    .centerCrop().crossFade().into(holder.image);
        }
        return convertView;
    }

    public class ViewHolder {
        public ImageView image;
    }
}
