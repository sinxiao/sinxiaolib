package com.xu.sinxiao.common;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentUtils {
    public FragmentUtils() {
    }

    public static void switchFragment(FragmentManager fragmentManager, int containerViewId, Fragment lastFragment, Fragment toFragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (null != toFragment) {
            if (!toFragment.isAdded()) {
                if (null != lastFragment) {
                    transaction.hide(lastFragment);
                }

                transaction.add(containerViewId, toFragment).commit();
            } else if (toFragment.isHidden()) {
                if (null != lastFragment) {
                    transaction.hide(lastFragment);
                }

                transaction.show(toFragment).commit();
            }

        }
    }

    public static void hideFragment(FragmentManager fragmentManager, Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (null != fragment && !fragment.isHidden()) {
            transaction.hide(fragment).commit();
        }

    }
}
