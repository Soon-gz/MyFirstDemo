package com.abings.baby.ui.signin;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.abings.baby.R;
import com.abings.baby.data.model.SignInHistoryModel;
import com.abings.baby.data.remote.RetrofitUtils;
import com.abings.baby.ui.adapter.ABRecyclerViewAdapter;
import com.abings.baby.ui.adapter.ABViewHolderHelper;
import com.abings.baby.utils.DateUtil;
import com.abings.baby.utils.TLog;
import com.bumptech.glide.Glide;

import java.util.Date;

/**
 * Created by Administrator on 2016/2/1.
 */
public class SignRecyclerViewAdapterInfoList extends ABRecyclerViewAdapter<SignInHistoryModel> {

    private Context context;

    public SignRecyclerViewAdapterInfoList(RecyclerView recyclerView, Context context) {
        super(recyclerView, R.layout.recycler_signin_history_itrm);
        this.context = context;
    }

    @Override
    public void setItemChildListener(final ABViewHolderHelper viewHolderHelper) {
    }

    @Override
    protected void fillData(ABViewHolderHelper viewHolderHelper, int position, SignInHistoryModel model) {
        viewHolderHelper.setText(R.id.sign_history_username,model.getUser_name());
        viewHolderHelper.setText(R.id.sign_history_babyname, model.getName());
        viewHolderHelper.setText(R.id.sign_history_phone, model.getMobile_photo());
        if (model.isIn()){
            Date date = DateUtil.getTimeFromString(model.getDatetime(),"yyyy/MM/dd hh:mm:ss");
            String hourminute = DateUtil.getStringFromTime(date,"HH:mm");
            TLog.getInstance().i("签到时间："+hourminute);
            viewHolderHelper.setText(R.id.sign_history_time,hourminute);
            viewHolderHelper.setImageResource(R.id.sign_history_inorout,R.drawable.surein);
        }else{
            viewHolderHelper.setText(R.id.sign_history_time,"");
            viewHolderHelper.setImageResource(R.id.sign_history_inorout,R.drawable.sureout);
        }
        Glide.with(context).load(RetrofitUtils.BASE_USER_PHOTO_URL + model.getUser_photo()).error(R.drawable.msg_content).dontAnimate()
                .thumbnail(0.1f).into(viewHolderHelper.getImageView(R.id.sign_history_head));

    }

}
