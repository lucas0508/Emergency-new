package com.tjmedicine.emergency.ui.other;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.tjmedicine.emergency.common.base.BaseActivity;
import com.tjmedicine.emergency.common.dialog.AppDownloadManager;


public class AndroidOPermissionActivity extends BaseActivity {

    public static final int INSTALL_PACKAGES_REQUESTCODE = 1;
    private AlertDialog mAlertDialog;
    public static AppDownloadManager.AndroidOInstallPermissionListener sListener;


    @Override
    protected int setLayoutResourceID() {
        return 0;
    }

    @Override
    protected void initView() {
        // 弹窗
        if (Build.VERSION.SDK_INT >= 26) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, INSTALL_PACKAGES_REQUESTCODE);
        } else {
            finish();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case INSTALL_PACKAGES_REQUESTCODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (sListener != null) {
                        sListener.permissionSuccess();
                        finish();
                    }
                } else {
                    //startInstallPermissionSettingActivity();
                    showDialog();
                }
                break;
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_DARK);
        builder.setTitle("");
        builder.setMessage("为了正常升级 “121急救” APP，请点击设置按钮，允许安装未知来源应用，本功能只限用于 “121急救” APP版本升级");
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startInstallPermissionSettingActivity();
                mAlertDialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (sListener != null) {
                    sListener.permissionFail();
                }
                mAlertDialog.dismiss();
                finish();
            }
        });
        mAlertDialog = builder.create();
        mAlertDialog.show();
        mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        mAlertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startInstallPermissionSettingActivity() {
        //注意这个是8.0新API
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // 授权成功
            if (sListener != null) {
                sListener.permissionSuccess();
            }
        } else {
            // 授权失败
            if (sListener != null) {
                sListener.permissionFail();
            }
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sListener = null;
    }


}
