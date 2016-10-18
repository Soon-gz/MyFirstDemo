package com.abings.baby.data;

import com.abings.baby.WineApplication;
import com.abings.baby.data.local.DatabaseHelper;
import com.abings.baby.data.local.PhotoUpAlbumHelper;
import com.abings.baby.data.local.PreferencesHelper;
import com.abings.baby.data.model.Album;
import com.abings.baby.data.model.CheckIn;
import com.abings.baby.data.model.Venue;
import com.abings.baby.data.remote.APIService;
import com.abings.baby.data.remote.GoogleAuthHelper;
import com.abings.baby.data.remote.RetrofitUtils;
import com.abings.baby.data.remote.RibotService;
import com.abings.baby.utils.DateUtil;
import com.abings.baby.utils.StringUtils;
import com.socks.library.KLog;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.greenrobot.event.EventBus;
import rx.Observable;
import rx.functions.Action0;
import rx.functions.Func1;

@Singleton
public class DataManager {

    private final APIService mRibotService;
    private final DatabaseHelper mDatabaseHelper;
    private final PreferencesHelper mPreferencesHelper;
    private final EventBus mEventPoster;
    private final GoogleAuthHelper mGoogleAuthHelper;
    private final PhotoUpAlbumHelper mPhotoUpAlbumHelper;

