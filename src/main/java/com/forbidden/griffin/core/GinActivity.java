package com.forbidden.griffin.core;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.forbidden.griffin.core.netstate.INetChangeObserver;
import com.forbidden.griffin.core.netstate.NetworkState;
import com.forbidden.griffin.core.netstate.NetworkStateReceiver;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;


/**
 * 框架Activity
 */
public class GinActivity<T extends ViewDataBinding> extends RxAppCompatActivity implements INetChangeObserver {
    private T dataBinding;
    /**
     * 第一次点击返回的系统时间
     */
    private long mFirstClickTime = 0;
    private GinManager mGinManager = GinManager.get();

    @Override
    protected void onStart() {
        super.onStart();
        NetworkStateReceiver.registerObserver(this);
    }

    @Override
    protected void onStop() {
        NetworkStateReceiver.removeRegisterObserver(this);
        super.onStop();
    }

    /**
     * 双击退出
     */
    public boolean onDoubleClickExit(long timeSpace) {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - mFirstClickTime > timeSpace) {
            doubleExitCallBack();
            mFirstClickTime = currentTimeMillis;
            return false;
        } else {
            return true;
        }
    }

    /**
     * 双击退出，间隔时间为2000ms
     *
     * @return
     */
    public boolean onDoubleClickExit() {
        return onDoubleClickExit(2000);
    }

    /**
     * 双击退出不成功的回调。 第一次点击后回调，直到第二次点击的时间超过了给定时间，每一个回合回调一次
     */
    public void doubleExitCallBack() {
        Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
    }

    /**
     * 隐藏键盘
     */
    public void hideKeyboard() {
        hideKeyboard(InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 隐藏键盘，使用hideSoftInputFromWindow中的flags
     *
     * @param flags InputMethodManager.hideSoftInputFromWindow（IBinder windowToken, int flags）中的flags
     */
    public void hideKeyboard(int flags) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View peekDecorView = getWindow().peekDecorView();
        inputMethodManager.hideSoftInputFromWindow(peekDecorView.getWindowToken(), flags);
    }

    /**
     * 显示键盘
     */
    public void showKeyboard() {
        showKeyboard(InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 显示键盘
     *
     * @param flags
     */
    public void showKeyboard(int flags) {
        InputMethodManager inputMethodManager = getInputMethodManager();
        View peekDecorView = getWindow().peekDecorView();
        inputMethodManager.showSoftInputFromInputMethod(peekDecorView.getWindowToken(), flags);
    }

    public void toggleSoftInputFromWindow(int hideFlags, int showFlags) {
        View peekDecorView = getWindow().peekDecorView();
        getInputMethodManager().toggleSoftInputFromWindow(peekDecorView.getWindowToken(), showFlags, hideFlags);
    }

    public void toggleSoftInputFromWindow() {
        View peekDecorView = getWindow().peekDecorView();
        getInputMethodManager().toggleSoftInputFromWindow(peekDecorView.getWindowToken(), InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public InputMethodManager getInputMethodManager() {
        return (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
    }

    /**
     * 点击事件,处理键盘隐藏
     *
     * @param view
     */
    public void onClick(View view) {
        hideKeyboard();
    }

    /**
     * 设置绑定数据的ContentView
     *
     * @param layoutId LayoutId
     */
    public void setBindingContentView(int layoutId) {
        dataBinding = DataBindingUtil.setContentView(this, layoutId);
    }

    /**
     * setContentView 是否绑定数据
     *
     * @param isBinding 是否绑定
     * @param layoutId  LayoutId
     */
    public void setContentView(boolean isBinding, int layoutId) {
        if (isBinding) {
            setBindingContentView(layoutId);
        } else {
            super.setContentView(layoutId);
        }
    }

    public T getDataBinding() {
        return dataBinding;
    }

    @Override
    public void onBackPressed() {
        if (null != mGinManager) {
            mGinManager.onBackPressed();
        }
        super.onBackPressed();
    }

    public void finishActivity(Activity activity) {
        if (null != mGinManager) {
            mGinManager.finishActivity(activity);
        }
    }

    public void finishActivity() {
        if (null != mGinManager) {
            mGinManager.finishActivity();
        }
    }

    @Override
    public void onConnect(NetworkState.NetType type) {

    }

    @Override
    public void onDisConnect() {

    }
}