package com.tjmedicine.emergency.common.global;

import android.os.Environment;


public class Constants {
    public static final String LOGGER_TAG = "121";
    public static final int CAMERA_PERMISSIONS_REQUEST_CODE = 21;
    public static final int STORAGE_PERMISSIONS_REQUEST_CODE = 22;
    public static final int WRITE_COARSE_LOCATION_REQUEST_CODE = 23;
    public static final int All_PERMISSIONS_CODE = 24;
    public static final int PERSON_NICK_NAME_REQUEST_CODE = 0x50;
    public static final int PERSON_NICK_NAME_RESULT_CODE = 0x51;

    /* WebActivity  key */
    public static final String WEB_KEY_URL = "WEB_URL";
    public static final String WEB_KEY_TITLE = "WEB_TITLE";

    public static final String WEB_KEY_FLAG = "WEB_FLAG";

    //1.线下活动，2.直播，3.其他
    public static final String WEB_KEY_TYPE = "WEB_TYPE";


    /**
     * KEY- 缓存 闪屏页图片
     */
    public static final String SPLASHACT_IMAGE = "SplashAct_Image";


    public static final int MAPROLE_VOLUNTEER = 1;
    public static final int MAPROLE_DOCTOR = 2;
    public static final int MAPROLE_AED = 3;

    public static final String B_BLE_START = "b_ble_start";
    public static final String B_BLE_END = "b_ble_end";
}
