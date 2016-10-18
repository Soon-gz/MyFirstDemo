package com.abings.baby.ui.waterfall;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.abings.baby.WineApplication;
import com.abings.baby.data.DataManager;
import com.abings.baby.data.local.PhotoUpAlbumHelper;
import com.abings.baby.data.model.WaterFallGridHeaderItem;
import com.abings.baby.data.model.WaterFallGridItem;
import com.abings.baby.data.remote.BaseBusEvent;
import com.abings.baby.data.remote.custom.SubscriberClass;
import com.abings.baby.injection.ActivityContext;
import com.abings.baby.ui.base.Presenter;
import com.abings.baby.utils.AssetsUtils;
import com.abings.baby.utils.DateUtil;
import com.abings.baby.utils.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/1/18.
 */
public class WaterfallPresenter implements Presenter<WaterfallMvpView> {

    private final DataManager mDataManager;
    public Subscription mSubscription;
    private WaterfallMvpView mMvpView;
    private Activity mActivity;
    private JSONObject mCachedRibots;
    private Map<Integer,List<WaterFallGridItem>> mapData = new HashMap<>();
    private Context context;

    @Inject
    public WaterfallPresenter(DataManager dataManager, PhotoUpAlbumHelper photoUpAlbumHelper,@ActivityContext Context context) {
        mDataManager = dataManager;
        this.context = context;
    }

    @Override
    public void attachView(WaterfallMvpView mvpView) {
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

    public void dismissProgress(final boolean showLoading) {
        if (showLoading) {
            mMvpView.showLoadingProgress(false);
        }
    }

    public void showLoadingProgress(final boolean showLoading) {
        if (showLoading) {
            mMvpView.showLoadingProgress(false);
        }
    }

    public void showError(String msg, final boolean showLoading) {
        if (showLoading) {
            mMvpView.showLoadingProgress(false);
            mMvpView.showError(msg);
        }
    }

    public void loadData(int pageNum, String class_id, boolean showLoading) {
//        mDataManager.posEvent(new BaseBusEvent("test"));
        loadData(false, pageNum, class_id, showLoading);
    }

    public void loadData(boolean allowMemoryCacheVersion,final int pageNum, String class_id, final boolean showLoading) {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }

        mSubscription = getListObservable(allowMemoryCacheVersion,pageNum,class_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SubscriberClass<WaterFallGridItem>(context,WaterFallGridItem.class) {
                    @Override
                    protected void onNextList(List<WaterFallGridItem> waterFallGridItems) {
                        mGirdList.clear();
                        List<WaterFallGridItem> list = new ArrayList<>();
                        boolean isContainsKey = mapData.containsKey(pageNum);
                        if(!isContainsKey){
                            mapData.put(pageNum, waterFallGridItems);
                            for (Map.Entry<Integer,List<WaterFallGridItem>> entry: mapData.entrySet()) {
                                int size = list.size();
                                list.addAll(size,entry.getValue());
                            }
                        }
                        mMvpView.setImgS(list);
                        for (WaterFallGridItem item : list) {
                            item.setCreate_datetime(paserTimeToChinese(item.getCreate_datetime()));
                        }

                        Collections.sort(list, new Comparator<WaterFallGridItem>() {
                            @Override
                            public int compare(WaterFallGridItem lhs, WaterFallGridItem rhs) {
                                return rhs.getCreate_datetime().compareTo(lhs.getCreate_datetime());
                            }
                        });

                        //Insert headers into list of items.
                        String lastHeader = "";
                        int sectionManager = -1;
                        int headerCount = 0;
                        int sectionFirstPosition = 0;

                        WaterFallGridItem tmp = new WaterFallGridItem();
                        tmp.setCreate_datetime(DateUtil.getCurrentTime("yyyy/MM/dd"));
                        list.add(0, tmp);

                        WaterFallGridHeaderItem item;
                        int listSize = list.size();
                        for (int i = 0; i < listSize; i++) {
                            WaterFallGridItem mGridItem1 = list.get(i);
                            String header = mGridItem1.getCreate_datetime();
                            if (!TextUtils.equals(lastHeader, header)) {
                                sectionManager = (sectionManager + 1) % 2;
                                sectionFirstPosition = i + headerCount;
                                lastHeader = header;
                                headerCount += 1;

                                item = new WaterFallGridHeaderItem(header, true, sectionFirstPosition);
                                mGirdList.add(item);
                            }
                            item = new WaterFallGridHeaderItem();
                            item.setItem(mGridItem1);
                            item.setIsHeader(false);
                            item.setSectionFirstPosition(sectionFirstPosition);
                            mGirdList.add(item);
                        }
                        mMvpView.showData(mGirdList, waterFallGridItems.size() < 50);
                    }
                });

    }


