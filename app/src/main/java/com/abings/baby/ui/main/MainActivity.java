package com.abings.baby.ui.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abings.baby.Const;
import com.abings.baby.R;
import com.abings.baby.WineApplication;
import com.abings.baby.data.model.BabyItem;
import com.abings.baby.data.model.ClassItem;
import com.abings.baby.data.model.TeacherDetail;
import com.abings.baby.data.model.UserDetail;
import com.abings.baby.data.remote.RetrofitUtils;
import com.abings.baby.ui.aboutme.AboutMeActivity;
import com.abings.baby.ui.baby.BabyInfoActivity;
import com.abings.baby.ui.base.BaseActivity;
import com.abings.baby.ui.contacts.ContactsActivity;
import com.abings.baby.ui.contacts.ContactsTeacherActivity;
import com.abings.baby.ui.feedback.FeedBackActivity;
import com.abings.baby.ui.finder.FinderActivity;
import com.abings.baby.ui.home2.HomeFragment2;
import com.abings.baby.ui.message.center.MsgCenterActivity;
import com.abings.baby.ui.message.send.SendMsgActivity;
import com.abings.baby.ui.signin.NormalPopup;
import com.abings.baby.ui.signin.SignInActivity;
import com.abings.baby.utils.ScreenUtils;
import com.bumptech.glide.Glide;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.mikepenz.fastadapter.IExpandable;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.socks.library.KLog;