    @Inject
    public DataManager(APIService ribotService, DatabaseHelper databaseHelper, PreferencesHelper preferencesHelper,
                       EventBus eventPosterHelper, GoogleAuthHelper googleAuthHelper, PhotoUpAlbumHelper
                               photoUpAlbumHelper) {
        mRibotService = ribotService;
        mDatabaseHelper = databaseHelper;
        mPreferencesHelper = preferencesHelper;
        mEventPoster = eventPosterHelper;
        mGoogleAuthHelper = googleAuthHelper;
        mPhotoUpAlbumHelper = photoUpAlbumHelper;
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

    /**
     * Sign in with a Google account.
     * 1. Retrieve an google auth code for the given account
     * 2. Sends code and account to API
     * 3. If success, saves ribot profile and API access token in preferences
     */
/*    public Observable<Ribot> signIn(Account account) {
        return mGoogleAuthHelper.retrieveAuthTokenAsObservable(account)
                .concatMap(new Func1<String, Observable<RibotService.SignInResponse>>() {
                    @Override
                    public Observable<RibotService.SignInResponse> call(String googleAccessToken) {
                        return mRibotService.signIn(new SignInRequest(googleAccessToken));
                    }
                })
                .map(new Func1<SignInResponse, Ribot>() {
                    @Override
                    public Ribot call(SignInResponse signInResponse) {
                        mPreferencesHelper.putAccessToken(signInResponse.accessToken);
                        mPreferencesHelper.putSignedInRibot(signInResponse.ribot);
                        return signInResponse.ribot;
                    }
                });
    }*/
    public Observable<Void> signOut() {
        return mDatabaseHelper.clearTables().doOnCompleted(new Action0() {
            @Override
            public void call() {
                mPreferencesHelper.clear();
                // mEventPoster.postEventSafely(new BusEvent.UserSignedOut());
            }
        });
    }

/*    public Observable<List<Ribot>> getRibots() {
        String auth = RibotService.Util.buildAuthorization(mPreferencesHelper.getAccessToken());
        return mRibotService.getRibots(auth, "checkins");
    }*/

    public Observable<JSONObject> getChinaNews() {
        return mRibotService.getChinaNews(RetrofitUtils.Api_key, 1);
    }


    /**
     * 1.1家长登入
     * 1.2老师登入
     *
     * @param mobile_phone 手机号
     * @param password     密码
     * @param school_id    学校id
     * @param os_ver       系统
     * @param device_id    设备id
     * @return
     */
    public Observable<JSONObject> postLogin(String mobile_phone, String password, String school_id, String
            os_ver, String device_id) {
        if (WineApplication.getInstance().isTeacher()) {
            return postTeacherLogin(mobile_phone, password, school_id, os_ver, device_id);
        } else {
            return postMemberLogin(mobile_phone, password, school_id, os_ver, device_id);
        }
    }

    /**
     * 1.1家长登入
     */
    public Observable<JSONObject> postMemberLogin(String mobile_phone, String password, String school_id, String
            os_ver, String device_id) {
        KLog.e("mobile_phone = " + mobile_phone + ", password = " + password + ", school_id = " + school_id + ", " +
                "os_ver = " + os_ver + ", device_id = " + device_id);
        return mRibotService.memberLogin(mobile_phone, password, school_id, os_ver, device_id);

    }

    /**
     * 1.2老师登入
     */
    public Observable<JSONObject> postTeacherLogin(String mobile_phone, String password, String school_id, String
            os_ver, String device_id) {
        KLog.e("mobile_phone = " + mobile_phone + ", password = " + password + ", school_id = " + school_id + ", " +
                "os_ver = " + os_ver + ", device_id = " + device_id);
        return mRibotService.teacherLogin(mobile_phone, password, school_id, os_ver, device_id);

    }

    public Observable<JSONObject> postMainPageImage(String class_id, String select_date) {
        KLog.e("class_id = " + class_id + ", select_date = " + select_date);
        if (StringUtils.isEmpty(select_date)) {
            return mRibotService.mainPageImage(getPreferencesHelper().getAccessToken(), WineApplication.getInstance()
                    .getRole(), class_id);
        } else {
            return mRibotService.mainPageImageByDate(getPreferencesHelper().getAccessToken(), WineApplication
                    .getInstance().getRole(), class_id, select_date);
        }
    }

    public Observable<JSONObject> postMainPageList(String class_id, String select_date) {
        if (StringUtils.isEmpty(select_date)) {
            return mRibotService.mainPageList(getPreferencesHelper().getAccessToken(), WineApplication.getInstance()
                    .getRole(), class_id);
        } else {
            return mRibotService.mainPageListByDate(getPreferencesHelper().getAccessToken(), WineApplication
                    .getInstance().getRole(), class_id, select_date);
        }

    }

    public Observable<JSONObject> postChangePassword(String mobile_phone,String password,String newPassword,String token,String role){
        return mRibotService.postChangeMyPassword(mobile_phone,password,newPassword,token,role);
    }

    public Observable<JSONObject> postMyBabys(String token) {
        return mRibotService.getBabys(token, WineApplication.getInstance().getRole());
    }

    public Observable<JSONObject> postMyClass(String token) {
        return mRibotService.getClass(token, WineApplication.getInstance().getRole());
    }

    public Observable<JSONObject> postCheckSchool(String mobile_phone) {
        if (WineApplication.getInstance().isTeacher()) {
            return mRibotService.checkSchool(mobile_phone);
        } else {
            return mRibotService.checkUserSchool(mobile_phone);
        }
    }


    public Observable<JSONObject> postNewList(String tag, String class_id, int pageNum) {
        KLog.e("Token = " + getPreferencesHelper().getAccessToken() + ", class_id = " + class_id + ", pageNum = " +
                pageNum + ", Role = " + WineApplication.getInstance().getRole());
        return mRibotService.newList(getPreferencesHelper().getAccessToken(), WineApplication.getInstance().getRole()
                , "" + pageNum, tag, class_id);
    }


    public Observable<JSONObject> postWaterfallList(int pageNum, String class_id) {
        return mRibotService.newWaterfallList(getPreferencesHelper().getAccessToken(), WineApplication.getInstance()
                .getRole(), "" + pageNum, class_id);
    }

    public Observable<JSONObject> postPhotoInfo(String pic_id) {
        return mRibotService.newPhotoInfo(getPreferencesHelper().getAccessToken(), WineApplication.getInstance()
                .getRole(), pic_id);
    }

    public Observable<JSONObject> postNewsDetail(String pk_image_news_id) {
        return mRibotService.newsDetail(getPreferencesHelper().getAccessToken(), WineApplication.getInstance()
                .getRole(), pk_image_news_id);
    }

    public Observable<JSONObject> postLikeAddCancle(String pic_id) {
        return mRibotService.newLikeAddCancle(getPreferencesHelper().getAccessToken(), WineApplication.getInstance()
                .getRole(), pic_id);
    }

    public Observable<JSONObject> userMessageUnReadCount() {
        KLog.e("teacherMessageUnReadCount");
        return mRibotService.userMessageUnReadCount(getPreferencesHelper().getAccessToken());
    }

    /**
     *删除日志动态信息（带图片一起删除）
     *@author Shuwen
     *created at 2016/8/2 11:27
     */
    public Observable<JSONObject> deletePic(String pk_image_news_id){
        return mRibotService.Fmsg_del(WineApplication.getInstance().getToken(),WineApplication.getInstance().getRole(),pk_image_news_id);

    }


    /**
     *忘记密码，修改密码，需要验证码和手机号
     *@author Shuwen
     *created at 2016/7/29 15:31
     */
    public Observable<JSONObject> forgetPassword(String mobile_phone,String password,String sms_code){
        return mRibotService.forgetpassword(mobile_phone, password, sms_code, WineApplication.getInstance().getRole());
    }


    /**
     *发送验证码短信
     *@author Shuwen
     *created at 2016/7/29 15:53
     */
    public Observable<JSONObject> getSmsCode(String mobile_phone){
        return mRibotService.getSmsCode(mobile_phone, WineApplication.getInstance().getRole());
    }

    /**
     *账号激活
     *@author Shuwen
     *created at 2016/7/29 16:10
     */
    public Observable<JSONObject> FmemberRegester(String mobile_phone,String password, String identification_id,String sms_code){
        return mRibotService.FmemberRegester(mobile_phone, password, identification_id, sms_code, WineApplication.getInstance().getRole());
    }

    /**
     *保存图片至个人相册
     *@author Shuwen
     *created at 2016/7/29 16:23
     */
    public Observable<JSONObject> saveImgToBaby(String fk_grade_class_id,String fk_tag_id,String content,String pk_image_news_image_id){
        return mRibotService.saveImgToBaby(WineApplication.getInstance().getToken(),WineApplication.getInstance().getRole(),fk_grade_class_id,fk_tag_id,content,pk_image_news_image_id);
    }


    public Observable<JSONObject> teacherMessageUnReadCount() {
        KLog.e("teacherMessageUnReadCount");
        return mRibotService.teacherMessageUnReadCount(getPreferencesHelper().getAccessToken());
    }

    /**
     * 5.10 老师发件箱以班级查询
     * @return
     */
    public Observable<JSONObject> teacherMessageSent() {
        KLog.e("teacherMessageSent");
        return mRibotService.teacherMessageSent(getPreferencesHelper().getAccessToken(),WineApplication.getInstance().getNowClass().getPk_grade_class_id());
    }

    /**
     * 5.8 老师发件箱以班级查询
     * @return
     */
    public Observable<JSONObject> teacherMessageSent2() {
        KLog.e("teacherMessageSent2");
        return mRibotService.teacherMessage(getPreferencesHelper().getAccessToken());
    }

    public Observable<JSONObject> messageReaded(String pk_message_id) {
        KLog.e("Token = " + getPreferencesHelper().getAccessToken() + ", Role = " + WineApplication.getInstance()
                .getRole() + ", pk_message_id = " + pk_message_id);
        return mRibotService.messageReaded(getPreferencesHelper().getAccessToken(), WineApplication.getInstance()
                .getRole(), pk_message_id);
    }


    public Observable<JSONObject> sendClassMessage(String token,String role,String fk_receive_class_id,String receiver_type,String subject,String content){
        return mRibotService.sendClassMsg(token,role,fk_receive_class_id,receiver_type,subject,content);
    }

    public Observable<JSONObject> postMsgAdd(String role, String class_id, String tag_id, String content) {
        KLog.e("Token = " + getPreferencesHelper().getAccessToken() + ", Role = " + WineApplication.getInstance()
                .getRole() + ", tag_id = " + tag_id + ", content = " + content);
        return mRibotService.newMsgAdd(getPreferencesHelper().getAccessToken(), WineApplication.getInstance().getRole
                (), class_id, tag_id, content);
    }


    public Observable<JSONObject> postTeacherContacts(String fk_grade_class_id, int pageNum) {
        return mRibotService.teacherContacts(getPreferencesHelper().getAccessToken(), fk_grade_class_id, pageNum + "");
    }

    public Observable<JSONObject> postUserContacts(String fk_grade_class_id, int pageNum) {
        return mRibotService.userContacts(getPreferencesHelper().getAccessToken(), fk_grade_class_id, pageNum + "");
    }

    public Observable<JSONObject> postUnreadContacts( String create_datetime,String readed,String role) {
        return mRibotService.UnreadContacts(getPreferencesHelper().getAccessToken(), create_datetime, readed + "",role);
    }

    public Observable<JSONObject> postclassTeachersContact(String fk_grade_class_id) {
        return mRibotService.classTeachers(getPreferencesHelper().getAccessToken(), WineApplication.getInstance()
                .getRole(), fk_grade_class_id);
    }

    /**
     * 4.4 接口功能:全校老师连络资料
     * @return
     */
    public Observable<JSONObject> postSchoolTeachersContact() {
        return mRibotService.schoolTeachers(getPreferencesHelper().getAccessToken());
    }

    /**
     * 5.1 接口功能:发消息给单个或多个联系人
     * @return
     */
    public Observable<JSONObject> sendMessage(String fk_receive_user_id, String receiver_type, String subject, String
            content) {
        return mRibotService.sendMessage(getPreferencesHelper().getAccessToken(), WineApplication.getInstance()
                .getRole(), fk_receive_user_id, receiver_type, subject, content);
    }


    public Observable<JSONObject> getTeacherMsg() {
        return mRibotService.teacherMessage(getPreferencesHelper().getAccessToken());
    }

    public Observable<JSONObject> getTeacherMsgReceived() {
        return mRibotService.teacherMessageReceived(getPreferencesHelper().getAccessToken());
    }

    public Observable<JSONObject> getUserMsg() {
        return mRibotService.userMessage(getPreferencesHelper().getAccessToken());
    }

    /**
     * 6.1 老师版个人资料
     *
     * @return
     */
    public Observable<JSONObject> getTeacherProfile() {
        return mRibotService.teacherProfile(getPreferencesHelper().getAccessToken());
    }

    public Observable<JSONObject> getUserProfile() {
        return mRibotService.userProfile(getPreferencesHelper().getAccessToken());
    }

    /**
     * 6.4 更改家长资料
     * @param email         邮箱
     * @param birthday      生日 格式为2016-7-26
     * @param address       地址
     * @param relation_desc 关系描述
     * @param sex           性别，0/True=男，1/false = 女
     * @return
     */
    public Observable<JSONObject> updateUserProfile(String email, String birthday, String address, String
            relation_desc, int sex) {
        return mRibotService.updateUserProfile(getPreferencesHelper().getAccessToken(), email, birthday, address,
                relation_desc, sex);
    }
    /**
     * 6.5 更改家长资料
     * @param email         邮箱
     * @param birthday      生日 格式为2016-7-26
     * @param sex           性别，0/True=男，1/false = 女
     * @return
     */
    public Observable<JSONObject> updateTeacherProfile(String email, String birthday, int sex) {
        return mRibotService.updateTeacherProfile(getPreferencesHelper().getAccessToken(), email, birthday, sex);
    }

    public Observable<JSONObject> Fbaby_update_name(String pk_baby_id, String nick_name) {
        return mRibotService.Fbaby_update_name(getPreferencesHelper().getAccessToken(), pk_baby_id, nick_name);
    }

    public Observable<JSONObject> Fmsg_del(String pk_image_news_id) {
        return mRibotService.Fmsg_image_news_image_del(getPreferencesHelper().getAccessToken(), WineApplication.getInstance()
                .getRole(), pk_image_news_id);
    }

    public Observable<JSONObject> Fmsg_image_news_image_del(String pk_image_news_image_id) {
        return mRibotService.Fmsg_image_news_image_del(getPreferencesHelper().getAccessToken(), WineApplication.getInstance()
                .getRole(), pk_image_news_image_id);
    }

    public Observable<JSONObject> feebackAdd(String content) {
        return mRibotService.feebackAdd(getPreferencesHelper().getAccessToken(), WineApplication.getInstance()
                .getFk_school_id(), content);
    }

    public Observable<JSONObject> newsSearch(String keyword, String class_id, String pageNum) {
        return mRibotService.newsSearch(getPreferencesHelper().getAccessToken(), WineApplication.getInstance()
                .getRole(), pageNum, class_id, keyword);
    }

//    public Observable<JSONObject> uploadMemberPhoto(String path) {
//        RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), new File(path));
//
//        MultipartTypedOutput multipartTypedOutput = new MultipartTypedOutput();
//        //注意“uploadfile[]”一定要带“[]”,表明上传的是数组，也就是多张图片,不能写“uploadfile”
//        multipartTypedOutput.addPart("file1", new TypedFile("", new File(path)));
//        multipartTypedOutput.addPart("token", new TypedString(getPreferencesHelper().getAccessToken()));
////        return mRibotService.uploadFile(multipartTypedOutput);
//
//        File img = new File(path);
//        KLog.e(" img.length() = " + img.length());
//        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), new File(path));
//
//        RequestBody token = RequestBody.create(MediaType.parse("text/plain"), getPreferencesHelper().getAccessToken());
//        KLog.e(" Token = " + getPreferencesHelper().getAccessToken());
////        return mRibotService.uploadFile(new TypedString(getPreferencesHelper().getAccessToken()), new TypedFile("",
//// new File(path)));
//        return mRibotService.uploadFile(token, requestBody);
//    }


//    @FormUrlEncoded
//    @POST("Fmsg_add.asp")
//    Observable<JSONObject> newMsgAdd(@Field("token") String token, @Field("role") String role, @Field
//            ("fk_grade_class_id") String classId, @Field("fk_tag_id") String tagId, @Field("content") String content);


    /**
     * Retrieve list of venues. Behaviour:
     * 1. Return cached venues (empty list if none is cached)
     * 2. Return API venues (if different to cached ones)
     * 3. Save new venues from API in cache
     * 5. If an error happens and cache is not empty, returns venues from cache.
     */
    public Observable<List<Venue>> getVenues() {
        String auth = RibotService.Util.buildAuthorization(mPreferencesHelper.getAccessToken());
        return null;
/*        return mRibotService.getVenues(auth)
                .doOnNext(new Action1<List<Venue>>() {
                    @Override
                    public void call(List<Venue> venues) {
                        mPreferencesHelper.putVenues(venues);
                    }
                })
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends List<Venue>>>() {
                    @Override
                    public Observable<? extends List<Venue>> call(Throwable throwable) {
                        return getVenuesRecoveryObservable(throwable);
                    }
                })
                .startWith(mPreferencesHelper.getVenuesAsObservable())
                .distinct();*/
    }

    // Returns venues from cache. If cache is empty, it forwards the error.
    private Observable<List<Venue>> getVenuesRecoveryObservable(Throwable error) {
        return mPreferencesHelper.getVenuesAsObservable().switchIfEmpty(Observable.<List<Venue>>error(error));
    }

    private Observable<List<Album>> getAlbumRecoveryObservable(Throwable error) {
        return mPhotoUpAlbumHelper.getAlbmsObservable().switchIfEmpty(Observable.<List<Album>>error(error));
    }
    /**
     * Performs a manual check in, either at a venue or a location.
     * Use CheckInRequest.fromVenue() or CheckInRequest.fromLabel() to create the request.
     * If the the check-in is successful, it's saved as the latest check-in.
     */
/*
    public Observable<CheckIn> checkIn(CheckInRequest checkInRequest) {
        String auth = RibotService.Util.buildAuthorization(mPreferencesHelper.getAccessToken());
        return mRibotService.checkIn(auth, checkInRequest)
                .doOnNext(new Action1<CheckIn>() {
                    @Override
                    public void call(CheckIn checkIn) {
                        mPreferencesHelper.putLatestCheckIn(checkIn);
                    }
                });
    }
*/

    /**
     * Marks a previous check-in as "checkedOut" and updates the value in preferences
     * if the check-in matches the latest check-in.
     */
   /* public Observable<CheckIn> checkOut(final String checkInId) {
        String auth = RibotService.Util.buildAuthorization(mPreferencesHelper.getAccessToken());
        return mRibotService.updateCheckIn(auth, checkInId,
                new RibotService.UpdateCheckInRequest(true))
                .doOnNext(new Action1<CheckIn>() {
                    @Override
                    public void call(CheckIn checkInUpdated) {
                        CheckIn latestCheckIn = mPreferencesHelper.getLatestCheckIn();
                        if (latestCheckIn != null && latestCheckIn.id.equals(checkInUpdated.id)) {
                            mPreferencesHelper.putLatestCheckIn(checkInUpdated);
                        }
                        String encounterCheckInId =
                                mPreferencesHelper.getLatestEncounterCheckInId();
                        if (encounterCheckInId != null &&
                                encounterCheckInId.equals(checkInUpdated.id)) {
                            mPreferencesHelper.clearLatestEncounter();
                        }
                    }
                });
    }*/

    /**
     * Returns today's latest manual check in, if there is one.
     */
    public Observable<CheckIn> getTodayLatestCheckIn() {
        return mPreferencesHelper.getLatestCheckInAsObservable().filter(new Func1<CheckIn, Boolean>() {
            @Override
            public Boolean call(CheckIn checkIn) {
                return DateUtil.isToday(checkIn.checkedInDate.getTime());
            }
        });
    }

/*    public Observable<Encounter> performBeaconEncounter(String beaconId) {
        String auth = RibotService.Util.buildAuthorization(mPreferencesHelper.getAccessToken());
        return mRibotService.performBeaconEncounter(auth, beaconId)
                .doOnNext(new Action1<Encounter>() {
                    @Override
                    public void call(Encounter encounter) {
                        mPreferencesHelper.putLatestEncounter(encounter);
                    }
                });
    }*/
/*

    public Observable<Encounter> performBeaconEncounter(String uuid, int major, int minor) {
        Observable<RegisteredBeacon> errorObservable = Observable.error(
                new BeaconNotRegisteredException(uuid, major, minor));
        return mDatabaseHelper.findRegisteredBeacon(uuid, major, minor)
                .switchIfEmpty(errorObservable)
                .concatMap(new Func1<RegisteredBeacon, Observable<Encounter>>() {
                    @Override
                    public Observable<Encounter> call(RegisteredBeacon registeredBeacon) {
                        return performBeaconEncounter(registeredBeacon.id);
                    }
                });
    }
*/

    public Observable<String> findRegisteredBeaconsUuids() {
        return mDatabaseHelper.findRegisteredBeaconsUuids();
    }

/*    public Observable<Void> syncRegisteredBeacons() {
        String auth = RibotService.Util.buildAuthorization(mPreferencesHelper.getAccessToken());
        return mRibotService.getRegisteredBeacons(auth)
                .concatMap(new Func1<List<RegisteredBeacon>, Observable<Void>>() {
                    @Override
                    public Observable<Void> call(List<RegisteredBeacon> beacons) {
                        return mDatabaseHelper.setRegisteredBeacons(beacons);
                    }
                })
                .doOnCompleted(postEventSafelyAction(new BusEvent.BeaconsSyncCompleted()));
    }*/

    //  Helper method to post events from doOnCompleted.
    private Action0 postEventSafelyAction(final Object event) {
        return new Action0() {
            @Override
            public void call() {
                //  mEventPoster.postEventSafely(event);
            }
        };
    }

/*    public void postEvent(Object event){
        mEventPoster.postEventSafely(event);
    }*/

    public void posEvent(Object event) {
        mEventPoster.post(event);
    }

    public EventBus getEventBus() {
        return mEventPoster;
    }

    /**
     * @param url      下载地址
     * @param path     存储位置
     * @param fileName 存储的文件名
     * @return
     */
    public Observable<String> downloadFile(String url, final String path, final String fileName) {
        return Observable.just(url).flatMap(new Func1<String, Observable<String>>() {
            @Override
            public Observable<String> call(String downloadUrl) {
                try {
                    OkHttpClient mOkHttpClient = new OkHttpClient();
                    mOkHttpClient.setReadTimeout(3000, TimeUnit.MILLISECONDS);
                    mOkHttpClient.setWriteTimeout(3000, TimeUnit.MILLISECONDS);
                    mOkHttpClient.setConnectTimeout(3000, TimeUnit.MILLISECONDS);
                    Request request = new Request.Builder().url(downloadUrl).tag(this).build();
                    Response response = mOkHttpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        InputStream is = response.body().byteStream();
                        byte[] buf = new byte[2048];
                        int len = 0;
                        FileOutputStream fos = null;
                        try {
                            is = response.body().byteStream();
                            final long total = response.body().contentLength();
                            long sum = 0;
                            File dir = new File(path);
                            if (!dir.exists()) {
                                dir.mkdirs();
                            }
                            File file = new File(dir, fileName);
                            fos = new FileOutputStream(file);
                            while ((len = is.read(buf)) != -1) {
                                sum += len;
                                fos.write(buf, 0, len);
                                final long finalSum = sum;
                            }
                            fos.flush();
                            return Observable.just(file.getAbsolutePath());
                        } finally {
                            if (is != null) is.close();
                            if (fos != null) fos.close();
                        }

                    } else {
                        return null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

    /**
     * 3.2 上传图片
     *
     * @param role
     * @param pk_image_news_id
     * @param path
     * @return
     */
    public Observable<JSONObject> addImage(String role, String pk_image_news_id, String path, String fileName) {

        RequestBody token = RequestBody.create(null, getPreferencesHelper().getAccessToken());
        RequestBody roleR = RequestBody.create(null, role);
        RequestBody pk_image_news_idR = RequestBody.create(null, pk_image_news_id);

        File file = new File(path);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        Map<String, RequestBody> photos = new HashMap<>();
        photos.put("file1\";filename=\"" + fileName, requestBody);

        return mRibotService.newImageAdd(token, roleR, pk_image_news_idR, photos);
    }

    /**
     *添加图片
     *@author Shuwen
     *created at 2016/7/29 17:48
     */
    public Observable<JSONObject> addImgWithFile(File file,String role, String pk_image_news_id,String fileName){
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        RequestBody token = RequestBody.create(null, getPreferencesHelper().getAccessToken());
        RequestBody roleR = RequestBody.create(null, role);
        RequestBody pk_image_news_idR = RequestBody.create(null, pk_image_news_id);
        Map<String, RequestBody> photos = new HashMap<>();
        photos.put("file1\";filename=\"" + fileName, requestBody);
        return mRibotService.newImageAdd(token, roleR, pk_image_news_idR, photos);
    }



    /**
     * 7.2 上传老师头像
     * @param path
     * @return
     */
    public Observable<JSONObject> uploadTeacherPhoto(String path) {
        RequestBody token = RequestBody.create(null, getPreferencesHelper().getAccessToken());
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), new File(path));
        return mRibotService.uploadTeacherPhoto(token, requestBody);
    }
    /**
     * 7.1 上传家长头像
     * @param path
     * @return
     */
    public Observable<JSONObject> uploadMemberPhoto(String path) {
        RequestBody token = RequestBody.create(null, getPreferencesHelper().getAccessToken());
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), new File(path));
        return mRibotService.uploadMemberPhoto(token, requestBody);
    }


    /**
     *7.3 上传宝贝头像
     *@author Shuwen
     *created at 2016/7/29 10:46
     */
    public Observable<JSONObject> uploadBabyPhoto(String pk_baby_id,String path){
        RequestBody token = RequestBody.create(null,getPreferencesHelper().getAccessToken());
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),new File(path));
        RequestBody requestBody1 = RequestBody.create(null,pk_baby_id);
        return mRibotService.uploadBabyPhoto(token, requestBody1, requestBody);
    }

