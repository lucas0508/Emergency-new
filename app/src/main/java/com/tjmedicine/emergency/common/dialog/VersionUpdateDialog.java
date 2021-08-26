package com.tjmedicine.emergency.common.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.orhanobut.logger.Logger;

public class VersionUpdateDialog {

    private Context mContext;
    private AppDownloadManager appDownloadManager;
    private AlertDialog.Builder builder;

    public VersionUpdateDialog(@NonNull Context context) {
        this.mContext = context;
        appDownloadManager = new AppDownloadManager((Activity) mContext);
    }

    /**
     * 显示提示更新对话框
     */
    public void showNoticeDialog(String version, String status, final String url,String content) {
        builder = new AlertDialog.Builder(mContext);
        builder.setTitle("检测到新版本！");
        builder.setMessage(content);
        Logger.d(version);
        if ("1".equals(status)) {//必須更新
            positivebtn(url, builder,content);
        } else {
            positivebtn(url, builder,content);
            builder.setNegativeButton("下次再说", (dialog, which) -> dialog.dismiss());
        }
        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
    }

    private void positivebtn(final String url, AlertDialog.Builder builder,String content) {
        builder.setPositiveButton("下载", (dialog, which) -> {
            appDownloadManager.setUpdateListener((currentByte, totalByte) -> {
                if ((currentByte == totalByte) && totalByte != 0) {
                    //updateDialog.dismiss();
                }
            });
            appDownloadManager.downloadApk(url, "121急救", content);
        });
    }
}
