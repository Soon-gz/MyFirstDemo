package com.abings.baby.utils;

import android.content.Context;
import android.util.Log;

import com.abings.baby.WineApplication;
import com.socks.library.KLog;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static CrashHandler INSTANCE;
    private static final String STACK_TRACE = "STACK_TRACE";
    public static final String TAG = CrashHandler.class.getSimpleName();
    private static final String VERSION_CODE = "versionCode";
    private static final String VERSION_NAME = "versionName";
    private static Properties mDeviceCrashInfo = new Properties();
    private DateFormat formatter;
    private Context mContext;
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private CrashHandler() {
        formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    }


    private String[] getCrashReportFiles(Context context) {
        return context.getFilesDir().list(new FilenameFilter() {
            public boolean accept(File file, String s) {
                return s.endsWith(".gospellcr");
            }
        });
    }

    public static CrashHandler getInstance() {
        if (INSTANCE == null)
            INSTANCE = new CrashHandler();
        return INSTANCE;
    }


    private void saveCrashInfoToFile(Throwable throwable) {
    }

    private void sendCrashReportsToServer(Context context) {

    }

    public void init(Context context) {
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public void sendPreviousReportsToServer() {
        (new Thread(new Runnable() {
            public void run() {
                sendCrashReportsToServer(mContext);
            }
        })).start();
    }

    public void uncaughtException(Thread thread, final Throwable ex) {
        KLog.e(getClass().getName(), (new StringBuilder("名字：")).append(thread.getName()).toString());
        KLog.e(getClass().getName(), (new StringBuilder("收到错误：  ")).append(ex.getCause()).append("    错误信息：   ").append(ex.getMessage())
                .toString());
        ex.printStackTrace();
        (new Thread(new Runnable() {
            public void run() {
                saveCrashInfoToFile(ex);
            }
        })).start();
    }

}
