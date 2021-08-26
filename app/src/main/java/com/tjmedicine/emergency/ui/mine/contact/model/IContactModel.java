package com.tjmedicine.emergency.ui.mine.contact.model;

import com.tjmedicine.emergency.common.base.IBaseModel;

import java.util.Map;

public interface IContactModel extends IBaseModel{


    void addContact(Map<String, Object> map, final OnCallbackListener listener);


    void findContact(OnCallbackDataListener listener);


    void editContact(String  id ,OnCallbackListener listener);

}
