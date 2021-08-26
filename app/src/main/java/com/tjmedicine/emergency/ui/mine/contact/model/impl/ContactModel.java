package com.tjmedicine.emergency.ui.mine.contact.model.impl;

import com.google.gson.reflect.TypeToken;
import com.tjmedicine.emergency.common.base.BaseModel;
import com.tjmedicine.emergency.common.global.GlobalConstants;
import com.tjmedicine.emergency.common.net.HttpProvider;
import com.tjmedicine.emergency.common.net.ResponseDataEntity;
import com.tjmedicine.emergency.ui.bean.ContactBean;
import com.tjmedicine.emergency.ui.mine.contact.model.IContactModel;

import java.util.Map;

public class ContactModel extends BaseModel implements IContactModel {

    @Override
    public void addContact(Map<String, Object> map, OnCallbackListener listener) {
        HttpProvider.doPost(GlobalConstants.APP_USER_ADD_CONTACT, map, new HttpProvider.ResponseCallback() {
            @Override
            public void callback(String responseText) {
                executeCallback(responseText, null, listener);
            }
        });
    }

    @Override
    public void findContact(OnCallbackDataListener listener) {
        HttpProvider.doGet(GlobalConstants.APP_USER_FIND_CONTACT, new HttpProvider.ResponseCallback() {
            @Override
            public void callback(String responseText) {
                executeDataCallback(responseText, new TypeToken<ResponseDataEntity<ContactBean>>() {
                }.getType(), listener);
            }
        });
    }


    @Override
    public void editContact(String id, OnCallbackListener listener) {
        HttpProvider.doGet(GlobalConstants.APP_USER_DELEATE_CONTACT + "?id=" + id, new HttpProvider.ResponseCallback() {
            @Override
            public void callback(String responseText) {
                executeCallback(responseText, null, listener);
            }
        });
    }
}
