package com.abings.baby.ui.waterfall.photoviewpagedetail;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.abings.baby.ui.waterfall.photoviewpagedetail.fragment.PhotoDatailFragment;

import java.util.List;

/**
 * Created by HaomingXu on 2016/7/20.
 */
public class PhotoviewpageAdapter extends FragmentStatePagerAdapter {

    private List<PhotoDatailFragment>list;
    public PhotoviewpageAdapter(FragmentManager fm ,List<PhotoDatailFragment>list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
