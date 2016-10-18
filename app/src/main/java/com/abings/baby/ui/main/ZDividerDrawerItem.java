package com.abings.baby.ui.main;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.abings.baby.R;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;
import com.mikepenz.materialdrawer.model.AbstractDrawerItem;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialize.util.UIUtils;

/**
 * Created by zwj on 2016/7/4.
 */
public class ZDividerDrawerItem extends AbstractDrawerItem<DividerDrawerItem, ZDividerDrawerItem.ViewHolder> {

    @Override
    public int getType() {
        return com.mikepenz.materialdrawer.R.id.material_drawer_item_divider;
    }

    @Override
    @LayoutRes
    public int getLayoutRes() {
        return R.layout.material_drawer_item_divider;
    }

    @Override
    public void bindView(ViewHolder viewHolder) {
        Context ctx = viewHolder.itemView.getContext();

        //set the identifier from the drawerItem here. It can be used to run tests
        viewHolder.itemView.setId(hashCode());

        //define how the divider should look like
        viewHolder.view.setClickable(false);
        viewHolder.view.setEnabled(false);
        viewHolder.view.setMinimumHeight(1);
        ViewCompat.setImportantForAccessibility(viewHolder.view,
                ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_NO);

        //set the color for the divider
//        viewHolder.divider.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(ctx, com.mikepenz.materialdrawer.R.attr.material_drawer_divider, R.color.material_drawer_item_divider_z));

        //call the onPostBindView method to trigger post bind view actions (like the listener to modify the item if required)
        onPostBindView(this, viewHolder.itemView);
    }

    @Override
    public ViewHolderFactory getFactory() {
        return new ItemFactory();
    }

    public static class ItemFactory implements ViewHolderFactory<ViewHolder> {
        public ViewHolder create(View v) {
            return new ViewHolder(v);
        }
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private View divider;

        private ViewHolder(View view) {
            super(view);
            this.view = view;
            this.divider = view.findViewById(com.mikepenz.materialdrawer.R.id.material_drawer_divider);
        }
    }
}
