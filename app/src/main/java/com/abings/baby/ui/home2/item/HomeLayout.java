package com.abings.baby.ui.home2.item;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.abings.baby.R;
import com.abings.baby.data.model.MainListItem;
import com.abings.baby.data.model.MainNewsItem;
import com.abings.baby.data.model.WaterFallGridItem;
import com.abings.baby.data.remote.RetrofitUtils;
import com.abings.baby.ui.waterfall.WaterfallActivity;
import com.abings.baby.utils.ImageLoaderUtil;
import com.abings.baby.utils.StringUtils;
import com.abings.baby.widget.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/1/18.
 */
public class HomeLayout extends FrameLayout {


    @Bind(R.id.backwards)
    ImageView backwards;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.main_page_weather)
    TextView mainPageWeather;
    @Bind(R.id.main_page_weather_image)
    ImageView mainPageWeatherImage;
    @Bind(R.id.forward)
    ImageView forward;
    @Bind(R.id.banner_splash_pager)
    ImageView convenientBanner;
//    ConvenientBanner convenientBanner;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerView;
    private List<View> views = new ArrayList<>();
    private RecyclerViewAdapterInfos mAdapter;
    private Context mContext;

    public HomeLayout(Context context) {
        super(context);
        mContext = context;
        initViews();
    }

    public HomeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initViews();
    }

    public HomeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initViews();
    }

    protected void initViews() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.framlayout_home, null, false);
        convenientBanner = (ImageView) view.findViewById(R.id.banner_splash_pager);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        addView(view);
        iniList();
    }


    private List mMainImgList = new ArrayList();
    private List<MainNewsItem> mMainNewsList = new ArrayList();
    WaterFallGridItem waterFallGridItem;

    public void updateBanner(List data) {
        if (data != null && data.size() > 0) {
            mMainImgList.clear();
            mMainImgList = data;
        } else {
            mMainImgList.clear();
            mMainImgList.add(new WaterFallGridItem());
        }
        waterFallGridItem = (WaterFallGridItem) mMainImgList.get(0);
        if (!StringUtils.isEmpty(waterFallGridItem.getImage())){
            ImageLoaderUtil.display(RetrofitUtils.BASE_NEWS_SMALL_IMG_URL + waterFallGridItem.getImage(), convenientBanner);
        }else{
            convenientBanner.setImageResource(R.drawable.a);
        }

        convenientBanner.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent(mContext, WaterfallActivity.class);
                mContext.startActivity(inten);
            }
        });
//        convenientBanner.setPages(new CBViewHolderCreator<BannerImageHolderView>() {
//            @Override
//            public BannerImageHolderView createHolder() {
//                return new BannerImageHolderView(new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (mMainImgList == null) {
//                            return;
//                        }
//                        Intent inten = new Intent(mContext, WaterfallActivity.class);
//                        mContext.startActivity(inten);
//                    }
//                });
//            }
//        }, mMainImgList);
//        convenientBanner.setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused});
//        convenientBanner.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
//        convenientBanner.startTurning(5000);
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
    }

    private void iniList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        mAdapter = new RecyclerViewAdapterInfos(mContext, recyclerView);
        recyclerView.setAdapter(mAdapter);

        mMainList.add(new MainListItem("食谱", "今日暂未添加", R.drawable.main_launch));
        mMainList.add(new MainListItem("拓展", "今日暂未添加", R.drawable.main_operation));
        mMainList.add(new MainListItem("活动", "今日暂未添加", R.drawable.main_activity));
        mMainList.add(new MainListItem("动态", "今日暂未添加", R.drawable.main_journal));


        mAdapter.setDatas(mMainList);
    }

    private List<MainListItem> mMainList = new ArrayList<>();

    public void updateList(List data) {
        mMainNewsList = data;
        if (mMainNewsList != null && mMainNewsList.size() > 0) {
            List<MainNewsItem> lunch = new ArrayList();
            List<MainNewsItem> operation = new ArrayList();
            List<MainNewsItem> activity = new ArrayList();
            List<MainNewsItem> journal = new ArrayList();
            for (MainNewsItem item : mMainNewsList) {
                if (item.getFk_tag_id().equals("1")) {
                    lunch.add(item);
                } else if (item.getFk_tag_id().equals("2")) {
                    operation.add(item);
                } else if (item.getFk_tag_id().equals("3")) {
                    activity.add(item);
                } else if (item.getFk_tag_id().equals("4")) {
                    journal.add(item);
                }
            }

            for(MainListItem mli:mMainList)
                mli.Clear();
            if (lunch.size() > 0) {
                mMainList.get(0).setContent(lunch.get(0).getContent());
                mMainList.get(0).setCount(lunch.size());
                mMainList.get(0).setFk_grade_class_id(lunch.get(0).getFk_grade_class_id());
                mMainList.get(0).setFk_tag_id(lunch.get(0).getFk_tag_id());
                mMainList.get(0).setMainNewsItem(lunch.get(0));
            }
            if (operation.size() > 0) {
                mMainList.get(1).setContent(operation.get(0).getContent());
                mMainList.get(1).setCount(operation.size());
                mMainList.get(1).setFk_grade_class_id(operation.get(0).getFk_grade_class_id());
                mMainList.get(1).setFk_tag_id(operation.get(0).getFk_tag_id());
                mMainList.get(1).setMainNewsItem(operation.get(0));
            }

            if (activity.size() > 0) {
                mMainList.get(2).setContent(activity.get(0).getContent());
                mMainList.get(2).setCount(activity.size());
                mMainList.get(2).setFk_grade_class_id(activity.get(0).getFk_grade_class_id());
                mMainList.get(2).setFk_tag_id(activity.get(0).getFk_tag_id());
                mMainList.get(2).setMainNewsItem(activity.get(0));
            }

            if (journal.size() > 0) {
                mMainList.get(3).setContent(journal.get(0).getContent());
                mMainList.get(3).setCount(journal.size());
                mMainList.get(3).setFk_grade_class_id(journal.get(0).getFk_grade_class_id());
                mMainList.get(3).setFk_tag_id(journal.get(0).getFk_tag_id());
                mMainList.get(3).setMainNewsItem(journal.get(0));
            }
        } else {
            mMainList.clear();
            mMainList.add(new MainListItem("食谱", "今日暂未添加", R.drawable.main_launch));
            mMainList.add(new MainListItem("拓展", "今日暂未添加", R.drawable.main_operation));
            mMainList.add(new MainListItem("活动", "今日暂未添加", R.drawable.main_activity));
            mMainList.add(new MainListItem("动态", "今日暂未添加", R.drawable.main_journal));
        }
        mAdapter.setDatas(mMainList);
    }
}
