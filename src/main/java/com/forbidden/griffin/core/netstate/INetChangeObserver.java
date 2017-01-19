package com.forbidden.griffin.core.netstate;

/**
 * 监听网络改变的观察者
 */
public interface INetChangeObserver
{
    /**
     * 网络连接连接时调用
     */
    public void onConnect(NetworkState.NetType type);

    /**
     * 当前没有网络连接
     */
    public void onDisConnect();
}
