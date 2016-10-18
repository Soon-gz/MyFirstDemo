package com.abings.baby.ui.main;

import android.content.Context;
import android.view.View;

import com.abings.baby.R;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * 作者：黄斌 on 2016/1/22 17:27
 * 说明：
 */
public class DataEngine {

    public static View getCustomHeaderView(final Context context) {
        View headerView = View.inflate(context, R.layout.view_custom_header, null);
        final BGABanner banner = (BGABanner) headerView.findViewById(R.id.banner);
        final List<View> views = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            views.add(View.inflate(context, R.layout.view_image, null));
        }
        banner.setViews(views);
/*        TW.getInstance().getEngine().getBannerModel().enqueue(new Callback<BannerModel>() {
            @Override
            public void onResponse(Response<BannerModel> response) {
                BannerModel bannerModel = response.body();
                for (int i = 0; i < views.size(); i++) {
                    Glide.with(context).load(bannerModel.imgs.get(i)).placeholder(R.mipmap.bga_refresh_loading01).error(R.mipmap.bga_refresh_loading02).dontAnimate().thumbnail(0.1f).into((ImageView) views.get(i));
                }
                banner.setTips(bannerModel.tips);
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });*/
        return headerView;
    }
}
