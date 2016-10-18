package com.abings.baby.ui.message.unread;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.abings.baby.R;
import com.abings.baby.data.model.Contact;
import com.abings.baby.data.model.MessageSendItem;
import com.abings.baby.data.model.UnreadContact;
import com.abings.baby.ui.adapter.ABOnRVItemClickListener;
import com.abings.baby.ui.base.BaseActivity;
import com.abings.baby.ui.contacts.ActivityRecyclerIndexAdapter;
import com.abings.baby.ui.contacts.detail.ContactDetailActivity;
import com.abings.baby.utils.ProgressDialogHelper;
import com.abings.baby.widget.DividerItemDecoration;
import com.abings.baby.widget.IndexView;
import com.abings.baby.widget.refreshlayout.BGARefreshLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/7.
 */
public class UnreadActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate,
        UnreadMvpView, ABOnRVItemClickListener {

    @Inject
    UnreadPresenter unreadPresenter;
    @Bind(R.id.rl_recyclerview_refresh)
    BGARefreshLayout mRefreshLayout;
    @Bind(R.id.user_recyclerview)
    RecyclerView mDataRv;
    @Bind(R.id.tv_recyclerindexview_topc)
    TextView mTopcTv;


    @Bind(R.id.content_layout)
    View content_layout;
    @Bind(R.id.title_center)
    TextView titleCenter;
    private int NowPageNum = 1;

    private List<UnreadContact> mDatas;

    private RecyclerViewAdapterUnread mAdapter;
    private LinearLayoutManager mLayoutManager;
    private String sendMsg;
    private MessageSendItem messageSendItem;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_unread;
    }

    @Override
    protected void iniInjector() {
        activityComponent().inject(this);
        unreadPresenter.attachView(this);
    }

    @Override
    protected boolean toggleOverridePendingTransition() {
        return true;
    }

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.LEFT;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        titleCenter.setText("未浏览");
        mDatas = new ArrayList<>();
//        mDataRv.addItemDecoration(new DividerItemDecoration(this,3));
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mDataRv.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerViewAdapterUnread(mDataRv,this);
        mAdapter.setOnRVItemClickListener(this);
        Serializable object=getIntent().getSerializableExtra("MSI");
        messageSendItem=(MessageSendItem)object;
        if(messageSendItem.getIsSchool()!=null) {
            unreadPresenter.loadContacts(messageSendItem.getCreate_datetime(), "1");
        }else{
            unreadPresenter.loadContacts(messageSendItem.getCreate_datetime(),"2" );
        }
        mAdapter.setDatas(mDatas);
        mDataRv.setAdapter(mAdapter);
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
    public void showData(final List data, final boolean canNext) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mAdapter.getDatas() == null) {
                    mAdapter.setDatas(data);
                } else {
                    mAdapter.addMoreDatas(data);
                }
                mDatas = mAdapter.getDatas();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatas.clear();
        unreadPresenter.detachView();
    }

    @Override
    public void showLoadingProgress(boolean show) {
        if (show) {
            ProgressDialogHelper.getInstance().showProgressDialog(this, "正在载入");
        } else {
            ProgressDialogHelper.getInstance().hideProgressDialog();
        }

    }

    @Override
    public void showMessage(String mes) {
        Toast.makeText(UnreadActivity.this, mes, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String mes) {
        showToast(mes);
    }

    @Override
    public void onTokenError() {
        startLoginActivity();
    }

    @OnClick(R.id.btn_back)
    void toBack() {
        finish();
    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" +  mDatas.get(position).getMobile_phone()));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
       startActivity(intent);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return true;
    }

}
