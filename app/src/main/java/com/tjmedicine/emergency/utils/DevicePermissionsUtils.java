package com.tjmedicine.emergency.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.tjmedicine.emergency.EmergencyApplication;
import com.tjmedicine.emergency.common.global.Constants;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class DevicePermissionsUtils {
    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";
    private static String mPhoneModel;
    private static String mSystemVersion;
    private static String mAppCurrentVersion;
    private static String mAppMaxVersion;
    private static String mAppMinVersion;

    public static List<String> permisstionList = new ArrayList<>();

    /**
     * 自动获取相机权限
     */
    public static void autoObtainCameraPermission(Activity context) {

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.CAMERA_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * 自动获取sdk权限
     */
    public static void autoObtainStoragePermission(Activity context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.STORAGE_PERMISSIONS_REQUEST_CODE);
        }
    }

    //SDK在Android 6.0下需要进行运行检测的权限如下：
    /*Manifest.permission.ACCESS_COARSE_LOCATION,
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.WRITE_EXTERNAL_STORAGE,
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.READ_PHONE_STATE*/

    //这里以ACCESS_COARSE_LOCATION为例
    public static void autoObtainLocationPermission(Activity context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    Constants.WRITE_COARSE_LOCATION_REQUEST_CODE);//自定义的code
        }
    }

    //        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            permisstionList.add(Manifest.permission.CAMERA);

    public static List<String> autoObtainNeedPermission(Activity context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permisstionList.add(Manifest.permission.CAMERA);
        }
        return permisstionList;
    }


    public static List<String> autoObtainNeedAllPermission(Activity context) {
        //位置
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permisstionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        //位置
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permisstionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        //相机权限
//        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            permisstionList.add(Manifest.permission.CAMERA);
//        }
        //sdk读权限
//        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            permisstionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
//        }
//        //sdk写权限
//        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            permisstionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        }
//        读手机状态
//        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            permisstionList.add(Manifest.permission.READ_PHONE_STATE);
//        }
        return permisstionList;
    }

    public static boolean isNotificationEnabled(Context context) {

        AppOpsManager mAppOps =
                (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);

        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;
        Class appOpsClass = null;

        /* Context.APP_OPS_MANAGER */
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());

            Method checkOpNoThrowMethod =
                    appOpsClass.getMethod(CHECK_OP_NO_THROW,
                            Integer.TYPE, Integer.TYPE, String.class);

            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
            int value = (Integer) opPostNotificationValue.get(Integer.class);

            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) ==
                    AppOpsManager.MODE_ALLOWED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getPhoneModel() {
        if (mPhoneModel == null)
            mPhoneModel = Build.MODEL;
        return mPhoneModel;
    }

    /**
     * 获取系统
     * ios / android / weixin
     *
     * @return
     */
    public static String getPhoneSystem() {
        return "android";
    }

    /**
     * 获取系统版本号
     *
     * @return
     */
    public static String getSystemVersion() {
        if (mSystemVersion == null)
            mSystemVersion = Build.VERSION.RELEASE;
        return mSystemVersion;
    }

    /**
     * 获取App当前版本
     *
     * @return
     */
    public static String getAppCurrentVersion() {
        if (mAppCurrentVersion == null) {
            PackageManager packageManager = EmergencyApplication.getContext().getPackageManager();
            PackageInfo packInfo;
            try {
                // getPackageName()是你当前类的包名，0代表是获取版本信息
                packInfo = packageManager.getPackageInfo(EmergencyApplication.getContext().getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                return "0.0";
            }
            mAppCurrentVersion = packInfo.versionName;

            String[] s = mAppCurrentVersion.split("\\.");
            mAppMaxVersion = s[0];
            mAppMinVersion = mAppCurrentVersion;
        }
        return mAppCurrentVersion;
    }

    /**
     * 获取当前版本 - 大版本
     *
     * @return
     */
    public static String getAppMaxVersion() {
        if (mAppMaxVersion == null)
            getAppCurrentVersion();
        return mAppMaxVersion;
    }

    /**
     * 获取当前版本 - 小版本
     *
     * @return
     */
    public static String getAppMinVersion() {
        if (mAppMinVersion == null)
            getAppCurrentVersion();
        return mAppMinVersion;
    }

    public static String getUniqueId() {
        String androidID = Settings.Secure.getString(EmergencyApplication.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        String id = androidID + Build.SERIAL;
        try {
            return toMD5(id);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return id;
        }
    }

    private static String toMD5(String text) throws NoSuchAlgorithmException {
        //获取摘要器 MessageDigest
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        //通过摘要器对字符串的二进制字节数组进行hash计算
        byte[] digest = messageDigest.digest(text.getBytes());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            //循环每个字符 将计算结果转化为正整数;
            int digestInt = digest[i] & 0xff;
            //将10进制转化为较短的16进制
            String hexString = Integer.toHexString(digestInt);
            //转化结果如果是个位数会省略0,因此判断并补0
            if (hexString.length() < 2) {
                sb.append(0);
            }
            //将循环结果添加到缓冲区
            sb.append(hexString);
        }
        //返回整个结果
        return sb.toString();
    }


    public static String listToString(List<String> strings) {
        String str = "";
        if (strings.size() > 0) {
            StringBuilder strBuilder = new StringBuilder();
            for (String letter : strings) {
                strBuilder.append(letter);
                strBuilder.append(",");
            }
            str = strBuilder.toString();
            str = str.substring(0, str.length() - ",".length());
            return str;
        }
        return "";
    }

    public static List<String> stringToList(String strings) {
        List<String> str = new ArrayList<>();
        if (!TextUtils.isEmpty(strings)) {
            StringBuilder strBuilder = new StringBuilder();
            String[] split = strings.split(",");
            str.addAll(Arrays.asList(split));
            return str;
        }
        return str;
    }
    public static List<String> stringToListByXG(String strings) {
        List<String> str = new ArrayList<>();
        if (!TextUtils.isEmpty(strings)) {
            StringBuilder strBuilder = new StringBuilder();
            String[] split = strings.split("/");
            str.addAll(Arrays.asList(split));
            return str;
        }
        return str;
    }
}
