package com.abings.baby.ui.contacts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.abings.baby.R;
import com.abings.baby.data.model.ClassTeacherItem;
import com.abings.baby.data.model.Contact;
import com.abings.baby.ui.adapter.ABOnRVItemClickListener;
import com.abings.baby.ui.base.BaseActivity;
import com.abings.baby.ui.base.BaseFragment;
import com.abings.baby.ui.contacts.detail.ContactDetailActivity;
import com.abings.baby.ui.contacts.detail.ContactDetailListAdapter;
import com.abings.baby.ui.login.LoginActivity;
import com.abings.baby.widget.DividerItemDecoration;
import com.abings.baby.widget.IndexView;
import com.abings.baby.widget.refreshlayout.BGARefreshLayout;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/1/18.
 */
public class ContactsFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate,
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


    private List<Contact> mDatas;

    private RecyclerIndexAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        contactsPresenter.detachView();
        ButterKnife.unbind(this);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_contacts;
    }

    @Override
    protected void iniInjector() {
        ((BaseActivity) getActivity()).activityComponent().inject(this);
        contactsPresenter.attachView(this);
    }

    @Override
    protected void onFirstUserVisible() {
        mAdapter = new RecyclerIndexAdapter(mDataRv);
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
        contactsPresenter.loadContacts(NowPageNum, true);
        mAdapter.setDatas(mDatas);
        mDataRv.setAdapter(mAdapter);
    }

    @Override
    protected View getLoadingTargetView() {
        return content_layout;
    }


    @Override
    protected void initViewsAndEvents() {
        mIndexView.setTipTv(mTipTv);
        mDataRv.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mLayoutManager = new LinearLayoutManager(getActivity());
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
                ArrayList<Contact> temp = new ArrayList<>(mDatas);
                for (Contact data : mDatas) {
                    if (data.getName().contains(s) || data.getPinyin().contains(s)) {
                    } else {
                        temp.remove(data);
                    }
                }
                if (mAdapter != null) {
                    mAdapter.setDatas(temp);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    protected void onUserVisible() {
//        contactsPresenter.loadContacts(NowPageNum, true);
    }

    @Override
    protected void onUserInvisible() {

    }


    @Override
    public boolean bindEvents() {
        return true;
    }


    private int NowPageNum = 1;

    @Override
    public void showData(final List data, final boolean canNext) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                KLog.e("data.size() = " + data.size());
                mDatas = data;
                if ((NowPageNum == 1) || (mAdapter.getDatas() == null)) {
                    mAdapter.setDatas(data);
                    mRefreshLayout.endRefreshing();
                } else {
                    mAdapter.addMoreDatas(data);
                    mRefreshLayout.endLoadingMore();
                }
                mRefreshLayout.setIsShowLoadingMoreView(canNext);
            }
        });
    }


    @Override
    public void showLoadingProgress(boolean show) {
        toggleShowLoading(show, getResources().getString(R.string.ese_loading_datas));
    }

    @Override
    public void showMessage(String mes) {

    }

    @Override
    public void showError(String mes) {

    }

    @Override
    public void onTokenError() {
        getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
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
            Intent inten = new Intent(getActivity(), ContactDetailActivity.class);
            inten.putExtra("contact", mAdapter.getItem(position).getUserContact());
            startActivity(inten);
        }

    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        NowPageNum = 1;
        contactsPresenter.loadContacts(NowPageNum, false);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        NowPageNum++;
        contactsPresenter.loadContacts(NowPageNum, false);
        return true;
    }

    @Override
    public void showSchoolContacts(List<Contact> schoolContacts) {

    }
}
