package com.abings.baby.ui.exercise;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.abings.baby.R;
import com.abings.baby.ui.adapter.ABRecyclerViewAdapter;
import com.abings.baby.ui.adapter.ABViewHolderHelper;

/**
 * Created by HaomingXu on 2016/7/11.
 */
public class ExerciseRVAdapter extends ABRecyclerViewAdapter<ExerciseModel> {

    private Context context;

    public ExerciseRVAdapter(RecyclerView recyclerView,Context context) {
        super(recyclerView, R.layout.exercise_recycler_item);
        this.context = context;
    }

    @Override
    protected void fillData(ABViewHolderHelper viewHolderHelper, int position, ExerciseModel model) {
        viewHolderHelper.setText(R.id.subject,model.getTitle());
        viewHolderHelper.setText(R.id.exercise_address,"活动地点："+model.getAddress());
        viewHolderHelper.setText(R.id.exercise_time,"活动时间："+model.getTime());

        if (model.isRead()){
            viewHolderHelper.getTextView(R.id.subject).setTextAppearance(context,R.style.MessageUnreadText);
            viewHolderHelper.getTextView(R.id.exercise_address).setTextAppearance(context,R.style.MessageUnreadText);
            viewHolderHelper.getTextView(R.id.exercise_time).setTextAppearance(context,R.style.MessageUnreadText);
        }else{
            viewHolderHelper.getTextView(R.id.subject).setTextAppearance(context, R.style.MessageReadedText);
            viewHolderHelper.getTextView(R.id.exercise_address).setTextAppearance(context, R.style.MessageReadedText);
            viewHolderHelper.getTextView(R.id.exercise_time).setTextAppearance(context, R.style.MessageReadedText);
        }
    }
}
