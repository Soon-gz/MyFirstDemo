package com.abings.baby.data.remote;


import com.estimote.sdk.repackaged.retrofit_v1_9_0.retrofit.http.Part;
import com.estimote.sdk.repackaged.retrofit_v1_9_0.retrofit.mime.MultipartTypedOutput;
import com.estimote.sdk.repackaged.retrofit_v1_9_0.retrofit.mime.TypedFile;
import com.estimote.sdk.repackaged.retrofit_v1_9_0.retrofit.mime.TypedString;
import com.squareup.okhttp.RequestBody;

import org.json.JSONObject;

import java.util.Map;

import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;

//import com.estimote.sdk.repackaged.retrofit_v1_9_0.retrofit.http.Body;
//import com.estimote.sdk.repackaged.retrofit_v1_9_0.retrofit.mime.MultipartTypedOutput;
//import com.estimote.sdk.repackaged.retrofit_v1_9_0.retrofit.mime.TypedFile;
//import com.estimote.sdk.repackaged.retrofit_v1_9_0.retrofit.mime.TypedString;
//import retrofit.http.Part;

/**
 * Created by Administrator on 2016/1/20.
 */
public interface APIService {

    @GET(RetrofitUtils.ChinaNew)
    Observable<JSONObject> getChinaNews(@Query("key") String key, @Query("page") int pageNo);

    @FormUrlEncoded
    @POST("FmemberLogin.asp")
    Observable<JSONObject> memberLogin(@Field("mobile_phone") String mobile_phone, @Field("password") String
            password, @Field("school_id") String school_id, @Field("os_ver") String os_ver, @Field("device_id")
                                       String device_id);

    @FormUrlEncoded
    @POST("FteacherLogin.asp")
    Observable<JSONObject> teacherLogin(@Field("mobile_phone") String mobile_phone, @Field("password") String
            password, @Field("school_id") String school_id, @Field("os_ver") String os_ver, @Field("device_id")
                                        String device_id);


    @FormUrlEncoded
    @POST("FteacherLogin_check_list.asp")
    Observable<JSONObject> checkSchool(@Field("mobile_phone") String mobile_phone);

    @FormUrlEncoded
    @POST("FmemberLogin_check_list.asp")
    Observable<JSONObject> checkUserSchool(@Field("mobile_phone") String mobile_phone);

    @FormUrlEncoded
    @POST("Fuser_babys_new.asp")
    Observable<JSONObject> getBabys(@Field("token") String token, @Field("role") String role);

    @FormUrlEncoded
    @POST("Fteacher_class.asp")
    Observable<JSONObject> getClass(@Field("token") String token, @Field("role") String role);

    @FormUrlEncoded
    @POST("Fmain_page_image.asp")
    Observable<JSONObject> mainPageImage(@Field("token") String token, @Field("role") String role, @Field
            ("fk_grade_class_id") String classId);

    @FormUrlEncoded
    @POST("Fmain_page_image_by_date.asp")
    Observable<JSONObject> mainPageImageByDate(@Field("token") String token, @Field("role") String role, @Field
            ("fk_grade_class_id") String classId, @Field("select_date") String select_date);

    @FormUrlEncoded
    @POST("Fmain_page_list.asp")
    Observable<JSONObject> mainPageList(@Field("token") String token, @Field("role") String role, @Field
            ("fk_grade_class_id") String classId);

    @FormUrlEncoded
    @POST("Fmain_page_list_by_date.asp")
    Observable<JSONObject> mainPageListByDate(@Field("token") String token, @Field("role") String role, @Field
            ("fk_grade_class_id") String classId, @Field("select_date") String select_date);

    @FormUrlEncoded
    @POST("Fnews_list.asp")
    Observable<JSONObject> newList(@Field("token") String token, @Field("role") String role, @Field("pageNumber")
    String PageNum, @Field("fk_tag_id") String tagId, @Field("fk_grade_class_id") String classId);


    @FormUrlEncoded
    @POST("Fmember_changePassWord.asp")
    Observable<JSONObject> postChangeMyPassword(@Field("mobile_phone") String mobile_phone, @Field
            ("password") String password , @Field("newPassword") String newPassword , @Field("token") String token
            ,@Field("role") String role);


