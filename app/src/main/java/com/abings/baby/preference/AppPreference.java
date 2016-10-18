package com.abings.baby.preference;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreference {

    private SharedPreferences mSharedPref;

    public AppPreference(Context context, String s)
    {
	mSharedPref = context.getSharedPreferences(s, 0);
    }

    public static AppPreference get(Context context, String s)
    {
	return new AppPreference(context, s);
    }

    public static String get(Context context, String s, String s1)
    {
	return get(context, s, s1, "");
    }

    public static String get(Context context, String s, String s1, String s2)
    {
	return get(context, s).get(s1, s2);
    }

    public static void save(Context context, String s, String s1, String s2)
    {
	get(context, s).save(s1, s2);
    }

    public void clear()
    {
	mSharedPref.edit().clear().commit();
    }

    public String get(String s)
    {
	return get(s, "");
    }

    public String get(String s, String s1)
    {
	return mSharedPref.getString(s, s1);
    }

    public boolean getBoolean(String s, boolean flag)
    {
	return mSharedPref.getBoolean(s, flag);
    }

    public int getInt(String s, int i)
    {
	return mSharedPref.getInt(s, i);
    }

    public void save(String s, int i)
    {
	mSharedPref.edit().putInt(s, i).commit();
    }

    public void save(String s, String s1)
    {
	mSharedPref.edit().putString(s, s1).commit();
    }

    public void save(String s, boolean flag)
    {
	mSharedPref.edit().putBoolean(s, flag).commit();
    }
}
