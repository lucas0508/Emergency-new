package com.tjmedicine.emergency.ui.mine.health.model.impl;

import com.google.gson.reflect.TypeToken;
import com.tjmedicine.emergency.common.base.BaseModel;
import com.tjmedicine.emergency.common.bean.HealthAllFileBeen;
import com.tjmedicine.emergency.common.bean.HealthFileBeen;
import com.tjmedicine.emergency.common.bean.HealthTagBeen;
import com.tjmedicine.emergency.common.global.GlobalConstants;
import com.tjmedicine.emergency.common.net.HttpProvider;
import com.tjmedicine.emergency.common.net.ResponseDataEntity;
import com.tjmedicine.emergency.common.net.ResponseEntity;
import com.tjmedicine.emergency.ui.bean.ContactBean;
import com.tjmedicine.emergency.ui.mine.health.model.IHealthModel;

import java.util.Map;

public class HealthModel extends BaseModel implements IHealthModel {



    @Override
    public void addHealthRecords(Map<String, Object> map, OnCallbackListener listener) {
        HttpProvider.doPost(GlobalConstants.APP_USER_ADDHEALTH_RECORDS, map, new HttpProvider.ResponseCallback() {
            @Override
            public void callback(String responseText) {
                executeCallback(responseText, null, listener);
            }
        });
    }

    @Override
    public void delHealthRecords(String id, OnCallbackListener listener) {
        HttpProvider.doGet(GlobalConstants.APP_USER_EDITHEALTH_RECORDS+"?id="+id, new HttpProvider.ResponseCallback() {
            @Override
            public void callback(String responseText) {
                executeCallback(responseText, null, listener);
            }
        });
    }

    @Override
    public void findHealthRecordsList(OnCallbackDataListener listener) {
        HttpProvider.doGet(GlobalConstants.APP_USER_FINDHEALTH_RECORD_LIST, new HttpProvider.ResponseCallback() {
            @Override
            public void callback(String responseText) {
                executeDataCallback(responseText, new TypeToken<ResponseDataEntity<HealthFileBeen>>() {
                }.getType(), listener);
            }
        });
    }

    @Override
    public void findHealthRecordsDetail(int id, OnCallbackListener listener) {
        HttpProvider.doGet(GlobalConstants.APP_USER_FINDHEALTH_RECORDS_DETAIL+"?healthId="+id , new HttpProvider.ResponseCallback() {
            @Override
            public void callback(String responseText) {
                executeCallback(responseText, new TypeToken<ResponseEntity<HealthAllFileBeen>>() {
                }.getType(), listener);
            }
        });
    }

    @Override
    public void findHealthRecordsTag(OnCallbackDataListener listener) {
        HttpProvider.doGet(GlobalConstants.APP_USER_FINDHEALTH_RECORDS_TAG, new HttpProvider.ResponseCallback() {
            @Override
            public void callback(String responseText) {
                executeDataCallback(responseText,  new TypeToken<ResponseDataEntity<HealthTagBeen>>() {
                }.getType(), listener);
            }
        });
    }
}
