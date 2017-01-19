package com.forbidden.griffin.core;

import android.app.Application;

/**
 * 初始化 {@link GinManager}
 * Created by Carlton on 2016/5/25.
 */

public class GinApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        GinManager.init(this);
    }
}
