package com.abings.baby.ui.contacts;

import android.content.Context;
import android.util.Log;

import com.abings.baby.WineApplication;
import com.abings.baby.data.DataManager;
import com.abings.baby.data.model.ClassTeacherItem;
import com.abings.baby.data.model.Contact;
import com.abings.baby.data.model.UserContact;
import com.abings.baby.data.remote.BaseBusEvent;
import com.abings.baby.data.remote.custom.Func1JSONObject;
import com.abings.baby.data.remote.custom.SubscriberJSONObject;
import com.abings.baby.injection.ActivityContext;
import com.abings.baby.ui.base.Presenter;
import com.abings.baby.utils.PingYinUtil;
import com.abings.baby.utils.PinyinComparator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.socks.library.KLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/1/18.
 */
public class ContactsPresenter implements Presenter<ContactsMvpView> {

    private final DataManager mDataManager;
    public Subscription mSubscription;
    private ContactsMvpView mMvpView;
    private JSONObject mCachedRibots;
    private List<Contact> mContacts = new ArrayList<Contact>();
    private Context mContext;

    @Inject
    public ContactsPresenter(DataManager dataManager, @ActivityContext Context context) {
        mDataManager = dataManager;
        mContext = context;
    }

    @Override
    public void attachView(ContactsMvpView mvpView) {
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

    public void loadContacts(int pageNum, final boolean showLoading) {
        String fk_grade_class_id = "";
        if (WineApplication.getInstance().isTeacher()) {
            fk_grade_class_id = WineApplication.getInstance().getNowClass()
                    .getPk_grade_class_id();
        } else {
            fk_grade_class_id = WineApplication.getInstance().getBaby().getFk_grade_class_id();
        }

        loadTeachers(fk_grade_class_id, pageNum, WineApplication.getInstance().isTeacher(), showLoading);
    }

    public void loadTeachers(final String fk_grade_class_id, final int pageNum, final boolean isTeacher, final boolean
            showLoading) {
        mSubscription = mDataManager.postclassTeachersContact(fk_grade_class_id).flatMap(new Func1JSONObject() {
            @Override
            protected Observable<JSONObject> callJSONObject(JSONObject result) throws JSONException {
                Gson gson = new Gson();
                JSONArray jsonArray = result.getJSONArray("result");
                Type type = new TypeToken<ArrayList<ClassTeacherItem>>() {
                }.getType();
                List<ClassTeacherItem> list = gson.fromJson(jsonArray.toString(), type);
                for (ClassTeacherItem c : list) {
                    Contact contact = new Contact();
                    contact.setName(c.getName());
                    contact.setPinyin("#" + PingYinUtil.converterToFirstSpell(c.getName()));
                    contact.setIsTeacher(true);
                    contact.setClassTeacherItem(c);
                    mContacts.add(contact);
                }
                Collections.sort(mContacts, new PinyinComparator());
                mMvpView.showData(mContacts, false);
                if (isTeacher) {
                    return mDataManager.postTeacherContacts(fk_grade_class_id, pageNum);
                } else {
                    return mDataManager.postUserContacts(fk_grade_class_id, pageNum);
                }
            }

            @Override
            protected Observable<JSONObject> onErrorState2() {
                mMvpView.showError("班级老师联络资料无数据！");
                return null;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new SubscriberJSONObject(mContext) {
            @Override
            public void onCompleted() {
                loadContacts(fk_grade_class_id, pageNum, WineApplication.getInstance().isTeacher());
            }

            @Override
            protected void onNextJSONObject(JSONObject result) throws JSONException {
            }
        });
    }

    public void loadContacts(final String fk_grade_class_id, final int pageNum, final boolean isTeacher) {
        mSubscription = getRibotsObservable(false, pageNum, isTeacher).subscribe(new SubscriberJSONObject(mContext){
            @Override
            protected void onNextJSONObject(JSONObject result) throws JSONException {
                mCachedRibots = result;
                if (result.has("state")) {
                    try {
                        int state = Integer.valueOf(result.getString("state"));
                        switch (state) {
                            case 0:
                                Gson gson = new Gson();
                                JSONArray array = result.getJSONArray("result");
                                if (array != null && array.length() > 0) {
                                    List<UserContact> list = new ArrayList<UserContact>();
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject obj = array.getJSONObject(i);
                                        UserContact item = new UserContact();
                                        item.setBirthday(obj.getString("birthday"));
                                        item.setSex(obj.getString("sex"));
                                        item.setFk_grade_class_id(obj.getString("fk_grade_class_id"));
                                        item.setFk_master_user_id(obj.getString("fk_master_user_id"));
                                        item.setCreate_datetime(obj.getString("create_datetime"));
                                        item.setPk_baby_id(obj.getString("pk_baby_id"));
                                        item.setName(obj.getString("name"));
                                        item.setNick_name(obj.getString("nick_name"));
                                        item.setPhoto(obj.getString("photo"));
                                        item.setFk_shool_id(obj.getString("fk_shool_id"));
                                        if (!obj.isNull("images")) {
                                            Object jjj = obj.get("images");
                                            if (jjj instanceof JSONArray) {
                                                JSONArray imagesArray = obj.getJSONArray("images");
                                                if (imagesArray != null && imagesArray.length() > 0) {
                                                    List<UserContact.ImagesBean> list1 = new ArrayList<UserContact.ImagesBean>();
                                                    for (int j = 0; j < imagesArray.length(); j++) {
                                                        UserContact.ImagesBean data = gson.fromJson(imagesArray.getJSONObject
                                                                (j).toString(), UserContact.ImagesBean.class);
                                                        list1.add(data);
                                                    }
                                                    item.setImages(list1);

                                                }
                                            }
                                        }
                                        list.add(item);

                                    }
                                    List<Contact> contacts = new ArrayList<Contact>();
                                    for (UserContact c : list) {
                                        Contact contact = new Contact();
                                        contact.setName(c.getName());
                                        contact.setPinyin(PingYinUtil.converterToFirstSpell(c.getName()));
                                        contact.setUserContact(c);
                                        contacts.add(contact);
                                    }
                                    Collections.sort(contacts, new PinyinComparator());
                                    mMvpView.showData(contacts, contacts.size() == 10);
                                }
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    mMvpView.showMessage("no data");
                }
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                if(WineApplication.getInstance().isTeacher()){
                    loadSchoolContacts();//只有老师,才会获取到全校通讯录
                }

            }
        });

    }


    private Observable<JSONObject> getRibotsObservable(boolean allowMemoryCacheVersion, int pageNum, boolean
            isTeacher) {
        if (allowMemoryCacheVersion && mCachedRibots != null) {
            return Observable.just(mCachedRibots);
        } else {
            if (isTeacher) {
                return mDataManager.postTeacherContacts(WineApplication.getInstance().getNowClass().getPk_grade_class_id(), pageNum).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io());
            } else {
                return mDataManager.postUserContacts(WineApplication.getInstance().getBaby().getFk_grade_class_id(), pageNum).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io());
            }
        }
    }

    /**
     * 加载全校通讯录
     */
    public void loadSchoolContacts(){
        if(mSubscription !=null){
            mSubscription.unsubscribe();
        }

        mSubscription = mDataManager.postSchoolTeachersContact().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new SubscriberJSONObject(mContext) {
            @Override
            protected void onNextJSONObject(JSONObject jsonObject) throws JSONException {
//                mMvpView.showSchoolContacts();
                Gson gson = new Gson();
                JSONArray array = jsonObject.getJSONArray("result");
                if (array != null && array.length() > 0) {
                    Type type = new TypeToken<List<ClassTeacherItem>>(){}.getType();
                    List<ClassTeacherItem> list = gson.fromJson(array.toString(),type);
                    List<Contact> contacts = new ArrayList<Contact>();
                    for (ClassTeacherItem c : list) {
                        Contact contact = new Contact();
                        contact.setIsTeacher(true);
                        contact.setName(c.getName());
                        contact.setPinyin(PingYinUtil.converterToFirstSpell(c.getName()));
                        contact.setClassTeacherItem(c);
                        contacts.add(contact);
                    }
                    Collections.sort(contacts, new PinyinComparator());
                    mMvpView.showSchoolContacts(contacts);
                }
            }
        });
    }

    public void onEventMainThread(BaseBusEvent event) {

    }
}
