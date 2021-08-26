package com.tjmedicine.emergency.common.cache.SharedPreferences;

import android.content.SharedPreferences;

import com.tjmedicine.emergency.EmergencyApplication;
import com.tjmedicine.emergency.common.bean.UserInfoEntity;


/**
 * 缓存文件类 - 用户信息相关
 *
 * @author QiZai
 * @date 2018/4/12
 */

public class UserInfo {

    /**
     * 缓存对象
     */
    private static SharedPreferences sharedPreferences = SharedPreferencesProvider.getInstance(
            EmergencyApplication.getContext(),
            "USER_INFO" // 缓存文件名称
    );
    /**
     * KEY token
     */
    private static final String KEY_TOKEN = "KEY_TOKEN";

    /**
     * KEY 用户手机号
     */
    private static final String KEY_USER_PHONE = "KEY_USER_PHONE";

    /**
     * KEY 真实姓名
     */
    private static final String KEY_REAL_NAME = "USER_REAL_NAME";


    /**
     * KEY 用户头像
     */
    private static final String KEY_AVATAR = "USER_AVATAR";

    /**
     * KEY 用户性别
     */
    private static final String KEY_PERSONSIGN = "USER_PERSONSIGN";


    /**
     * KEY 用户等级
     */
    private static final String KEY_GRADETYPE = "GRADETYPE";


    /**
     * KEY 用户是否设置支付密码
     */
    private static final String KEY_USERNAME = "USERNAME";


    /**
     * KEY 用户修改支付密码生成随机数校验
     */
    private static final String KEY_TIMESTAMP = "TIMESTAMP";

    /**
     * KEY 城市编码
     */
    private static final String KEY_CITY_CODE = "CITY_CODE";

    private static final String KEY_CITY_CODE_NAME = "CITY_CODE_NAME";


    private static final String KEY_CITY_CODE_LANG = "CITY_CODE_LANG";

    private static final String KEY_CITY_CODE_LAT= "CITY_CODE_LAT";


    public static boolean setLang(String lang) {
        return sharedPreferences.edit().putString(KEY_CITY_CODE_LANG, lang).commit();
    }

    public static String getLang() {
        return sharedPreferences.getString(KEY_CITY_CODE_LANG, "");
    }


    public static boolean setLat(String lang) {
        return sharedPreferences.edit().putString(KEY_CITY_CODE_LAT, lang).commit();
    }

    public static String getLat() {
        return sharedPreferences.getString(KEY_CITY_CODE_LAT, "");
    }


    public static boolean setCityCode(String city) {
        return sharedPreferences.edit().putString(KEY_CITY_CODE, city).commit();
    }

    public static String getCityCode() {
        return sharedPreferences.getString(KEY_CITY_CODE, "");
    }




    public static boolean setCityCodeName(String city) {
        return sharedPreferences.edit().putString(KEY_CITY_CODE_NAME, city).commit();
    }

    public static String getCityCodeName() {
        return sharedPreferences.getString(KEY_CITY_CODE_NAME, null);
    }

    public static boolean setUserPhone(String userPhone) {
        return sharedPreferences.edit().putString(KEY_USER_PHONE, userPhone).commit();
    }

    public static String getUserPhone() {
        return sharedPreferences.getString(KEY_USER_PHONE, null);
    }


    public static boolean setUserInfoImage(String avatar) {
        return sharedPreferences.edit().putString(KEY_AVATAR, avatar).commit();
    }

    public static String getUserInfoImage() {
        return sharedPreferences.getString(KEY_AVATAR, null);
    }


    public static boolean setUserPersonSign(String personsign) {
        return sharedPreferences.edit().putString(KEY_PERSONSIGN, personsign).commit();
    }

    public static String getUserPersonSign() {
        return sharedPreferences.getString(KEY_PERSONSIGN, null);
    }

    public static boolean setUserNiceName(String niceName) {
        return sharedPreferences.edit().putString(KEY_REAL_NAME, niceName).commit();
    }


    public static String getUserNiceName() {
        return sharedPreferences.getString(KEY_REAL_NAME, null);
    }



    /**
     * 获取用户id
     *
     * @return Long，没有返回0
     */
    public static String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, "");
    }

    public static void setToken(String token) {
        String base_token = "Bearer " + token;
        sharedPreferences.edit().putString(KEY_TOKEN, base_token).apply();
    }

    public static String getPersonSign() {
        return sharedPreferences.getString(KEY_PERSONSIGN, null);
    }


    public static void setUserInfo(UserInfoEntity userInfo) {
        String base_token = "Bearer " + userInfo.getToken();
        sharedPreferences.edit()
                .putString(KEY_TOKEN, base_token)
                .putString(KEY_AVATAR, userInfo.getAvatar())
                .putString(KEY_REAL_NAME, userInfo.getNickname())
                .putString(KEY_USER_PHONE, userInfo.getPhone())
                .putString(KEY_PERSONSIGN, userInfo.getPersonSign())
                .apply();
    }

    /**
     * 删除用户缓存
     */
    public static boolean removeUserInfo() {
        return sharedPreferences.edit()
                .remove(KEY_TOKEN)
                .remove(KEY_AVATAR)
                .remove(KEY_REAL_NAME)
                .remove(KEY_USER_PHONE)
                .remove(KEY_PERSONSIGN)
                .commit();
    }
}

