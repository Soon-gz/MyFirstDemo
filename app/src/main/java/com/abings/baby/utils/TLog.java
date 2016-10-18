/*
 * Copyright (c) 2015 [1076559197@qq.com | tchen0707@gmail.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License�?);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.abings.baby.utils;


import android.util.Log;

/**
 * Author:  abings
 * Date:    2015/10/10.
 * Description: Log utils
 */
public class TLog {
    /**
     * This flag to indicate the log is enabled or disabled.
     */
    private boolean isLogEnable = true;
    private static volatile TLog instance = null;
    private String mtag = "Temp";
    /**
     * Disable the log output.
     */
    public void disableLog() {
        isLogEnable = false;
    }
    
    private TLog(){
    	
    }
    
    public static TLog getInstance() {
        if (null == instance) {
            synchronized (TLog.class) {
                if (null == instance) {
                    instance = new TLog();
                }
            }
        }
        return instance;
    }    

    /**
     * Enable the log output.
     */
    public void enableLog() {
        isLogEnable = true;
    }

    /**
     * Debug
     *
     * @param tag
     * @param msg
     */
    public void d(String tag, String msg) {
        if (isLogEnable) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            Log.d(tag, rebuildMsg(stackTraceElement, msg));
        }
    }
    
    public void d(String msg) {
        if (isLogEnable) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            Log.d(mtag, rebuildMsg(stackTraceElement, msg));
        }
    }

    /**
     * Information
     *
     * @param tag
     * @param msg
     */
    public void i(String tag, String msg) {
        if (isLogEnable) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            Log.i(tag, rebuildMsg(stackTraceElement, msg));
        }
    }
    
    public void i(String msg) {
        if (isLogEnable) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            Log.i(mtag, rebuildMsg(stackTraceElement, msg));
        }
    }    

    /**
     * Verbose
     *
     * @param tag
     * @param msg
     */
    public void v(String tag, String msg) {
        if (isLogEnable) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            Log.v(tag, rebuildMsg(stackTraceElement, msg));
        }
    }
    public void v( String msg) {
        if (isLogEnable) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            Log.v(mtag, rebuildMsg(stackTraceElement, msg));
        }
    }
    /**
     * Warning
     *
     * @param tag
     * @param msg
     */
    public void w(String tag, String msg) {
        if (isLogEnable) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            Log.w(tag, rebuildMsg(stackTraceElement, msg));
        }
    }
    public void w(String msg) {
        if (isLogEnable) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            Log.w(mtag, rebuildMsg(stackTraceElement, msg));
        }
    }
    /**
     * Error
     *
     * @param tag
     * @param msg
     */
    public void e(String tag, String msg) {
        if (isLogEnable) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            Log.e(tag, rebuildMsg(stackTraceElement, msg));
        }
    }
    public void e(String msg) {
        if (isLogEnable) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            Log.e(mtag, rebuildMsg(stackTraceElement, msg));
        }
    }
    /**
     * Rebuild Log Msg
     *
     * @param msg
     * @return
     */
    private String rebuildMsg(StackTraceElement stackTraceElement, String msg) {
        StringBuffer sb = new StringBuffer();
        sb.append(stackTraceElement.getFileName());
        sb.append(" (");
        sb.append(stackTraceElement.getLineNumber());
        sb.append(") ");
        sb.append(stackTraceElement.getMethodName());
        sb.append(": ");
        sb.append(msg);
        return sb.toString();
    }
}
