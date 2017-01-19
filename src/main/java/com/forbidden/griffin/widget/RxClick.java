package com.forbidden.griffin.widget;

import android.util.Log;
import android.view.View;

import rx.Observable;
import rx.Subscriber;

/**
 * 防止多次点击
 */
public final class RxClick
{
    /**
     * 默认的点击的间隔的最小时间，单位：毫秒
     */
    public static final int DEFAULT_CLICK_TIME = 500;
    private static final String TAG = RxClick.class.getSimpleName();
    private View mView;
    private long mPreClickTime = 0;

    private RxClick(View view)
    {
        mView = view;
    }

    public static RxClick click(View view)
    {
        return new RxClick(view);
    }

    public Observable time(final int time)
    {
        return Observable.create(new Observable.OnSubscribe<Object>()
        {
            @Override
            public void call(final Subscriber<? super Object> subscriber)
            {
                mView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        long l = System.currentTimeMillis() - mPreClickTime; // 两次点击的时间间隔
                        if (l >= time)
                        {
                            mPreClickTime = System.currentTimeMillis();
                            subscriber.onNext(mView);
                        } else
                        {
                            Log.d(TAG, time + "毫秒内，不能重复点击");
                        }
                    }
                });
            }
        });
    }
}
