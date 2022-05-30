package com.tjmedicine.emergency.ui.device.ecg;

import com.tjmedicine.emergency.common.base.IBaseModel;

import java.util.Map;


public interface ICeshiModel extends IBaseModel {

    void querySData(Map<String, Object> map, IBaseModel.OnCallbackDataListener onCallbackDataListener);


    void querySSData(Map<String, Object> map, IBaseModel.OnCallbackListener onCallbackListener);

}
