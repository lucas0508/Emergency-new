package com.tjmedicine.emergency.ui.mine.auth.model.impl;

import com.google.gson.reflect.TypeToken;
import com.tjmedicine.emergency.common.base.BaseModel;
import com.tjmedicine.emergency.common.bean.VolunteerBean;
import com.tjmedicine.emergency.common.global.GlobalConstants;
import com.tjmedicine.emergency.common.net.HttpProvider;
import com.tjmedicine.emergency.common.net.ResponseDataEntity;
import com.tjmedicine.emergency.common.net.ResponseEntity;
import com.tjmedicine.emergency.ui.bean.ContactBean;
import com.tjmedicine.emergency.ui.mine.auth.model.IAuthModel;

import org.bouncycastle.asn1.ocsp.ResponseData;

import java.util.Map;

public class AuthModel extends BaseModel implements IAuthModel {


    @Override
    public void goRealAuth(Map<String, Object> map, OnCallbackListener listener) {
        HttpProvider.doPost(GlobalConstants.APP_USER_AUTH, map, new HttpProvider.ResponseCallback() {
            @Override
            public void callback(String responseText) {
                executeCallback(responseText, null, listener);
            }
        });
    }

    @Override
    public void findRealAuth(OnCallbackListener listener) {
        HttpProvider.doGet(GlobalConstants.APP_USER_FINDREALAUTH, new HttpProvider.ResponseCallback() {
            @Override
            public void callback(String responseText) {
                executeCallback(responseText,new TypeToken<ResponseEntity<VolunteerBean>>() {
                }.getType(), listener);
            }
        });
    }
}