    @FormUrlEncoded
    @POST("Fimage_list.asp")
    Observable<JSONObject> newWaterfallList(@Field("token") String token, @Field("role") String role, @Field
            ("pageNumber") String PageNum, @Field("fk_grade_class_id") String classId);

    @FormUrlEncoded
    @POST("Fimage_list_detail.asp")
    Observable<JSONObject> newPhotoInfo(@Field("token") String token, @Field("role") String role, @Field
            ("pk_image_news_image_id") String pic_id);

    @FormUrlEncoded
    @POST("Fnews_detail.asp")
    Observable<JSONObject> newsDetail(@Field("token") String token, @Field("role") String role, @Field
            ("pk_image_news_id") String pk_image_news_id);


    @FormUrlEncoded
    @POST("Fimage_news_like_add_cancel.asp")
    Observable<JSONObject> newLikeAddCancle(@Field("token") String token, @Field("role") String role, @Field
            ("fk_image_news_id") String pic_id);

    /**
     * 3.1
     *
     * @param token
     * @param role
     * @param classId
     * @param tagId
     * @param content
     * @return
     */
    @FormUrlEncoded
    @POST("Fmsg_add.asp")
    Observable<JSONObject> newMsgAdd(@Field("token") String token, @Field("role") String role, @Field
            ("fk_grade_class_id") String classId, @Field("fk_tag_id") String tagId, @Field("content") String content);

    /**
     * 3.2
     *
     * @param token
     * @param role
     * @param pk_image_news_id
     * @param params
     * @return
     */
    @Multipart
    @POST("Fmsg_add_image.asp")
    Observable<JSONObject> newImageAdd(@retrofit.http.Part("token") RequestBody token,
                                       @retrofit.http.Part("role") RequestBody role,
                                       @retrofit.http.Part("pk_image_news_id") RequestBody pk_image_news_id,
                                       @retrofit.http.PartMap Map<String, RequestBody> params);

  /**
     * 7.2 上传老师头像
     *
     * @param token
     * @param file1
     * @return
     */
    @Multipart
    @POST("Fupload_teacher_photo.asp")
   Observable<JSONObject> uploadTeacherPhoto(@retrofit.http.Part("token") RequestBody token,
                                       @retrofit.http.Part("file1\";filename=\"image.jpg") RequestBody file1);


    /**
     *9.2 忘记密码
     *@author Shuwen
     *created at 2016/7/29 15:27
     */
    @FormUrlEncoded
    @POST("FmemberSMS_forgetPassWord.asp")
    Observable<JSONObject> forgetpassword(@Field("mobile_phone")String mobile_phone,
                                          @Field("password")String password,
                                          @Field("sms_code")String sms_code,
                                          @Field("role")String role);
    /**
     *9.1 接收验证码
     *@author Shuwen
     *created at 2016/7/29 15:52
     */
    @FormUrlEncoded
    @POST("FmemberSMS_Send.asp")
    Observable<JSONObject> getSmsCode(@Field("mobile_phone")String mobile_phone,
                                      @Field("role")String role);


    /**
     *3.5 用户端将班级动态的图片保存至自己的个人相册
     *@author Shuwen
     *created at 2016/7/29 16:23
     */
    @FormUrlEncoded
    @POST("Fmsg_image_news_image_copy.asp")
    Observable<JSONObject> saveImgToBaby(@Field("token")String token,
                                         @Field("role")String role,
                                         @Field("fk_grade_class_id")String fk_grade_class_id,
                                         @Field("fk_tag_id")String fk_tag_id,
                                         @Field("content")String content,
                                         @Field("pk_image_news_image_id")String pk_image_news_image_id);

    /**
     *1.3  账号激活
     *@author Shuwen
     *created at 2016/7/29 16:09
     */
    @FormUrlEncoded
    @POST("FmemberFirst_Apply.asp")
    Observable<JSONObject> FmemberRegester(@Field("mobile_phone")String mobile_phone,
                                           @Field("password")String password,
                                           @Field("identification_id")String identification_id,
                                           @Field("sms_code")String sms_code,
                                           @Field("role")String role);



