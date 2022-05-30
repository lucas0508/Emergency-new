package com.tjmedicine.emergency.ui.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.mob.MobSDK;
import com.mob.OperationCallback;
import com.orhanobut.logger.Logger;
import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.common.base.ActivityManager;
import com.tjmedicine.emergency.common.base.BaseActivity;
import com.tjmedicine.emergency.common.dialog.ConfirmAgreementDialog;
import com.tjmedicine.emergency.ui.main.MainActivity;
import com.tjmedicine.emergency.utils.DevicePermissionsUtils;
import com.tjmedicine.emergency.utils.PreferenceUtil;

import java.util.List;


public class SplashActivity extends BaseActivity {


    private final Handler mHideHandler = new Handler();
    private final Runnable gotoPage = new Runnable() {
        @Override
        public void run() {
            ActivityJump();
        }
    };
    /**
     * 设置首页停留时间
     ***/
    private boolean isFirst;
    boolean granted = true;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {

        //透明化状态栏
        setStatusBarTranslucent(this);
        isFirst = PreferenceUtil.getBoolean("isFirst", true);
        mHideHandler.postDelayed(gotoPage, 3000);

    }

    private void ActivityJump() {

        if (isFirst) {
//            mApp.getConfirmAgreementDialog().show();
            mApp.getConfirmAgreementDialog().show("服务协议和隐私政策", new ConfirmAgreementDialog.ConfirmCallback() {
                @Override
                public void onOk() {
                    PreferenceUtil.put("isFirst", false);
                    initPermission();
                }

                @Override
                public void onCancel() {
                    ActivityManager.getInstance().kill();
                }
            });
        } else {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }
    }

    private void initPermission() {
        requestRunPermission(DevicePermissionsUtils.autoObtainNeedAllPermission(this),
                new BaseActivity.PermissionListener() {
                    @Override
                    public void onGranted() {
                        granted = true;
                        submitPrivacyGrantResult(granted);
                        if (isFirst) {
                            startActivity(new Intent(SplashActivity.this, GuideActivity.class));
                        } else {
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        }
                        finish();
                    }

                    @Override
                    public void onDenied(List<String> deniedPermission) {
                        Logger.d("权限拒绝");
                        granted = false;
                        if (isFirst) {
                            startActivity(new Intent(SplashActivity.this, GuideActivity.class));
                        } else {
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        }
                        finish();
                    }
                }
        );

    }

    //设置状态栏透明
    public void setStatusBarTranslucent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            //window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            View decorView = window.getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            //透明着色
            //window.setStatusBarColor(Color.TRANSPARENT);
            //window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }


    private void submitPrivacyGrantResult(boolean granted) {
        MobSDK.submitPolicyGrantResult(granted, new OperationCallback<Void>() {
            @Override
            public void onComplete(Void data) {
                Log.d("SplashActivity", "隐私协议授权结果提交：成功");
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("SplashActivity", "隐私协议授权结果提交：失败");
            }
        });
    }
}
