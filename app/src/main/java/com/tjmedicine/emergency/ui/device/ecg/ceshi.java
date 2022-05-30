package com.tjmedicine.emergency.ui.device.ecg;

import com.google.gson.reflect.TypeToken;
import com.tjmedicine.emergency.common.base.BaseModel;
import com.tjmedicine.emergency.common.bean.StudyBean;
import com.tjmedicine.emergency.common.global.GlobalConstants;
import com.tjmedicine.emergency.common.net.HttpProvider;
import com.tjmedicine.emergency.common.net.ResponseDataEntity;
import com.tjmedicine.emergency.common.net.ResponseEntity;
import com.tjmedicine.emergency.ui.teach.model.IMeStudyModel;

import java.util.Map;

public class ceshi extends BaseModel implements ICeshiModel {




    @Override
    public void querySData(Map<String, Object> map, OnCallbackDataListener onCallbackDataListener) {
        HttpProvider.doGet(GlobalConstants.APP_USER_GETREMOTEECG + HttpProvider.getUrlDataByMap(map), new HttpProvider.ResponseCallback() {
            @Override
            public void callback(String responseText) {
                executeDataCallback(responseText, new TypeToken<ResponseDataEntity<ceshiBean>>() {
                }.getType(), onCallbackDataListener);
            }
        });
    }

    @Override
    public void querySSData(Map<String, Object> map, OnCallbackListener onCallbackListener) {
        HttpProvider.doGet(GlobalConstants.APP_USER_GETREMOTEECG + HttpProvider.getUrlDataByMap(map), new HttpProvider.ResponseCallback() {
            @Override
            public void callback(String responseText) {
                executeCallback(responseText, new TypeToken<ResponseEntity<ceshiBean>>() {
                }.getType(), onCallbackListener);
            }
        });
    }
}
