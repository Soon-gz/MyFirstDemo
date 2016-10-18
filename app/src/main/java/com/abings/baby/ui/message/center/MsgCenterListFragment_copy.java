package com.abings.baby.ui.message.center;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abings.baby.R;
import com.abings.baby.data.model.MessageItem;
import com.abings.baby.ui.adapter.ABOnRVItemClickListener;
import com.abings.baby.ui.base.BaseActivity;
import com.abings.baby.ui.base.BaseFragment;
import com.abings.baby.ui.login.LoginActivity;
import com.abings.baby.ui.message.center.detail.MessageDetailActivity;
import com.abings.baby.widget.refreshlayout.BGARefreshLayout;
import com.abings.baby.widget.refreshlayout.BGAStickinessRefreshViewHolder;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者：黄斌 on 2016/2/27 18:47
 * 说明：
 */
public class MsgCenterListFragment_copy extends BaseFragment implements ABOnRVItemClickListener, MsgCenterMvpView, BGARefreshLayout.BGARefreshLayoutDelegate {

    @Inject
    MsgCenterPresenter msgCenterPresenter;
    @Bind(R.id.rv_recyclerview_data)
    RecyclerView mDataRv;
    //    @Bind(R.id.school_friend_member_search_input)
//    EditText mSearchInput;
    int mUnReadCount = 0;
    @Bind(R.id.date)
    TextView date;
    @Bind(R.id.count)
    TextView count;
    @Bind(R.id.tips)
    LinearLayout tips;
    @Bind(R.id.rl_recyclerview_refresh)
    BGARefreshLayout mRefreshLayout;

    private RecyclerViewAdapterInfoList adapter;

    public static final MsgCenterListFragment_copy newInstance(Bundle bl) {
        MsgCenterListFragment_copy f = new MsgCenterListFragment_copy();
        f.setArguments(bl);
        return f;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_message_center;
    }

    @Override
    protected void iniInjector() {
        ((BaseActivity) getActivity()).activityComponent().inject(this);
        msgCenterPresenter.attachView(this);
    }

    @Override
    protected void onFirstUserVisible() {
        msgCenterPresenter.loadData(false);
    }

    private void iniAdapter() {
        if (adapter == null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            mDataRv.setLayoutManager(layoutManager);
            adapter = new RecyclerViewAdapterInfoList(mDataRv, getActivity());
            adapter.setOnRVItemClickListener(this);
            mDataRv.setAdapter(adapter);
        }
    }

    @Override
    protected View getLoadingTargetView() {
        return mDataRv;
    }

    private List<MessageItem> mDatas;

    @Override
    protected void initViewsAndEvents() {
        iniBgaRefreshLayout();
    }


    @Override
    protected void onUserVisible() {
        msgCenterPresenter.loadData(false);
    }

    @Override
    protected void onUserInvisible() {
        tips.setVisibility(View.GONE);
    }

    @Override
    public boolean bindEvents() {
        return false;
    }

    @Override
    public void showData(List data, boolean canNext) {
        iniAdapter();
        mDatas = data;
        adapter.setDatas(mDatas);
        mRefreshLayout.endRefreshing();
    }

    @Override
    public void showLoadingProgress(boolean show) {
    }

    @Override
    public void showMessage(String mes) {
        Toast.makeText(getActivity(), mes, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String mes) {
        showMessage(mes);
    }

    @Override
    public void onTokenError() {
        getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    @OnClick(R.id.tips)
    public void onClick() {
        mDataRv.scrollToPosition(0);
    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        Intent intent = new Intent(getActivity(), MessageDetailActivity.class);
        intent.putExtra("message", mDatas.get(position));
        getActivity().startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void updateUnReadCount(int count) {
        mUnReadCount = count;
    }

    private void iniBgaRefreshLayout() {
        mRefreshLayout.setDelegate(this);
        BGAStickinessRefreshViewHolder stickinessRefreshViewHolder = new BGAStickinessRefreshViewHolder(getActivity(), true);
        stickinessRefreshViewHolder.setStickinessColor(R.color.colorPrimary);
        stickinessRefreshViewHolder.setRotateImage(R.mipmap.bga_refresh_stickiness);
        stickinessRefreshViewHolder.setRefreshViewBackgroundColorRes(R.color.white);
        mRefreshLayout.setRefreshViewHolder(stickinessRefreshViewHolder);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        msgCenterPresenter.loadData(false);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}
