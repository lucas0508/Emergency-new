package com.tjmedicine.emergency.ui.teach.model.impl;

import com.google.gson.reflect.TypeToken;
import com.tjmedicine.emergency.common.base.BaseModel;
import com.tjmedicine.emergency.common.base.IBaseModel;
import com.tjmedicine.emergency.common.bean.BannerBean;
import com.tjmedicine.emergency.common.global.GlobalConstants;
import com.tjmedicine.emergency.common.net.HttpProvider;
import com.tjmedicine.emergency.common.net.ResponseDataEntity;
import com.tjmedicine.emergency.ui.bean.ContactBean;
import com.tjmedicine.emergency.ui.teach.model.IBannerModel;

public class BannerModel extends BaseModel implements IBannerModel {


    @Override
    public void queryBannerData(OnCallbackDataListener callbackDataListener) {
        HttpProvider.doGet(GlobalConstants.APP_USER_BANNER, new HttpProvider.ResponseCallback() {
            @Override
            public void callback(String responseText) {
                executeDataCallback(responseText, new TypeToken<ResponseDataEntity<BannerBean>>() {
                }.getType(), callbackDataListener);
            }
        });
    }
}
