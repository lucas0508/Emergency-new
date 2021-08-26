package com.tjmedicine.emergency.ui.other.model.impl;

import com.google.gson.reflect.TypeToken;
import com.tjmedicine.emergency.common.base.BaseModel;
import com.tjmedicine.emergency.common.bean.VersionUpdateEntity;
import com.tjmedicine.emergency.common.global.GlobalConstants;
import com.tjmedicine.emergency.common.net.HttpProvider;
import com.tjmedicine.emergency.common.net.ResponseEntity;
import com.tjmedicine.emergency.ui.other.model.IVersionUpdateModel;

public class VersionUpdateModel extends BaseModel implements IVersionUpdateModel {


    @Override
    public void updateVersion(final OnCallbackListener onCallbackListener) {


        HttpProvider.doGet(GlobalConstants.APP_VERSION_UPDATE, new HttpProvider.ResponseCallback() {
            @Override
            public void callback(String responseText) {
                delayedExecuteCallback(responseText, new TypeToken<ResponseEntity<VersionUpdateEntity>>() {
                }.getType(), onCallbackListener);
            }
        });
    }
}