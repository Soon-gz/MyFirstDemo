package com.abings.baby.ui.exercise.createnew;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.abings.baby.utils.StringUtils;
import com.flyco.dialog.entity.DialogMenuItem;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.utils.CornerUtils;
import com.flyco.dialog.widget.base.BottomBaseDialog;

import java.util.ArrayList;

/**
 * Dialog like iOS ActionSheet(iOS风格对话框)
 */
public class ChooseClassDialog extends BottomBaseDialog<ChooseClassDialog> {
    /**
     * ListView
     */
    private ListView mLv;
    /**
     * title
     */
    private TextView mTvTitle;
    /**
     * title underline(标题下划线)
     */
    private View mVLineTitle;
    /**
     * mCancel button(取消按钮)
     */
    private TextView mTvCancel;
    /**
     * corner radius,dp(圆角程度,单位dp)
     */
    private float mCornerRadius = 0;
    /**
     * title background color(标题背景颜色)
     */
    private int mTitleBgColor = Color.parseColor("#ddffffff");
    /**
     * title text(标题)
     */
    private String mTitle = "提示";
    /**
     * title height(标题栏高度)
     */
    private float mTitleHeight = 48;
    /**
     * title textcolor(标题颜色)
     */
    private int mTitleTextColor = Color.parseColor("#8F8F8F");
    /**
     * title textsize(标题字体大小,单位sp)
     */
    private float mTitleTextSize = 17.5f;
    /**
     * ListView background color(ListView背景色)
     */
    private int mLvBgColor = Color.parseColor("#ddffffff");
    /**
     * divider color(ListView divider颜色)
     */
    private int mDividerColor = Color.parseColor("#D7D7D9");
    /**
     * divider height(ListView divider高度)
     */
    private float mDividerHeight = 0.8f;
    /**
     * item press color(ListView item按住颜色)
     */
    private int mItemPressColor = Color.parseColor("#ffcccccc");
    /**
     * item textcolor(ListView item文字颜色)
     */
    private int mItemTextColor = Color.parseColor("#44A2FF");
    /**
     * item textcolor(ListView item背景颜色)
     */
    private int mItembgColor = Color.parseColor("#EFA93F");
    /**
     * item textsize(ListView item文字大小)
     */
    private float mItemTextSize = 16.5f;
    /**
     * item height(ListView item高度)
     */
    private float mItemHeight = 45;
    /**
     * enable title show(是否显示标题)
     */
    private boolean mIsTitleShow = true;

    /**
     * enable title show(是否显示取消)
     */
    private boolean mIsCancelShow = true;

    /***
     * cancel btn text(取消按钮内容)
     */
    private String mCancelText = "取消";
    /**
     * cancel btn text color(取消按钮文字颜色)
     */
    private int mCancelTextColor = Color.parseColor("#44A2FF");
    /**
     * cancel btn text size(取消按钮文字大小)
     */
    private float mCancelTextSize = 17.5f;
    /**
     * adapter(自定义适配器)
     */
    private BaseAdapter mAdapter;
    /**
     * operation items(操作items)
     */
    private ArrayList<DialogMenuItem> mContents = new ArrayList<>();
    private OnOperItemClickL mOnOperItemClickL;
    private LayoutAnimationController mLac;
    private String[]itemsNum;
    private EditText editText;
    private ImageView clear;
    private ImageView chooseclass;


    public void setOnOperItemClickL(OnOperItemClickL onOperItemClickL) {
        mOnOperItemClickL = onOperItemClickL;
    }

    public ChooseClassDialog(Context context, ArrayList<DialogMenuItem> baseItems, View animateView) {
        super(context, animateView);
        mContents.addAll(baseItems);
        init();
    }

    public ChooseClassDialog(Context context, String[] items, View animateView,EditText editText,ImageView chooseclass,ImageView clear) {
        super(context, animateView);
        itemsNum = items;
        this.chooseclass = chooseclass;
        this.clear = clear;
        mContents = new ArrayList<>();
        this.editText = editText;
        for (String item : items) {
            DialogMenuItem customBaseItem = new DialogMenuItem(item, 0);
            mContents.add(customBaseItem);
        }
        init();
    }

    public ChooseClassDialog(Context context, BaseAdapter adapter, View animateView) {
        super(context, animateView);
        mAdapter = adapter;
        init();
    }

    private void init() {
        widthScale(1.0f);
        /** LayoutAnimation */
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                0f, Animation.RELATIVE_TO_SELF, 6f, Animation.RELATIVE_TO_SELF, 0);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.setDuration(350);
        animation.setStartOffset(150);

