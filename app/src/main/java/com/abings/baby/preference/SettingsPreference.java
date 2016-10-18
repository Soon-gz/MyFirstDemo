package com.abings.baby.preference;

import android.content.Context;

public class SettingsPreference {

    private static final String USER_ACOUNT = "acount";
    private static final String USER_PASSWD = "passwd";

    private static final String NAME = "SETTINGS";

    public SettingsPreference() {
    }

    public static void setUserAcount(Context context, String acount) {
        (new AppPreference(context, "SETTINGS")).save(USER_ACOUNT, acount);
    }

    public static String getUserAcount(Context context) {
        return (new AppPreference(context, "SETTINGS")).get(USER_ACOUNT, "");
    }

    public static void setUserPasswd(Context context, String passwd) {
        (new AppPreference(context, "SETTINGS")).save(USER_PASSWD, passwd);
    }

    public static String getUserPasswd(Context context) {
        return (new AppPreference(context, "SETTINGS")).get(USER_PASSWD, "");
    }
}
