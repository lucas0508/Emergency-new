package com.tjmedicine.emergency.ui.teach.model.impl;

import com.google.gson.reflect.TypeToken;
import com.tjmedicine.emergency.common.base.BaseModel;
import com.tjmedicine.emergency.common.bean.BannerBean;
import com.tjmedicine.emergency.common.bean.StudyBean;
import com.tjmedicine.emergency.common.bean.TeachingBean;
import com.tjmedicine.emergency.common.global.GlobalConstants;
import com.tjmedicine.emergency.common.net.HttpProvider;
import com.tjmedicine.emergency.common.net.ResponseDataEntity;
import com.tjmedicine.emergency.common.net.ResponseEntity;
import com.tjmedicine.emergency.ui.bean.SignUpBean;
import com.tjmedicine.emergency.ui.teach.model.IBannerModel;
import com.tjmedicine.emergency.ui.teach.model.IMeStudyModel;

import java.util.Map;

public class MeStudyModel extends BaseModel implements IMeStudyModel {


    @Override
    public void queryMeStudyData(Map<String, Object> map, OnCallbackListener callbackDataListener) {
        HttpProvider.doGet(GlobalConstants.APP_USER_ME_STUDY + HttpProvider.getUrlDataByMap(map), new HttpProvider.ResponseCallback() {
            @Override
            public void callback(String responseText) {
                executeCallback(responseText, new TypeToken<ResponseEntity<StudyBean>>() {
                }.getType(), callbackDataListener);
            }
        });
    }
}
