package com.abings.baby.ui.message.center.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abings.baby.R;
import com.abings.baby.WineApplication;
import com.abings.baby.data.model.MessageItem;
import com.abings.baby.data.model.MessageSendItem;
import com.abings.baby.data.remote.RetrofitUtils;
import com.abings.baby.ui.base.BaseActivity;
import com.abings.baby.ui.message.unread.UnreadActivity;
import com.abings.baby.utils.TLog;
import com.bumptech.glide.Glide;
import com.socks.library.KLog;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by huangbin on 16/2/17.
 */
public class MessageDetailActivity extends BaseActivity implements MessageDetailMvpView {

    @Inject
    MessageDetailPresenter presenter;
    @Bind(R.id.text_title)
    TextView title;
    @Bind(R.id.text_subtitle)
    TextView textSubtitle;
    @Bind(R.id.text_content)
    TextView textContent;
    @Bind(R.id.search_icon)
    View search_icon;
    @Bind(R.id.unread_count)
    TextView unread_count;
    @Bind(R.id.content_photo)
    ImageView content_photo;
    @Bind(R.id.content_person)
    TextView content_person;

    private MessageItem mDetail;
    private MessageSendItem mDetail2;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_message_detail;
    }

    @Override
    protected void iniInjector() {
        activityComponent().inject(this);
        presenter.attachView(this);
    }

    @Override
    protected boolean toggleOverridePendingTransition() {
        return false;
    }

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return null;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);  //隐藏标题
        if (getIntent().hasExtra("message")) {
            Serializable objects = getIntent().getSerializableExtra("message");
            if (objects instanceof MessageItem) {
                mDetail = (MessageItem) objects;
                if (mDetail != null) {
                    content_person.setText(mDetail.getSender_name());
                    title.setText(mDetail.getSubject());
                    textSubtitle.setText(mDetail.getCreate_datetime().replace("-", "/"));
                    textContent.setText(mDetail.getContent());
                    presenter.messageReaded(mDetail.getPk_message_id());
                    unread_count.setVisibility(View.INVISIBLE);
                    search_icon.setVisibility(View.INVISIBLE);
                    Glide.with(this).load(RetrofitUtils.BASE_TEACHER_PHOTO_URL + (mDetail.getPhoto())).placeholder(R.drawable.image_onload).error(R.drawable.msg_content).dontAnimate()
                            .thumbnail(0.1f).into(content_photo);
                }
            }
            if (objects instanceof MessageSendItem) {
                mDetail2 = (MessageSendItem) objects;
                if (mDetail2 != null) {
                    Glide.with(this).load(RetrofitUtils.BASE_TEACHER_PHOTO_URL + WineApplication.getInstance().getPhoto_url()).placeholder(R.drawable.image_onload).error(R.drawable.msg_content).dontAnimate()
                            .thumbnail(0.1f).into(content_photo);
                    content_person.setText(mDetail2.getSender_name());
                    KLog.e(mDetail2.toString());
                    KLog.e(mDetail2.toString());
                    title.setText(mDetail2.getSubject());
                    textSubtitle.setText(mDetail2.getCreate_datetime().split("\\.")[0].replace("-", "/"));
                    textContent.setText(mDetail2.getContent());
//                    presenter.messageReaded(mDetail2.getPk_message_id());
                    if (mDetail2.getTOTAL_UNREADED().equals("")) {
                        unread_count.setVisibility(View.INVISIBLE);
                        search_icon.setVisibility(View.INVISIBLE);
                    } else {
                        unread_count.setText("未浏览" + mDetail2.getTOTAL_UNREADED() + "人");
                        unread_count.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MessageDetailActivity.this, UnreadActivity.class);
                                intent.putExtra("MSI", mDetail2);
                                startActivity(intent);
                            }
                        });
                    }
                }
            }

        }

    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }

    @Override
    public boolean bindEvents() {
        return false;
    }

    @Override
    public void showData(List data, boolean canNext) {
    }

    @Override
    public void showLoadingProgress(boolean show) {
    }

    @Override
    public void showMessage(String mes) {
    }

    @Override
    public void showError(String mes) {
    }

    @Override
    public void onTokenError() {

    }

    @OnClick(R.id.btn_back)
    void toBack() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
