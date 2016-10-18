package com.abings.baby.ui.main;

import com.abings.baby.R;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;

/**
 * Created by zwj on 2016/7/3.
 */
public class ZPrimaryDrawerItem extends PrimaryDrawerItem {

    @Override
    public int getLayoutRes() {
        return R.layout.material_drawer_item_primary_z;
    }

    @Override
    public int getType() {
        return R.id.material_drawer_item_primary_z;
    }
}
