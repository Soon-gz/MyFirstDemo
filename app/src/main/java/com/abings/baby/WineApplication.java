package com.abings.baby;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.abings.baby.data.DataManager;
import com.abings.baby.data.model.BabyItem;
import com.abings.baby.data.model.ClassItem;
import com.abings.baby.data.remote.BaseBusEvent;
import com.abings.baby.injection.component.ApplicationComponent;
import com.abings.baby.injection.component.DaggerApplicationComponent;
import com.abings.baby.injection.module.ApplicationModule;
import com.abings.baby.utils.CrashHandler;
import com.abings.baby.utils.ImageLoaderUtil;
import com.socks.library.KLog;
import com.umeng.socialize.PlatformConfig;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/1/14.
 */
public class WineApplication extends Application {

    ApplicationComponent mApplicationComponent;
    @Inject
    EventBus mEventBus;
    @Inject
    DataManager mDataManager;



    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isTeacher() {
        if (role.equals("1")) {
            return true;
        } else {
            return false;
        }
    }

    // 1=老师 2=家长
    private String role = "1";
    public static final String USER_TYPE = "2";
    public static final String TEACHER_TYPE = "1";

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private String token = "";

    public String getFk_school_id() {
        return fk_school_id;
    }

    public void setFk_school_id(String fk_school_id) {
        this.fk_school_id = fk_school_id;
    }

    private String fk_school_id = "";

    private String photo_url="";

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getClassId() {
        if (isTeacher()) {
            return getNowClass().getPk_grade_class_id();
        } else {
            return getBaby().getFk_grade_class_id();
        }
    }


    public void initUmeng() {
        //各个平台的配置，建议放在全局Application或者程序入口
        //微信    wx12342956d1cab4f9,a5ae111de7d9ea137e88a5e02c07c94d
        if (isTeacher()){
            PlatformConfig.setSinaWeibo("1955662730", "c6fc43671134e690c977d7ca66bbe574");
            PlatformConfig.setQQZone("1105394095", "L8NzDrSHZg83QTxw");
            PlatformConfig.setWeixin("wx77a4a7dbcd04602e", "ae2d7d5d75d6ade3ac7d02ac31c76137");
        }else{
            PlatformConfig.setWeixin("wx0cad0007408825e5", "b25335558c8f6fb614b4f88a91f42ffe");
            //新浪微博
            PlatformConfig.setSinaWeibo("3194081559", "597896b2ff61d772aced5b0215bfffad");
            PlatformConfig.setQQZone("1105390578", "2zyvkzmHAOOffri5");
        }
    }

    public static WineApplication getInstance() {
        return instance;
    }

    private static WineApplication instance;

    private static List<BabyItem> myBabys = new ArrayList<BabyItem>();
    private static int nowBaby;

    private static List<ClassItem> myClasss = new ArrayList<ClassItem>();
    private static int nowClass;

    @Override
    public void onCreate() {
        if (BuildConfig.IS_TEACHER) {
            role = "1";
        } else {
            role = "2";
        }
        super.onCreate();

        mApplicationComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this))
                .build();
        mApplicationComponent.inject(this);
        mEventBus.register(this);
        instance = this;
        ImageLoaderUtil.init(this);
        initUmeng();
        initCrashReporter();
        KLog.init("HelloBaby", false);
    }

    public List<BabyItem> getMyBabys() {
        return myBabys;
    }

    public BabyItem getBaby() {
        return myBabys.get(nowBaby);
    }

    public BabyItem getBaby(int index) {
        if (myBabys.size() > index) {
            nowBaby = index;
            return myBabys.get(index);
        }
        return null;
    }

    public BabyItem getBaby(String id) {
        for (BabyItem bb : myBabys) {
            if (bb.getPk_baby_id().equals(id)) {
                nowBaby = myBabys.indexOf(bb);
                return bb;
            }
        }
        return null;
    }


    public List<ClassItem> getMyClasss() {
        return myClasss;
    }

    public ClassItem getNowClass() {
        return myClasss.get(nowClass);
    }

    public ClassItem getNowClass(String id,boolean changeNowClass) {
        for (ClassItem clas : myClasss) {
            if (clas.getPk_grade_class_id().equals(id)) {
                if(changeNowClass)
                    nowClass = myClasss.indexOf(clas);
                return clas;
            }
        }
        return null;
    }

    public ClassItem getNowClass(int index) {
        if (myClasss.size() > index) {
            nowClass = index;
            return myClasss.get(index);
        }
        return null;
    }

    private String helloWorld() {
        return "Hello World";
    }

    private String helloWorld2() {

        return "Hello World";
    }

    public static WineApplication get(Context context) {
        return (WineApplication) context.getApplicationContext();
    }

    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }

    public void onEventMainThread(BaseBusEvent event) {
        Log.e("ab", "App收到BUS数据" + event.mes);
    }

    private void initCrashReporter() {
        CrashHandler crashhandler = CrashHandler.getInstance();
        crashhandler.init(getBaseContext());
        crashhandler.sendPreviousReportsToServer();
    }
}
