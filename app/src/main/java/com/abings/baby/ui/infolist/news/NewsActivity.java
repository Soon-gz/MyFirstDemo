package com.abings.baby.ui.infolist.news;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.abings.baby.R;
import com.abings.baby.data.model.NewsDetail;
import com.abings.baby.ui.base.BaseActivity;
import com.abings.baby.ui.home2.CustomActionSheetDialog;
import com.abings.baby.ui.home2.ShareBottomDialog;
import com.abings.baby.ui.publishvideo.VideoPlayActivity;
import com.abings.baby.ui.waterfall.photoviewpagedetail.PhotoViewpageDetailActivity;
import com.abings.baby.utils.ContentToPictureUtils;
import com.abings.baby.utils.ProgressDialogHelper;
import com.abings.baby.utils.StringUtils;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by huangbin on 16/2/17.
 */
public class NewsActivity extends BaseActivity implements NewsMvpView {

    @Inject
    NewsPresenter photoDetailPresenter;
    @Bind(R.id.btn_back)
    ImageView btnBack;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.btn_share)
    ImageView btnShare;
    @Bind(R.id.text_subtitle)
    TextView textSubtitle;
    @Bind(R.id.favorite_check)
    ImageView favoriteCheck;
    @Bind(R.id.favorite_num)
    TextView favoriteNum;
    @Bind(R.id.text_content)
    TextView textContent;
    @Bind(R.id.rv_recyclerview_data)
    RecyclerView mDataRv;
    @Bind(R.id.scrollview)
    ScrollView mscrollview;
    @Bind(R.id.fromname)
    TextView fromName;

    public static final int IMAGE_SIZE=32768;//微信分享图片大小限制

    private ArrayList<String>newslist;

    private String pk_image_news_id;
    private NewsDetail mNewsDetail;
    private boolean isFirstIn = true;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_news;
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (isFirstIn){
            isFirstIn = false;
            return;
        }
        newslist.clear();
        photoDetailPresenter.loadData(pk_image_news_id);
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        newslist = new ArrayList<>();
        if (getIntent().hasExtra("from_search")) {
            pk_image_news_id = getIntent().getStringExtra("pk_image_news_id");
            title.setText("搜索结果");
        } else if (getIntent().hasExtra("tag_id")) {
            pk_image_news_id = getIntent().getStringExtra("pk_image_news_id");
            switch (Integer.valueOf(getIntent().getStringExtra("tag_id"))) {
                case 1:
                    title.setText("食谱");
                    break;
                case 2:
                    title.setText("拓展");
                    break;
                case 3:
                    title.setText("活动");
                    break;
                case 4:
                    title.setText("动态");
                    break;
            }

        }
        photoDetailPresenter.loadData(pk_image_news_id);
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
        showToast(mes);
    }

    @Override
    public void showError(String mes) {

    }

    @Override
    public void onTokenError() {
        startLoginActivity();
    }

    private StaggeredHomeAdapter mStaggeredHomeAdapter;

    private void iniAdapter() {
        if (mStaggeredHomeAdapter == null){
            mDataRv.setLayoutManager(new ExStaggeredGridLayoutManager(2,
                    StaggeredGridLayoutManager.VERTICAL));
            mStaggeredHomeAdapter = new StaggeredHomeAdapter(NewsActivity.this, mNewsDetail.getImages());
            mDataRv.setAdapter(mStaggeredHomeAdapter);
            initEvent();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDataRv = null;
        mStaggeredHomeAdapter = null;
        mNewsDetail = null;
        photoDetailPresenter.detachView();
    }

    private void initEvent() {
        mStaggeredHomeAdapter.setOnItemClickLitener(new StaggeredHomeAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                if ("video".equals(mNewsDetail.getImages().get(position).getType())){
                    Intent intent = new Intent(NewsActivity.this, VideoPlayActivity.class);
                    intent.putExtra("video", mNewsDetail.getImages().get(position).getVideo());
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(NewsActivity.this, PhotoViewpageDetailActivity.class);
                    intent.putExtra("newslist", newslist);
                    intent.putExtra("position",position);
                    startActivity(intent);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
    }


    @Override
    public void updateView(NewsDetail info) {
        if (info != null) {
            mNewsDetail = info;
            if (mNewsDetail.getImages() != null && mNewsDetail.getImages().size() > 0){
                iniAdapter();
                mStaggeredHomeAdapter.setDatas(mNewsDetail.getImages());
                mStaggeredHomeAdapter.notifyDataSetChanged();
            }else{
                mDataRv.setVisibility(View.GONE);
            }


            textSubtitle.setText(mNewsDetail.getCreate_datetime());
            textContent.setText(mNewsDetail.getContent());
            fromName.setText(mNewsDetail.getName());
            if (!StringUtils.isEmpty(mNewsDetail.getLike_count()) && Integer.valueOf(info.getLike_count()) > 0) {
                favoriteNum.setText(mNewsDetail.getLike_count());
                favoriteCheck.setImageResource(R.mipmap.icon_product_collect_checked);
            } else {
                favoriteNum.setText("");
                favoriteCheck.setImageResource(R.mipmap.icon_product_collect);
            }
            if(mNewsDetail.getImages()!=null){
                for (int i = 0; i < mNewsDetail.getImages().size(); i++) {
                    newslist.add(mNewsDetail.getImages().get(i).getPk_image_news_image_id());
                }
            }
        }
    }

    @Override
    public void updateFavorite(boolean flage) {
        if (flage) {
            favoriteCheck.setImageResource(R.mipmap.icon_product_collect_checked);
        } else {
            favoriteCheck.setImageResource(R.mipmap.icon_product_collect);
        }
        photoDetailPresenter.loadData(pk_image_news_id);
    }

    @Override
    public void showDialog(String msg) {
        ProgressDialogHelper.getInstance().showProgressDialog(this, msg);
    }

    @Override
    public void finishActivity() {
        NewsActivity.this.finish();
    }

    @Override
    public void dialogDismiss() {
        ProgressDialogHelper.getInstance().hideProgressDialog();
    }

    @OnClick(R.id.btn_back)
    void toBack() {
        finish();
    }

    @OnClick(R.id.favorite_check)
    void toFavorite() {
        if (mNewsDetail != null) {
            photoDetailPresenter.likeAddCancle(mNewsDetail.getPk_image_news_id());
        }
    }

    @OnClick(R.id.btn_share)
    void toShare() {
        showPopup();
    }

    private void showPopup() {
        final String[] stringItems = {"分享", "删除"};
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
                        if (mNewsDetail == null) {
                            showToast("没有内容可供分享");
                            return;
                        }
                        showSharePopup();
                        break;
                    case 1:
                        photoDetailPresenter.deletePic(mNewsDetail.getPk_image_news_id());
                        break;
                    default:
                        break;
                }
            }
        });

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
        if (mNewsDetail.getImages() != null && mNewsDetail.getImages().size() > 0) {
            Bitmap bmp=ContentToPictureUtils.getBitmapByView(mscrollview);
            int options = 100;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, output);
            int options2 = 100;
            while (output.toByteArray().length > IMAGE_SIZE && options2 != 10) {
                output.reset(); //清空baos
                bmp.compress(Bitmap.CompressFormat.JPEG, options2, output);//这里压缩options%，把压缩后的数据存放到baos中
                options2 -= 10;
            }
            bmp= BitmapFactory.decodeByteArray(output.toByteArray(), 0, output.toByteArray().length);
            image=new UMImage(this,bmp);
        }

        switch (i) {
            case 0:
                dialog.shareWeixin(NewsActivity.this, "HelloBaby", mNewsDetail.getContent(), image, umShareListener,false);
                break;
            case 1:
                dialog.shareWeixinCircle(NewsActivity.this, "HelloBaby", mNewsDetail.getContent(), image, umShareListener,true);
                break;
            case 2:
                dialog.shareQQ(NewsActivity.this, "HelloBaby", mNewsDetail.getContent(), image, umShareListener);
                break;
            case 3:
                dialog.shareWeibo(NewsActivity.this, "HelloBaby", mNewsDetail.getContent(), image, umShareListener);
                break;
            default:
                break;
        }
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(NewsActivity.this, platform + " 分享成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(NewsActivity.this, platform + " 分享失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(NewsActivity.this, platform + " 分享取消", Toast.LENGTH_SHORT).show();
        }
    };

}
