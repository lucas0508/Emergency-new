package com.tjmedicine.emergency.ui.login.model.impl;

import com.facebook.stetho.common.LogUtil;
import com.google.gson.reflect.TypeToken;
import com.tjmedicine.emergency.common.base.BaseModel;
import com.tjmedicine.emergency.common.base.IBaseModel;
import com.tjmedicine.emergency.common.bean.UserInfoEntity;
import com.tjmedicine.emergency.common.global.GlobalConstants;
import com.tjmedicine.emergency.common.net.HttpProvider;
import com.tjmedicine.emergency.common.net.ResponseEntity;
import com.tjmedicine.emergency.ui.login.model.ILoginModel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author QiZai
 * @desc
 * @date 2018/4/10
 */

public class LoginModel extends BaseModel implements ILoginModel {


    @Override
    public void sendValidateCode(String phone, String type, final IBaseModel.OnCallbackListener listener) {
        //+ "&type=" + type
        String URL = GlobalConstants.APP_LOAD_CAPTCHA + "?phone=" + phone ;
        HttpProvider.doGet(URL, new HttpProvider.ResponseCallback() {
            @Override
            public void callback(String responseText) {
                LogUtil.e("LoginModel:", responseText);
                executeCallback(responseText, null, listener);
            }
        });
    }

    @Override
    public void toLogin(Map<String, Object> data, final IBaseModel.OnCallbackListener listener) {
        String URL = GlobalConstants.APP_LOGIN_INTERFACE;
        HttpProvider.doPost(URL, data, new HttpProvider.ResponseCallback() {
            @Override
            public void callback(String responseText) {
                executeCallback(responseText, new TypeToken<ResponseEntity<UserInfoEntity>>() {
                }.getType(), listener);
            }
        });
    }

    @Override
    public void toWXLogin(String code, final IBaseModel.OnCallbackListener listener) {
//        String url = GlobalConstants.APP_WXLOGIN ;
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("code", code);
        HttpProvider.doPost("url", objectMap, new HttpProvider.ResponseCallback() {
            @Override
            public void callback(String responseText) {
                delayedExecuteCallback(responseText, new TypeToken<ResponseEntity<UserInfoEntity>>() {
                }.getType(), listener);
            }
        });
    }


    public static void main(String args[]) {
        Map<String, Object> mLoginFormData = new HashMap<>();
        mLoginFormData.put("unionId", "111111111111111");
        mLoginFormData.put("openId", "22222222333333333333333332");
       // String data = SecurityUtil.encryptRSAPublic(mLoginFormData);
//        System.out.print("加密数据：" + data);
    }
}
