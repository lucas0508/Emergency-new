package com.tjmedicine.emergency.ui.teach.model;

import com.tjmedicine.emergency.common.base.IBaseModel;

import java.util.Map;

public interface IMeStudyModel extends IBaseModel {

    void queryMeStudyData(Map<String, Object> map,OnCallbackListener onCallbackDataListener);


}
