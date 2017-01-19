package com.forbidden.griffin.core;

import android.app.Application;
import android.content.SharedPreferences;

import com.forbidden.griffin.tools.GinSharedP;


import static android.content.Context.MODE_PRIVATE;

/**
 * 工具Application，包含常用的一些基本功能
 * Created by Carlton on 15/10/3.
 */
public class UtilManager {
    private static Application mApp;

    public UtilManager(Application application) {
        mApp = application;
        if (null == mApp) {
            throw new RuntimeException("参数application 不能是null");
        }
    }

    protected Application getApp() {
        return mApp;
    }

    /**
     * 返回SharedPreference编辑器
     *
     * @param name
     * @param mode
     * @return
     */
    public SharedPreferences.Editor getEditor(String name, int mode) {
        return GinSharedP.getEditor(mApp, name, mode);
    }

    /**
     * 返回SharedPreference的私有编辑器
     *
     * @param name
     * @return
     */
    public SharedPreferences.Editor getEditor(String name) {
        return getEditor(name, MODE_PRIVATE);
    }

    public boolean saveBoolean(String name, String key, boolean value) {
        return saveBoolean(name, MODE_PRIVATE, key, value);
    }

    public boolean saveBoolean(String name, int mode, String key, boolean value) {
        return getEditor(name, mode).putBoolean(key, value).commit();
    }

    public boolean getBoolean(String name, int mode, String key, boolean defaultValue) {
        return mApp.getSharedPreferences(name, mode).getBoolean(key, defaultValue);
    }

    public boolean getBoolean(String name, String key) {
        return mApp.getSharedPreferences(name, MODE_PRIVATE).getBoolean(key, false);
    }

    public boolean getBoolean(String name, String key, boolean defaultValue) {
        return mApp.getSharedPreferences(name, MODE_PRIVATE).getBoolean(key, defaultValue);
    }

    /**
     * 保存String数据到私有SharedPreference
     *
     * @param name
     * @param key
     * @param value
     * @return
     */
    public boolean saveString(String name, String key, String value) {
        return saveString(name, MODE_PRIVATE, key, value);
    }

    /**
     * 保存String数据到SharedPreference
     *
     * @param name
     * @param mode
     * @param key
     * @param value
     * @return
     */
    public boolean saveString(String name, int mode, String key, String value) {
        return getEditor(name, mode).putString(key, value).commit();
    }

    /**
     * 返回SharedPreference中String数据
     *
     * @param name
     * @param mode
     * @param key
     * @param defaultValue
     * @return
     */
    public String getString(String name, int mode, String key, String defaultValue) {
        return mApp.getSharedPreferences(name, mode).getString(key, defaultValue);
    }

    /**
     * 返回SharedPreference中String数据，私有模型，默认值为：""
     *
     * @param name
     * @param key
     * @return
     */
    public String getString(String name, String key) {
        return getString(name, MODE_PRIVATE, key, "");
    }

    /**
     * 返回SharedPreference中int数据
     *
     * @param name
     * @param mode
     * @param key
     * @param defaultValue
     * @return
     */
    public int getInteger(String name, int mode, String key, int defaultValue) {
        return mApp.getSharedPreferences(name, mode).getInt(key, defaultValue);
    }

    public int getInteger(String name, String key, int defaultValue) {
        return getInteger(name, MODE_PRIVATE, key, defaultValue);
    }

    /**
     * 返回SharedPreference中String数据，私有模型，默认值为：0
     *
     * @param name
     * @param key
     * @return
     */
    public int getInteger(String name, String key) {
        return getInteger(name, MODE_PRIVATE, key, 0);
    }

    /**
     * 保存int数据到SharedPreference
     *
     * @param name
     * @param mode
     * @param key
     * @param value
     * @return
     */
    public boolean saveInteger(String name, int mode, String key, int value) {
        return getEditor(name, mode).putInt(key, value).commit();
    }

    /**
     * 保存int数据到私有SharedPreference
     *
     * @param name
     * @param key
     * @param value
     * @return
     */
    public boolean saveInteger(String name, String key, int value) {
        return saveInteger(name, MODE_PRIVATE, key, value);
    }

    /**
     * 清空SharedPreference数据
     *
     * @param name
     * @return
     */
    public boolean clearSharedPreference(String name) {
        return getEditor(name).clear().commit();
    }
}
