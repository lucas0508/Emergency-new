package com.tjmedicine.emergency.ui.teach.model.impl;

import com.google.gson.reflect.TypeToken;
import com.tjmedicine.emergency.common.base.BaseModel;
import com.tjmedicine.emergency.common.bean.BannerBean;
import com.tjmedicine.emergency.common.bean.HealthAllFileBeen;
import com.tjmedicine.emergency.common.bean.HealthFileBeen;
import com.tjmedicine.emergency.common.bean.TeachingBean;
import com.tjmedicine.emergency.common.global.GlobalConstants;
import com.tjmedicine.emergency.common.net.HttpProvider;
import com.tjmedicine.emergency.common.net.ResponseDataEntity;
import com.tjmedicine.emergency.common.net.ResponseEntity;
import com.tjmedicine.emergency.ui.teach.model.IBannerModel;
import com.tjmedicine.emergency.ui.teach.model.ITeachingModel;

import java.util.Map;

public class TeachingModel extends BaseModel implements ITeachingModel {


    @Override
    public void findTeachingList(Map<String, Object> map, OnCallbackListener listener) {
        HttpProvider.doGet(GlobalConstants.APP_USER_FIND_COURSE +
                HttpProvider.getUrlDataByMap(map),
                new HttpProvider.ResponseCallback() {
            @Override
            public void callback(String responseText) {
                executeCallback(responseText, new TypeToken<ResponseEntity<TeachingBean>>() {
                }.getType(), listener);
            }
        });
    }

    @Override
    public void findTeachingDetail(int id, OnCallbackListener listener) {
        HttpProvider.doGet(GlobalConstants.APP_USER_FINDINFO + "?courseId=" + id, new HttpProvider.ResponseCallback() {
            @Override
            public void callback(String responseText) {
                executeCallback(responseText, new TypeToken<ResponseEntity<TeachingBean>>() {
                }.getType(), listener);
            }
        });
    }
}
