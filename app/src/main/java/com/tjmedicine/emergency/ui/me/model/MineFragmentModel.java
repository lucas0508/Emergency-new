package com.tjmedicine.emergency.ui.me.model;

import com.google.gson.reflect.TypeToken;
import com.tjmedicine.emergency.common.base.BaseModel;
import com.tjmedicine.emergency.common.base.IBaseModel;
import com.tjmedicine.emergency.common.bean.UserInfoEntity;
import com.tjmedicine.emergency.common.global.GlobalConstants;
import com.tjmedicine.emergency.common.net.HttpProvider;
import com.tjmedicine.emergency.common.net.ResponseEntity;
import com.tjmedicine.emergency.ui.me.model.impl.IMineFragmentModel;

import java.util.Map;

public class MineFragmentModel extends BaseModel implements IMineFragmentModel {
    @Override
    public void queryUserInfoData(IBaseModel.OnCallbackListener onCallbackListener) {
        HttpProvider.doGet(GlobalConstants.APP_GETUSERINFO, new HttpProvider.ResponseCallback() {
            @Override
            public void callback(String responseText) {
                executeCallback(responseText,new TypeToken<ResponseEntity<UserInfoEntity>>() {
                }.getType(),onCallbackListener);
            }
        });
    }

    @Override
    public void submitUserInfoData(Map<String, Object> objectMap, IBaseModel.OnCallbackListener onCallbackListener) {
        HttpProvider.doPost(GlobalConstants.APP_MODIFY_USER_INFORMATION,objectMap, new HttpProvider.ResponseCallback() {
            @Override
            public void callback(String responseText) {
                executeCallback(responseText,new TypeToken<ResponseEntity<UserInfoEntity>>() {
                }.getType(),onCallbackListener);
            }
        });
    }

}
