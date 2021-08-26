package com.tjmedicine.emergency.ui.me.model.impl;



import com.tjmedicine.emergency.common.base.IBaseModel;

import java.util.Map;

public interface IMineFragmentModel {


    void queryUserInfoData(IBaseModel.OnCallbackListener onCallbackListener);


    void submitUserInfoData(Map<String, Object> objectMap, IBaseModel.OnCallbackListener onCallbackListener);
}
