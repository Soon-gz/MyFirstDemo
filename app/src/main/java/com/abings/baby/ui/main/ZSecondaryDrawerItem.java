package com.abings.baby.ui.main;

import com.abings.baby.R;
import com.mikepenz.materialdrawer.holder.ColorHolder;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;

/**
 * Created by zwj on 2016/7/3.
 */
public class ZSecondaryDrawerItem extends SecondaryDrawerItem {
    private ColorHolder background;
    public ZSecondaryDrawerItem withBackgroundColor(int backgroundColor) {
        this.background = ColorHolder.fromColor(backgroundColor);
        return this;
    }

    public ZSecondaryDrawerItem withBackgroundRes(int backgroundRes) {
        this.background = ColorHolder.fromColorRes(backgroundRes);
        return this;
    }

    @Override
    public void bindView(ZSecondaryDrawerItem.ViewHolder holder) {
        super.bindView(holder);
        if (background != null) {
            background.applyToBackground(holder.itemView);
        }
    }

    @Override
    public int getLayoutRes() {
        return R.layout.material_drawer_item_primary_secondary_z;
    }

    @Override
    public int getType() {
        return R.id.material_drawer_item_primary_secondary_z;
    }
}
