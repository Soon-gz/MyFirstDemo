package com.abings.baby.ui.home2.item;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.abings.baby.R;
import com.abings.baby.data.model.MainListItem;
import com.abings.baby.ui.adapter.ABRecyclerViewAdapter;
import com.abings.baby.ui.adapter.ABViewHolderHelper;
import com.abings.baby.ui.infolist.InfoListActivity;
import com.abings.baby.utils.StringUtils;

/**
 * Created by Administrator on 2016/2/1.
 */
public class RecyclerViewAdapterInfos extends ABRecyclerViewAdapter<MainListItem> {

    private boolean mIsIgnoreChange = true;
    private Context mContext;
    private String[] types = {"1","2","3","4"};
    public RecyclerViewAdapterInfos(Context context, RecyclerView recyclerView) {
        super(recyclerView, R.layout.recyler_item_infotype);
        mContext = context;
    }

    @Override
    public void setItemChildListener(final ABViewHolderHelper viewHolderHelper) {
    }

    @Override
    protected void fillData(ABViewHolderHelper viewHolderHelper, final int position, MainListItem model) {
        viewHolderHelper.setImageResource(R.id.info_image, model.getImageRes());
        viewHolderHelper.setText(R.id.typeName, model.getTypeName());
        viewHolderHelper.setText(R.id.content, model.getContent());
        viewHolderHelper.setText(R.id.count, model.getCount() + "");

        if (StringUtils.isEmpty(model.getFk_grade_class_id())) {
            viewHolderHelper.setTextColorRes(R.id.content, R.color.text_grey);
        } else {
            viewHolderHelper.setTextColorRes(R.id.content, R.color.text_light_black);
        }
//        TextView textViewContent = viewHolderHelper.getTextView(R.id.content);
//        TextView textViewTypeName = viewHolderHelper.getTextView(R.id.content);
//        textViewContent.setTextSize(16);
//        textViewTypeName.setTextSize(16);

        viewHolderHelper.getView(R.id.content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("3".equals(gettype(position))){
                    //接口还在更新中。2016/7/28
//                    Intent intent = new Intent(mContext, Exercise.class);
//                    mContext.startActivity(intent);
                    Toast.makeText(mContext, "程序员正在拼命开发中,敬请期待...", Toast.LENGTH_SHORT).show();
                }else{
                    Intent inten = new Intent(mContext, InfoListActivity.class);
                    inten.putExtra("Tag", gettype(position));
                    mContext.startActivity(inten);
                }
            }
        });
    }

    public String gettype(int position){
        return types[position];
    }

    public boolean isIgnoreChange() {
        return mIsIgnoreChange;
    }
}