        mLac = new LayoutAnimationController(animation, 0.12f);
        mLac.setInterpolator(new DecelerateInterpolator());
    }

    @Override
    public View onCreateView() {
        LinearLayout ll_container = new LinearLayout(mContext);
        ll_container.setOrientation(LinearLayout.VERTICAL);
        ll_container.setBackgroundColor(Color.TRANSPARENT);

        /** title */
        mTvTitle = new TextView(mContext);
        mTvTitle.setGravity(Gravity.CENTER);
        mTvTitle.setPadding(dp2px(10), dp2px(5), dp2px(10), dp2px(5));

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.topMargin = dp2px(20);

        ll_container.addView(mTvTitle, params);

        /** title underline */
        mVLineTitle = new View(mContext);
        ll_container.addView(mVLineTitle);

        /** listview */
        mLv = new ListView(mContext);
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        if (itemsNum.length > 6){
            int height = wm.getDefaultDisplay().getHeight();
            mLv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, height*1/2, 1));
        }else{
            mLv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1));
        }
        mLv.setCacheColorHint(Color.TRANSPARENT);
        mLv.setFadingEdgeLength(0);
        mLv.setVerticalScrollBarEnabled(false);
        mLv.setSelector(new ColorDrawable(Color.TRANSPARENT));

        ll_container.addView(mLv);

        /** mCancel btn */
        mTvCancel = new TextView(mContext);
        mTvCancel.setGravity(Gravity.CENTER);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.topMargin = dp2px(7);
        lp.bottomMargin = dp2px(7);
        mTvCancel.setLayoutParams(lp);

        ll_container.addView(mTvCancel);
        return ll_container;
    }

    @Override
    protected void onStart() {
        padding(0, 0, 0, 0);
        super.onStart();
    }

    @Override
    public void setUiBeforShow() {
        /** title */
        float radius = dp2px(mCornerRadius);
        mTvTitle.setHeight(dp2px(mTitleHeight));
        mTvTitle.setBackgroundDrawable(CornerUtils.cornerDrawable(mTitleBgColor, new float[]{radius, radius, radius,
                radius, 0, 0, 0, 0}));
        mTvTitle.setText(mTitle);
        mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTitleTextSize);
        mTvTitle.setTextColor(mTitleTextColor);
        mTvTitle.setVisibility(mIsTitleShow ? View.VISIBLE : View.GONE);

        /** title underline */
        mVLineTitle.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, dp2px(mDividerHeight)));
        mVLineTitle.setBackgroundColor(mDividerColor);
        mVLineTitle.setVisibility(mIsTitleShow ? View.VISIBLE : View.GONE);

        /** mCancel btn */
        mTvCancel.setHeight(dp2px(mItemHeight));
        mTvCancel.setText(mCancelText);
        mTvCancel.setTextSize(TypedValue.COMPLEX_UNIT_SP, mCancelTextSize);
        mTvCancel.setTextColor(mCancelTextColor);
        mTvCancel.setBackgroundDrawable(CornerUtils.listItemSelector(radius, mLvBgColor, mItemPressColor, 1, 0));

        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mTvCancel.setVisibility(mIsCancelShow ? View.VISIBLE : View.GONE);

        /** listview */
        mLv.setDivider(new ColorDrawable(mDividerColor));
        mLv.setDividerHeight(dp2px(mDividerHeight));

        if (mIsTitleShow) {
            mLv.setBackgroundDrawable(CornerUtils.cornerDrawable(mLvBgColor, new float[]{0, 0, 0, 0, radius, radius, radius,
                    radius}));
        } else {
            mLv.setBackgroundDrawable(CornerUtils.cornerDrawable(mLvBgColor, radius));
        }

        if (mAdapter == null) {
            mAdapter = new ListDialogAdapter();
        }

        mLv.setAdapter(mAdapter);
        mLv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mOnOperItemClickL != null) {
                    mOnOperItemClickL.onOperItemClick(parent, view, position, id);
                }
            }
        });

        mLv.setLayoutAnimation(mLac);
    }

    /**
     * set title background color(设置标题栏背景色)
     */
    public ChooseClassDialog titleBgColor(int titleBgColor) {
        mTitleBgColor = titleBgColor;
        return this;
    }

    /**
     * set title text(设置标题内容)
     */
    public ChooseClassDialog title(String title) {
        mTitle = title;
        return this;
    }

    /**
     * set titleHeight(设置标题高度)
     */
    public ChooseClassDialog titleHeight(float titleHeight) {
        mTitleHeight = titleHeight;
        return this;
    }

    /**
     * set title textsize(设置标题字体大小)
     */
    public ChooseClassDialog titleTextSize_SP(float titleTextSize_SP) {
        mTitleTextSize = titleTextSize_SP;
        return this;
    }

    /**
     * set title textcolor(设置标题字体颜色)
     */
    public ChooseClassDialog titleTextColor(int titleTextColor) {
        mTitleTextColor = titleTextColor;
        return this;
    }

    /**
     * enable title show(设置标题是否显示)
     */
    public ChooseClassDialog isTitleShow(boolean isTitleShow) {
        mIsTitleShow = isTitleShow;
        return this;
    }

    /**
     * enable title show(设置标题是否显示)
     */
    public ChooseClassDialog isCancelShow(boolean isCancelShow) {
        mIsCancelShow = isCancelShow;
        return this;
    }

    /**
     * set ListView background color(设置ListView背景)
     */
    public ChooseClassDialog lvBgColor(int lvBgColor) {
        mLvBgColor = lvBgColor;
        return this;
    }

    /**
     * set corner radius(设置圆角程度,单位dp)
     */
    public ChooseClassDialog cornerRadius(float cornerRadius_DP) {
        mCornerRadius = cornerRadius_DP;
        return this;
    }

    /**
     * set divider color(ListView divider颜色)
     */
    public ChooseClassDialog dividerColor(int dividerColor) {
        mDividerColor = dividerColor;
        return this;
    }

    /**
     * set divider height(ListView divider高度)
     */
    public ChooseClassDialog dividerHeight(float dividerHeight_DP) {
        mDividerHeight = dividerHeight_DP;
        return this;
    }

    /**
     * set item press color(item按住颜色)
     */
    public ChooseClassDialog itemPressColor(int itemPressColor) {
        mItemPressColor = itemPressColor;
        return this;
    }

    /**
     * set item textcolor(item字体颜色)* @return ActionSheetDialog
     */
    public ChooseClassDialog itemTextColor(int itemTextColor) {
        mItemTextColor = itemTextColor;
        return this;
    }

    /**
     * set item textsize(item字体大小)
     */
    public ChooseClassDialog itemTextSize(float itemTextSize_SP) {
        mItemTextSize = itemTextSize_SP;
        return this;
    }

    /**
     * set item height(item高度)
     */
    public ChooseClassDialog itemHeight(float itemHeight_DP) {
        mItemHeight = itemHeight_DP;
        return this;
    }

    /**
     * 设置每项item的背景颜色
     * */
    public ChooseClassDialog setItemBGcolor(int position,int color){
//        layouts.get(position).setBackgroundColor(color);
        mLv.getChildAt(position).setBackgroundColor(color);
        return this;
    }



    int[] flages;
    public ChooseClassDialog setFlags(int[] flags){
        flages = flags;
        return this;
    }

    class ListDialogAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mContents.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressWarnings("deprecation")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final DialogMenuItem item = mContents.get(position);


            final LinearLayout llItem = new LinearLayout(mContext);
            llItem.setOrientation(LinearLayout.HORIZONTAL);
            llItem.setGravity(Gravity.CENTER_VERTICAL);

            CheckBox checkBox = new CheckBox(mContext);
            checkBox.setText(item.mOperName);
            checkBox.setButtonDrawable(null);
            checkBox.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            checkBox.setSingleLine(true);
            checkBox.setGravity(Gravity.CENTER);
            checkBox.setTextColor(mItemTextColor);
            checkBox.setTextSize(TypedValue.COMPLEX_UNIT_SP, mItemTextSize);
            checkBox.setHeight(dp2px(mItemHeight));
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    StringBuilder stringBuilder = new StringBuilder(editText.getText());
                    if (isChecked){
                        flages[position] = 1;
                        int mov = stringBuilder.indexOf(itemsNum[position] + ",");
                        if (mov == -1) {
                            stringBuilder.append(itemsNum[position] + ",");
                        }
                        editText.setText(stringBuilder);
                        llItem.setBackgroundColor(mItembgColor);
                    }else{
                        flages[position] = 0;
                        String[] strs = stringBuilder.toString().split(",");
                        StringBuilder stringBuilder1 = new StringBuilder();
                        for (int i = 0; i < strs.length; i++) {
                            if (!itemsNum[position].equals(strs[i])) {
                                stringBuilder1.append(strs[i] + ",");
                            }
                        }
                        editText.setText(stringBuilder1);
                        llItem.setBackgroundColor(mTitleBgColor);
                    }
                    if (!StringUtils.isEmpty(editText.getText().toString().trim())) {
                        chooseclass.setVisibility(View.GONE);
                        clear.setVisibility(View.VISIBLE);
                    } else {
                        chooseclass.setVisibility(View.VISIBLE);
                        clear.setVisibility(View.GONE);
                    }
                }
            });
            if (flages[position] == 1){
                checkBox.setChecked(true);
            }
            llItem.addView(checkBox);
            return llItem;
        }
    }
}
