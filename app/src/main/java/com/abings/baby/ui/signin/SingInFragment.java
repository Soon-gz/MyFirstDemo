package com.abings.baby.ui.signin;


import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.abings.baby.R;
import com.abings.baby.WineApplication;
import com.abings.baby.data.model.SignInHistoryModel;
import com.abings.baby.ui.adapter.ABOnRVItemClickListener;
import com.abings.baby.ui.base.BaseActivity;
import com.abings.baby.ui.base.BaseFragment;
import com.abings.baby.widget.refreshlayout.BGARefreshLayout;
import com.abings.baby.widget.refreshlayout.BGAStickinessRefreshViewHolder;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;

/**
 * A simple {@link Fragment} subclass.
 */
public class SingInFragment extends BaseFragment implements ABOnRVItemClickListener, SingInFrgMvpView, BGARefreshLayout.BGARefreshLayoutDelegate{


    @Inject
    SingInFrgPrecenter mPrecenter;

    @Bind(R.id.sign_history_recyclerview_refresh)
    BGARefreshLayout mRefreshLayout;
    @Bind(R.id.sign_history_recyclerview_data)
    RecyclerView mDataRv;

    private SignRecyclerViewAdapterInfoList adapter;
    private List<SignInHistoryModel> mDatas;
    private String leave = "0";
    private String datetime;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_sing_in;
    }

    @Override
    protected void iniInjector() {
        ((BaseActivity) getActivity()).activityComponent().inject(this);
        mPrecenter.attachView(this);
    }

    @Override
    protected void onFirstUserVisible() {
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        leave = getArguments().getString("leave");
        datetime = getArguments().getString("datetime");
        mPrecenter.loadAttendSearchData(WineApplication.getInstance().getNowClass().getPk_grade_class_id(),WineApplication.getInstance().getFk_school_id(),datetime,leave);
        iniBgaRefreshLayout();
    }

    public void onEventMainThread(SingFrEventClick eventClick) {
        datetime = eventClick.getdatetime();
        mPrecenter.loadAttendSearchData(WineApplication.getInstance().getNowClass().getPk_grade_class_id(),WineApplication.getInstance().getFk_school_id(),eventClick.getdatetime(),leave);
    }

    private void iniBgaRefreshLayout() {
        mRefreshLayout.setDelegate(this);
        BGAStickinessRefreshViewHolder stickinessRefreshViewHolder = new BGAStickinessRefreshViewHolder(getActivity(), true);
        stickinessRefreshViewHolder.setStickinessColor(R.color.colorPrimary);
        stickinessRefreshViewHolder.setRotateImage(R.mipmap.bga_refresh_stickiness);
        stickinessRefreshViewHolder.setRefreshViewBackgroundColorRes(R.color.white);
        mRefreshLayout.setRefreshViewHolder(stickinessRefreshViewHolder);
    }

    private void iniAdapter() {
        if (adapter == null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            mDataRv.setLayoutManager(layoutManager);
            adapter = new SignRecyclerViewAdapterInfoList(mDataRv, getActivity());
            adapter.setOnRVItemClickListener(this);
            mDataRv.setAdapter(adapter);
        }
    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

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

    }

    @Override
    public void onTokenError() {

    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {

    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        mPrecenter.loadAttendSearchData(WineApplication.getInstance().getNowClass().getPk_grade_class_id(),WineApplication.getInstance().getFk_school_id(),datetime,leave);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}
