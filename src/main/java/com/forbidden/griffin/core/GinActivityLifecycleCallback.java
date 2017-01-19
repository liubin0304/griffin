package com.forbidden.griffin.core;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Activity生命周期回调
 * Created by Carlton on 12/7/15.
 */
public class GinActivityLifecycleCallback implements Application.ActivityLifecycleCallbacks
{
    private GinManager mApp;
    /**
     * 是否在前台
     */
    private boolean isForeground = false;

    public GinActivityLifecycleCallback(GinManager application)
    {
        mApp = application;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState)
    {
        mApp.getAppManager().addActivity(activity);
        mApp.setCurrentActivity();
    }

    @Override
    public void onActivityStarted(Activity activity)
    {
    }

    @Override
    public void onActivityResumed(Activity activity)
    {
        isForeground = true;
    }

    @Override
    public void onActivityPaused(Activity activity)
    {
        isForeground = false;
    }

    @Override
    public void onActivityStopped(Activity activity)
    {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState)
    {

    }

    @Override
    public void onActivityDestroyed(Activity activity)
    {
        mApp.getAppManager().removeActivity(activity);
        mApp.setCurrentActivity();
    }

    public boolean isForeground()
    {
        return isForeground;
    }

    public boolean isBackground()
    {
        return !isForeground();
    }
}
