package com.abings.baby.ui.waterfall;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.abings.baby.R;
import com.abings.baby.WineApplication;
import com.abings.baby.data.model.WaterFallGridItem;
import com.abings.baby.ui.base.BaseActivity;
import com.abings.baby.ui.publish.PublishActivity;
import com.abings.baby.ui.waterfall.photoviewpagedetail.PhotoViewpageDetailActivity;
import com.abings.baby.utils.ProgressDialogHelper;
import com.abings.baby.utils.StringUtils;
import com.tonicartos.superslim.LayoutManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by huangbin on 16/2/17.
 */
public class WaterfallActivity extends BaseActivity implements WaterfallMvpView{

    @Inject
    WaterfallPresenter waterfallPresenter;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.btn_back)
    ImageView btnBack;
    @Bind(R.id.title_center)
    TextView titleCenter;

    private boolean isFirstIn = true;
    private int NowPageNum = 1;
    private WaterfallGridAdapter mAdapter;
    private ArrayList<String> imgIds;
    private List<WaterFallGridItem> lists;
    private int temp = 0;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_waterfall;
    }

    @Override
    protected void iniInjector() {
        activityComponent().inject(this);
        waterfallPresenter.attachView(this);
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
        imgIds = new ArrayList<>();
        lists = new ArrayList<>();
//        if (WineApplication.getInstance().isTeacher()) {
//            titleCenter.setText(WineApplication.getInstance().getNowClass().getClass_name());
//        } else {
//            titleCenter.setText(WineApplication.getInstance().getBaby().getName());
//        }
        waterfallPresenter.loadData(NowPageNum, WineApplication.getInstance().getClassId(), true);
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
        iniAdapter();
        if ((NowPageNum == 1) || (mAdapter.getDatas() == null)) {
            mAdapter.setDatas(data);
        } else {
            mAdapter.addMoreDatas(data);
            if(canNext){
                showToast("已经全部加载完毕");
            }
        }
    }

    @Override
    public void showLoadingProgress(boolean show) {
        if (show) {
            ProgressDialogHelper.getInstance().showProgressDialog(this, "加载中...");
        } else {
            ProgressDialogHelper.getInstance().hideProgressDialog();
        }
    }

    @Override
    public void showMessage(String mes) {

    }

    @Override
    public void showError(String mes) {

    }

    @Override
    public void onTokenError() {
        startLoginActivity();
    }

    private void iniAdapter() {
        if (mAdapter == null) {
            mAdapter = new WaterfallGridAdapter(this);
            mAdapter.setOnItemClickLitener(new WaterfallGridAdapter.OnItemClickLitener() {
                @Override
                public void onItemClick(View view, int position) {
                    if (mAdapter.getItem(position).isHeader) {
                        return;
                    }
                    if (StringUtils.isEmpty(mAdapter.getItem(position).getItem().getFk_grade_class_id()) && StringUtils.isEmpty(mAdapter.getItem(position).getItem().getImage())) {
                        Intent inten = new Intent(WaterfallActivity.this, PublishActivity.class);
                        startActivity(inten);
                        return;
                    }

                    Intent inten = new Intent(WaterfallActivity.this, PhotoViewpageDetailActivity.class);
                    inten.putStringArrayListExtra("waterfall", imgIds);
                    for (int i = 0; i < lists.size(); i++) {
                        if (lists.get(i).getImage().equals(mAdapter.getDatas().get(position).getItem().getImage())){
                            temp = i;
                            break;
                        }
                    }
                    inten.putExtra("position", temp);
                    startActivity(inten);

                }
            });
            mRecyclerView.setLayoutManager(new LayoutManager(this));
            mRecyclerView.setAdapter(mAdapter);
            setHeaderMode(LayoutManager.LayoutParams.HEADER_ALIGN_START);
            setMarginsFixed(true);
            setHeadersSticky(true);
            setHeadersOverlaid(false);

            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                private int mY = -1;
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && mY > 0) {
                        int lastVisiblePosition = ((LayoutManager)recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                        int count = mAdapter.getItemCount();
                        if (lastVisiblePosition + 1 == count) {
                            NowPageNum++;
                            waterfallPresenter.loadData(NowPageNum, WineApplication.getInstance().getClassId(), false);
                        }
                    }
                }
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    mY = dy;
                }
            });
        }
    }

    @OnClick(R.id.btn_back)
    void toBack() {
        finish();
    }

    private int mHeaderDisplay;

    private boolean mAreMarginsFixed;


    public void setHeadersOverlaid(boolean areHeadersOverlaid) {
        mHeaderDisplay = areHeadersOverlaid ? mHeaderDisplay | LayoutManager.LayoutParams.HEADER_OVERLAY :
                mHeaderDisplay & ~LayoutManager.LayoutParams.HEADER_OVERLAY;
        mAdapter.setHeaderDisplay(mHeaderDisplay);
    }

    public void setHeadersSticky(boolean areHeadersSticky) {
        mHeaderDisplay = areHeadersSticky ? mHeaderDisplay | LayoutManager.LayoutParams.HEADER_STICKY :
                mHeaderDisplay & ~LayoutManager.LayoutParams.HEADER_STICKY;
        mAdapter.setHeaderDisplay(mHeaderDisplay);
    }

    public void setMarginsFixed(boolean areMarginsFixed) {
        mAreMarginsFixed = areMarginsFixed;
        mAdapter.setMarginsFixed(areMarginsFixed);
    }

    public void setHeaderMode(int mode) {
        mHeaderDisplay = mode | (mHeaderDisplay & LayoutManager.LayoutParams.HEADER_OVERLAY) | (mHeaderDisplay &
                LayoutManager.LayoutParams.HEADER_STICKY);
        mAdapter.setHeaderDisplay(mHeaderDisplay);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        waterfallPresenter.detachView();
    }

    @Override
    public void setImgS(List<WaterFallGridItem> list) {
        if (list != null){
            if (lists == null){
                lists = list;
            }else{
                lists.addAll(list);
            }
            Log.i("TAG00", "当前s数量："+list.size());
            for (int i = 0; i < list.size(); i++) {
                imgIds.add(list.get(i).getPk_image_news_image_id());
            }
        }

    }
}
