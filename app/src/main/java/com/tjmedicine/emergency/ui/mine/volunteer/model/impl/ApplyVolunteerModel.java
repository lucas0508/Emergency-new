package com.tjmedicine.emergency.ui.mine.volunteer.model.impl;

import com.google.gson.reflect.TypeToken;
import com.tjmedicine.emergency.common.base.BaseModel;
import com.tjmedicine.emergency.common.base.IBaseModel;
import com.tjmedicine.emergency.common.bean.VolunteerBean;
import com.tjmedicine.emergency.common.global.GlobalConstants;
import com.tjmedicine.emergency.common.net.HttpProvider;
import com.tjmedicine.emergency.common.net.ResponseEntity;
import com.tjmedicine.emergency.ui.mine.volunteer.model.IApplyVolunteerModel;

import java.util.Map;

public class ApplyVolunteerModel extends BaseModel implements IApplyVolunteerModel {


    @Override
    public void applyVolunteer(Map<String, Object> map, IBaseModel.OnCallbackListener listener) {
        HttpProvider.doPost(GlobalConstants.APP_USER_APPLY_VOLUNTEER, map, new HttpProvider.ResponseCallback() {
            @Override
            public void callback(String responseText) {
                executeCallback(responseText,null, listener);
            }
        });
    }


    @Override
    public void queryVolunteer(IBaseModel.OnCallbackListener listener) {
        HttpProvider.doGet(GlobalConstants.APP_USER_QUERY_VOLUNTEER, new HttpProvider.ResponseCallback() {
            @Override
            public void callback(String responseText) {
                executeCallback(responseText, new TypeToken<ResponseEntity<VolunteerBean>>() {
                }.getType(), listener);
            }
        });
    }
}
