package com.forbidden.griffin.core;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;

import com.forbidden.griffin.log.GinLog;

import java.util.Stack;


/**
 * 应用管理类
 */
public class AppManager {
    private static final String TAG = "AppManager";
    private static Stack<Activity> mActivityStack;
    private static AppManager mInstance;

    private AppManager() {
    }

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (mInstance == null) {
            synchronized (AppManager.class) {
                if (mInstance == null) {
                    mInstance = new AppManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 返回列表
     *
     * @return Activity列表
     */
    public Stack<Activity> list() {
        return mActivityStack;
    }

    /**
     * Activity的数量
     *
     * @return 数量
     */
    public int count() {
        return mActivityStack == null ? 0 : mActivityStack.size();
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (mActivityStack == null) {
            mActivityStack = new Stack<Activity>();
        }
        mActivityStack.add(activity);
        GinLog.i(TAG, "        ->>>>>> add | Activity:" + activity + "\n                    | Activities:" + mActivityStack);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = null;
        if (mActivityStack != null && mActivityStack.size() > 0) {
            activity = mActivityStack.lastElement();
            GinLog.i(TAG, "        ->>>>>> current | Activity:" + activity + "\n                        | Activities:" + mActivityStack);
        }
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        if (mActivityStack == null || mActivityStack.empty()) {
            return;
        }
        Activity activity = mActivityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null && mActivityStack != null && mActivityStack.remove(activity)) {
            try {
                activity.finish();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            GinLog.i(TAG, "        ->>>>>> finish | Activity:" + activity + "\n                       | Activities:" + mActivityStack);
            activity = null;
        }
    }

    /**
     * 返回
     */
    public void onBackPressed() {
        if (mActivityStack == null || mActivityStack.empty()) {
            return;
        }
        Activity activity = mActivityStack.lastElement();
        mActivityStack.remove(activity);
        GinLog.i(TAG, "        ->>>>>> back | Activity:" + activity + "\n                     | Activities:" + mActivityStack);
    }

    /**
     * 移除指定的Activity
     */
    public void removeActivity(Activity activity) {
        if (activity != null && mActivityStack != null) {
            mActivityStack.remove(activity);
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        if (mActivityStack == null) {
            return;
        }
        for (Activity activity : mActivityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if (mActivityStack == null) {
            return;
        }
        for (int i = 0, size = mActivityStack.size(); i < size; i++) {
            if (null != mActivityStack.get(i)) {
                try {
                    mActivityStack.get(i).finish();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        mActivityStack.clear();
    }

    /**
     * 关闭其他的Activity
     */
    public void finishOtherActivity() {
        if (mActivityStack == null) {
            return;
        }
        for (int i = 0; i < mActivityStack.size() - 1; i++) {
            if (null != mActivityStack.get(i)) {
                try {
                    mActivityStack.remove(i).finish();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * 退出应用程序
     * {@link android.Manifest.permission#KILL_BACKGROUND_PROCESSES}
     *
     * @param context 上下文
     */
    @TargetApi(Build.VERSION_CODES.FROYO)
    public void appExit(Context context) {
        try {
            ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            String packageName = context.getPackageName();
            finishAllActivity();
            activityMgr.killBackgroundProcesses(packageName);
        } catch (Exception ignored) {

        }
    }
}
