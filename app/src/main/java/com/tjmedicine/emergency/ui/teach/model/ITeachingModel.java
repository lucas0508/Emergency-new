package com.tjmedicine.emergency.ui.teach.model;

import com.tjmedicine.emergency.common.base.IBaseModel;

import java.util.Map;

public interface ITeachingModel extends IBaseModel {


    void findTeachingList(Map<String, Object> map,OnCallbackListener listener);

    void findTeachingDetail(int id, OnCallbackListener listener);

}
