package com.abings.baby.ui.finder;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.abings.baby.R;
import com.abings.baby.data.model.NewsListItem;
import com.abings.baby.ui.adapter.ABOnRVItemClickListener;
import com.abings.baby.ui.base.BaseActivity;
import com.abings.baby.ui.base.BaseFragment;
import com.abings.baby.ui.infolist.news.NewsActivity;
import com.abings.baby.utils.StringUtils;
import com.abings.baby.widget.DividerItemDecoration;
import com.abings.baby.widget.dialog.animation.BounceEnter.BounceTopEnter;
import com.abings.baby.widget.dialog.animation.SlideExit.SlideBottomExit;
import com.abings.baby.widget.dialog.dialog.widget.MaterialDialog;
import com.abings.baby.widget.refreshlayout.BGARefreshLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 作者：黄斌 on 2016/2/21 15:23
 * 说明：
 */
public class FinderFragment extends BaseFragment implements FinderMvpView, ABOnRVItemClickListener, BGARefreshLayout.BGARefreshLayoutDelegate {

    @Inject
    FinderPresenter finderPresenter;
    @Bind(R.id.main_lay)
    View main_lay;
    @Bind(R.id.school_friend_member_search_input)
    EditText schoolFriendMemberSearchInput;
    @Bind(R.id.rv_recyclerview_data)
    RecyclerView rvRecyclerviewData;
    @Bind(R.id.rl_recyclerview_refresh)
    BGARefreshLayout mRefreshLayout;

    private MaterialDialog dialog;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_finder;
    }

    @Override
    protected void iniInjector() {
        ((BaseActivity) getActivity()).activityComponent().inject(this);
        finderPresenter.attachView(this);
    }

    @Override
    protected void onFirstUserVisible() {
    }

    @Override
    protected View getLoadingTargetView() {
        return main_lay;
    }

    @Override
    protected void initViewsAndEvents() {
        schoolFriendMemberSearchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_SEARCH){
                    goSearch();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onUserVisible() {
    }

    @Override
    protected void onUserInvisible() {
        schoolFriendMemberSearchInput.setText("");
        if (mAdapter!=null){
            mAdapter.getDatas().clear();
            mAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public boolean bindEvents() {
        return false;
    }

    @Override
    public void showData(List data, boolean canNext) {
        iniAdapter();
        if (mAdapter.getDatas() == null) {
            mAdapter.setDatas(new ArrayList<NewsListItem>());
        }
        if ((NowPageNum == 1) || (mAdapter.getDatas() == null)) {
            mAdapter.setDatas(data);
            mRefreshLayout.endRefreshing();
        } else {
            mAdapter.addMoreDatas(data);
            mRefreshLayout.endLoadingMore();
        }
        mRefreshLayout.setIsShowLoadingMoreView(canNext);
    }

    @Override
    public void showLoadingProgress(boolean show) {
        if (show){
            dialog = new MaterialDialog(getActivity(), R.layout.dialog_progress);
            dialog.setCanceledOnTouchOutside(false);
            dialog.btnNum(1).btnText("取消").title("正在搜索中...").showAnim(new BounceTopEnter())//
                    .dismissAnim(new SlideBottomExit())//
                    .show();
        }else{
            dialog.dismiss();
        }

    }

    @OnClick(R.id.relax)
    public void quxiao(){

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

    private void goSearch(){
        if (!StringUtils.isEmpty(schoolFriendMemberSearchInput.getText().toString())) {
            finderPresenter.search(schoolFriendMemberSearchInput.getText().toString(), NowPageNum, true);
        }else{
            if(mAdapter!=null){
                mAdapter.clear();
                mAdapter.notifyDataSetChanged();
            }
            Toast.makeText(getActivity(),"请输入搜索内容",Toast.LENGTH_SHORT).show();
        }
    }

    private int NowPageNum = 1;
    private FinderRecyclerViewAdapter mAdapter;

    private void iniAdapter() {
        if (mAdapter == null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            rvRecyclerviewData.setLayoutManager(layoutManager);
            rvRecyclerviewData.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
            mAdapter = new FinderRecyclerViewAdapter(rvRecyclerviewData);
            mAdapter.setOnRVItemClickListener(this);
            rvRecyclerviewData.setAdapter(mAdapter);
        }
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        NowPageNum = 1;
        finderPresenter.search(schoolFriendMemberSearchInput.getText().toString(), NowPageNum, false);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        NowPageNum++;
        finderPresenter.search(schoolFriendMemberSearchInput.getText().toString(), NowPageNum, false);
        return true;
    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        Intent inten = new Intent(getActivity(), NewsActivity.class);
        inten.putExtra("pk_image_news_id", mAdapter.getItem(position).getPk_image_news_id());
        inten.putExtra("from_search", true);
        inten.putExtra("title", mAdapter.getItem(position).getContent());
        startActivity(inten);
    }

}
