package com.tjmedicine.emergency.ui.mine.auth.model;

import com.tjmedicine.emergency.common.base.IBaseModel;

import java.util.Map;

public interface IAuthModel extends IBaseModel{


    void goRealAuth(Map<String, Object> map, final OnCallbackListener listener);


    void findRealAuth(OnCallbackListener listener);


}
