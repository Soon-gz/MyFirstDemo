/*
 * Copyright (c) 2015 [1076559197@qq.com | tchen0707@gmail.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.abings.baby.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.socks.library.KLog;

import java.util.List;


/**
 * Date:    2014/11/25.
 * Description:
 */
public class HomeItemFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> mListFragments = null;
    private boolean canLoop = true;
    private final int MULTIPLE_COUNT = 100;

    public HomeItemFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        mListFragments = fragments;
    }


    @Override
    public int getCount() {
        return canLoop ? getRealCount() * MULTIPLE_COUNT : getRealCount();
    }

    @Override
    public Fragment getItem(int index) {
        int realPosition = toRealPosition(index);
        KLog.e("index = " + index + ", realPosition = " + realPosition);
        KLog.e("getCount() = " + getCount());

        if (mListFragments != null && realPosition > -1 && realPosition < mListFragments.size()) {
            return mListFragments.get(realPosition);
        } else {
            return null;
        }
    }

    public int getRealCount() {
        return mListFragments == null ? 0 : mListFragments.size();
    }

    public int toRealPosition(int position) {
        int realCount = getRealCount();
        if (realCount == 0) return 0;
        int realPosition = position % realCount;
        return realPosition;
    }

    public void setCanLoop(boolean canLoop) {
        this.canLoop = canLoop;
    }

}
