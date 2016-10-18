package com.abings.baby.ui.adapter.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.abings.baby.R;
import com.abings.baby.data.model.WaterFallGridItem;
import com.abings.baby.data.remote.RetrofitUtils;
import com.abings.baby.utils.ImageLoaderUtil;
import com.abings.baby.utils.StringUtils;
import com.bigkoo.convenientbanner.holder.Holder;

public class BannerImageHolderView implements Holder<WaterFallGridItem> {
    private ImageView imageView;
    private View.OnClickListener listen;

    public BannerImageHolderView(View.OnClickListener listen) {
        this.listen = listen;
    }

    @Override
    public View createView(Context context) {
        //Image、自定义View
        imageView = (ImageView) View.inflate(context, R.layout.view_image, null);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, WaterFallGridItem data) {
        if (!StringUtils.isEmpty(data.getImage())){
            ImageLoaderUtil.display(RetrofitUtils.BASE_NEWS_SMALL_IMG_URL + data.getImage(), imageView);
        }
        imageView.setOnClickListener(listen);
    }

}