import java.util.Hashtable;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bingoogolapple.bgabanner.BGAViewPager;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class MainActivity extends BaseActivity implements MainMvpView {

    private static final String EXTRA_AUTO_CHECK_IN_DISABLED = "io.ribot.app.ui.main.MainActivity" + "" +
            ".EXTRA_AUTO_CHECK_IN_DISABLED";
    @Inject
    MainPresenter mMainPresenter;

    @Bind(R.id.viewPager)
    BGAViewPager mViewPager;
    @Bind(R.id.btn_back)
    ImageView btn_back;
    @Bind(R.id.btn_search)
    ImageView btn_search;
    @Bind(R.id.btn_qrcode)
    ImageView btn_qrcode;
    @Bind(R.id.title_center)
    TextView title;
    @Bind(R.id.toolbar)
    RelativeLayout toolbar;
    @Bind(R.id.text_back)
    TextView textBack;
    @Bind(R.id.layoutAll)
    LinearLayout layoutAll;

    private int nowFragmentId = 0;
    private Drawer result = null;
    private PrimaryDrawerItem msgItem, contactsItem;
    public static MainActivity SingleInstance = null;

    public static Intent getStartIntent(Context context, boolean autoCheckInDisabled) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(EXTRA_AUTO_CHECK_IN_DISABLED, autoCheckInDisabled);
        return intent;
    }

    @Override
    protected boolean toggleOverridePendingTransition() {
        return false;
    }

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.SCALE;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    public MainPresenter getPresenter() {
        return mMainPresenter;
    }


    @Override
    protected View getLoadingTargetView() {
        return mViewPager;
    }

    @Override
    protected void iniInjector() {
        activityComponent().inject(this);
        mMainPresenter.attachView(this);
    }


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        iniDrawLayout(savedInstanceState);
        mMainPresenter.intFragments(mViewPager, mMainPresenter.getPagerFragments(), this);
        mMainPresenter.toFragment(mViewPager, 0);

        SingleInstance = this;

        initTitle();

        mMainPresenter.loadUserInfo(WineApplication.getInstance().isTeacher());

        initPermission();
    }

    private void initPermission() {
        //6.0以后权限申请
        PermissionGen.needPermission(MainActivity.this, 100,
                new String[]{Manifest.permission.GET_ACCOUNTS, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @PermissionSuccess(requestCode = 100)
    public void doSuccess() {
    }

    @PermissionFail(requestCode = 100)
    public void doFail() {
        Toast.makeText(MainActivity.this, "获取权限失败", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainPresenter.detachView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sign_out:
                mMainPresenter.signOut();
                return true;
            case R.id.action_check_in:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*****
     * MVP View methods implementation
     *****/

    @Override
    public void onSignedOut() {
    }

    @Override
    public void updateView(final UserDetail info) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                head_name.setText(info.getName());
                Glide.with(MainActivity.this).load(RetrofitUtils.BASE_USER_PHOTO_URL + info.getPhoto()).placeholder(R
                        .drawable.image_onload).error(R.drawable.left_menu_header_image).dontAnimate().into(head_protrait);
            }
        });

    }

    @Override
    public void updateView(final TeacherDetail info) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                head_name.setText(info.getName());
                Glide.with(MainActivity.this).load(RetrofitUtils.BASE_TEACHER_PHOTO_URL + info.getPhoto())
                        .placeholder(R.drawable.image_onload).error(R.drawable.left_menu_header_image).dontAnimate().into
                        (head_protrait);
            }
        });

    }

    //更新未读信息
    @Override
    public void updateUnReadCount(int count) {
//        if (count > 0) {
//            msgItem.withBadge(count + "").withBadgeStyle(new BadgeStyle().withTextColor(getResources().getColor(R
//                    .color.baby_orange)).withColorRes(R.color.white));
//        }
    }

    @Override
    public boolean bindEvents() {
        return false;
    }

    @Override
    public void showData(List data, boolean canNext) {

    }

    @Override
    public void showLoadingProgress(boolean show) {
    }

    @Override
    public void showMessage(String mes) {

    }

    @Override
    public void showError(String mes) {
        Toast.makeText(MainActivity.this, mes, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTokenError() {

    }


    private ImageView head_protrait;
    private TextView head_name;

    private void iniDrawLayout(Bundle savedInstanceState) {

        contactsItem = new ZPrimaryDrawerItem()//
                .withName("通讯录")//设置名字
                .withIcon(getResources().getDrawable(R.drawable.left_menu_contacts))//设置图片资源
                .withIdentifier(Const.Fragment_Contacts);//设置级别
        msgItem = new ZPrimaryDrawerItem().withName("消息中心").withIcon(getResources().getDrawable(R.drawable
                .left_menu_message)).withIdentifier(Const.Fragment_Meg);
//        未读消息设置0，先取消
//        msgItem.withBadge("0").withBadgeStyle(new BadgeStyle().withTextColor(getResources().getColor(R.color
//                .baby_orange)).withColorRes(R.color.white));


        DrawerBuilder buid = new DrawerBuilder();

        View headerView = LayoutInflater.from(this).inflate(R.layout.navheader, null);
        head_protrait = (ImageView) headerView.findViewById(R.id.head_protrait);
        head_protrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AboutMeActivity.class));
            }
        });
        head_name = (TextView) headerView.findViewById(R.id.head_name);
        int drawerWidthPx = ScreenUtils.getScreenWidth(this) * 2 / 3;
        buid.withActivity(this).withDrawerWidthPx(drawerWidthPx).withHeader(headerView).withHeaderDivider(false);
        buid.withInnerShadow(true);
        PrimaryDrawerItem primaryDrawerItem = new ZPrimaryDrawerItem();
        if (WineApplication.getInstance().isTeacher()) {
            primaryDrawerItem.withName("班级").withIcon(getResources().getDrawable(R.drawable.left_menu_classes))
                    .withIdentifier(19);
            for (int i = 0; i < WineApplication.getInstance().getMyClasss().size(); i++) {
                primaryDrawerItem.withSubItems(new ZSecondaryDrawerItem().withBackgroundColor(0xFF982F27).withName(WineApplication.getInstance()
                        .getMyClasss().get(i).getClass_name()).withLevel(2).withIdentifier(2000 + i));
            }
        } else {
            primaryDrawerItem.withName("宝宝").withIcon(getResources().getDrawable(R.drawable.left_menu_baby))
                    .withIdentifier(19);

            for (int i = 0; i < WineApplication.getInstance().getMyBabys().size(); i++) {

                primaryDrawerItem.withSubItems(new ZSecondaryDrawerItem().withBackgroundColor(0xFF982F27).withName(WineApplication.getInstance()
                        .getMyBabys().get(i).getName()).withLevel(2).withIdentifier(2000 + i).withSelectedColor(Color.parseColor("#FF982F27")).withSetSelected(true));
//                Log.i("TAG00","用户id:"+WineApplication.getInstance().getMyBabys().get(i).getFk_user_id());
            }
        }

        buid.addDrawerItems(primaryDrawerItem.withSetSelected(true), new ZDividerDrawerItem());


        PrimaryDrawerItem feedBackItem = new ZPrimaryDrawerItem().withName("意见反馈").withIcon(getResources().getDrawable
                (R.drawable.left_menu_feedback)).withIdentifier(Const.Fragment_FeedBack);

        buid.addDrawerItems(contactsItem, new ZDividerDrawerItem(), msgItem, new ZDividerDrawerItem(), new
                ZPrimaryDrawerItem().withName("设置").withIcon(getResources().getDrawable(R.drawable.left_menu_setting))
                .withIdentifier(Const.Fragment_Setting), new ZDividerDrawerItem());

        buid.addDrawerItems(feedBackItem);

        result = buid.withSavedInstance(savedInstanceState).withOnDrawerItemClickListener(new Drawer
                .OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                if (drawerItem != null) {
                    Intent intent = null;
                    //展开子项
                    if (((IExpandable) drawerItem).getSubItems() != null) {
                        result.getAdapter().toggleExpandable(position);
                        return true;
                    } else switch ((int) drawerItem.getIdentifier()) {
                        case Const.Fragment_Contacts:
                            if (WineApplication.getInstance().isTeacher()) {
                                startActivity(new Intent(MainActivity.this, ContactsTeacherActivity.class));
                            } else {
                                startActivity(new Intent(MainActivity.this, ContactsActivity.class));
                            }
                            return false;
                        case Const.Fragment_FeedBack:
                            startActivity(new Intent(MainActivity.this, FeedBackActivity.class));
                            return false;
                        case Const.Fragment_Meg:
                            if (WineApplication.getInstance().isTeacher()) {
                                startActivity(new Intent(MainActivity.this, MsgCenterActivity.class));
                                return false;
                            }
                        case Const.Fragment_Setting:
                            toFragemtn((int) drawerItem.getIdentifier());
                            break;
                    }

                    if (drawerItem.getIdentifier() >= 2000) {
                        gotoItemDetail((int) drawerItem.getIdentifier() - 2000);
                    }
                    if (intent != null) {
                        //跳转操作
                    }
                }
                return false;
            }
        }).build();
        if (Build.VERSION.SDK_INT >= 19) {
            // result.getDrawerLayout().setFitsSystemWindows(false);
        }
    }


    @OnClick(R.id.btn_search)
    public void search() {
        startActivity(new Intent(MainActivity.this, FinderActivity.class));
    }

    @OnClick({R.id.text_back, R.id.btn_back})
    public void titleBack() {
        if (nowFragmentId == 0) {
            if (result.isDrawerOpen()) {
                result.closeDrawer();
            } else result.openDrawer();
        } else {
            nowFragmentId = 0;
            initTitle();
            btn_qrcode.setVisibility(View.GONE);
            textBack.setVisibility(View.INVISIBLE);
            btn_back.setVisibility(View.VISIBLE);
            btn_back.setImageResource(R.drawable.main_side_menu_bar);
            btn_search.setVisibility(View.VISIBLE);
            btn_search.setImageResource(R.drawable.main_search);
            result.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            mMainPresenter.toFragment(mViewPager, Const.Fragment_Home);
        }
    }

    public void initTitle() {
        if (WineApplication.getInstance().isTeacher()) {
            if (WineApplication.getInstance().getMyClasss().size() > 0) {
                ClassItem clas = WineApplication.getInstance().getNowClass();
                title.setText(clas.toString());
            }
        } else {
            if (WineApplication.getInstance().getMyBabys().size() > 0) {
                BabyItem bi = WineApplication.getInstance().getBaby();
                title.setText(bi.getName());
            }
        }
        title.setClickable(true);
        title.setTextSize(20);
    }

    public void gotoItemDetail(int i) {
        if (WineApplication.getInstance().isTeacher()) {
            Intent intent = new Intent(MainActivity.this, BabyInfoActivity.class);
            intent.putExtra("class", WineApplication.getInstance().getNowClass(i));
            intent.putExtra("position", i + "");
            startActivity(intent);
        } else {
            Intent intent = new Intent(MainActivity.this, BabyInfoActivity.class);
            intent.putExtra("baby", WineApplication.getInstance().getBaby(i));
            intent.putExtra("position", i + "");
            startActivity(intent);
        }

    }

    public void switchItem(int i) {
        if (WineApplication.getInstance().isTeacher()) {
            WineApplication.getInstance().getNowClass(i);
        } else {
            WineApplication.getInstance().getBaby(i);
        }
        initTitle();
        ((HomeFragment2) mMainPresenter.getOneFragment()).refreshData(true);
    }

    @Override
    public void onBackPressed() {
        if (nowFragmentId == 0) {
            if (result.isDrawerOpen()) {
                result.closeDrawer();
            } else {
                finish();
            }
        } else {
            nowFragmentId = 0;
            initTitle();
            btn_qrcode.setVisibility(View.GONE);
            textBack.setVisibility(View.INVISIBLE);
            btn_back.setVisibility(View.VISIBLE);
            btn_back.setImageResource(R.drawable.main_side_menu_bar);
            btn_search.setImageResource(R.drawable.main_search);
            result.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            mMainPresenter.toFragment(mViewPager, Const.Fragment_Home);
            btn_search.setVisibility(View.VISIBLE);
        }
    }

    public void toFragemtn(int index) {
        KLog.e("index = " + index);
        title.setClickable(false);
        btn_back.setImageResource(R.drawable.icon_return);
        btn_back.setVisibility(View.INVISIBLE);
        textBack.setVisibility(View.VISIBLE);
        title.setClickable(false);
        result.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        nowFragmentId = index;
        btn_qrcode.setVisibility(View.GONE);
        switch (index) {
            case Const.Fragment_Setting://2
                title.setText("设置");
                btn_search.setVisibility(View.GONE);
                title.setTextSize(18);
                break;
            case Const.Fragment_Contacts://1
//                btn_search.setVisibility(View.GONE);
//                title.setText("通讯录");
//                startActivity(new Intent(this, ContactsActivity.class));
//                titleBack();
                return;
            case Const.Fragment_Meg://4
                btn_search.setVisibility(View.GONE);
                if (WineApplication.getInstance().isTeacher()) {
                    btn_qrcode.setVisibility(View.VISIBLE);
                }
                title.setText("信息中心");
                title.setTextSize(18);
                break;

            case Const.Fragment_FeedBack://3
                btn_search.setVisibility(View.GONE);
                title.setText("意见反馈");
                title.setTextSize(18);
                break;
            case Const.Fragment_Search://5
                btn_search.setVisibility(View.GONE);
                break;
        }

        mMainPresenter.toFragment(mViewPager, index);
    }

    private void showPopup() {
        ListPopup.Builder builder = new ListPopup.Builder(this);
        if (WineApplication.getInstance().isTeacher()) {
            List<ClassItem> clss = WineApplication.getInstance().getMyClasss();
            if (clss.size() <= 1) {
                return;
            }
            int i = 0;
            for (ClassItem ci : WineApplication.getInstance().getMyClasss()) {
                if (!ci.getPk_grade_class_id().equals(WineApplication
                        .getInstance().getNowClass().getPk_grade_class_id())) {
                    String title = ci.toString();
                    builder.addItem(i, title);
                }
                i++;
            }
        } else {
            if (WineApplication.getInstance().getMyBabys().size() <= 1) {
                return;
            }
            int i = 0;
            for (BabyItem bi : WineApplication.getInstance().getMyBabys()) {
                if (!bi.getPk_baby_id().equals(WineApplication
                        .getInstance().getBaby().getPk_baby_id())) {
                    String title = bi.getName();
                    builder.addItem(i, title);
                }
                i++;

            }
        }
        final ListPopup mListPopup = builder.build();
        mListPopup.setOnListPopupItemClickListener(new ListPopup.OnListPopupItemClickListener() {
            @Override
            public void onItemClick(int what) {
                mListPopup.dismiss();
                switchItem(what);
            }
        });
        mListPopup.showPopupWindow(title);
    }

    /**
     * 点击主页中间的名字，弹出下拉选择框，选择班级或宝宝（二维码签到暂时弃用）
     *
     * @author Shuwen
     * created at 2016/7/29 13:38
     */
    @OnClick({R.id.title_center, R.id.btn_qrcode})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_center:
                showPopup();
                break;
            case R.id.btn_qrcode:
