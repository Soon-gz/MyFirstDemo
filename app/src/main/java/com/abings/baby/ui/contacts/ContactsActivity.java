package com.abings.baby.ui.contacts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.abings.baby.R;
import com.abings.baby.data.model.Contact;
import com.abings.baby.ui.adapter.ABOnRVItemClickListener;
import com.abings.baby.ui.base.BaseActivity;
import com.abings.baby.ui.contacts.detail.ContactDetailActivity;
import com.abings.baby.utils.ProgressDialogHelper;
import com.abings.baby.widget.DividerItemDecoration;
import com.abings.baby.widget.IndexView;
import com.abings.baby.widget.refreshlayout.BGARefreshLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by huangbin on 16/2/19.
 */
public class ContactsActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate,
        ContactsMvpView, ABOnRVItemClickListener {

    @Inject
    ContactsPresenter contactsPresenter;
    @Bind(R.id.rl_recyclerview_refresh)
    BGARefreshLayout mRefreshLayout;
    @Bind(R.id.user_recyclerview)
    RecyclerView mDataRv;
    @Bind(R.id.tv_recyclerindexview_tip)
    TextView mTipTv;
    @Bind(R.id.tv_recyclerindexview_topc)
    TextView mTopcTv;
    @Bind(R.id.indexview)
    IndexView mIndexView;
    @Bind(R.id.school_friend_member_search_input)
    EditText mSearchInput;

    @Bind(R.id.content_layout)
    View content_layout;
    @Bind(R.id.title_center)
    TextView titleCenter;
    private int NowPageNum = 1;

//    显示数据源
    protected List<Contact> mDatas;
    //班级通讯录
    protected List<Contact> mClassContacts = new ArrayList<>();
    //全校通讯录
    protected List<Contact> mSchoolContacts = new ArrayList<>();


    protected ActivityRecyclerIndexAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private String sendMsg;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_contacts;
    }

    @Override
    protected void iniInjector() {
        activityComponent().inject(this);
        contactsPresenter.attachView(this);
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

        mDatas = new ArrayList<>();
        mIndexView.setTipTv(mTipTv);
        mDataRv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mDataRv.setLayoutManager(mLayoutManager);
        mSearchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mDatas == null || mDatas.size() <= 0) {
                    return;
                }
                ArrayList<Contact> temp = new ArrayList<>();
                for (Contact data : mDatas) {
                    if (data.getName().contains(s) || data.getPinyin().contains(s)) {
                        temp.add(data);
                    }
                }
                if (mAdapter != null) {
                    mAdapter.setDatas(temp);
                }
                if (temp.size() == 0){
                    showMessage("搜索“"+s+"”没有结果。");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mAdapter = new ActivityRecyclerIndexAdapter(mDataRv);
        mAdapter.setOnRVItemClickListener(this);
        mIndexView.setOnChangedListener(new IndexView.OnChangedListener() {
            @Override
            public void onChanged(String text) {
                int position = mAdapter.getPositionForSection(text.charAt(0));
                if (position != -1) {
                    // position的item滑动到RecyclerView的可见区域，如果已经可见则不会滑动
                    mLayoutManager.scrollToPosition(position);
                }
            }
        });
//        contactsPresenter.loadSchoolContacts();
        contactsPresenter.loadContacts(NowPageNum, true);
        mAdapter.setDatas(mDatas);
        mDataRv.setAdapter(mAdapter);
        if (getIntent().hasExtra("sendMsg")){
            sendMsg =  getIntent().getStringExtra("sendMsg");
//            groupCheckState = true;
            mAdapter.setIsShowCheckBox(true);
            mAdapter.notifyDataSetChanged();
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
    public void showData(final List data, final boolean canNext) {
        // 获取班级老师联系人执行一次
        // 获取家长联系人执行一次
        mDatas.clear();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mAdapter.getDatas() == null) {
                    mClassContacts.clear();
                    mClassContacts.addAll(data);
                } else {
                    mClassContacts.addAll(data);

                }
                mDatas.addAll(mClassContacts);
                mAdapter.setDatas(mDatas);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatas.clear();
        contactsPresenter.detachView();
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
        Toast.makeText(ContactsActivity.this, mes, Toast.LENGTH_SHORT).show();
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

        if (mAdapter.getItem(position).isTeacher()) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            Uri data = Uri.parse("tel:" + mAdapter.getItem(position).getClassTeacherItem().getMobile_phone());
            intent.setData(data);
            startActivity(intent);
            return;
        }
        if (mAdapter.getItem(position).getUserContact().getImages() != null && mAdapter.getItem(position)
                .getUserContact().getImages().size() > 0) {
            Intent inten = new Intent(this, ContactDetailActivity.class);
            inten.putExtra("contact", mAdapter.getItem(position).getUserContact());
            startActivity(inten);
        } else {
            showToast("无家长联系方式！");
        }

    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
//        NowPageNum = 1;
//        contactsPresenter.loadContacts(NowPageNum, false);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
//        NowPageNum++;
//        contactsPresenter.loadContacts(NowPageNum, false);
        return true;
    }

    @Override
    public void showSchoolContacts(List<Contact> schoolContacts) {
        mSchoolContacts = schoolContacts;
    }

//    private boolean groupCheckState = false;

//    @OnClick(R.id.group_msg)
//    public void onClick() {
//        if (groupCheckState) {
//            if (mAdapter.getCheckCount() > 0) {
//                ArrayList<Contact> contacts = new ArrayList<>();
//                HashMap<Integer, Boolean> map = mAdapter.getIsSelected();
//                for (int i = 0; i < map.size(); i++) {
//                    if (map.get(i)) {
//                        contacts.add(mAdapter.getDatas().get(i));
//                    }
//                }
//                if (contacts.size() > 0) {
//                    Intent intent = new Intent(this, SendMsgActivity.class);
//                    intent.putExtra("contacts", contacts);
//                    intent.putExtra("group_send", true);
//                    startActivity(intent);
//                }
//            }
//            if (sendMsg == null){
//                groupCheckState = false;
//                mAdapter.setIsShowCheckBox(false);
//                mAdapter.notifyDataSetChanged();
//            }else{
//                finish();
//            }
//        } else {
//            groupCheckState = true;
//            mAdapter.setIsShowCheckBox(true);
//            mAdapter.notifyDataSetChanged();
//        }
//    }

}
