package com.xu.sinxiao.common;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

public class PermissionsUtils {
    public static final int REQUEST_CODE_PERMISSIONS = 1;

    public PermissionsUtils() {
    }

    public static void requestPermissions(@NonNull Activity activity, @NonNull String[] permissions) {
        ActivityCompat.requestPermissions(activity, permissions, 1);
    }

    public static boolean checkSelfPermission(@NonNull Context context, @NonNull String[] permissions) {
        String[] var2 = permissions;
        int var3 = permissions.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            String permission = var2[var4];
            if (ActivityCompat.checkSelfPermission(context, permission) != 0) {
                return false;
            }
        }

        return true;
    }

    public static boolean checkHasAllPermission(@NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length == permissions.length) {
            int[] var2 = grantResults;
            int var3 = grantResults.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                int result = var2[var4];
                if (result != 0) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }
}
