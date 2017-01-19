package com.forbidden.griffin.core.netstate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.forbidden.griffin.log.GinLog;

import java.util.ArrayList;


/**
 * 检测网络状态需要配置：
 * {@code <receiver android:name="im.yangqiang.android.unicorn.core.netstate.NetworkStateReceiver">
 * <intent-filter>
 * <action android:name="android.net.conn.CONNECTIVITY_CHANGE">
 * <action android:name="uin.android.net.conn.CONNECTIVITY_CHANGE">
 * <intent-filter>
 * <receiver> }
 */
public class NetworkStateReceiver extends BroadcastReceiver {
    private final String TAG = "NetworkStateReceiver";
    /**
     * 网络是否有效
     */
    private static Boolean mNetworkAvailable = false;
    private static NetworkState.NetType mNetType;
    private static ArrayList<INetChangeObserver> mNetChangeObserverArrayList = new ArrayList<INetChangeObserver>();
    private final static String ANDROID_NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    public final static String DK_ANDROID_NET_CHANGE_ACTION = "uin.android.net.conn.CONNECTIVITY_CHANGE";
    private static BroadcastReceiver mReceiver;

    private static BroadcastReceiver getReceiver() {
        if (mReceiver == null) {
            mReceiver = new NetworkStateReceiver();
        }
        return mReceiver;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mReceiver = NetworkStateReceiver.this;
        if (intent.getAction().equalsIgnoreCase(ANDROID_NET_CHANGE_ACTION) || intent.getAction().equalsIgnoreCase(DK_ANDROID_NET_CHANGE_ACTION)) {
            GinLog.i(TAG, "网络状态改变.");
            if (!NetworkState.isNetworkAvailable(context)) {
                GinLog.i(TAG, "没有网络连接.");
                mNetworkAvailable = false;
            } else {
                GinLog.i(TAG, "网络连接成功.");
                mNetType = NetworkState.getAPNType(context);
                mNetworkAvailable = true;
            }
            notifyObserver();
        }
    }

    /**
     * 注册网络状态广播
     *
     * @param mContext
     */
    public static void registerNetworkStateReceiver(Context mContext) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(DK_ANDROID_NET_CHANGE_ACTION);
        filter.addAction(ANDROID_NET_CHANGE_ACTION);
        mContext.getApplicationContext().registerReceiver(getReceiver(), filter);
    }

    /**
     * 检查网络状态
     *
     * @param mContext
     */
    public static void checkNetworkState(Context mContext) {
        Intent intent = new Intent();
        intent.setAction(DK_ANDROID_NET_CHANGE_ACTION);
        mContext.sendBroadcast(intent);
    }

    /**
     * 注销网络状态广播
     *
     * @param mContext
     */
    public static void unRegisterNetworkStateReceiver(Context mContext) {
        if (mReceiver != null) {
            try {
                mContext.getApplicationContext().unregisterReceiver(mReceiver);
            } catch (Exception e) {
                GinLog.d("NetworkStateReceiver", e.getMessage());
            }
        }

    }

    /**
     * 获取当前网络状态，true为网络连接成功，否则网络连接失败
     *
     * @return
     */
    public static Boolean isNetworkAvailable() {
        return mNetworkAvailable;
    }

    public static NetworkState.NetType getAPNType() {
        return mNetType;
    }

    private void notifyObserver() {

        for (int i = 0; i < mNetChangeObserverArrayList.size(); i++) {
            INetChangeObserver observer = mNetChangeObserverArrayList.get(i);
            if (observer != null) {
                if (isNetworkAvailable()) {
                    observer.onConnect(mNetType);
                } else {
                    observer.onDisConnect();
                }
            }
        }

    }

    /**
     * 注册网络连接观察者
     */
    public static void registerObserver(INetChangeObserver observer) {
        if (mNetChangeObserverArrayList == null) {
            mNetChangeObserverArrayList = new ArrayList<INetChangeObserver>();
        }
        mNetChangeObserverArrayList.add(observer);
    }

    /**
     * 注销网络连接观察者
     */
    public static void removeRegisterObserver(INetChangeObserver observer) {
        if (mNetChangeObserverArrayList != null) {
            mNetChangeObserverArrayList.remove(observer);
        }
    }
}