  /**
     * 7.1 上传家长头像
     *
     * @param token
     * @param file1
     * @return
     */
    @Multipart
    @POST("Fupload_member_photo.asp")
   Observable<JSONObject> uploadMemberPhoto(@retrofit.http.Part("token") RequestBody token,
                                       @retrofit.http.Part("file1\";filename=\"image.jpg") RequestBody file1);

    /**
     * 7.3 上传宝贝头像(Fupload_baby_photo.asp)
     *
     * 呼叫参数说明:参数: <enctype="multipart/form-data">
     *图片格式限制为 jpg 或jpeg 尺寸 长宽都不可超过512
     *'token = "e72a44611e94f6939171fcd423998eeaa8c4d3e3" (只有家长账
     *号 可以上传)
     *'pk_baby_id =3 ‘ 宝贝 ID
     *图片参数名 file1
     *图片存放相对路径参考 webservice/upload/babyPhotos/a.jpg
     *
     *@author Shuwen
     *created at 2016/7/29 10:44
     */
    @Multipart
    @POST("Fupload_baby_photo.asp")
    Observable<JSONObject> uploadBabyPhoto(@retrofit.http.Part("token")RequestBody token,
                                           @retrofit.http.Part("pk_baby_id")RequestBody pk_baby_id,
                                           @retrofit.http.Part("file1\";filename=\"image.jpg")RequestBody file1);

    /**
     * 3.6 视频
     *
     * @param token
     * @param role
     * @param pk_image_news_id
     * @param params
     * @return
     */
    @Multipart
    @POST("Fmsg_add_video.asp")
    Observable<JSONObject> newVideoAdd(@retrofit.http.Part("token") RequestBody token,
                                       @retrofit.http.Part("role") RequestBody role,
                                       @retrofit.http.Part("pk_image_news_image_id") RequestBody pk_image_news_id,
                                       @retrofit.http.PartMap Map<String, RequestBody> params);


    @FormUrlEncoded
    @POST("Fmsg_del.asp")
    Observable<JSONObject> Fmsg_del(@Field("token") String token, @Field("role") String role, @Field
            ("pk_image_news_id") String pk_image_news_id);

    @FormUrlEncoded
    @POST("Fmsg_image_news_image_del.asp")
    Observable<JSONObject> Fmsg_image_news_image_del(@Field("token") String token, @Field("role") String role, @Field
            ("pk_image_news_image_id") String pk_image_news_image_id);


    /**
     * 4.2 接口功能:老师版联系人
     * @param token
     * @param classId
     * @param pageNumber
     * @return
     */
    @FormUrlEncoded
    @POST("Fteacher_contacts.asp")
    Observable<JSONObject> teacherContacts(@Field("token") String token, @Field("fk_grade_class_id") String classId,
                                           @Field("pageNumber") String pageNumber);

    /**
     * 4.1 接口功能:家长联系人
     * @param token
     * @param classId
     * @param pageNumber
     * @return
     */
    @FormUrlEncoded
    @POST("Fuser_contacts.asp")
    Observable<JSONObject> userContacts(@Field("token") String token, @Field("fk_grade_class_id") String classId,
                                        @Field("pageNumber") String pageNumber);

    @FormUrlEncoded
    @POST("Fmessage_list.asp")
    Observable<JSONObject> UnreadContacts(@Field("token") String token, @Field("create_datetime") String create_datetime,
                                        @Field("readed") String readed,@Field("role") String role);

    /**
     * 4.3 班级老师连络资料
     * @param token
     * @param role
     * @param classId
     * @return
     */
    @FormUrlEncoded
    @POST("Fclass_teachers.asp")
    Observable<JSONObject> classTeachers(@Field("token") String token, @Field("role") String role, @Field("fk_grade_class_id") String classId);

    /**
     * 4.4 接口功能:全校老师连络资料
     * @param token
     * @return
     */
    @FormUrlEncoded
    @POST("Fteacher_school_contacts.asp")
    Observable<JSONObject> schoolTeachers(@Field("token") String token);


