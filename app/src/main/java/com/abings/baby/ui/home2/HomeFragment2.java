package com.abings.baby.ui.home2;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abings.baby.R;
import com.abings.baby.WineApplication;
import com.abings.baby.ui.base.BaseActivity;
import com.abings.baby.ui.base.BaseFragment;
import com.abings.baby.ui.home2.item.HomeLayout;
import com.abings.baby.ui.login.LoginActivity;
import com.abings.baby.ui.publish.PublishActivity;
import com.abings.baby.ui.publishvideo.VideoRecordActivity;
import com.abings.baby.utils.DateUtil;
import com.abings.baby.widget.refreshlayout.BGARefreshLayout;
import com.abings.baby.widget.refreshlayout.BGAStickinessRefreshViewHolder;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.foamtrace.photopicker.PhotoPickerActivity;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;
import com.socks.library.KLog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/1/18.
 */
public class HomeFragment2 extends BaseFragment implements HomeMvpView, BGARefreshLayout.BGARefreshLayoutDelegate {

    @Inject
    HomePresenter mHomePresenter;
    @Bind(R.id.viewPager)
    LoopViewPager mViewPager;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.layout_master)
    LinearLayout layoutMaster;
    @Bind(R.id.refreshLayout)
    BGARefreshLayout bgaRefreshLayout;
    @Bind(R.id.btn_public)
    ImageView btnPublic;

    private boolean isFirstIn = true;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mHomePresenter.detachView();
        ButterKnife.unbind(this);
    }


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_home2;
    }

    @Override
    protected void iniInjector() {
        ((BaseActivity) getActivity()).activityComponent().inject(this);
        mHomePresenter.attachView(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isFirstIn){
            isFirstIn = false;
            return;
        }
        refreshData(DateUtil.getStringFromTime(DateUtil.getTimeFromString(curDate, "yyyy.MM.dd"),
                "yyyy-MM-dd"));
    }

    @Override
    protected void onFirstUserVisible() {
        initDate();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshData(DateUtil.getStringFromTime(DateUtil.getTimeFromString(curDate, "yyyy.MM.dd"),
                        "yyyy-MM-dd"));
            }
        }, 300L);
    }

    private String mFk_grade_class_id;

    @Override
    protected View getLoadingTargetView() {
        return mViewPager;
    }

    @Override
    protected void initViewsAndEvents() {
        if (WineApplication.getInstance().isTeacher()) {
            if (WineApplication.getInstance().getMyClasss().size() > 0) {
                mFk_grade_class_id = WineApplication.getInstance().getNowClass().getPk_grade_class_id();
            }
        } else {
            if (WineApplication.getInstance().getMyBabys().size() > 0) {
                mFk_grade_class_id = WineApplication.getInstance().getBaby().getFk_grade_class_id();
            }
        }
        iniBgaRefreshLayout();
        initDate();
        int len = 20;
        List<String> times = new ArrayList<>(len);
        String temp = curDate;
        for (int i = 0; i < len; i++) {
            temp = DateUtil.getSpecifiedDayBefore(temp, "yyyy.MM.dd");
            times.add(temp);
        }


        List<HomeLayout> homelist = new ArrayList<>();
        homelist.add(new HomeLayout(getActivity()));
        homelist.add(new HomeLayout(getActivity()));
        homelist.add(new HomeLayout(getActivity()));
        homelist.add(new HomeLayout(getActivity()));
        LoopPageAdapter pageAdapter = new LoopPageAdapter(new ViewHolderCreator<HomeFramHolderView>() {
            @Override
            public HomeFramHolderView createHolder() {
                return new HomeFramHolderView();
            }
        }, homelist);

        mViewPager.setAdapter(pageAdapter, true);
        mViewPager.setChangeViewCallback(new LoopViewPager.ChangeViewCallback() {
            @Override
            public void changeView(boolean left, boolean right) {
                if (right) {
                    curDate = DateUtil.getSpecifiedDayBefore(curDate, "yyyy.MM.dd");
                    time.setText(curDate + "  " + DateUtil.getWeekDay(curDate, "yyyy.MM.dd"));
                } else if (left) {
                    curDate = DateUtil.getSpecifiedDayAfter(curDate, "yyyy.MM.dd");
                    time.setText(curDate + "  " + DateUtil.getWeekDay(curDate, "yyyy.MM.dd"));
                }
                if (DateUtil.getCurrentTime("yyyy.MM.dd").equals(curDate)) {
                    mViewPager.setIsCanLeftScroll(false);
                    refreshDataRightAndLeft(DateUtil.getStringFromTime(DateUtil.getTimeFromString(curDate, "yyyy.MM.dd"), "yyyy-MM-dd"));
                } else {
                    mViewPager.setIsCanLeftScroll(true);
                    refreshDataRightAndLeft(DateUtil.getStringFromTime(DateUtil.getTimeFromString(curDate, "yyyy.MM.dd"), "yyyy-MM-dd"));
                }
            }

            @Override
            public void getCurrentPageIndex(int position) {
            }
        });
    }


    private List<HomeLayout> homelist = new ArrayList<>();



    @Override
    protected void onUserVisible() {


    }

    @Override
    protected void onUserInvisible() {
    }

    @Override
    public boolean bindEvents() {
        return true;
    }

    @Override
    public void showData(List data, boolean canNext) {

    }

    @Override
    public void showLoadingProgress(final boolean show) {
        bgaRefreshLayout.endRefreshing();
    }

    @Override
    public void showMessage(final String mes) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), mes, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void showError(String mes) {

    }

    @Override
    public void onTokenError() {
        getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    public void refreshData(boolean showLoading) {
        mViewPager.setCurrentItem(mViewPager.getLastItem(), false);
        mHomePresenter.loadData(mFk_grade_class_id, DateUtil.getStringFromTime(DateUtil.getTimeFromString(curDate, "yyyy.MM.dd"),
                "yyyy-MM-dd"), showLoading);
    }

    public void refreshData(String time) {
        mHomePresenter.loadData(mFk_grade_class_id, time, false);
    }

    public void refreshDataRightAndLeft(String time) {
        mHomePresenter.loadData(mFk_grade_class_id, time, true);
    }

    private String curDate = "";

    public void initDate() {
        curDate = DateUtil.getDate("yyyy.MM.dd");
        DateUtil.getStringFromTime(DateUtil.getTimeFromString(curDate, "yyyy.MM.dd"), "yyyy-MM-dd");
        time.setText(curDate + "  " + DateUtil.getCurrentWeekDay());
    }

    @Override
    public void updateMainPageList(final List data) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                KLog.e(((HomeLayout) mViewPager.getAdapter().getPrimaryItem()));
                KLog.e(((HomeLayout) mViewPager.getCurItem()));
                KLog.e(((HomeLayout) mViewPager.getChildAt(mViewPager.getCurrentItem())));
                KLog.e(((HomeLayout) mViewPager.findViewById(mViewPager.getCurrentItem())));
                ((HomeLayout) mViewPager.getAdapter().getPrimaryItem()).updateBanner(data);
            }
        });
    }

    @Override
    public void updateNewsList(List data) {
        ((HomeLayout) mViewPager.getAdapter().getPrimaryItem()).updateList(data);
    }

    private void iniBgaRefreshLayout() {
        bgaRefreshLayout.setDelegate(this);
        BGAStickinessRefreshViewHolder stickinessRefreshViewHolder = new BGAStickinessRefreshViewHolder(getActivity()
                , false);
        stickinessRefreshViewHolder.setStickinessColor(R.color.colorPrimary);
        stickinessRefreshViewHolder.setRotateImage(R.mipmap.bga_refresh_stickiness);
        stickinessRefreshViewHolder.setRefreshViewBackgroundColorRes(R.color.white);
        bgaRefreshLayout.setRefreshViewHolder(stickinessRefreshViewHolder);
    }


    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        refreshData(DateUtil.getStringFromTime(DateUtil.getTimeFromString(curDate, "yyyy.MM.dd"),
                "yyyy-MM-dd"));
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }

    @OnClick(R.id.btn_public)
    public void Public() {
        showPopup();
    }

    private static final int REQUEST_CAMERA_CODE = 10;
    private ArrayList<String> imagePaths = new ArrayList<>();

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                // 选择照片
                case REQUEST_CAMERA_CODE:
                    ArrayList<String> list = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
                    Intent intent1 = new Intent(getActivity(), PublishActivity.class);
                    intent1.putExtra("type", 1);
                    intent1.putExtra("imgs", list);
                    startActivity(intent1);
                    break;

                case SELECT_PIC_BY_TACK_PHOTO:
                    ArrayList<String> list1 = new ArrayList<>();
                    list1.add(imageFilePath);
                    Intent intent = new Intent(getActivity(), PublishActivity.class);
                    intent.putExtra("type", 1);
                    intent.putExtra("imgs", list1);
                    startActivity(intent);
                    break;
            }
        }
    }


    private void showPopup() {
        if (WineApplication.getInstance().isTeacher()){
            final String[] stringItems = {"视频","照片", "文字"};
            final CustomActionSheetDialog dialog = new CustomActionSheetDialog(getActivity(), stringItems, null);
            dialog.isTitleShow(false).isCancelShow(false)
                    .itemTextColor(getActivity().getResources().getColor(R.color.ese_white))
                    .itemPressColor(getActivity().getResources().getColor(R.color.ese_orange))
                    .lvBgColor(getActivity().getResources().getColor(R.color.baby_orange))
                    .show();

            dialog.setOnOperItemClickL(new OnOperItemClickL() {
                @Override
                public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                    dialog.dismiss();
                    switch (position) {
                        case 0:
//                        mHomePresenter.postVideoPic();
                            Intent video = new Intent(getActivity(), VideoRecordActivity.class);
                            startActivity(video);
                            break;
                        case 1:
                            showPopup2();
                            break;
                        case 2:
                            Intent intent1 = new Intent(getActivity(), PublishActivity.class);
                            intent1.putExtra("type", 2);
                            startActivity(intent1);
                            break;
                        default:
                            break;
                    }
                }
            });
        }else{
            final String[] stringItems = {"视频","照片"};
            final CustomActionSheetDialog dialog = new CustomActionSheetDialog(getActivity(), stringItems, null);
            dialog.isTitleShow(false).isCancelShow(false)
                    .itemTextColor(getActivity().getResources().getColor(R.color.ese_white))
                    .itemPressColor(getActivity().getResources().getColor(R.color.ese_orange))
                    .lvBgColor(getActivity().getResources().getColor(R.color.baby_orange))
                    .show();

            dialog.setOnOperItemClickL(new OnOperItemClickL() {
                @Override
                public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                    dialog.dismiss();
                    switch (position) {
                        case 0:
                            Intent video = new Intent(getActivity(), VideoRecordActivity.class);
                            startActivity(video);
                            break;
                        case 1:
                            showPopup2();
                            break;
                        default:
                            break;
                    }
                }
            });
        }

    }




    private void showPopup2() {
        final String[] stringItems = {"拍照", "相册"};
        final CustomActionSheetDialog dialog = new CustomActionSheetDialog(getActivity(), stringItems, null);
        dialog.isTitleShow(false).isCancelShow(false)
                .itemTextColor(getActivity().getResources().getColor(R.color.ese_white))
                .itemPressColor(getActivity().getResources().getColor(R.color.ese_orange))
                .lvBgColor(getActivity().getResources().getColor(R.color.baby_orange))
                .show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                switch (position) {
                    case 0:
                        takePhoto();
                        break;
                    case 1:
                        PhotoPickerIntent intent = new PhotoPickerIntent(getActivity());
                        intent.setSelectModel(SelectModel.MULTI);
                        intent.setShowCarema(true); // 是否显示拍照
                        intent.setMaxTotal(9); // 最多选择照片数量，默认为6
                        intent.setSelectedPaths(imagePaths); // 已选中的照片地址， 用于回显选中状态
                        startActivityForResult(intent, REQUEST_CAMERA_CODE);
                        break;
                    default:
                        break;
                }
            }
        });

    }


    private Uri photoUri;
    /***
     * 使用照相机拍照获取图片
     */
    public static final int SELECT_PIC_BY_TACK_PHOTO = 1;
    private String imageFilePath;

    /**
     * 拍照获取图片
     */
    private void takePhoto() {
        //执行拍照前，应该先判断SD卡是否存在
        String SDState = Environment.getExternalStorageState();
        if (SDState.equals(Environment.MEDIA_MOUNTED)) {
            // 拍照
            //设置图片的保存路径,作为全局变量
            imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/filename.jpg";
            File temp = new File(imageFilePath);
            Uri imageFileUri = Uri.fromFile(temp);//获取文件的Uri
            Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//跳转到相机Activity
            it.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);//告诉相机拍摄完毕输出图片到指定的Uri
            startActivityForResult(it, SELECT_PIC_BY_TACK_PHOTO);
        } else {
            showToast("内存卡不存在");
        }
    }
}