    /**
     * 3.6 传输mp4
     *
     * @param role
     * @param pk_image_news_id
     * @param path
     * @return
     */
    public Observable<JSONObject> addVideo(String role, String pk_image_news_id, String path, String videoName) {
        RequestBody token = RequestBody.create(null, getPreferencesHelper().getAccessToken());
        RequestBody roleR = RequestBody.create(null, role);
        RequestBody pk_image_news_idR = RequestBody.create(null, pk_image_news_id);
        File file = new File(path);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        Map<String, RequestBody> video = new HashMap<>();
        String key = "file1\";filename=\"" + videoName;
        video.put(key, requestBody);
        return mRibotService.newVideoAdd(token, roleR, pk_image_news_idR, video);
    }

    /**
     * 10.3 获取子账户列表
     *
     * @return
     */
    public Observable<JSONObject> getAssociation() {
        return mRibotService.getAssociation(getPreferencesHelper().getAccessToken());
    }

    /**
     * 10.2 删除子账户
     *
     * @param pk_user_id
     * @return
     */
    public Observable<JSONObject> delAssociation(String pk_user_id) {
        return mRibotService.delAssociation(getPreferencesHelper().getAccessToken(), pk_user_id);
    }

    /**
     * 10.1 添加子账户
     *
     * @param name          名字
     * @param relation_desc 宝贝关系
     * @param mobile_phone  手机号码
     * @return
     */
    public Observable<JSONObject> addAssociation(String name, String relation_desc, String mobile_phone) {
        return mRibotService.addAssociation(getPreferencesHelper().getAccessToken(), name, relation_desc, mobile_phone);
    }