    /**
     * 5.1 接口功能:发消息给单个或多个联系人
     * @param token
     * @return
     */
    @FormUrlEncoded
    @POST("Fsend_message.asp")
    Observable<JSONObject> sendMessage(@Field("token") String token, @Field("role") String role, @Field
            ("fk_receive_user_id") String fk_receive_user_id, @Field("receiver_type") String receiver_type, @Field
                                               ("subject") String subject, @Field("content") String content);


    @FormUrlEncoded
    @POST("Fteacher_my_message_sent.asp")
    Observable<JSONObject> teacherMessage(@Field("token") String token);

    @FormUrlEncoded
    @POST("Fteacher_my_message.asp")
    Observable<JSONObject> teacherMessageReceived(@Field("token") String token);


    @FormUrlEncoded
    @POST("Fuser_my_message.asp")
    Observable<JSONObject> userMessage(@Field("token") String token);

    @FormUrlEncoded
    @POST("Fuser_my_message_unreadcount.asp")
    Observable<JSONObject> userMessageUnReadCount(@Field("token") String token);


    @FormUrlEncoded
    @POST("Fteacher_my_message_unreadcount.asp")
    Observable<JSONObject> teacherMessageUnReadCount(@Field("token") String token);

    @FormUrlEncoded
    @POST("Fteacher_my_message_sent_new.asp")
    Observable<JSONObject> teacherMessageSent(@Field("token") String token,@Field("class_id") String class_id);


    @FormUrlEncoded
    @POST("Fsend_message_class.asp")
    Observable<JSONObject> sendClassMsg(@Field("token") String token,@Field("role")String role ,
                                        @Field("fk_receive_class_id")String fk_receive_class_id,@Field("receiver_type")String receiver_type
            ,@Field("subject")String subject,@Field("content")String content);

    @FormUrlEncoded
    @POST("Fmessage_readed.asp")
    Observable<JSONObject> messageReaded(@Field("token") String token, @Field("role") String role, @Field("pk_message_id") String pk_message_id);


    @FormUrlEncoded
    @POST("Fuser_my_profile.asp")
    Observable<JSONObject> userProfile(@Field("token") String token);

    /**
     * 6.4 更改家长资料
     * @param token
     * @param email
     * @param birthday
     * @param address
     * @param relation_desc
     * @return
     */
    @FormUrlEncoded
    @POST("Fuser_update.asp")
    Observable<JSONObject> updateUserProfile(@Field("token") String token, @Field("email") String email, @Field("birthday") String birthday,
                                             @Field("address") String address, @Field("relation_desc") String relation_desc, @Field("sex") int sex);

    /**
     *  6.5 更改家长资料
     * @param token
     * @param email
     * @param birthday
     * @param sex
     * @return
     */
    @FormUrlEncoded
    @POST("Fteacher_update.asp")
    Observable<JSONObject> updateTeacherProfile(@Field("token") String token, @Field("email") String email, @Field("birthday") String birthday, @Field("sex") int sex);


    @FormUrlEncoded
    @POST("Fbaby_update_name.asp")
    Observable<JSONObject> Fbaby_update_name(@Field("token") String token, @Field("pk_baby_id") String pk_baby_id, @Field("nick_name") String nick_name);

    /**
     * 6.1 老师版个人资料
     * @param token
     * @return
     */
    @FormUrlEncoded
    @POST("Fteacher_my_profile.asp")
    Observable<JSONObject> teacherProfile(@Field("token") String token);


    @FormUrlEncoded
    @POST("Ffeeback_add.asp")
    Observable<JSONObject> feebackAdd(@Field("token") String token, @Field("fk_school_id") String fk_school_id, @Field("content") String content);


    @FormUrlEncoded
    @POST("Fnews_search.asp")
    Observable<JSONObject> newsSearch(@Field("token") String token, @Field("role") String role, @Field("pageNumber")
    String PageNum, @Field("fk_grade_class_id") String classId, @Field("keyword") String keyword);


    @FormUrlEncoded
    @POST("Fupload_member_photo.asp")
    Observable<JSONObject> uploadMemberPhoto(@Field("token") String token);


    @Multipart
    @POST("Fupload_member_photo.asp")
    Observable<JSONObject> uploadFile(@Part("token") TypedString token, @Part("file1") TypedFile file);

    @Multipart
    @POST("Fupload_member_photo.asp")
    Observable<JSONObject> uploadFile(@Body MultipartTypedOutput multipartTypedOutput);


