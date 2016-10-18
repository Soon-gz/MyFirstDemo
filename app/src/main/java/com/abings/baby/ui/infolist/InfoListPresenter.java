package com.abings.baby.ui.infolist;

import android.content.Context;

import com.abings.baby.data.DataManager;
import com.abings.baby.data.model.NewsListItem;
import com.abings.baby.data.remote.custom.SubscriberJSONObject;
import com.abings.baby.injection.ActivityContext;
import com.abings.baby.ui.base.Presenter;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/1/18.
 */
public class InfoListPresenter implements Presenter<InfoListMvpView> {

    private final DataManager mDataManager;
    public Subscription mSubscription;
    private InfoListMvpView mMvpView;
    private Context mContext;

    @Inject
    public InfoListPresenter(DataManager dataManager, @ActivityContext Context context) {
        mDataManager = dataManager;
        mContext = context;
    }

    @Override
    public void attachView(InfoListMvpView mvpView) {
        mMvpView = mvpView;
        if (mMvpView.bindEvents()) {
            mDataManager.getEventBus().register(this);
        }
    }

    @Override
    public void detachView() {
        if (mMvpView.bindEvents()) {
            mDataManager.getEventBus().unregister(this);
        }
        mMvpView = null;
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    public void loadData(String tag, String class_id, int pageNum, boolean showLoading) {
        if (mSubscription != null) mSubscription.unsubscribe();
        mSubscription = mDataManager.postNewList(tag, class_id, pageNum).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new SubscriberJSONObject(mContext) {
                    @Override
                    protected void onNextJSONObject(JSONObject result) throws JSONException {
                        Gson gson = new Gson();
                        JSONArray array = result.getJSONArray("result");
                        if (array != null && array.length() > 0) {
                            List<NewsListItem> list = new ArrayList<NewsListItem>();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);
                                NewsListItem item = new NewsListItem();
                                item.setContent(obj.getString("content"));
                                item.setCreate_datetime(obj.getString("create_datetime"));
                                item.setImage_count(obj.getString("image_count"));
                                item.setLike_count(obj.getString("like_count"));
                                item.setPk_image_news_id(obj.getString("pk_image_news_id"));
                                if (Integer.valueOf(item.getImage_count()) > 0 && !obj.isNull("images")) {
                                    Object jjj = obj.get("images");
                                    if (jjj instanceof JSONArray) {
                                        JSONArray imagesArray = obj.getJSONArray("images");
                                        if (imagesArray != null && imagesArray.length() > 0) {
                                            List<NewsListItem.ImagesBean> list1 = new ArrayList<>();
                                            for (int j = 0; j < imagesArray.length(); j++) {
                                                NewsListItem.ImagesBean data = gson.fromJson(imagesArray.getJSONObject
                                                        (j).toString(), NewsListItem.ImagesBean.class);
                                                list1.add(data);
                                            }
                                            item.setImages(list1);
                                        }
                                    }
                                }
                                list.add(item);
                            }

                            mMvpView.showData(list, list.size() == 10);
                        };
                    }
                });
    }

    public void parseNewsList(JSONObject result) {

    }

}
