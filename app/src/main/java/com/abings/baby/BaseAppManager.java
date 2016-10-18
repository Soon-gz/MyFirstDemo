
/*
 * https://github.com/square/retrofit  通过非常简单的注解方式来晚上网络访问
 * http://square.github.io/picasso/  Square公司开源的一个Android图形缓存库
*/
	
package com.abings.baby;

import android.app.Activity;

import java.util.Stack;

/**
 * 自定义Activity堆栈管理
 * 
 * @author abings
 * 
 */
public class BaseAppManager {

	private static final String TAG = BaseAppManager.class.getSimpleName();

	private static BaseAppManager instance = null;
	private static Stack<Activity> mActivities;

	private BaseAppManager() {

	}

	public static BaseAppManager getInstance() {
		if (null == instance) {
			synchronized (BaseAppManager.class) {
				if (null == instance) {
					instance = new BaseAppManager();
				}
			}
		}
		return instance;
	}

	public int size() {
		return mActivities.size();
	}

	public synchronized Activity getForwardActivity() {
		return size() > 0 ? mActivities.get(size() - 1) : null;
	}

	/**
	 * 添加Activity到堆栈
	 */
	public synchronized void addActivity(Activity activity) {
		if (mActivities == null) {
			mActivities = new Stack<Activity>();
		}
		mActivities.add(activity);
	}

	public synchronized void removeActivity(Activity activity) {
		if (mActivities != null && mActivities.contains(activity)) {
			mActivities.remove(activity);
		}
	}

	/**
	 * 移除当前Activity（堆栈中最后一个压入的）
	 */
	public synchronized void removeActivity() {
		if (mActivities != null) {
			Activity activity = mActivities.lastElement();
			removeActivity(activity);
		}
	}
	
	/**
	 * 结束当前Activity（堆栈中最后一个压入的）
	 */
	public synchronized void finishActivity() {
		Activity activity = mActivities.lastElement();
		finishActivity(activity);
	}
	
	/**
	 * 结束指定的Activity
	 */
	public synchronized void finishActivity(Activity activity) {
		if (activity != null) {
			mActivities.remove(activity);
			activity.finish();
			activity = null;
		}
	}	

	public synchronized void clear() {
		for (int i = mActivities.size() - 1; i > -1; i--) {
			Activity activity = mActivities.get(i);
			removeActivity(activity);
			activity.finish();
			i = mActivities.size();
		}
	}

	public synchronized void clearToTop() {
		for (int i = mActivities.size() - 2; i > -1; i--) {
			Activity activity = mActivities.get(i);
			removeActivity(activity);
			activity.finish();
			i = mActivities.size() - 1;
		}
	}
}
