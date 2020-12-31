package com.xu.sinxiao.common;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;

public class DialigUtils {
    public static void showInforDialog(Context context, String info) {
        new AlertDialog.Builder(context).setIcon(R.drawable.sinxiao_infor).setTitle(R.string.sinxiao_infor).setMessage(info).setPositiveButton(R.string.sinxiao_confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create().show();
    }

    public static void showYesOrNoDialog(Context context, String info, DialogInterface.OnClickListener onPositiveClick) {
        new AlertDialog.Builder(context).setIcon(R.drawable.sinxiao_infor).setTitle(R.string.sinxiao_infor).setMessage(info).setNegativeButton(R.string.sinxiao_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton(R.string.sinxiao_confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (onPositiveClick != null) {
                    onPositiveClick.onClick(dialogInterface, i);
                }
                dialogInterface.dismiss();
            }
        }).create().show();
    }

    public static void showErrorDialog(Context context, String error) {
        new AlertDialog.Builder(context).setIcon(R.drawable.sinxiao_warn).setTitle(R.string.sinxiao_infor).setMessage(error).setPositiveButton(R.string.sinxiao_confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create().show();
    }

    private static Dialog loadingDailog;

    public static void showLoadingDialog(Context context) {
        loadingDailog = new AlertDialog.Builder(context).setView(LayoutInflater.from(context).inflate(R.layout.layout_loading, null)).create();
        loadingDailog.setCancelable(false);
        loadingDailog.show();
    }

    public static void dissmissLoadingDailig() {
        if (loadingDailog != null && loadingDailog.isShowing()) {
            loadingDailog.dismiss();
        }
        loadingDailog = null;
    }
}