    @Multipart
    @POST("Fupload_member_photo.asp")
    Observable<JSONObject> uploadFile(@retrofit.http.Part("token") RequestBody token,
                                      @retrofit.http.Part("file1") RequestBody imgs);

    /**
     * 10.3 获取子账户列表
     *
     * @param token
     * @return
     */
    @FormUrlEncoded
    @POST("Fuser_master_get_association.asp")
    Observable<JSONObject> getAssociation(@Field("token") String token);
    /**
     * 10.2 删除子账户
     *
     * @param token
     * @return
     */
    @FormUrlEncoded
    @POST("Fuser_master_del_association.asp")
    Observable<JSONObject> delAssociation(@Field("token") String token,@Field("pk_user_id") String pk_user_id);

    /**
     * 10.1 添加子账户
     * @param token
     * @param name
     * @param relation_desc
     * @param mobile_phone
     * @return
     */
    @FormUrlEncoded
    @POST("Fuser_master_add_association.asp")
    Observable<JSONObject> addAssociation(@Field("token") String token,@Field("name") String name,@Field("relation_desc") String relation_desc,@Field("mobile_phone") String mobile_phone);

    /**
     * 出席认证/签到/考勤
     * @param pk_grade_class_id 宝宝班级id
     * @param pk_school_id 学校id
     * @param pk_baby_id 宝宝id
     * @param pk_user_id 签到人id
     * @return
     */
    @FormUrlEncoded
    @POST("Fattend_vaildate.asp")
    Observable<JSONObject> attendVaildate(@Field("pk_grade_class_id") String pk_grade_class_id,
                                          @Field("pk_school_id") String pk_school_id,
                                          @Field("pk_baby_id") String pk_baby_id,
                                          @Field("pk_user_id") String pk_user_id);

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
    @FormUrlEncoded
    @POST("Fattend_add.asp")
    Observable<JSONObject> attendAdd(@Field("fk_grade_class_id") String fk_grade_class_id,
                                     @Field("fk_school_id") String fk_school_id,
                                     @Field("fk_baby_id") String fk_baby_id,
                                     @Field("fk_user_id") String fk_user_id,
                                     @Field("relation_desc") String relation_desc,
                                     @Field("leave") String leave);


    /**
     * 签到/考勤 历史记录，按时间查询
     * @param fk_grade_class_id 宝宝班级id
     * @param fk_school_id 学校id
     * @param datetime 查询时间
     * @param leave 0=到校 1=离校
     * @return
     */
    @FormUrlEncoded
    @POST("Fattend_search_by_date.asp")
    Observable<JSONObject>attendSeach(@Field("fk_grade_class_id") String fk_grade_class_id,
                                      @Field("fk_school_id") String fk_school_id,
                                      @Field("datetime") String datetime,
                                      @Field("leave") String leave,
                                      @Field("attend")String attend);


//    @Multipart
//    @POST("Fupload_member_photo.asp")
//    Observable<JSONObject> uploadFile(@Header("token") String token,
//                                      @retrofit.http.Part("file1") RequestBody imgs);


//    public static void upload(String path) {
//        String descriptionString = "hello, this is description speaking";
//        String[] m = new String[2];
//        m[0] = "share.png";
//        m[1] = "Screenshot_20160128-140709.png";
//        File[] ssssss = new File[2];
//        File file1 = new File("/storage/emulated/0/sc/share.png");
//        File file = new File("/storage/emulated/0/Pictures/ScreenShots/Screenshot_20160128-140709.png");
//        ssssss[0] = file;
//        ssssss[0] = file1;
//        RequestBody requestBody[] = new RequestBody[3];
//        RequestBody requestBody1 =
//                RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        RequestBody requestBody2 = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
//        requestBody[0] = requestBody1;
//        requestBody[1] = requestBody2;
//        Call<String> call = apiManager.uploadImage(m[0], requestBody1, requestBody2, null);
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Response<String> response, Retrofit retrofit) {
//                Log.v("Upload", response.message());
//                Log.v("Upload", "success");
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                Log.e("Upload", t.toString());
//            }
//        });
//
//    }
}
