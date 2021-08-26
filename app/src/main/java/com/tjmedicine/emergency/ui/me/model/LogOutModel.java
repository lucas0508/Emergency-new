package com.tjmedicine.emergency.ui.me.model;


import com.tjmedicine.emergency.common.base.BaseModel;
import com.tjmedicine.emergency.common.global.GlobalConstants;
import com.tjmedicine.emergency.common.net.HttpProvider;
import com.tjmedicine.emergency.ui.me.model.impl.ILogOutModel;

public class LogOutModel extends BaseModel implements ILogOutModel {


    @Override
    public void logout(OnCallbackListener listener) {
        String url = GlobalConstants.APP_LOGOUT;
        HttpProvider.doPost(url,null, responseText -> {
            executeCallback(responseText,null, listener);
        });
    }
}