    /**
     * 出席认证/签到/考勤
     * @param pk_grade_class_id 宝宝班级id
     * @param pk_school_id 学校id
     * @param pk_baby_id 宝宝id
     * @param pk_user_id 签到人id
     * @return
     */
    public Observable<JSONObject> attendVaildate(String pk_grade_class_id,
                                                 String pk_school_id,
                                                 String pk_baby_id,
                                                 String pk_user_id) {
        return mRibotService.attendVaildate(pk_grade_class_id, pk_school_id, pk_baby_id, pk_user_id);
    }

    /**
     * 签到/考勤 添加
     * @param fk_grade_class_id 宝宝班级id
     * @param fk_school_id 学校id
     * @param fk_baby_id 宝宝id
     * @param fk_user_id 签到人id
     * @param relation_desc 关系人描述
     * @param leave 0=到校 1=离校
     * @return
     */
    public Observable<JSONObject> attendAdd(String fk_grade_class_id,
                                            String fk_school_id,
                                            String fk_baby_id,
                                            String fk_user_id,
                                            String relation_desc,
                                            String leave) {
        return mRibotService.attendAdd(fk_grade_class_id, fk_school_id, fk_baby_id, fk_user_id, relation_desc, leave);
    }

    /**
     * 签到/考勤 历史记录，按时间查询
     * @param fk_grade_class_id 宝宝班级id
     * @param fk_school_id 学校id
     * @param datetime 查询时间
     * @param leave 0=到校 1=离校
     * @return
     */
    public Observable<JSONObject> attendSearch(String fk_grade_class_id,
                                            String fk_school_id,
                                            String datetime,
                                            String leave,
                                               String attend) {
        return mRibotService.attendSeach(fk_grade_class_id, fk_school_id, datetime, leave,attend);
    }


}
