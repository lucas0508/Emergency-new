package com.tjmedicine.emergency.common.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.facebook.stetho.common.LogUtil;
import com.gyf.immersionbar.ImmersionBar;
import com.tjmedicine.emergency.R;
import com.tjmedicine.emergency.common.dialog.DialogManage;
import com.tjmedicine.emergency.common.global.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;


/**
 * @author QiZai
 * @desc
 * @date 2018/4/10
 */

public abstract class BaseActivity extends AppCompatActivity {

    private PermissionListener mListener;
    private ImageButton mClose;
    /**
     * Activity, Fragment 公共方法处理类
     */
    public DialogManage mApp;

    /**
     * 获取Activity 方便内部类使用
     *
     * @return
     */
    public Activity getContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayoutResourceID());
        // PushAgent.getInstance(context).onAppStart();
        ButterKnife.bind(this);
        ActivityManager.getInstance().addActivity(this);

        //强制竖屏
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN |
                        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED
        );//默认带有EditText的界面不弹出键盘
        // StatusBarUtils.darkMode(this);
        //StatusBarUtils.immersive(this,R.color.white);
        initStatusColor();
//        setStatusBar();
        mApp = new DialogManage(getContext());
        initView();
    }

    /**
     * 全屏
     */
    public void setStatusBar() {
        View decorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
        }
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

    }


    /**
     * 此方法用于返回Fragment设置ContentView的布局文件资源ID
     *
     * @return 布局文件资源ID
     */
    protected abstract int setLayoutResourceID();

    /**
     * 初始化view
     */
    protected abstract void initView();


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mClose = findViewById(R.id.ib_close);
        if (mClose != null)
            mClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
    }

    private void initStatusColor() {
        //设置共同沉浸式样式
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarColorInt(Color.WHITE)
                .navigationBarColorInt(Color.BLACK)
                .keyboardEnable(true)
                .autoDarkModeEnable(true)
                .init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().closeActivity(this);
    }

    /**
     * 设置关闭按钮的显示与隐藏
     *
     * @param visibility
     */
    public void setCloseView(int visibility) {
        if (mClose != null)
            mClose.setVisibility(visibility);
    }

    /**
     * 授权运行
     *
     * @param permissionList 需要权限
     * @param listener       权限监听
     */
    public void requestRunPermission(List<String> permissionList, PermissionListener listener) {
        mListener = listener;
        LogUtil.d("权限：", permissionList.toArray());
        LogUtil.d("权限数量：", permissionList.size());
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), Constants.All_PERMISSIONS_CODE);
        } else {
            //表示全都授权了
            mListener.onGranted();
        }
    }

    /**
     * 隐藏软键盘
     *
     * @param token
     */
    public void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，
     * 来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 申请授权处理结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.All_PERMISSIONS_CODE:
                try {
                    if (grantResults.length > 0) {
                        //存放没授权的权限
                        List<String> deniedPermissions = new ArrayList<>();
                        for (int i = 0; i < grantResults.length; i++) {
                            int grantResult = grantResults[i];
                            String permission = permissions[i];
                            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                                deniedPermissions.add(permission);
                            }
                        }
                        if (deniedPermissions.isEmpty()) {
                            //说明都授权了
                            mListener.onGranted();
                        } else {
                            mListener.onDenied(deniedPermissions);
                            LogUtil.e("refuse", "Permission");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    public interface PermissionListener {

        void onGranted();

        void onDenied(List<String> per);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    /**
     * [页面跳转]
     * * @param clz
     */
    public void startActivity(Class<?> clz) {
        startActivity(new Intent(this, clz));
    }

    /**
     * [携带数据的页面跳转]
     * * @param clz * @param bundle
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