    private Observable<JSONObject> getListObservable(boolean allowMemoryCacheVersion, int pageNum, String class_id) {
        if (allowMemoryCacheVersion && mCachedRibots != null) {
            return Observable.just(mCachedRibots);
        } else {
            return mDataManager.postWaterfallList(pageNum, class_id);
//                    .observeOn(AndroidSchedulers.mainThread()).subscribeOn
//                    (Schedulers.io());
        }
    }

    private void onEventMainThread(BaseBusEvent event) {
        String data = AssetsUtils.getDataFromAssets(WineApplication.getInstance(), "home/waterfall.json");
        try {
            JSONObject obj = new JSONObject(data);
            JSONArray jsonArray = obj.getJSONArray("result");
            Gson gson = new Gson();
            List<WaterFallGridItem> list = new ArrayList<WaterFallGridItem>();
            Type type = new TypeToken<ArrayList<WaterFallGridItem>>() {
            }.getType();
            list = gson.fromJson(jsonArray.toString(), type);



            for (WaterFallGridItem item : list) {
                item.setCreate_datetime(paserTimeToChinese(item.getCreate_datetime()));

            }
            Collections.sort(list, new Comparator<WaterFallGridItem>() {
                @Override
                public int compare(WaterFallGridItem lhs, WaterFallGridItem rhs) {
                    return lhs.getCreate_datetime().compareTo(rhs.getCreate_datetime());
                }
            });


            //Insert headers into list of items.
            String lastHeader = "";
            int sectionManager = -1;
            int headerCount = 0;
            int sectionFirstPosition = 0;
            for (int i = 0; i < list.size(); i++) {
                WaterFallGridItem mGridItem1 = list.get(i);
                WaterFallGridHeaderItem item;
                String header = mGridItem1.getCreate_datetime();
                if (!TextUtils.equals(lastHeader, header)) {
                    sectionManager = (sectionManager + 1) % 2;
                    sectionFirstPosition = i + headerCount;
                    lastHeader = header;
                    headerCount += 1;
                    item = new WaterFallGridHeaderItem(header, true, sectionFirstPosition);
                    mGirdList.add(item);
                }
                item = new WaterFallGridHeaderItem();
                item.setItem(mGridItem1);
                item.setIsHeader(false);
                item.setSectionFirstPosition(sectionFirstPosition);
                mGirdList.add(item);
            }
            mMvpView.showData(mGirdList, mGirdList.size() == 50);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private List<WaterFallGridHeaderItem> mGirdList = new ArrayList<>();
    private ImageScanner mScanner;
    private Map<String, Integer> sectionMap = new HashMap<String, Integer>();
    private static int section = 1;
    ArrayList<WaterFallGridHeaderItem> mItems = new ArrayList<>();



    public static String paserTimeToChinese(String time) {
        String regex = "\\d{4}/\\d{2}/\\d{2}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(time);
        if(matcher.matches()){
            return time;
        }
        if (!StringUtils.isEmpty(time)) {
            SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            try {
                Date date = simpledateformat.parse(time);
//                return (new SimpleDateFormat("yyyy年MM月dd日")).format(date);
                return (new SimpleDateFormat("yyyy/MM/dd")).format(date);
            } catch (ParseException parseexception) {
                parseexception.printStackTrace();
                return "";
            }
        }
        return "";
    }
}