//                扫描二维码，这里代码留着，以备打卡功能
                if (WineApplication.getInstance().isTeacher()) {
                   Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                    intent.putExtra("title",title.getText().toString().trim());
                    startActivity(intent);
                } else {

                    NormalPopup normalPopup = new NormalPopup(MainActivity.this);
                    Bitmap bitmap = CreateTwoDCode();
                    if(bitmap!=null){
                        normalPopup.getImageView().setImageBitmap(bitmap);
                    }else{
                        //做个错误的代码
                    }
                    normalPopup.showPopupWindow();
                }
                break;
        }
    }

    /**
     * 在AboutMeAcitivty调用，达到实时更新头像的效果
     *
     * @author Shuwen
     * created at 2016/7/29 13:37
     */
    public void updateHeader(final String name) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (WineApplication.getInstance().isTeacher()) {
                    Glide.with(MainActivity.this).load(RetrofitUtils.BASE_TEACHER_PHOTO_URL + name).placeholder(R.drawable.image_onload).error(R.drawable.left_menu_header_image).dontAnimate().into(head_protrait);
                } else {
                    Glide.with(MainActivity.this).load(RetrofitUtils.BASE_USER_PHOTO_URL + name).placeholder(R.drawable.image_onload).error(R.drawable.left_menu_header_image).dontAnimate().into(head_protrait);
                }
            }
        });
    }

    /**
     * 在BabyInfoActivity调用，达到实时更新宝宝头像的效果
     *
     * @author Shuwen
     * created at 2016/7/29 13:37
     */
    public void setChildHead(String position, String photo) {
        WineApplication.getInstance().getBaby(Integer.parseInt(position)).setPhoto(photo);
    }


    /**
     * 将指定的内容生成成二维码
     * <p/>
     * 将要生成二维码的内容
     *
     * @return 返回生成好的二维码事件
     */
    private Bitmap CreateTwoDCode() {
        try {

            WineApplication wineApplication = WineApplication.getInstance();
            String pk_grade_class_id = wineApplication.getClassId();
            String pk_school_id = wineApplication.getFk_school_id();
            String pk_baby_id = wineApplication.getBaby().getPk_baby_id();
            String pk_user_id = wineApplication.getBaby().getFk_user_id();
            String content = pk_grade_class_id + "," + pk_school_id + "," + pk_baby_id + "," + pk_user_id;
            // 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
            Hashtable hints = new Hashtable();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); //编码
            hints.put(EncodeHintType.MARGIN, 2);
            BitMatrix matrix = new MultiFormatWriter().encode(content,BarcodeFormat.QR_CODE, 800, 800,hints);
            int width = matrix.getWidth();
            int height = matrix.getHeight();
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (matrix.get(x, y)) {
                        pixels[y * width + x] = 0xff000000;
                    } else {
                        pixels[y * width + x] = 0xffffffff;
                    }
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(width, height,Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

}
