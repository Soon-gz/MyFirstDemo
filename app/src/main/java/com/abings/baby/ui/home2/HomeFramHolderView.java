package com.abings.baby.ui.home2;

import android.content.Context;
import android.view.View;

import com.abings.baby.ui.home2.item.HomeLayout;
import com.bigkoo.convenientbanner.holder.Holder;

public class HomeFramHolderView implements Holder<HomeLayout> {
    private View view;

    public HomeFramHolderView() {
    }

    @Override
    public View createView(Context context) {
        view = new HomeLayout(context);
        return view;
    }

    @Override
    public void UpdateUI(Context context, int position, HomeLayout data) {

    }
}
