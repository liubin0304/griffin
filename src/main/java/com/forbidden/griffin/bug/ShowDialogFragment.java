package com.forbidden.griffin.bug;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.lang.reflect.Field;

/**
 * 继承的DialogFragment，解决sdk大于11，使用show()报错的问题：
 * java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
 */
public class ShowDialogFragment extends DialogFragment {
    @Override
    public int show(FragmentTransaction transaction, String tag) {
        int id = 0;
        try {
            Field dismissed = DialogFragment.class.getDeclaredField("mDismissed");
            dismissed.setAccessible(true);
            dismissed.setBoolean(this, false);

            Field shownByMe = DialogFragment.class.getDeclaredField("mShownByMe");
            shownByMe.setAccessible(true);
            shownByMe.setBoolean(this, true);

            transaction.add(this, tag);
            id = transaction.commitAllowingStateLoss();

            Field viewDestroyed = DialogFragment.class.getDeclaredField("mViewDestroyed");
            viewDestroyed.setAccessible(true);
            viewDestroyed.setBoolean(this, false);

            Field backStackId = DialogFragment.class.getDeclaredField("mBackStackId");
            backStackId.setAccessible(true);
            backStackId.setInt(this, id);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return id;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            Field dismissed = DialogFragment.class.getDeclaredField("mDismissed");
            dismissed.setAccessible(true);
            dismissed.setBoolean(this, false);

            Field shownByMe = DialogFragment.class.getDeclaredField("mShownByMe");
            shownByMe.setAccessible(true);
            shownByMe.setBoolean(this, true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, getClass().getSimpleName());
        ft.commitAllowingStateLoss();
    }
}
