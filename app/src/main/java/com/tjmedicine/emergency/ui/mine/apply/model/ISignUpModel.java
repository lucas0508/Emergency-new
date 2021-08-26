package com.tjmedicine.emergency.ui.mine.apply.model;

import com.tjmedicine.emergency.common.base.IBaseModel;

import java.util.Map;

public interface ISignUpModel extends IBaseModel {


    void findSignUp(OnCallbackDataListener listener);

    void signUp(String activityId, OnCallbackListener listener);

}
