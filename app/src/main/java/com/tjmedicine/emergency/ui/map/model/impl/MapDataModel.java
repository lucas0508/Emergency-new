package com.tjmedicine.emergency.ui.map.model.impl;

import com.google.gson.reflect.TypeToken;
import com.tjmedicine.emergency.common.base.BaseModel;
import com.tjmedicine.emergency.common.bean.BannerBean;
import com.tjmedicine.emergency.common.bean.MapDataBeen;
import com.tjmedicine.emergency.common.global.GlobalConstants;
import com.tjmedicine.emergency.common.net.HttpProvider;
import com.tjmedicine.emergency.common.net.ResponseDataEntity;
import com.tjmedicine.emergency.ui.map.model.IMapDataModel;

import java.util.HashMap;
import java.util.Map;

public class MapDataModel extends BaseModel implements IMapDataModel {



    @Override
    public void updateAddress(double lat,double lng,OnCallbackListener listener) {
        Map<String,Object> map = new HashMap();
        map.put("lat",lat);
        map.put("lng",lng);
        HttpProvider.doPost(GlobalConstants.APP_USER_UPDATE_ADDRESS, map,new HttpProvider.ResponseCallback() {
            @Override
            public void callback(String responseText) {
                executeCallback(responseText,null, listener);
            }
        });
    }

    @Override
    public void queryMapData(int type, OnCallbackDataListener listener) {
        HttpProvider.doGet(GlobalConstants.APP_USER_MAP_DATA+"?type="+type, new HttpProvider.ResponseCallback() {
            @Override
            public void callback(String responseText) {
                executeDataCallback(responseText, new TypeToken<ResponseDataEntity<MapDataBeen>>() {
                }.getType(), listener);
            }
        });
    }


}
