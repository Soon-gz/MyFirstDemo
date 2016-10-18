package com.abings.baby.ui.waterfall.photoviewpagedetail;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abings.baby.R;
import com.abings.baby.WineApplication;
import com.abings.baby.data.model.PhotoInfo;
import com.abings.baby.data.remote.RetrofitUtils;
import com.abings.baby.ui.base.BaseActivity;
import com.abings.baby.ui.home2.CustomActionSheetDialog;
import com.abings.baby.ui.home2.ShareBottomDialog;
import com.abings.baby.ui.waterfall.BackgroundToForegroundTransformer;
import com.abings.baby.ui.waterfall.photoviewpagedetail.fragment.EventClick;
import com.abings.baby.ui.waterfall.photoviewpagedetail.fragment.PhotoDatailFragment;
import com.abings.baby.utils.ProgressDialogHelper;
import com.abings.baby.utils.StringUtils;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class PhotoViewpageDetailActivity extends BaseActivity implements PhotoViewpageMvpView {

    private List<String> imagesList;
    private PhotoInfo mInfo;
    private boolean isPhoto = false;
    private int nums;
    private List<PhotoDatailFragment> listFragments;
    private PhotoviewpageAdapter adapter;
    private int currentPosition;
    private int favorites = 0;
    private boolean isHide = false;

    @Inject
    PhotoViewpageDetailPrecenter photoDetailPresenter;

    @Bind(R.id.viewpage)
    PhotoViewPager viewPager;
    @Bind(R.id.title_center)
    TextView titleCenter;
    @Bind(R.id.text_name)
    TextView textName;
    @Bind(R.id.favorite_check)
    ImageView favoriteCheck;
    @Bind(R.id.favorite_num)
    TextView favoriteNum;
    @Bind(R.id.header)
    RelativeLayout relativeLayout_head;
    @Bind(R.id.footer)
    RelativeLayout relativeLayout_foot;

    @Bind(R.id.btn_share)
    ImageView btnshare;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_photo_viewpage_detail;
    }

    @Override
    protected void iniInjector() {
        activityComponent().inject(this);
        photoDetailPresenter.attachView(this);
    }

    @Override
    protected boolean toggleOverridePendingTransition() {
        return false;
    }

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return null;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    public void onEventMainThread(EventClick eventClick) {
        if ("img".equals(eventClick.getMes())) {
            if (isHide) {
                relativeLayout_head.setVisibility(View.VISIBLE);
                relativeLayout_foot.setVisibility(View.VISIBLE);
                isHide = false;
            } else {
                relativeLayout_head.setVisibility(View.GONE);
                relativeLayout_foot.setVisibility(View.GONE);
                isHide = true;
            }
        }
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        listFragments = new ArrayList<>();
        EventBus.getDefault().register(this);
        if (getIntent().hasExtra("waterfall")) {
            isPhoto = true;
            imagesList = getIntent().getStringArrayListExtra("waterfall");
//            Log.i("TAG00", "数量：" + imagesList.size());
        } else if (getIntent().hasExtra("newslist")) {
            isPhoto = false;
            imagesList = getIntent().getStringArrayListExtra("newslist");
        }
        if (getIntent().hasExtra("position")) {
            currentPosition = getIntent().getIntExtra("position", 0);
        }

//        Log.i("TAG00","当前位置："+currentPosition);
        nums = imagesList.size();

        for (int i = 0; i < nums; i++) {
            PhotoDatailFragment photoDatailFragment = new PhotoDatailFragment();
            listFragments.add(photoDatailFragment);
        }
        adapter = new PhotoviewpageAdapter(getSupportFragmentManager(), listFragments);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(currentPosition);
        photoDetailPresenter.loadData(imagesList.get(currentPosition));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                photoDetailPresenter.loadData(imagesList.get(position));
                if (position > 0 && position + 1 < imagesList.size()) {
//                    Log.i("TAG00","前一个type:"+listFragments.get(position -1).getType());
//                    Log.i("TAG00","后一个type:"+listFragments.get(position +1).getType());
                    if ("video".equals(listFragments.get(position - 1).getType())) {
                        listFragments.get(position - 1).setStop();
                    } else if ("video".equals(listFragments.get(position + 1).getType())) {
                        listFragments.get(position + 1).setStop();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setOffscreenPageLimit(5);
        viewPager.setPageTransformer(false,new BackgroundToForegroundTransformer());
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }

    @Override
    public boolean bindEvents() {
        return true;
    }

    @Override
    public void showData(List data, boolean canNext) {

    }

    @Override
    public void showLoadingProgress(boolean show) {
        if (show) {
            ProgressDialogHelper.getInstance().showProgressDialog(this, "正在下载中...");
        } else {
            ProgressDialogHelper.getInstance().hideProgressDialog();
        }
    }

    @Override
    public void showMessage(final String mes) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(PhotoViewpageDetailActivity.this, mes, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void showError(String mes) {

    }

    @Override
    public void onTokenError() {

    }

    @OnClick(R.id.btn_back)
    void toBack() {
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        photoDetailPresenter.detachView();
    }

    @Override
    public void showDialog(String msg) {
        ProgressDialogHelper.getInstance().showProgressDialog(this, msg);
    }

    @Override
    public void updateView(PhotoInfo info) {
        StringBuilder time = new StringBuilder();
        String[] times;
        String[] times0;
        String[] datenames = {"年", "月", "日"};
        if (info != null) {
            mInfo = info;
            if (info.getFk_user_type().equals("2") && !StringUtils.isEmpty(info.getUser_name())) {
                textName.setText("来自:" + info.getUser_name());
            } else if (!StringUtils.isEmpty(info.getTeacher_name())){
                textName.setText("来自:" + info.getTeacher_name());
            }
            if ("video".equals(info.getType())){
                btnshare.setVisibility(View.GONE);
            }else{
                btnshare.setVisibility(View.VISIBLE);
            }
            if (!StringUtils.isEmpty(info.getLike_count()) && Integer.valueOf(info.getLike_count()) > 0) {
                favoriteNum.setText(info.getLike_count());
                favoriteCheck.setImageResource(R.mipmap.icon_product_collect_checked);
                favorites = Integer.parseInt(info.getLike_count());
            } else {
                favoriteNum.setText("");
                favoriteCheck.setImageResource(R.mipmap.icon_product_collect);
            }
            listFragments.get(currentPosition).setImg(mInfo);
            times0 = mInfo.getCreate_datetime().split(" ");
            times = times0[0].split("/");
            for (int i = 0; i < times.length; i++) {
                time.append(times[i]);
                time.append(datenames[i]);
            }
            titleCenter.setText(time);
        }
    }


    @OnClick(R.id.favorite_check)
    void toFavorite() {
        if (mInfo != null) {
            photoDetailPresenter.likeAddCancle(mInfo);
        }
    }

    @Override
    public void finishActivity() {
        this.finish();
    }

    @Override
    public void dialogDismiss() {
        ProgressDialogHelper.getInstance().hideProgressDialog();
    }

    @OnClick(R.id.btn_share)
    void toShare() {
        showPopup();
    }

    private void showPopup() {
        if (!isPhoto) {
            if (WineApplication.getInstance().isTeacher()) {
                final String[] stringItems = {"保存至班级相册","保存到手机相册", "分享", "删除"};
                final CustomActionSheetDialog dialog = new CustomActionSheetDialog(this, stringItems, null);
                dialog.isTitleShow(false).isCancelShow(false)
                        .itemTextColor(getResources().getColor(R.color.ese_white))
                        .itemPressColor(getResources().getColor(R.color.ese_orange))
                        .lvBgColor(getResources().getColor(R.color.baby_orange))
                        .show();
                dialog.setOnOperItemClickL(new OnOperItemClickL() {
                    @Override
                    public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                        dialog.dismiss();
                        switch (position) {
                            case 1:
                                if (mInfo == null || mInfo.getImage() == null) {
                                    showToast("没有可用图片保存");
                                    return;
                                }
                                photoDetailPresenter.saveImage(mInfo.getImage());
                                break;
                            case 2:
                                if (mInfo == null || mInfo.getImage() == null) {
                                    showToast("没有图片可供分享");
                                    return;
                                }
                                showSharePopup();
                                break;
                            case 3:
                                if (mInfo == null || mInfo.getImage() == null) {
                                    showToast("没有图片可删");
                                    return;
                                } else {
                                    photoDetailPresenter.deletePic(mInfo);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                });
            } else {
                final String[] stringItems = {"保存至宝宝相册", "保存到手机相册", "分享"};
                final CustomActionSheetDialog dialog = new CustomActionSheetDialog(this, stringItems, null);
                dialog.isTitleShow(false).isCancelShow(false)
                        .itemTextColor(getResources().getColor(R.color.ese_white))
                        .itemPressColor(getResources().getColor(R.color.ese_orange))
                        .lvBgColor(getResources().getColor(R.color.baby_orange))
                        .show();
                dialog.setOnOperItemClickL(new OnOperItemClickL() {
                    @Override
                    public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                        dialog.dismiss();
                        switch (position) {
                            case 0:
                                if (mInfo == null || mInfo.getImage() == null) {
                                    showToast("没有可用图片保存");
                                    return;
                                }
                                photoDetailPresenter.saveImgToBaby(WineApplication.getInstance().getClassId(), "4", "", mInfo.getPk_image_news_image_id());
                                break;
                            case 1:
                                if (mInfo == null || mInfo.getImage() == null) {
                                    showToast("没有可用图片保存");
                                    return;
                                }
                                photoDetailPresenter.saveImage(mInfo.getImage());
                                break;
                            case 2:
                                if (mInfo == null || mInfo.getImage() == null) {
                                    showToast("没有图片可供分享");
                                    return;
                                }
                                showSharePopup();
                                break;
//                            case 3:
//                                if (mInfo == null || mInfo.getImage() == null) {
//                                    showToast("没有图片可删");
//                                    return;
//                                } else {
//                                    photoDetailPresenter.deletePic(mInfo);
//                                }
//                                break;
                            default:
                                break;
                        }
                    }
                });
            }

        } else {
            final String[] stringItems = {"保存到本地", "分享", "删除"};
            final CustomActionSheetDialog dialog = new CustomActionSheetDialog(this, stringItems, null);
            dialog.isTitleShow(false).isCancelShow(false)
                    .itemTextColor(getResources().getColor(R.color.ese_white))
                    .itemPressColor(getResources().getColor(R.color.ese_orange))
                    .lvBgColor(getResources().getColor(R.color.baby_orange))
                    .show();
            dialog.setOnOperItemClickL(new OnOperItemClickL() {
                @Override
                public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                    dialog.dismiss();
                    switch (position) {
                        case 0:
                            if (mInfo == null || mInfo.getImage() == null) {
                                showToast("没有可用图片保存");
                                return;
                            }
                            photoDetailPresenter.saveImage(mInfo.getImage());
                            break;
                        case 1:
                            if (mInfo == null || mInfo.getImage() == null) {
                                showToast("没有图片可供分享");
                                return;
                            }
                            showSharePopup();
                            break;
                        case 2:
                            if (mInfo == null || mInfo.getImage() == null) {
                                showToast("没有图片可删");
                                return;
                            } else {
                                photoDetailPresenter.deletePic(mInfo);
                            }
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }

    ShareBottomDialog dialog;

    void showSharePopup() {
        dialog = new ShareBottomDialog(this, null);
        dialog.setListner(new ShareBottomDialog.onItemClickLitener() {
            @Override
            public void onItemClick(int position) {
                dialog.dismiss();
                share(position);
            }
        }).show();
    }

    void share(int i) {
        UMImage image = null;
        if (mInfo.getImage() != null) {
            image = new UMImage(this, RetrofitUtils.BASE_IMG_URL + mInfo.getImage());
            image.setTitle(mInfo.getContent());
        }

        switch (i) {
            case 0:
                dialog.shareWeixin(PhotoViewpageDetailActivity.this, "HelloBaby", mInfo.getContent(), image, umShareListener, false);
                break;
            case 1:
                dialog.shareWeixinCircle(PhotoViewpageDetailActivity.this, "HelloBaby", mInfo.getContent(), image, umShareListener, false);
                break;
            case 2:
                dialog.shareQQ(PhotoViewpageDetailActivity.this, "HelloBaby", mInfo.getContent(), image, umShareListener);
                break;
            case 3:
                dialog.shareWeibo(PhotoViewpageDetailActivity.this, "HelloBaby", mInfo.getContent(), image, umShareListener);
                break;
            default:
                break;
        }
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(PhotoViewpageDetailActivity.this, platform + " 分享成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(PhotoViewpageDetailActivity.this, platform + " 分享失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(PhotoViewpageDetailActivity.this, platform + " 分享取消", Toast.LENGTH_SHORT).show();
        }
    };

}
