package com.forbidden.griffin.core;

import android.app.Activity;
import android.app.Application;
import android.support.v7.app.AppCompatActivity;

import com.forbidden.griffin.log.GinLog;


/**
 * 提供核心功能的Application
 * Created by Carlton on 15/10/3.
 */
public class GinManager extends UtilManager {
    private static final String TAG = "GinManager";
    private static GinManager INSTANCE;
    /**
     * 当前的Activity,在Activity栈最顶上的Activity
     */
    private AppCompatActivity mCurrentActivity;
    /**
     * Activity管理器
     */
    private AppManager mAppManager;
    /**
     * App生命周期
     */
    private GinActivityLifecycleCallback mLifecycleCallback;

    public GinManager(Application application) {
        super(application);
        init();
    }

    public static GinManager get() {
        return instance();
    }

    public synchronized static void init(Application application) {
        if (INSTANCE == null) {
            INSTANCE = new GinManager(application);
        }
    }

    public static GinManager instance() {
        if (null == INSTANCE) {
            throw new RuntimeException("请先在Application的onCreate方法中调用 #init（Application application）方法，或者继承UinApplication");
        }
        return INSTANCE;
    }

    private void init() {
        getAppManager();
        mLifecycleCallback = new GinActivityLifecycleCallback(this);
        getApp().registerActivityLifecycleCallbacks(mLifecycleCallback);
    }

    /**
     * 是否在前台运行
     *
     * @return
     */
    public boolean isForeground() {
        return mLifecycleCallback.isForeground();
    }

    /**
     * 是否在后台运行
     *
     * @return
     */
    public boolean isBackground() {
        return mLifecycleCallback.isBackground();
    }

    /**
     * 是否运行
     *
     * @return
     */
    public boolean isRunning() {
        return getAppManager().count() > 0;
    }

    /**
     * 返回应用管理类，批量退出Activity或者获取当前Activity等
     *
     * @return
     */
    public AppManager getAppManager() {
        if (mAppManager == null) {
            mAppManager = AppManager.getAppManager();
        }
        return mAppManager;
    }

    /**
     * 退出应用程序
     */
    public void exitApp() {
        GinLog.d(TAG, "退出程序");
        mAppManager.appExit(getApp());
    }

    /**
     * 设置当前显示的Activity
     */
    public void setCurrentActivity() {
        Activity currentActivity = mAppManager.currentActivity();
        if (currentActivity instanceof AppCompatActivity) {
            mCurrentActivity = (GinActivity) currentActivity;
        }
    }

    public AppCompatActivity currentActivity() {
        setCurrentActivity();
        return mCurrentActivity;
    }

    /**
     * 返回Activity，在Activity的onBackPressed中调用，用于管理Activity
     */
    public void onBackPressed() {
        mAppManager.onBackPressed();
        setCurrentActivity();
    }

    /**
     * 停止一个Activity，同Activity.finish(),用于管理Activity
     */
    public void finishActivity(Activity activity) {
        mAppManager.finishActivity(activity);
        setCurrentActivity();
    }

    /**
     * 停止一个Activity，同Activity.finish(),用于管理Activity
     */
    public void finishActivity() {
        mAppManager.finishActivity();
        setCurrentActivity();
    }
}
