package com.tjmedicine.emergency.ui.mine.apply.model.impl;

import com.google.gson.reflect.TypeToken;
import com.tjmedicine.emergency.common.base.BaseModel;
import com.tjmedicine.emergency.common.global.GlobalConstants;
import com.tjmedicine.emergency.common.net.HttpProvider;
import com.tjmedicine.emergency.common.net.ResponseDataEntity;
import com.tjmedicine.emergency.ui.bean.ContactBean;
import com.tjmedicine.emergency.ui.bean.SignUpBean;
import com.tjmedicine.emergency.ui.mine.apply.model.ISignUpModel;

import java.util.Map;

public class SignUpModel extends BaseModel implements ISignUpModel {

    @Override
    public void findSignUp(OnCallbackDataListener listener) {
        HttpProvider.doGet(GlobalConstants.APP_USER_FIND_SIGNUP, new HttpProvider.ResponseCallback() {
            @Override
            public void callback(String responseText) {
                executeDataCallback(responseText, new TypeToken<ResponseDataEntity<SignUpBean>>() {
                }.getType(), listener);
            }
        });
    }

    @Override
    public void signUp(String activityId, OnCallbackListener listener) {
        HttpProvider.doGet(GlobalConstants.APP_USER_SIGNUP+activityId, new HttpProvider.ResponseCallback() {
            @Override
            public void callback(String responseText) {
                executeCallback(responseText,null, listener);
            }
        });
    }
}
