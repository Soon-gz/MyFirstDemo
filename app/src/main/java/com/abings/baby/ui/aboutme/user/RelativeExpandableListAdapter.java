package com.abings.baby.ui.aboutme.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abings.baby.R;
import com.abings.baby.data.model.RelativePerson;
import com.abings.baby.ui.aboutme.association.AssociationActivity;

import java.util.List;

/**
 * Created by zwj on 2016/7/25.
 */
public class RelativeExpandableListAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<RelativePerson.ResultBean> mListData;
    private String[] groupTitles = new String[]{"相关人员"};//组标题
    private boolean editState = false;
    private String pk_user_id;
    private AboutMeFragmentMvpView mMvpView;
    public RelativeExpandableListAdapter(Context context, List<RelativePerson.ResultBean> listData) {
        mContext = context;
        mListData = listData;
    }


    public void attachView(AboutMeFragmentMvpView mvpView) {
        this.mMvpView = mvpView;
    }

    @Override
    public int getGroupCount() {
        return groupTitles.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mListData.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupTitles[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mListData.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setGravity(Gravity.CENTER_VERTICAL);
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        double hei = height*0.082;
        int itemheight = (new   Double(hei)).intValue();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, itemheight, 4.0f);
        double hei2 = height*0.036;
        int itemheight2 = (new   Double(hei2)).intValue();
        LinearLayout.LayoutParams lp_imag = new LinearLayout.LayoutParams(0, itemheight2, 1.0f);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        TextView textView = getTextView();
        textView.setLayoutParams(lp);
        textView.setText(getGroup(groupPosition).toString());
        ImageView imageView = new ImageView(mContext);
        if (isExpanded) {
            imageView.setImageResource(R.drawable.pullbtnup);
        } else {
            imageView.setImageResource(R.drawable.pullbtn2);
        }
        imageView.setLayoutParams(lp_imag);
        linearLayout.addView(textView);
        linearLayout.addView(imageView);
        return linearLayout;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if (childPosition != mListData.size() - 1) {
            //检测是否最后一个item
            if (!editState) {
                //没编辑状态
                TextView textView = getChildText();
                textView.setText(mListData.get(childPosition).getName());
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, AssociationActivity.class);
                        intent.putExtra(RelativePerson.ResultBean.kName, mListData.get(childPosition));
                        mContext.startActivity(intent);
                    }
                });
                return textView;
            } else {
                //编辑状态
                LinearLayout linearLayout = new LinearLayout(mContext);
                linearLayout.setGravity(Gravity.CENTER_VERTICAL);
                TextView textView = getChildText();
                textView.setText(mListData.get(childPosition).getName());
                WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                int height = wm.getDefaultDisplay().getHeight();
                double hei = height*0.082;
                int itemheight = (new   Double(hei)).intValue();
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0,itemheight, 4.0f);
                LinearLayout.LayoutParams lp_imag = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2.0f);
                textView.setLayoutParams(lp);
                linearLayout.addView(textView);
                TextView textView_del = getChildText();
                textView_del.setText("删除");
                textView_del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //删除接口
                        mMvpView.deleteRelativeItem(childPosition);
                    }
                });
                textView_del.setLayoutParams(lp_imag);
                textView_del.setGravity(Gravity.RIGHT);
                linearLayout.addView(textView_del);
                return linearLayout;
            }
        } else {
            //最后一行
            LinearLayout linearLayout = new LinearLayout(mContext);
            linearLayout.setGravity(Gravity.CENTER_VERTICAL);
            TextView textView = getTextView();
            textView.setText(mListData.get(childPosition).getName());
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            int height = wm.getDefaultDisplay().getHeight();
            double hei = height*0.082;
            int itemheight = (new   Double(hei)).intValue();
            double hei2 = height*0.036;
            int itemheight2 = (new   Double(hei2)).intValue();
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, itemheight, 4.0f);
            LinearLayout.LayoutParams lp_imag = new LinearLayout.LayoutParams(0, itemheight2, 1.0f);
            textView.setLayoutParams(lp);
            linearLayout.addView(textView);
            ImageView imageView = new ImageView(mContext);
            imageView.setImageResource(R.drawable.addmid);
            imageView.setLayoutParams(lp_imag);
            linearLayout.addView(imageView);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, AssociationActivity.class);
                    ((Activity)mContext).startActivityForResult(intent, AssociationActivity.kAdd);

                }
            });
            return linearLayout;
        }
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    /**
     * 设置编辑，在后面加上删除按钮
     *
     * @param editState
     */
    public void setEditState(boolean editState) {
        this.editState = editState;
        notifyDataSetChanged();
    }

    private TextView getTextView() {
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView textView = new TextView(mContext);
        textView.setLayoutParams(lp);
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getWidth();
        double hei = height*0.0185;
        int itemheight = (new   Double(hei)).intValue();
        textView.setPadding(itemheight, itemheight, itemheight, itemheight);
        textView.setTextColor(Color.parseColor("#272727"));
        textView.setTextSize(14);
        textView.setTextAppearance(mContext, R.style.SmallText);
        return textView;
    }

    private TextView getChildText() {
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView textView = new TextView(mContext);
        textView.setLayoutParams(lp);
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getWidth();
        double hei = height*0.046;
        int itemheight = (new   Double(hei)).intValue();
        double width = height*0.0185;
        int itemwidth = (new   Double(hei)).intValue();
        textView.setPadding(itemheight, itemwidth, itemwidth, itemwidth);
        textView.setTextColor(Color.parseColor("#272727"));
        textView.setTextSize(14);
        textView.setTextAppearance(mContext, R.style.SearcheEditText);
        return textView;
    }

}
